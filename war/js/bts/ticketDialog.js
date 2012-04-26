function initTicketDialog() {
	$("#ticketDialog").dialog({
		modal:true,
		autoOpen:false,
		width:850,
		resizable:false,
		open:function(event) {
			document.body.style.overflow = "hidden";
		},
		close:function(event) {
			//詳細ダイアログを開いている場合、overflowの変更はしない
			if($("ticketDetailDialog").dialog("isOpen") == true) {
				return;
			}
			document.body.style.overflow = "visible";
		}
	});
	$("#ticketDetailDialog").dialog({
		modal:true,
		autoOpen:false,
		width:850,
		resizable:false,
		open:function(event) {
			document.body.style.overflow = "hidden";
		},
		close:function(event) {
			document.body.style.overflow = "visible";
			//詳細ダイアログを閉じたタイミングで一覧再描画
			refresh();
		}
	});
	$("#ticketCommentDialog").dialog({
		modal:true,
		autoOpen:false,
		width:500,
		resizable:false
	});
	$("#ticketSummaryDialog").dialog({
		modal:true,
		autoOpen:false,
		width:600,
		resizable:false
	});

	//Ticket登録・更新ダイアログ
	$.datepicker.setDefaults($.extend($.datepicker.regional['ja']));
	$("#edit_ticket_period").datepicker();

	$("#ticketDialog-add").click(function(){
		executeTicket();
	});
	$("#ticketDialog-cancel").click(function(){
		$("#ticketDialog").dialog("close");
	});
	
	//Ticket詳細ダイアログ
	$("#ticketDetailDialog-cancel").click(function(){
		$("#ticketDetailDialog").dialog("close");
	});
	$("#ticketDetail-edit").click(function(){
		openEditTicketDialog($("#detail_ticket_keyToString").val())
	})
	$("#detail_ticket_status").change(function(){
		changeTicketStatus();
	});
	$("#ticketDetail-Comment-add").click(function(){
		openTicketDetailCommentDialog();
	});
	$("#ticketDetail-child-add").click(function(){
		var ticketNo = $("#detail_ticket_no").text();
		var baseKey = $("#detail_ticket_keyToString").val();
		openEditTicketDialog("", ticketNo, baseKey, "child");
	});
	$("#ticketDetail-copy-add").click(function(){
		$("#ticketDetailDialog").dialog("close");
		var baseKey = $("#detail_ticket_keyToString").val();
		openEditTicketDialog("", "", baseKey, "copyIns");
	});
	
	//Ticketコメントダイアログ
	$("#ticketCommentDialog-add").click(function(){
		executeTicketComment();
	});
	$("#ticketCommentDialog-cancel").click(function(){
		$("#ticketCommentDialog").dialog("close");
	});
	
	//Ticket概要ダイアログ
	$("#ticketSummary-detail").click(function(){
		var key = $("#detail_summary_keyToString").val();
		$("#ticketSummaryDialog").dialog("close");
		openDetailTicketDialog(key);
	});
	$("#ticketSummaryDialog-cancel").click(function(){
		$("#ticketSummaryDialog").dialog("close");
	});
}

//Ticketコメント登録
function executeTicketComment() {
	var params = {};
	params["status"] = $("#edit_ticket_comment_status").val();
	params["comment"] = $("#edit_ticket_comment").val();
	params["versionNo"] = $("#edit_ticket_comment_versionNo").val();
	params["keyToString"] = $("#edit_ticket_comment_keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketCommentExecute",
		data: params
	});
	
	//後処理の登録
	//
	task.pipe(
		function(data) {

			var key = $("#edit_ticket_comment_keyToString").val();
			
			//共通エラーチェック
			if(errorCheck(data) == false) {
				if(data.status == -1 ) {
					//validateの場合、tokenを再発行
					return reSetToken();
				} else {
					//強制的にダイアログを閉じて、再検索
					$("#ticketCommentDialog").dialog("close");
					return openDetailTicketDialog(key, true);
				}
				return;
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			$("#ticketCommentDialog").dialog("close");
			
			//詳細ダイアログリフレッシュ(メッセージを見せる必要上、1秒sleepしてから実行)
			setTimeout(function(){ openDetailTicketDialog(key, true) }, 1000);
		}
	);
}


//TicketコメントダイアログOpen
function openTicketDetailCommentDialog() {
	$("#edit_ticket_comment_status").val($("#detail_ticket_status").val());
	$("#edit_ticket_comment_versionNo").val($("#detail_ticket_versionNo").val());
	$("#edit_ticket_comment_keyToString").val($("#detail_ticket_keyToString").val());
	$("#edit_ticket_comment").val("");
	$("#ticketCommentDialog").dialog("open");
}

//ステータス変更
function changeTicketStatus() {
	
	var params = {};
	params["status"] = $("#detail_ticket_status").val();
	params["versionNo"] = $("#detail_ticket_versionNo").val();
	params["keyToString"] = $("#detail_ticket_keyToString").val();
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
				if(data.status == -1 ) {
					//validateの場合、tokenを再発行
					return reSetToken();
				} else {
					//強制的にダイアログを閉じて、再検索
					$("#ticketDetailDialog").dialog("close");
					return;
				}
				return;
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			
			var key = $("#detail_ticket_keyToString").val();
			//詳細ダイアログリフレッシュ(メッセージを見せる必要上、1秒sleepしてから実行)
			setTimeout(function(){ openDetailTicketDialog(key, true) }, 1000);
		}
	);
}

//Ticket登録・更新
function executeTicket() {
	var params = createExecuteTicketParams();
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketExecute",
		data: params
	});
	
	//後処理の登録
	//
	task.pipe(
		function(data) {
			//共通エラーチェック
			if(errorCheck(data) == false) {
				if(data.status == -1 ) {
					//validateの場合、tokenを再発行
					return reSetToken();
				} else {
					//強制的にダイアログを閉じて、再検索
					$("#ticketDialog").dialog("close");
					return refresh();
				}
				return;
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			$("#ticketDialog").dialog("close");
			
			var key = $("#edit_ticket_keyToString").val();
			if(key == '') {
				//基本Keyが未設定で、ベースKeyが設定されている場合、ベースKeyに紐付く詳細情報を参照する
				key = $("#edit_ticket_base_keyToString").val();
			}
			if(key == '') {
				//新規の場合、一覧再描画
				return refresh();
			} else {
				//更新の場合、詳細ダイアログオープン(メッセージを見せる必要上、1秒sleepしてから開く)
				setTimeout(function(){ openDetailTicketDialog(key, true) }, 1000);
			}
		}
	);
}

//Ticket登録パラメータ設定
function createExecuteTicketParams() {
	var params = {};
	params["status"] = $("#edit_ticket_status").val();
	params["title"] = $("#edit_ticket_title").val();
	params["content"] = $("#edit_ticket_content").val();
	params["endCondition"] = $("#edit_ticket_endCondition").val();
	params["period"] = unFormatDate($("#edit_ticket_period").val());
	
	params["priority"] = $("#edit_ticket_priority").val();
	params["targetKind"] = $("#edit_ticket_kind").val();
	params["category"] = $("#edit_ticket_category").val();
	params["targetVersion"] = $("#edit_ticket_targetVersion").val();
	params["milestone"] = $("#edit_ticket_milestone").val();
	params["targetMember"] = $("#edit_ticket_targetMember").val();
	params["parentKey"] = $("#edit_ticket_parentKey").val();
	
	params["versionNo"] = $("#edit_ticket_versionNo").val();
	params["keyToString"] = $("#edit_ticket_keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	return params;
}


/** 
 * ticketダイアログオープン.
 * @param key 対象Key
 * @param ticketNo チケットNo(子Ticket作成時に使用)
 * @param baseKey 基準Key
 * @param type "child":子Ticket作成/"copyIns":コピー新規
 * 
 */
function openEditTicketDialog(key, ticketNo, baseKey, type) {
	var title = "";
	var buttonLabel = "";
	if(key == '') {
		title = "チケット登録";
		buttonLabel = "登録する";
	} else {
		title = "チケット変更";
		buttonLabel = "変更する";
	}
	$("#ui-dialog-title-ticketDialog").empty();
	$("#ui-dialog-title-ticketDialog").append(title);
	$("#ticketDialog-add").attr({value:buttonLabel});
	
	var params = {};
	params["keyToString"] = key;
	
	if(ticketNo == undefined) {
		ticketNo = "";
	}
	if(baseKey == undefined) {
		baseKey = "";
	}
	if(type == undefined) {
		type = "";
	}
	//コピー新規の場合、初期値をベースKeyに紐付くものを更新
	if(baseKey != '' && type == 'copyIns') {
		params["keyToString"] = baseKey;
	}
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketEditInfo",
		data: params
	});
	
	//後処理の登録
	//表示対象データが存在せず、再検索を行える場合、再検索をします。
	task.pipe(
		function(data) {
			
			if(errorCheck(data) == false) {
				if(data.status == -6) {
					//表示対象データが存在しない場合、再検索して表示
					return refresh();
				}
				return;
			}
			//tokenの設定
			$("#token").val(data.token);
			
			//form情報の設定
			var form = data.result;
			
			$("#edit_ticket_status").empty();
			$.each(form.ticketMst.statusList, function(){
				$("#edit_ticket_status").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#edit_ticket_status").val(form.status);

			$("#edit_ticket_title").val(form.title);
			$("#edit_ticket_content").val(form.content);
			$("#edit_ticket_endCondition").val(form.endCondition);
			$("#edit_ticket_period").val(formatDateyyyyMMdd(form.period));

			$("#edit_ticket_priority").empty();
			$.each(form.ticketMst.priorityList, function(){
				$("#edit_ticket_priority").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#edit_ticket_priority").val(form.priority);

			$("#edit_ticket_kind").empty();
			$.each(form.ticketMst.kindList, function(){
				$("#edit_ticket_kind").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#edit_ticket_kind").val(form.targetKind);

			$("#edit_ticket_category").empty();
			$.each(form.ticketMst.categoryList, function(){
				$("#edit_ticket_category").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#edit_ticket_category").val(form.category);

			$("#edit_ticket_targetVersion").empty();
			$.each(form.ticketMst.versionList, function(){
				$("#edit_ticket_targetVersion").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#edit_ticket_targetVersion").val(form.targetVersion);

			$("#edit_ticket_milestone").empty();
			$.each(form.ticketMst.milestoneList, function(){
				$("#edit_ticket_milestone").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#edit_ticket_milestone").val(form.milestone);

			$("#edit_ticket_targetMember").empty();
			$.each(form.ticketMst.memberList, function(){
				$("#edit_ticket_targetMember").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#edit_ticket_targetMember").val(form.targetMember);
			$("#edit_ticket_parentKey").val(form.parentKey);

			$("#edit_ticket_versionNo").val(form.versionNo);
			$("#edit_ticket_keyToString").val(form.keyToString);
			$("#edit_ticket_base_keyToString").val("");

			if(type == "child") {
				//子チケット作成の場合
				$("#edit_ticket_versionNo").val("");
				$("#edit_ticket_keyToString").val("");
				$("#edit_ticket_base_keyToString").val(baseKey);
				$("#edit_ticket_parentKey").val(ticketNo);
			} else if(type=="copyIns") {
				//コピー新規の場合
				$("#edit_ticket_versionNo").val("");
				$("#edit_ticket_keyToString").val("");
				$("#edit_ticket_base_keyToString").val("");
			}
			
			$("#ticketDialog").dialog("open");
			return;
		}
	);

}

//Ticket詳細ダイアログオープン
function openDetailTicketDialog(key, onlyRefresh) {
	
	if(onlyRefresh == undefined) {
		onlyRefresh = false;
	}
	var params = {};
	params["keyToString"] = key;
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketDetailInfo",
		data: params
	});
	
	//後処理の登録
	//表示対象データが存在せず、再検索を行える場合、再検索をします。
	task.pipe(
		function(data) {
			
			if(errorCheck(data) == false) {
				if(data.status == -6) {
					//表示対象データが存在しない場合、再検索して表示
					return refresh();
				}
				return;
			}
			
			//tokenの設定
			$("#token").val(data.token);
			
			//form情報の設定
			var form = data.result.form;
			
			$("#detail_ticket_no").text(form.id);
			//コメント用のステータス変更にもステータス構成情報を設定する
			$("#detail_ticket_status").empty();
			$("#edit_ticket_comment_status").empty();
			$.each(form.ticketMst.statusList, function(){
				$("#detail_ticket_status").append($("<option />").attr({value:this.value}).text(this.label));
				$("#edit_ticket_comment_status").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#detail_ticket_status").val(form.status);

			$("#detail_ticket_title").text(defaultString4Init(form.title, "　"));
			$("#detail_ticket_content").html(defaultString4Init(data.result.contentView, "　"));
			$("#detail_ticket_endCondition").html(defaultString4Init(data.result.endConditionView, "　"));
			$("#detail_ticket_period").text(defaultString4Init(formatDateyyyyMMdd(form.period), "　"));

			
			$("#detail_ticket_priority").text(defaultString4Init(form.priority, "　"));
			$("#detail_ticket_category").text(defaultString4Init(form.category, "　"));
			
			$("#detail_ticket_milestone").text(defaultString4Init(data.result.milestone, "　"));
			$("#detail_ticket_kind").text(defaultString4Init(form.targetKind, "　"));
			$("#detail_ticket_targetVersion").text(defaultString4Init(form.targetVersion, "　"));
			$("#detail_ticket_targetMember").text(defaultString4Init(data.result.targetMember, "　"));

			$("#detail_ticket_versionNo").val(form.versionNo);
			$("#detail_ticket_keyToString").val(form.keyToString);

			//コメント再描画
			renderTicketCommentList(data.result.commentList);

			//関連情報描画
			renderTicketConn(data.result);
			
			if(onlyRefresh == false) {
				$("#ticketDetailDialog").dialog("open");
			}
			$("#ticketDetailDialog .dialog-container .scroll_area").scrollTop(0);
			$(".scroll_area").scrollTop(0);
			return;
		}
	);
}

//関連情報描画
function renderTicketConn(result) {
	$("#detail_ticket_conn_area").empty();
	
	var connArray = new Array();
	if(result.parentTicket != null) {
		var parentTicket = result.parentTicket;
		var obj = { 'keyToString': parentTicket.keyToString, 
				'title': parentTicket.title, 'id':parentTicket.id, 
				'type':"親", 'status':parentTicket.status};
		connArray.push(obj);
	}
	
	$.each(result.childTicketList, function(){
		var obj = { 'keyToString': this.keyToString, 'title': this.title, 'id':this.id, 
				'type':"子", 'status':this.status};
		connArray.push(obj);
	});
	
	if(connArray.length == 0) {
		return;
	}
	
	var $dl = $("<dl />");
	var $dt = $("<dt />").text("関連");
	
	var $table = $("<table />").addClass("table table-bordered result_table comment_list_table");
	var $tbody = $("<tbody />");
	
	
	$.each(connArray, function(){
		var $a = $("<a />").attr({href:"javascript:void(0)"}).text(this.id).addClass("link");
		var keyToString = this.keyToString
		$a.click(function(){
			openTicketSummaryDialog(keyToString);
		});
		
		var $tr = $("<tr />");
		$tr.append($("<td />").text(this.type).attr({width:"50px"}))
		    .append($("<td />").append($a).attr({width:"100px"}))
		    .append($("<td />").text(this.status).attr({width:"100px"}))
			.append($("<td />").text(this.title));
		$tbody.append($tr)
		
	});
	$table.append($tbody);
	var $div = $("<div />").append($table);
	var $dd = $("<dd />").append($div);
	$dl = $dl.append($dt).append($dd);
	$("#detail_ticket_conn_area").append($dl);
}

//Ticket概要ダイアログオープン
function openTicketSummaryDialog(key) {
	
	var params = {};
	params["keyToString"] = key;
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketDetailInfo",
		data: params
	});
	
	//後処理の登録
	//表示対象データが存在せず、再検索を行える場合、再検索をします。
	task.pipe(
		function(data) {
			
			if(errorCheck(data) == false) {
				if(data.status == -6) {
					//表示対象データが存在しない場合、再検索して表示
					openDetailTicketDialog($("#detail_ticket_keyToString").val(), true);
				}
				return;
			}
			
			//tokenの設定
			$("#token").val(data.token);
			
			//form情報の設定
			var form = data.result.form;
			$("#detail_ticket_summary_no").text(form.id);
			$("#detail_ticket_summary_title").text(form.title);
			$("#detail_ticket_summary_content").html(defaultString4Init(data.result.contentView, "　"));
			$("#detail_ticket_summary_endCondition").html(defaultString4Init(data.result.endConditionView, "　"));
			$("#detail_ticket_summary_period").text(defaultString4Init(formatDateyyyyMMdd(form.period), "　"));

			$("#detail_summary_versionNo").val(form.versionNo);
			$("#detail_summary_keyToString").val(form.keyToString);
			
			//ダイアログ表示時に、inputにフォーカスが当たってしまうので、text系の入力が無い画面には
			//フォーカスを当てないようにする
			prependDummyText("ticketSummaryDialog");
			$("#ticketSummaryDialog").dialog("open");
			removeDummyText("ticketSummaryDialog");
			
			$("#ticketSummaryDialog .dialog-container .scroll_area").scrollTop(0);
			return;
		}
	);
}


//コメント再描画
function renderTicketCommentList(list) {
	$("#ticket_comment_list").empty();

	if(list.length == 0) {
		return;
	}
	
	var $h2 = $("<h2 />").addClass("title small").text("コメント").css({"margin-top":"1em"});
	var $table = $("<table />").addClass("table table-bordered result_table comment_list_table");
	var $tbody = $("<tbody />");
	$.each(list, function(){
		
		var createdAt = this.createdAt;
		var comment = this.comment;
		var keyToString = this.model.keyToString;
		var versionNo = this.model.version;
		var createMemberName = this.createMemberName;

		var $delBtn = $("<input />").attr({type:"button", value:"削"}).addClass("btn btn-danger btn-mini");
		$delBtn.click(function(){
			deleteTicketComment(keyToString, versionNo);
		});
		
		var $tr = $("<tr />");
		$tr.append($("<td />").html(comment))
		    .append($("<td />").text(createMemberName).attr({width:"120px"}))
			.append($("<td />").text(createdAt).attr({width:"120px"}))
			.append($("<td />").append($delBtn).attr({width:"50px"}));
		$tbody.append($tr)
	});
	$table.append($tbody);
	$("#ticket_comment_list").append($h2).append($table);
}

//Ticketコメント削除
function deleteTicketComment(keyToString, versionNo) {
	if(window.confirm("コメントを削除します。本当によろしいですか？") == false) {
		return;
	}
	var params = {};
	params["keyToString"] = $("#detail_ticket_keyToString").val();
	params["commentKeyString"] = keyToString;
	params["commentVersionNo"] = versionNo;
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketCommentDelete",
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

			//詳細ダイアログリフレッシュ(メッセージを見せる必要上、1秒sleepしてから実行)
			var key = $("#detail_ticket_keyToString").val();
			setTimeout(function(){ openDetailTicketDialog(key, true) }, 1000);
		}
	);

}
