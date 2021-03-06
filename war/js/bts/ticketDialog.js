/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
function initTicketDialog() {
	$("#ticketDialog").dialog({
		modal:true,
		autoOpen:false,
		width:850,
		resizable:false,
		open:function(event) {
			openModalDialog();
		},
		close:function(event) {
			closeModelDialog();
		},
		show: 'clip',
        hide: 'clip'
	});
	$("#ticketDetailDialog").dialog({
		modal:true,
		autoOpen:false,
		width:850,
		resizable:false,
		open:function(event) {
			openModalDialog();
		},
		close:function(event) {
			closeModelDialog();
			//詳細ダイアログを閉じたタイミングで一覧再描画
			refresh();
		},
		show: 'clip',
        hide: 'clip'
	});
	$("#ticketCommentDialog").dialog({
		modal:true,
		autoOpen:false,
		width:500,
		resizable:false,
		show: 'clip',
        hide: 'clip',
		open:function(event) {
			openModalDialog();
		},
		close:function(event) {
			closeModelDialog();
		}
	});
	$("#ticketSummaryDialog").dialog({
		modal:true,
		autoOpen:false,
		width:600,
		resizable:false,
		show: 'clip',
        hide: 'clip',
		open:function(event) {
			openModalDialog();
		},
		close:function(event) {
			closeModelDialog();
		}
	});
	$("#ticketFileUploadDialog").dialog({
		modal:true,
		autoOpen:false,
		width:600,
		resizable:false,
		show: 'clip',
        hide: 'clip',
		open:function(event) {
			openModalDialog();
		},
		close:function(event) {
			closeModelDialog();
		}
	});

	//Ticket登録・更新ダイアログ
	$.datepicker.setDefaults($.extend($.datepicker.regional['ja']));
	$("#edit_ticket_startDate").datepicker();
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
		var ticketNo = $("#detail_ticket_no_val").val();
		var baseKey = $("#detail_ticket_keyToString").val();
		openEditTicketDialog("", ticketNo, baseKey, "child");
	});
	$("#ticketDetail-copy-add").click(function(){
		$("#ticketDetailDialog").dialog("close");
		var baseKey = $("#detail_ticket_keyToString").val();
		setTimeout(function(){ openEditTicketDialog("", "", baseKey, "copyIns") }, 600);
	});
	$("#ticketDetail-add-file").click(function(){
		var key = $("#detail_ticket_keyToString").val();
		$("#ticket_file_upload_keyToString").val(key);
		$("#fileUploadToken").val($("#token").val());
		//fileの入力値を初期化
		$("#ticket_upload_file").replaceWith($("#ticket_upload_file").clone());
		$("#ticket_upload_file_comment").val("");
		
		prependDummyText("ticketFileUploadDialog");
		$("#ticketFileUploadDialog").dialog("open");
		removeDummyText("ticketFileUploadDialog");
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
	
	//ファイルアップロードダイアログ
	$("#ticketFileUploadDialog-cancel").click(function(){
		$("#ticketFileUploadDialog").dialog("close");
	});
	$("#ticketFileUploadDialog-add").click(function(){
		executeTicketFileUploadCheck();
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
	
	//validateチェック
	var v = new Validate();
	v.addRules({value:params["comment"],option:'required',error_args:"コメント"});
	v.addRules({value:params["comment"],option:'maxLength',error_args:"コメント", size:1024});
	if(v.execute() == false) {
		return;
	}

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
			viewLoadingMsg();
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
	prependDummyText("ticketCommentDialog");
	$("#ticketCommentDialog").dialog("open");
	removeDummyText("ticketCommentDialog");

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
			viewLoadingMsg();
			setTimeout(function(){ openDetailTicketDialog(key, true) }, 1000);
		}
	);
}

//Ticket登録・更新
function executeTicket() {
	var params = createExecuteTicketParams();
	
	if(ticketValidate(params) == false) {
		return;
	}
	
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
				viewLoadingMsg();
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
	params["startDate"] = unFormatDate($("#edit_ticket_startDate").val());
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

//Ticket登録validate
function ticketValidate(params) {
	var v = new Validate();
	v.addRules({value:params["title"],option:'required',error_args:"件名"});
	v.addRules({value:params["title"],option:'maxLength',error_args:"件名", size:128});

	v.addRules({value:params["content"],option:'maxLength',error_args:"内容", size:1024});
	v.addRules({value:params["endCondition"],option:'maxLength',error_args:"終了条件", size:1024});

	v.addRules({value:params["startDate"],option:'date',error_args:"開始日"});
	v.addRules({value:params["period"],option:'date',error_args:"期限"});

	v.addRules({value:params["parentKey"],option:'number',error_args:"親チケットNo"});
	return v.execute();
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
			$("#edit_ticket_startDate").val(formatDateyyyyMMdd(form.startDate));
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

			$("#edit_ticket_project_id").text("");
			if(form.projectId != null && form.projectId != '' ) {
				$("#edit_ticket_project_id").text(form.projectId + "-");
			}
			
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
			prependDummyText("ticketDialog");
			$("#ticketDialog").dialog("open");
			removeDummyText("ticketDialog");
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
			
			var ticketNo = "";
			if(form.projectId != null && form.projectId != '') {
				ticketNo = ticketNo + form.projectId + "-";
			}
			$("#detail_ticket_no").text(ticketNo + form.id);
			$("#detail_ticket_no_val").val(form.id);
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
			$("#detail_ticket_startDate").text(defaultString4Init(formatDateyyyyMMdd(form.startDate), "　"));
			$("#detail_ticket_period").text(defaultString4Init(formatDateyyyyMMdd(form.period), "　"));

			
			$("#detail_ticket_priority").text(defaultString4Init(form.priority, "　"));
			$("#detail_ticket_category").text(defaultString4Init(form.category, "　"));
			
			$("#detail_ticket_milestone").text(defaultString4Init(data.result.milestone, "　"));
			$("#detail_ticket_kind").text(defaultString4Init(form.targetKind, "　"));
			$("#detail_ticket_targetVersion").text(defaultString4Init(form.targetVersion, "　"));
			$("#detail_ticket_targetMember").text(defaultString4Init(data.result.targetMember, "　"));

			$("#detail_ticket_versionNo").val(form.versionNo);
			$("#detail_ticket_keyToString").val(form.keyToString);

			//ファイルアップロード再描画
			renderTicketUploadFileList(data.result.uploadFileList);
			
			//コメント再描画
			renderTicketCommentList(data.result.commentList);

			//関連情報描画
			renderTicketConn(data.result, ticketNo);
			
			if(onlyRefresh == false) {
				prependDummyText("ticketDetailDialog");
				$("#ticketDetailDialog").dialog("open");
				removeDummyText("ticketDetailDialog");
			}
			$("#ticketDetailDialog .dialog-container .scroll_area").scrollTop(0);
			$(".scroll_area").scrollTop(0);
			return;
		}
	);
}

//関連情報描画
function renderTicketConn(result, prefix) {
	$("#detail_ticket_conn_area").empty();
	
	var connArray = new Array();
	if(result.parentTicket != null) {
		var parentTicket = result.parentTicket;
		var obj = { 'keyToString': parentTicket.keyToString, 
				'title': parentTicket.title, 'id':parentTicket.no, 
				'type':"親", 'status':parentTicket.status};
		connArray.push(obj);
	}
	
	$.each(result.childTicketList, function(){
		var obj = { 'keyToString': this.keyToString, 'title': this.title, 'id':this.no, 
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
		var $a = $("<a />").attr({href:"javascript:void(0)"}).text(prefix + this.id).addClass("link");
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
			var ticketNo = "";
			if(form.projectId != null && form.projectId != '') {
				ticketNo = ticketNo + form.projectId + "-";
			}
			$("#detail_ticket_summary_no").text(ticketNo + form.id);
			$("#detail_ticket_summary_title").text(form.title);
			$("#detail_ticket_summary_content").html(defaultString4Init(data.result.contentView, "　"));
			$("#detail_ticket_summary_endCondition").html(defaultString4Init(data.result.endConditionView, "　"));
			$("#detail_ticket_summary_startDate").text(defaultString4Init(formatDateyyyyMMdd(form.startDate), "　"));
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

//アップロードファイル再描画
function renderTicketUploadFileList(list) {
	$("#ticket_upload_file_list").empty();

	if(list.length == 0) {
		return;
	}
	
	var $dl = $("<dl />");
	var $dt = $("<dt />").text("ファイル");

	var $table = $("<table />").addClass("table table-bordered result_table comment_list_table");
	var $tbody = $("<tbody />");
	$.each(list, function(){
		
		var keyToString = this.keyToString;
		var versionNo = this.version;
		var createMemberName = this.createMemberName;
		var filename = this.filename;
		var viewComment = this.viewComment;
		var viewCreation = formatDateyyyyMMdd(this.viewCreation);

		var $a = $("<a />").attr({href:"javascript:void(0)"}).text(filename).addClass("link");
		$a.click(function(){
			downloadTicketUploadFile(keyToString);
		});

		var $delBtn = $("<input />").attr({type:"button", value:"削"}).addClass("btn btn-danger btn-mini");
		$delBtn.click(function(){
			deleteTicketUploadFile(keyToString, versionNo);
		});
		
		var $tr = $("<tr />");
		$tr.append($("<td />").append($a))
			.append($("<td />").html(viewComment))
			.append($("<td />").text(viewCreation).attr({width:"80px"}))
			.append($("<td />").append($delBtn).attr({width:"50px"}));
		$tbody.append($tr)
	});
	$table.append($tbody);
	var $div = $("<div />").append($table);
	var $dd = $("<dd />").append($div);
	$dl = $dl.append($dt).append($dd);
	$("#ticket_upload_file_list").append($dl);
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
			viewLoadingMsg();
			setTimeout(function(){ openDetailTicketDialog(key, true) }, 1000);
		}
	);
}

//Ticketに紐付くファイル添付(validation)
function executeTicketFileUploadCheck() {

	var comment = $("#ticket_upload_file_comment").val();
	var file = $("#ticket_upload_file").val();

	//validate
	var v = new Validate();
	v.addRules({value:file,option:'required',error_args:"ファイル"});
	v.addRules({value:comment,option:'maxLength',error_args:"コメント", size:1024});
	if(v.execute() == false) {
		return;
	}
	
	viewLoadingMsg();
	$("#fileUploadForm").attr({"action":"/bts/ticket/uploadCheck"});
	$("#fileUploadForm")[0].submit(function () {
		return false;
	});
}

//添付ファイルupload後の処理
function afterUploadCheck(token, isError) {
	unBlockLoadingMsg();
	$("#token").val(token);
	$("#fileUploadToken").val(token);
	if(isError == false) {
		executeTicketFileUpload();
	}
}

//Ticketに紐付くファイル添付
function executeTicketFileUpload() {
	viewLoadingMsg();
	$("#fileUploadForm").attr({"action":"/bts/ticket/upload"});
	$("#fileUploadForm")[0].submit(function () {
		return false;
	});
}

//添付ファイルupload後の処理
function afterUpload(msg) {
	unBlockLoadingMsg();
	$("#ticketFileUploadDialog").dialog("close");
	
	if(msg != '') {
		viewToastMsg(msg);
	}
	
	//詳細ダイアログ再描画
	openDetailTicketDialog($("#detail_ticket_keyToString").val(), true);
}
//添付ファイルダウンロード
function downloadTicketUploadFile(keyString) {
	$("#uploadFileKeyString").val(keyString);
	$("#uploadFileTicketKeyString").val($("#detail_ticket_keyToString").val());
	$("#fileDownloader").attr({"action":"/bts/ticket/downLoad"});
	$("#fileDownloader")[0].submit(function () {
		return false;
	});
}

//アップロードファイル削除
function deleteTicketUploadFile(keyToString, versionNo) {
	if(window.confirm("ファイルを削除します。本当によろしいですか？") == false) {
		return;
	}
	var params = {};
	params["keyToString"] = $("#detail_ticket_keyToString").val();
	params["version"] = versionNo;
	params["uploadFileKeyToString"] = keyToString;
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketUploadFileDelete",
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
			viewLoadingMsg();
			setTimeout(function(){ openDetailTicketDialog(key, true) }, 1000);
		}
	);
}
