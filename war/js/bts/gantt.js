/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
var g_searchParams;

$(function(){
	
	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});

	initTicketDialog();

	$("#exportGantttBtn").click(function(){
		exportGanttt();
	});
	
	var selectedProjectName =  $("#targetProjects option:selected").text();
	$("#selectedProjectName").text("(" + selectedProjectName + ")");

	initGanttSearchInfo();
});

//初期表示時処理
function initGanttSearchInfo() {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketSearchInfo"
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
	
	var ticketMst = data.result.ticketMst;
	
	setStatusCheckBoc($("#status_area"), ticketMst.searchStatusList);
	
	$("#search_milestone").empty();
	$.each(ticketMst.milestoneList, function(){
		$("#search_milestone").append($("<option />").attr({value:this.value}).text(this.label));
	});
	$("#search_targetMember").empty();
	$.each(ticketMst.memberList, function(){
		$("#search_targetMember").append($("<option />").attr({value:this.value}).text(this.label));
	});

	var form = data.result;
	$("input[type='checkbox'][name='search_status']").val(form.status);
}


//チャート出力
function exportGanttt() {
	var params = createSearchGanttParams();
	g_searchParams = params;
	searchAndRender(params);
}

//検索＆レンダリング
function searchAndRender(params) {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/bts/gantt/ajax/ganttList",
		data: params
	}).then(
		function(data) {
			render(data);
		}
	);
}

//チャート表示
function render(data) {
	$("#result_area").empty();
	$("#listCnt").val("0");

	//共通エラーチェック
	if(errorCheck(data) == false) {
		return;
	}
	infoCheck(data);
	//tokenの設定
	$("#token").val(data.token);
	
	var ticketList = data.result.ticketList;
	$("#listCnt").val(ticketList.length);
	if(ticketList.length == 0) {
		return;
	}

	//ガントチャート作成用のデータを作成する
	var gantList = new Array();
	$.each(ticketList, function(){
		var obj = {};
		obj.id = this.model.keyToString;
		obj.name = this.model.title;
		
		var serie = {};
		serie.name = this.targetMemberName;
		serie.start = parseDate(this.startDate);
		serie.end = parseDate(this.period);
		var series = new Array();
		series.push(serie);
		obj.series = series;
		
		gantList.push(obj);
	});

	$("#result_area").ganttView({
		data: gantList,
		slideWidth: 400,
		behavior: {
			clickable: false,
			draggable: false,
			resizable: false,
		}
	});
}

//再検索判断処理
//一覧に表示されている場合、再検索する、と判断します。
function isReRender() {
	if($("#listCnt").val() != '0') {
		return true;
	}
	return false;
}

//再表示処理
//ダイアログからの最表示時に呼び出されます
function refresh() {
	reSearchAndRender();
}

//再検索処理
function reSearchAndRender() {
	if(isReRender()) {
		return searchAndRender(g_searchParams);
	}
	return "";
}

//Ticket検索条件設定
function createSearchGanttParams() {
	var params = {};
	params["milestone"] = $("#search_milestone").val();
	params["targetMember"] = $("#search_targetMember").val();

	params["status"] = new Array();
	$("input[type='checkbox'][name='search_status']").each(function(index){
		if($(this).prop("checked") == true) {
			params["status"].push($(this).val());
		}
	});
	return params;
}
