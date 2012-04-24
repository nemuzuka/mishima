function initTodoDialog() {
	$("#todoDialog").dialog({
		modal:true,
		autoOpen:false,
		width:750,
		resizable:false,
		open:function(event) {
			document.body.style.overflow = "hidden";
		},
		close:function(event) {
			document.body.style.overflow = "visible";
		}
	});
	$("#todoDetailDialog").dialog({
		modal:true,
		autoOpen:false,
		width:600,
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
	$("#todoCommentDialog").dialog({
		modal:true,
		autoOpen:false,
		width:500,
		resizable:false
	});

	//TODO登録・更新ダイアログ
	$.datepicker.setDefaults($.extend($.datepicker.regional['ja']));
	$("#edit_todo_period").datepicker();

	$("#todoDialog-add").click(function(){
		executeTodo();
	});
	$("#todoDialog-cancel").click(function(){
		$("#todoDialog").dialog("close");
	});
	
	//TODO詳細ダイアログ
	$("#todoDetailDialog-cancel").click(function(){
		$("#todoDetailDialog").dialog("close");
	});
	$("#todoDetail-edit").click(function(){
		$("#todoDetailDialog").dialog("close");
		openEditTodoDialog($("#detail_todo_keyToString").val())
	})
	$("#detail_todo_status").change(function(){
		changeTodoStatus();
	});
	$("#todoDetail-Comment-add").click(function(){
		openTodoDetailCommentDialog();
	});
	
	//TODOコメントダイアログ
	$("#todoCommentDialog-add").click(function(){
		executeTodoComment();
	});
	$("#todoCommentDialog-cancel").click(function(){
		$("#todoCommentDialog").dialog("close");
	});
}

//TODOコメント登録
function executeTodoComment() {
	var params = {};
	params["status"] = $("#edit_todo_comment_status").val();
	params["comment"] = $("#edit_todo_comment").val();
	params["versionNo"] = $("#edit_todo_comment_versionNo").val();
	params["keyToString"] = $("#edit_todo_comment_keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/todo/ajax/todoCommentExecute",
		data: params
	});
	
	//後処理の登録
	//
	task.pipe(
		function(data) {

			var key = $("#edit_todo_comment_keyToString").val();
			
			//共通エラーチェック
			if(errorCheck(data) == false) {
				if(data.status == -1 ) {
					//validateの場合、tokenを再発行
					return reSetToken();
				} else {
					//強制的にダイアログを閉じて、再検索
					$("#todoCommentDialog").dialog("close");
					return openDetailTodoDialog(key, true);
				}
				return;
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			$("#todoCommentDialog").dialog("close");
			
			//詳細ダイアログリフレッシュ(メッセージを見せる必要上、1秒sleepしてから実行)
			setTimeout(function(){ openDetailTodoDialog(key, true) }, 1000);
		}
	);
}


//TODOコメントダイアログOpen
function openTodoDetailCommentDialog() {
	$("#edit_todo_comment_status").val($("#detail_todo_status").val());
	$("#edit_todo_comment_versionNo").val($("#detail_todo_versionNo").val());
	$("#edit_todo_comment_keyToString").val($("#detail_todo_keyToString").val());
	$("#edit_todo_comment").val("");
	$("#todoCommentDialog").dialog("open");
}

//ステータス変更
function changeTodoStatus() {
	
	var params = {};
	params["todoStatus"] = $("#detail_todo_status").val();
	params["versionNo"] = $("#detail_todo_versionNo").val();
	params["keyToString"] = $("#detail_todo_keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();

	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/todo/ajax/todoStatusExecute",
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
					$("#todoDetailDialog").dialog("close");
					return;
				}
				return;
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			
			var key = $("#detail_todo_keyToString").val();
			//詳細ダイアログリフレッシュ(メッセージを見せる必要上、1秒sleepしてから実行)
			setTimeout(function(){ openDetailTodoDialog(key, true) }, 1000);
		}
	);
}

//TODO登録・更新
function executeTodo() {
	var params = createExecuteTodoParams();
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/todo/ajax/todoExecute",
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
					$("#todoDialog").dialog("close");
					return refresh();
				}
				return;
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			$("#todoDialog").dialog("close");
			
			var key = $("#edit_todo_keyToString").val();
			if(key == '') {
				//新規の場合、一覧再描画
				return refresh();
			} else {
				//更新の場合、詳細ダイアログオープン(メッセージを見せる必要上、1秒sleepしてから開く)
				setTimeout(function(){ openDetailTodoDialog(key) }, 1000);
			}
		}
	);
}

//TODO登録パラメータ設定
function createExecuteTodoParams() {
	var params = {};
	params["todoStatus"] = $("#edit_todo_status").val();
	params["title"] = $("#edit_todo_title").val();
	params["content"] = $("#edit_todo_content").val();
	params["period"] = unFormatDate($("#edit_todo_period").val());
	params["versionNo"] = $("#edit_todo_versionNo").val();
	params["keyToString"] = $("#edit_todo_keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	return params;
}


//TODOダイアログオープン
function openEditTodoDialog(key) {
	var title = "";
	var buttonLabel = "";
	if(key == '') {
		title = "TODO登録";
		buttonLabel = "登録する";
	} else {
		title = "TODO変更";
		buttonLabel = "変更する";
	}
	$("#ui-dialog-title-todoDialog").empty();
	$("#ui-dialog-title-todoDialog").append(title);
	$("#todoDialog-add").attr({value:buttonLabel});
	
	var params = {};
	params["keyToString"] = key;
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/todo/ajax/todoEditInfo",
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
			
			$("#edit_todo_status").empty();
			$.each(form.statusList, function(){
				$("#edit_todo_status").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#edit_todo_status").val(form.todoStatus);

			$("#edit_todo_title").val(form.title);
			$("#edit_todo_content").val(form.content);
			$("#edit_todo_period").val(formatDateyyyyMMdd(form.period));

			$("#edit_todo_versionNo").val(form.versionNo);
			$("#edit_todo_keyToString").val(form.keyToString);
			
			$("#todoDialog").dialog("open");
			return;
		}
	);

}

//TODO詳細ダイアログオープン
function openDetailTodoDialog(key, onlyRefresh) {
	
	if(onlyRefresh == undefined) {
		onlyRefresh = false;
	}
	
	var params = {};
	params["keyToString"] = key;
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/todo/ajax/todoDetailInfo",
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
			
			//コメント用のステータス変更にもステータス構成情報を設定する
			$("#detail_todo_status").empty();
			$("#edit_todo_comment_status").empty();
			$.each(form.statusList, function(){
				$("#detail_todo_status").append($("<option />").attr({value:this.value}).text(this.label));
				$("#edit_todo_comment_status").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#detail_todo_status").val(form.todoStatus);

			$("#detail_todo_title").text(form.title);
			$("#detail_todo_content").html(data.result.contentView);
			$("#detail_todo_period").text(formatDateyyyyMMdd(form.period));

			$("#detail_todo_versionNo").val(form.versionNo);
			$("#detail_todo_keyToString").val(form.keyToString);
			
			//コメント再描画
			renderTodoCommentList(data.result.commentList);

			if(onlyRefresh == false) {
				$("#todoDetailDialog").dialog("open");
			}
			return;
		}
	);
}

//コメント再描画
function renderTodoCommentList(list) {
	$("#todo_comment_list").empty();

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

		var $delBtn = $("<input />").attr({type:"button", value:"削"}).addClass("btn btn-danger btn-mini");
		$delBtn.click(function(){
			deleteTodoComment(keyToString, versionNo);
		});
		
		var $tr = $("<tr />");
		$tr.append($("<td />").html(comment))
			.append($("<td />").text(createdAt).attr({width:"120px"}))
			.append($("<td />").append($delBtn).attr({width:"50px"}));
		$tbody.append($tr)
	});
	$table.append($tbody);
	$("#todo_comment_list").append($h2).append($table);
}

//TODOコメント削除
function deleteTodoComment(keyToString, versionNo) {
	if(window.confirm("コメントを削除します。本当によろしいですか？") == false) {
		return;
	}
	var params = {};
	params["keyToString"] = $("#detail_todo_keyToString").val();
	params["commentKeyString"] = keyToString;
	params["commentVersionNo"] = versionNo;
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/todo/ajax/todoCommentDelete",
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
			var key = $("#detail_todo_keyToString").val();
			setTimeout(function(){ openDetailTodoDialog(key, true) }, 1000);
		}
	);

}
