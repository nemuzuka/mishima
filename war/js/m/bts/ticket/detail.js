$(function(){

	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});

	$("#back").on("click", function(){
		back();
	});
	
	$("#detail_ticket_status").on("change", function(){
		changeTicketStatus();
	});
	
	$("#addComment").on("click", function(){
		moveUrl("/mobile/bts/ticket/comment");
	});

	$("#nav_back").on("click", function(){
		back();
	});

	$("#nav_edit").on("click", function(){
		var dashbord = $("#dashbord").val();
		var keyToString = $("#keyToString").val();
		moveUrl("/mobile/bts/ticket/edit?keyToString=" + keyToString + "&dashbord=" + dashbord);
	});

	$("#nav_delete").on("click", function(){
		deleteTicket();
	});
	
	executeSearch();
});

//詳細に表示する情報を取得し、表示する
function executeSearch() {
	setAjaxDefault();
	var params = {};
	params["keyToString"] = $("#keyToString").val();

	return $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketDetailInfo",
		data: params
	}).then(
		function(data) {
			render(data);
		}
	);
}

//詳細情報表示
function render(data) {
	if(errorCheck(data) == false) {
		return back();
	}
	
	//tokenの設定
	$("#token").val(data.token);
	
	//form情報の設定
	var form = data.result.form;
	
	var ticketNo = "";
	if(form.projectId != null && form.projectId != '') {
		ticketNo = ticketNo + form.projectId + "-";
	}
	$("#detail_ticket_no").text("(" + ticketNo + form.id + ")");
	
	$("#detail_ticket_status").empty();
	$.each(form.ticketMst.statusList, function(){
		$("#detail_ticket_status").append($("<option />").attr({value:this.value}).text(this.label));
	});
	$("#detail_ticket_status").val(form.status);
	$("#detail_ticket_status").selectmenu("refresh");
	
	$("#detail_ticket_title").text(defaultString4Init(form.title, "　"));
	$("#detail_ticket_content").html(defaultString4Init(data.result.contentView, "　"));
	$("#detail_ticket_endCondition").html(defaultString4Init(data.result.endConditionView, "　"));
	$("#detail_ticket_startDate").text(defaultString4Init(formatDateyyyyMMdd(form.startDate), "　"));
	$("#detail_ticket_period").text(defaultString4Init(formatDateyyyyMMdd(form.period), "　"));

	
	$("#detail_ticket_priority").text(defaultString4Init(form.priority, "　"));
	$("#detail_ticket_category").text(defaultString4Init(form.category, "　"));
	
	$("#detail_ticket_milestone").text(defaultString4Init(data.result.milestone, "　"));
	$("#detail_ticket_kind").text(defaultString4Init(form.targetKind, "　"));
	$("#detail_ticket_targetVersion").text(defaultString4Init(form.targetVersion, "　"));
	$("#detail_ticket_targetMember").text(defaultString4Init(data.result.targetMember, "　"));

	$("#detail_ticket_versionNo").val(form.versionNo);
	
	//コメント再描画
	renderTodoCommentList(data.result.commentList);

	//チケット詳細をWebストレージに格納
	sessionStorage.setItem("ticket_detail", JSON.stringify(data));
	sessionStorage.setItem("dashbord", $("#dashbord").val());
	sessionStorage.setItem("keyToString", $("#keyToString").val());
	return;
}

//コメント再描画
function renderTodoCommentList(list) {
	$("#comment_table").empty();

	if(list.length == 0) {
		return;
	}
	
	var $tbody = $("<tbody />");
	$.each(list, function(){
		
		var createdAt = this.createdAt;
		var comment = this.comment;
		var keyToString = this.model.keyToString;
		var versionNo = this.model.version;
		var createMemberName = this.createMemberName;

		var $tr = $("<tr />");
		$tr.append($("<td />").html(comment))
		    .append($("<td />").text(createMemberName));
		$tbody.append($tr)
	});
	$("#comment_table").append($tbody);
}

//ステータス変更
function changeTicketStatus() {
	
	var params = {};
	params["status"] = $("#detail_ticket_status").val();
	params["versionNo"] = $("#detail_ticket_versionNo").val();
	params["keyToString"] = $("#keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();

	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketStatusExecute",
		data: params
	});

	//後処理の登録
	//
	task.pipe(
		function(data) {
			//共通エラーチェック
			if(errorCheck(data) == false) {
				return back();
			}
			infoCheck(data);
			setTimeout('refresh()', 2000);
		}
	);
}

//チケット削除
function deleteTicket() {
	var name = $("#detail_ticket_title").text();
	if(window.confirm("チケット「" + name + "」を削除します。本当によろしいですか？") == false) {
		return;
	}
	
	var params = {};
	params["keyToString"] = $("#keyToString").val();
	params["versionNo"] = $("#detail_ticket_versionNo").val();
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
			setTimeout('back()', 2000);
		}
	);
}


//詳細画面最表示
function refresh() {
	var dashbord = $("#dashbord").val();
	var keyToString = $("#keyToString").val();
	moveUrl("/mobile/bts/ticket/detail?keyToString=" + keyToString + "&dashbord=" + dashbord);
}

//ダッシュボードからの遷移の場合、ダッシュボード画面へ遷移します
//一覧からの遷移の場合、一覧画面へ遷移します
function back() {
	var dashbord = $("#dashbord").val();
	var keyToString = $("#keyToString").val();
	if(dashbord == 'true') {
		moveUrl("/");
	} else {
		moveUrl("/mobile/bts/ticket/list");
	}
}