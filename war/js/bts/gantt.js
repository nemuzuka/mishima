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
	var ganttList = new Array();
	//マイルストーンが設定されている場合、ガントチャートに追加
	if(data.result.milestoneName != null && data.result.milestoneName != '') {
		var obj = {};
		obj.model = {};
		obj.model.no = null;
		obj.model.keyToString = "";
		obj.model.title = data.result.milestoneName;
		obj.model.status = "";
		obj.targetMemberName = "";
		obj.startDate = data.result.milestoneStartDate;
		obj.period = data.result.milestoneEndDate;
		obj.periodStatusLabel = "";
		obj.periodStatusCode = "";
		obj.updateStartDate = data.result.updateStartDate;
		obj.updatePeriod = data.result.updateEndDate;
		obj.nestingLevel = 0;
		ganttList.push(createGanttEntity(obj, true));
	}

	$.each(ticketList, function(){
		ganttList.push(createGanttEntity(this, false));
	});

	var startDate = parseDate(data.result.startDate);
	var endDate = parseDate(data.result.endDate);
	
	$("#result_area").ganttView({
		data: ganttList,
		start: startDate,
		end: endDate,
		slideWidth: 450,
		behavior: {
			clickable: false,
			draggable: false,
			resizable: false,
		}
	});
	
	var left_size = getMoveLeft();
	if(left_size != 0) {
		//スクロールバーを移動
		var $targetDiv = $(".ganttview-slide-container");
		var offset = $targetDiv.offset();
		$targetDiv.scrollLeft(left_size - (offset.left + 30));
	}
}

//チャートの中に当日が存在する場合、その位置を返却します
//当日が存在しない場合、0を返却します
function getMoveLeft() {
	var dataFormat = new DateFormat("yyyyMMdd");
	var nowDate = new Date();
	var className = "date_" + dataFormat.format(nowDate);
	var $target = 
		$("." + className).offset();
	if($target == null) {
		return 0;
	}
	return $target.left;
}


/**
 * ガントチャート要素作成.
 * 開始日、終了日の入力は必須の想定です。
 */
function createGanttEntity(param, isMilestone) {
	var obj = {};
	obj.id = param.model.keyToString;
	obj.name = param.model.title;
	obj.no = param.model.no;
	
	var serie = {};
	serie.name = param.targetMemberName;
	serie.status = param.model.status;
	serie.start = parseDate(param.startDate);
	serie.end = parseDate(param.period);
	serie.periodStatusLabel = param.periodStatusLabel;
	serie.periodStatusCode = param.periodStatusCode;
	serie.milestone = isMilestone;
	serie.updateStartDate = param.updateStartDate;
	serie.updatePeriod = param.updatePeriod;
	serie.nestingLevel = param.nestingLevel;
	
	//背景色の設定
	if(isMilestone) {
		//マイルストーンの場合
		if(param.updateStartDate == false && param.updatePeriod == false) {
			//開始日・終了日両方設定されている場合
			serie.color = "milestone milestone-both";
		} else if(param.updateStartDate == false) {
			//開始日のみ設定されている場合
			serie.color = "milestone milestone-onlyStart";
		} else if(param.updatePeriod == false) {
			//終了日のみ設定されている場合
			serie.color = "milestone milestone-onlyEnd";
		} else {
			//両方共設定されていない場合
			serie.color = "milestone gantt-transparent";
		}
	} else {
		//Ticketの場合
		var colorCss = "";
		if(param.closeStatus == true) {
			colorCss = "close";
		} else {
			colorCss = "open";
			if(param.periodStatusCode == '1') {
				colorCss = "today";
			} else if(param.periodStatusCode == '2') {
				colorCss = "periodDate";
			}
		}
		
		if(param.updateStartDate == false && param.updatePeriod == false) {
			//開始日・終了日両方設定されている場合
			colorCss = colorCss + "-ticket-both";
		} else if(param.updateStartDate == false) {
			//開始日のみ設定されている場合
			colorCss = colorCss + "-ticket-onlyStart";
		} else if(param.updatePeriod == false) {
			//終了日のみ設定されている場合
			colorCss = colorCss + "-ticket-onlyEnd";
		} else {
			//両方共設定されていない場合
			colorCss = "gantt-transparent";
		}
		serie.color = colorCss;
	}
	
	var series = new Array();
	series.push(serie);
	obj.series = series;
	return obj;
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
	//何もしない
//	reSearchAndRender();
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
