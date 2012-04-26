$(function(){
	
	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});
	
	initTicketDialog();
	initTodoDialog();
	
	searchAndRender();
});

//再表示処理
//ダイアログからの最表示時に呼び出されます
function refresh() {
	searchAndRender();
}

//検索＆レンダリング
function searchAndRender() {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/bts/dashboard/ajax/dashboardList"
	}).then(
		function(data) {
			render(data);
		}
	);
}

//一覧表示
function render(data) {
	$("#result_area").empty();

	//共通エラーチェック
	if(errorCheck(data) == false) {
		return;
	}

	//tokenの設定
	$("#token").val(data.token);
	
	var viewMsg = false;
	if(data.result.viewProjectList == true) {
		//TODOListを書き込む
		writeTodoList(data.result.todoList);
		//プロジェクトListを書き込む
		writeProjectList(data.result.projectList);
		
		if(data.result.todoList.length == 0 && data.result.projectList.length == 0) {
			viewMsg = true;
		}
		
	} else {
		//TicketListを書き込む
		writeTicketList(data.result.ticketList);
		//TODOListを書き込む
		writeTodoList(data.result.todoList);
		if(data.result.ticketList.length == 0 && data.result.todoList.length == 0) {
			viewMsg = true;
		}
	}

	//1件も表示するデータが存在しない場合、メッセージを表示
	if(viewMsg == true) {
		msg = "表示するデータは存在しません。";
		var t = $.toaster({showTime:1000, centerX:true, centerY:true});
		t.toast(msg);
	}
}

//プロジェクト一覧描画
function writeProjectList(list) {
	if(list.length == 0) {
		return;
	}
	//一覧をレンダリング
	var $table = $("<table />").addClass("table table-bordered result_table");
	var $thead = $("<thead />").append($("<tr />")
				.append($("<th />").text("プロジェクト名").attr({width:"40%"}))
				.append($("<th />").text("プロジェクト概要"))
			);
	$table.append($thead);
	
	var $tbody = $("<tbody />");
	$.each(list, function(){
		var keyToString = this.model.keyToString;
		var projectName = this.model.projectName;
		var projectSummaryView = this.projectSummaryView;

		var $a = $("<a />").attr({href:"javascript:void(0)"}).text(projectName);
		$a.click(function(){
			moveUrl("/changeProject?projectKey=" + keyToString);
		});

		var $tr = $("<tr />");
		$tr.append($("<td />").append($a))
			.append($("<td />").html(projectSummaryView));
		$tbody.append($tr)
	});
	$table.append($tbody);

	var $h = $("<h2 />").addClass("title").text("参加プロジェクト");
	$("#result_area").append($h).append($table);
	
}

//TODO一覧描画
function writeTodoList(list) {
	if(list.length == 0) {
		return;
	}

	var $table = $("<table />").addClass("table table-bordered result_table");
	var $thead = $("<thead />").append($("<tr />")
				.append($("<th />").text("ステータス").attr({width:"80px"}))
				.append($("<th />").text("件名"))
				.append($("<th />").text("期限").attr({width:"100px"}))
			);
	$table.append($thead);
	
	var $tbody = $("<tbody />");
	$.each(list, function(){
		
		var keyToString = this.model.keyToString;
		var todoStatus = this.todoStatus;
		var title = this.model.title;
		var versionNo = this.model.version;
		var period = this.period;
		var createdAt = this.createdAt;
		var periodStatusLabel = this.periodStatusLabel;
		var periodStatusCode = this.periodStatusCode;

		var $a = $("<a />").attr({href:"javascript:void(0)"}).text(title);
		$a.click(function(){
			openDetailTodoDialog(keyToString);
		});
		
		var $todoStatusSpan = $("<span />").text(todoStatus);
		var $periodStatusSpan = $("<span />");
		if(periodStatusCode != '') {
			$periodStatusSpan.text(periodStatusLabel);
			if(periodStatusCode == '1') {
				$periodStatusSpan = $periodStatusSpan.addClass("label label-warning");
			} else {
				$periodStatusSpan = $periodStatusSpan.addClass("label label-important");
			}
		}
		var $statusDiv = $("<div />").append($todoStatusSpan).append($("<br />")).append($periodStatusSpan);
		
		var $tr = $("<tr />");
		$tr.append($("<td />").append($statusDiv))
			.append($("<td />").append($a))
			.append($("<td />").text(formatDateyyyyMMdd(period)));
		$tbody.append($tr)
	});
	$table.append($tbody);
	
	var cnt = $("#dashboard_limit_cnt").val();
	var $h = $("<h2 />").addClass("title").text("未完了TODO(上位" + cnt + "件)");
	$("#result_area").append($h).append($table);
}

//Ticket一覧描画
function writeTicketList(list) {
	if(list.length == 0) {
		return;
	}
	
	//一覧をレンダリング
	var $table = $("<table />").addClass("table table-bordered result_table");
	var $thead = $("<thead />").append($("<tr />")
				.append($("<th />").text("チケットNo").attr({width:"80px"}))
				.append($("<th />").text("ステータス").attr({width:"120px"}))
				.append($("<th />").text("件名"))
				.append($("<th />").text("期限").attr({width:"100px"}))
			);
	$table.append($thead);
	
	var $tbody = $("<tbody />");
	$.each(list, function(){
		var keyToString = this.model.keyToString;
		var status = this.model.status;
		var title = this.model.title;
		var memberName = this.targetMemberName;
		var id = this.model.no;
		var versionNo = this.model.version;
		var period = this.period;
		var createdAt = this.createdAt;
		var periodStatusLabel = this.periodStatusLabel;
		var periodStatusCode = this.periodStatusCode;

		var $a = $("<a />").attr({href:"javascript:void(0)"}).text(id);
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
			.append($("<td />").text(formatDateyyyyMMdd(period)));
		$tbody.append($tr)
	});
	$table.append($tbody);

	var selectedProjectName =  $("#targetProjects option:selected").text();
	var cnt = $("#dashboard_limit_cnt").val();
	var $h = $("<h2 />").addClass("title").text("「" + selectedProjectName + "」の未完了チケット(上位" + cnt + "件)");
	$("#result_area").append($h).append($table);

}

