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

	$("#searchTicketBtn").click(function(){
		searchTicket();
	});
	
	$("#addTicketBtn").click(function(){
		openEditTicketDialog("");
	});
	
	var selectedProjectName =  $("#targetProjects option:selected").text();
	$("#selectedProjectName").text("(" + selectedProjectName + ")");

	initTicket();
});

//初期表示時処理
function initTicket() {
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
	
	$("#status_area").empty();
	var $statusDiv = $("<div />");
	$.each(ticketMst.searchStatusList, function(){
		var $label = $("<label />").addClass("checkbox inline");
		var $checkbox = $("<input />").attr({type:"checkbox", name:"search_status", value:this.value});
		var $span = $("<span />").text(this.label);
		$label = $label.append($checkbox).append($span);
		$statusDiv.append($label);
	});
	$("#status_area").append($statusDiv);
	
	$("#search_priority").empty();
	$.each(ticketMst.priorityList, function(){
		$("#search_priority").append($("<option />").attr({value:this.value}).text(this.label));
	});
	$("#search_kind").empty();
	$.each(ticketMst.kindList, function(){
		$("#search_kind").append($("<option />").attr({value:this.value}).text(this.label));
	});
	$("#search_category").empty();
	$.each(ticketMst.categoryList, function(){
		$("#search_category").append($("<option />").attr({value:this.value}).text(this.label));
	});
	$("#search_version").empty();
	$.each(ticketMst.versionList, function(){
		$("#search_version").append($("<option />").attr({value:this.value}).text(this.label));
	});
	$("#search_milestone").empty();
	$.each(ticketMst.milestoneList, function(){
		$("#search_milestone").append($("<option />").attr({value:this.value}).text(this.label));
	});
	$("#search_targetMember").empty();
	$.each(ticketMst.memberList, function(){
		$("#search_targetMember").append($("<option />").attr({value:this.value}).text(this.label));
	});
	
	$.datepicker.setDefaults($.extend($.datepicker.regional['ja']));
	$("#search_fromPeriod").datepicker();
	$("#search_toPeriod").datepicker();

	var form = data.result;
	$("input[type='checkbox'][name='search_status']").val(form.status);
}


//Ticket検索
function searchTicket() {
	var params = createSearchTicketParams();
	g_searchParams = params;
	searchAndRender(params);
}

//検索＆レンダリング
function searchAndRender(params) {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketList",
		data: params
	}).then(
		function(data) {
			render(data);
		}
	);
}

//一覧表示
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
	
	var result = data.result;
	$("#listCnt").val(result.length);
	if(result.length == 0) {
		return;
	}

	var $msgDiv = $("<div />");
	if(result.length >= 1000) {
		$msgDiv.append($("<span />").text("1000件以上存在します").addClass("label label-warning"));
	} else {
		$msgDiv.append($("<span />").text("該当件数：" + result.length + "件").addClass("label label-info"));
	}
	
	//一覧をレンダリング
	var $table = $("<table />").addClass("table table-bordered result_table");
	var $thead = $("<thead />").append($("<tr />")
				.append($("<th />").text("チケットNo").attr({width:"80px"}))
				.append($("<th />").text("ステータス"))
				.append($("<th />").text("件名"))
				.append($("<th />").text("期限").attr({width:"100px"}))
				.append($("<th />").text("担当者"))
				.append($("<th />").text("").attr({width:"50px"}))
			);
	$table.append($thead);
	
	var $tbody = $("<tbody />");
	$.each(result, function(){
		var keyToString = this.model.keyToString;
		var status = this.model.status;
		var title = this.model.title;
		var memberName = this.targetMemberName;
		var no = this.model.no;
		var versionNo = this.model.version;
		var period = this.period;
		var createdAt = this.createdAt;
		var periodStatusLabel = this.periodStatusLabel;
		var periodStatusCode = this.periodStatusCode;

		var $delBtn = $("<input />").attr({type:"button", value:"削"}).addClass("btn btn-danger btn-mini");
		$delBtn.click(function(){
			deleteTicket(title, keyToString, versionNo);
		});
		
		var $a = $("<a />").attr({href:"javascript:void(0)"}).text(no);
		$a.click(function(){
			openDetailTicketDialog(keyToString);
		});
		
		var $statusSpan = $("<span />").text(status);
		var $periodStatusSpan = $("<span />");
		if(periodStatusCode != '') {
			$periodStatusSpan.text(periodStatusLabel);
			if(periodStatusCode == '1') {
				$periodStatusSpan = $periodStatusSpan.addClass("label label-warning");
			} else {
				$periodStatusSpan = $periodStatusSpan.addClass("label label-important");
			}
		}
		var $statusDiv = $("<div />").append($statusSpan).append($("<br />")).append($periodStatusSpan);
		
		var $tr = $("<tr />");
		$tr.append($("<td />").append($a))
			.append($("<td />").append($statusDiv))
			.append($("<td />").text(title))
			.append($("<td />").text(formatDateyyyyMMdd(period)))
			.append($("<td />").text(memberName))
			.append($("<td />").append($delBtn));
		$tbody.append($tr)
	});
	$table.append($tbody);
	
	$("#result_area").append($("<hr />")).append($msgDiv).append($table);
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
function createSearchTicketParams() {
	var params = {};
	params["kind"] = $("#search_kind").val();
	params["priority"] = $("#search_priority").val();
	params["category"] = $("#search_category").val();
	params["version"] = $("#search_version").val();
	params["milestone"] = $("#search_milestone").val();
	params["targetMember"] = $("#search_targetMember").val();
	
	params["title"] = $("#search_title").val();
	params["fromPeriod"] = unFormatDate($("#search_fromPeriod").val());
	params["toPeriod"] = unFormatDate($("#search_toPeriod").val());
	params["status"] = new Array();
	$("input[type='checkbox'][name='search_status']").each(function(index){
		if($(this).prop("checked") == true) {
			params["status"].push($(this).val());
		}
	});
	return params;
}

//Ticket削除
function deleteTicket(title, keyToString, version) {
	if(window.confirm("チケット「" + title + "」を削除します。本当によろしいですか？") == false) {
		return;
	}
	
	var params = {};
	params["keyToString"] = keyToString;
	params["versionNo"] = version;
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketDelete",
		data: params
	});
	
	//後処理の登録
	//
	task.pipe(
		function(data) {
			//共通エラーチェック
			errorCheck(data);
			//メッセージを表示して、戻る
			infoCheck(data);
			return reSearchAndRender();
		}
	);
}
