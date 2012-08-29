$(function(){

	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});
	sessionStorage.clear();
	executeSearch();
});

//ダッシュボードに表示する情報を取得し、表示する
function executeSearch() {
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
	
	var $writeList = null;
	if(data.result.viewProjectList == true) {
		//TODOListを書き込む
		$writeList = writeTodoList(data.result.todoList, $writeList);
		//プロジェクトListを書き込む
		$writeList = writeProjectList(data.result.projectList, $writeList);
	} else {
		//TicketListを書き込む
		$writeList = writeTicketList(data.result.ticketList, $writeList);
		//TODOListを書き込む
		$writeList = writeTodoList(data.result.todoList, $writeList);
	}

	//1件も表示するデータが存在しない場合、メッセージを表示
	if($writeList == null) {
		var $div = $("<div />").text("表示するデータは存在しません。");
		$("#result_area").append($div);
	} else {
		$("#result_area").append($writeList);
		$writeList.listview();
	}
}

//プロジェクト一覧描画
function writeProjectList(list, $writeList) {
	if(list.length == 0) {
		return $writeList;
	}

	var $ul = $writeList;
	if($ul == null) {
		$ul = $("<ul />").attr({"data-role":"listview","data-inset":"true" });
	}

	$.each(list, function(index){
		if(index == 0) {
			var $li = $("<li />").attr({"data-role":"list-divider"});
			$li.text("参加プロジェクト");
			$ul.append($li);
		}

		var keyToString = this.model.keyToString;
		var projectName = this.model.projectName;
		var projectSummaryView = this.projectSummaryView;

		var $a = $("<a />").attr({href: "#"});
		$a.on("click", function(){
			moveUrl("/changeProject?projectKey=" + keyToString + "&mobile=true");
		})

		var $h1 = $("<h1 />").text(projectName);
		var $projectSummaryP = $("<p />").html(projectSummaryView);
		$a.append($h1).append($projectSummaryP);

		$ul.append($("<li />").append($a));
	});
	
	return $ul;
}

//TODO一覧描画
function writeTodoList(list, $writeList) {
	if(list.length == 0) {
		return $writeList;
	}
	
	var $ul = $writeList;
	if($ul == null) {
		$ul = $("<ul />").attr({"data-role":"listview","data-inset":"true" });
	}
	
	var cnt = list.length;
	$.each(list, function(index){

		if(index == 0) {
			var $li = $("<li />").attr({"data-role":"list-divider"});
			$li.text("未完了TODO(上位" + cnt + "件)");
			$ul.append($li);
		}

		var keyToString = this.model.keyToString;
		var todoStatus = this.todoStatus;
		var title = this.model.title;
		var versionNo = this.model.version;
		var period = this.period;
		var periodStatusLabel = this.periodStatusLabel;
		var periodStatusCode = this.periodStatusCode;

		var $a = $("<a />").attr({href: "#"});
		$a.on("click", function(){
			moveUrl("/mobile/bts/todo/detail?keyToString=" + keyToString + "&dashbord=true");
		})
		
		var $todoStatusSpan = $("<span />").text(todoStatus).addClass("label label-info");
		var $periodStatusSpan = $("<span />");
		if(periodStatusCode != '') {
			$periodStatusSpan.text(periodStatusLabel);
			if(periodStatusCode == '1') {
				$periodStatusSpan = $periodStatusSpan.addClass("label label-warning");
			} else {
				$periodStatusSpan = $periodStatusSpan.addClass("label label-important");
			}
		}
		var $periodSpan = "";
		if(period != null && period != "") {
			$periodSpan = $("<span />").text(formatDateyyyyMMdd(period));
		}
		
		var $h1 = $("<h1 />").text(title);
		var $p = $("<p />").append($todoStatusSpan).append("　").append($periodStatusSpan);
		var $p_aside = $("<p />").append($periodSpan).addClass("ui-li-aside");
		$a.append($h1).append($p).append($p_aside);

		$ul.append($("<li />").append($a));
	});
	return $ul;
}

//Ticket一覧描画
function writeTicketList(list, $writeList) {
	if(list.length == 0) {
		return $writeList;
	}
	
	var $ul = $writeList;
	if($ul == null) {
		$ul = $("<ul />").attr({"data-role":"listview","data-inset":"true" });
	}
	var cnt = list.length;

	$.each(list, function(index){
		
		if(index == 0) {
			var $li = $("<li />").attr({"data-role":"list-divider"});
			$li.text("未完了担当チケット(上位" + cnt + "件)");
			$ul.append($li);
		}
		
		var keyToString = this.model.keyToString;
		var status = this.model.status;
		var title = this.model.title;
		var memberName = this.targetMemberName;
		var viewNo = this.viewNo;
		var versionNo = this.model.version;
		var period = this.period;
		var createdAt = this.createdAt;
		var periodStatusLabel = this.periodStatusLabel;
		var periodStatusCode = this.periodStatusCode;

		var $a = $("<a />").attr({href: "#"});
		$a.on("click", function(){
			moveUrl("/mobile/bts/ticket/detail?keyToString=" + keyToString + "&dashbord=true");
		})
		
		var $viewNoSpan = $("<span />").text(viewNo);
		var $statusSpan = $("<span />").text(status).addClass("label label-info");
		var $periodStatusSpan = $("<span />");
		if(periodStatusCode != '') {
			$periodStatusSpan.text(periodStatusLabel);
			if(periodStatusCode == '1') {
				$periodStatusSpan = $periodStatusSpan.addClass("label label-warning");
			} else {
				$periodStatusSpan = $periodStatusSpan.addClass("label label-important");
			}
		}
		var $periodSpan = "";
		if(period != null && period != "") {
			$periodSpan = $("<span />").text(formatDateyyyyMMdd(period));
		}

		var $h1 = $("<h1 />").text(title);
		var $p = $("<p />").append($statusSpan).append("　").append($periodStatusSpan);
		var $p_aside = $("<p />").append($viewNoSpan).append("<br />").append($periodSpan).addClass("ui-li-aside");
		$a.append($h1).append($p).append($p_aside);

		$ul.append($("<li />").append($a));
	});
	return $ul;
}
