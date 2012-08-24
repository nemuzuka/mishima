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
		searchTicket();
	});

	$("#nav_create").on("click", function(){
		moveUrl("/mobile/bts/ticket/edit?keyToString=&dashbord=false");
	});

	init();
});

//初期表示時処理
function init() {
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

	var form = data.result;

	//検索条件初期値をWebstorageより設定
	var ticket_search_param = JSON.parse(sessionStorage.getItem("ticket_search_param"));
	if(ticket_search_param == null) {
		ticket_search_param = {};
		ticket_search_param["status"] = form.status;
	}
	var ticketMst = data.result.ticketMst;

	$("#ticket_status").empty();
	$.each(ticketMst.searchStatusList, function() {
		$("#ticket_status").append($('<option>').attr({ value: this.value }).text(this.label));
	});
	$("#ticket_status").val(ticket_search_param["status"]);
	$("#ticket_status").selectmenu('refresh', true);

	$("#targetMember").empty();
	$.each(ticketMst.memberList, function() {
		$("#targetMember").append($('<option>').attr({ value: this.value }).text(this.label));
	});
	$("#targetMember").val(ticket_search_param["targetMember"]);
	$("#targetMember").selectmenu('refresh', true);
	
	$("#search_title").val(ticket_search_param["title"]);
	$("#search_fromPeriod").val(formatDateyyyyMMdd(ticket_search_param["fromPeriod"]));
	$("#search_toPeriod").val(formatDateyyyyMMdd(ticket_search_param["toPeriod"]));
}

//チケット検索
function searchTicket() {
	var params = createSearchTicketParams();
	
	//validate
	var v = new Validate();
	v.addRules({value:params["fromPeriod"],option:'date',error_args:"期限From"});
	v.addRules({value:params["toPeriod"],option:'date',error_args:"期限To"});
	if(v.execute() == false) {
		return;
	}

	sessionStorage.setItem("ticket_search_param", JSON.stringify(params));
	moveUrl("/mobile/bts/ticket/list");
}

//チケット検索条件設定
function createSearchTicketParams() {
	var params = {};
	params["title"] = $("#ticket_title").val();
	params["targetMember"] = $("#targetMember").val();
	params["fromPeriod"] = unFormatDate($("#search_fromPeriod").val());
	params["toPeriod"] = unFormatDate($("#search_toPeriod").val());
	params["status"] = getSelectArray("ticket_status");
	return params;
}
