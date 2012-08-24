/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
$(function(){
	
	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});
	
	$("#nav_search").on("click", function(){
		searchTodo();
	});

	$("#nav_create").on("click", function(){
		moveUrl("/mobile/bts/todo/edit?keyToString=&dashbord=false");
	});

	init();
});

//初期表示時処理
function init() {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/bts/todo/ajax/todoSearchInfo"
	}).then(
		function(data) {
			renderSearchInfo(data);
		}
	);
}

//検索条件設定
function renderSearchInfo(data) {
	//共通エラーチェック
	if(errorCheck(data) == false) {
		return;
	}

	var form = data.result;

	//検索条件初期値をWebstorageより設定
	var todo_search_param = JSON.parse(sessionStorage.getItem("todo_search_param"));
	if(todo_search_param == null) {
		todo_search_param = {};
		todo_search_param["status"] = form.status;
	}
	
	$("#todo_status").empty();
	$.each(form.statusList, function() {
		$("#todo_status").append($('<option>').attr({ value: this.value }).text(this.label));
	});
	$("#todo_status").val(todo_search_param["status"]);
	$("#todo_status").selectmenu('refresh', true);
	
	$("#search_tag").empty();
	$.each(form.tagList, function(){
		$("#search_tag").append($("<option />").attr({value:this.value}).text(this.label));
	});
	$("#search_tag").val(todo_search_param["tag"]);
	$("#search_tag").selectmenu("refresh");
	
	$("#search_title").val(todo_search_param["title"]);
	$("#search_fromPeriod").val(formatDateyyyyMMdd(todo_search_param["fromPeriod"]));
	$("#search_toPeriod").val(formatDateyyyyMMdd(todo_search_param["toPeriod"]));
}

//TODO検索
function searchTodo() {
	var params = createSearchTodoParams();
	
	//validate
	var v = new Validate();
	v.addRules({value:params["fromPeriod"],option:'date',error_args:"期限From"});
	v.addRules({value:params["toPeriod"],option:'date',error_args:"期限To"});
	if(v.execute() == false) {
		return;
	}

	sessionStorage.setItem("todo_search_param", JSON.stringify(params));
	moveUrl("/mobile/bts/todo/list");
}

//TODO検索条件設定
function createSearchTodoParams() {
	var params = {};
	params["title"] = $("#search_title").val();
	params["tag"] = $("#search_tag").val();
	params["fromPeriod"] = unFormatDate($("#search_fromPeriod").val());
	params["toPeriod"] = unFormatDate($("#search_toPeriod").val());
	params["status"] = getSelectArray("todo_status");
	return params;
}
