$(function(){

	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});

	$("#back").on("click", function(){
		back();
	});
	
	$("#detail_todo_status").on("change", function(){
		changeTodoStatus();
	});
	
	$("#addComment").on("click", function(){
		moveUrl("/mobile/bts/todo/comment");
	});

	$("#nav_back").on("click", function(){
		back();
	});

	$("#nav_edit").on("click", function(){
		var dashbord = $("#dashbord").val();
		var keyToString = $("#keyToString").val();
		moveUrl("/mobile/bts/todo/edit?keyToString=" + keyToString + "&dashbord=" + dashbord);
	});

	$("#nav_delete").on("click", function(){
		deleteTodo();
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
		url: "/bts/todo/ajax/todoDetailInfo",
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
	
	$("#detail_todo_status").empty();
	$.each(form.statusList, function(){
		$("#detail_todo_status").append($("<option />").attr({value:this.value}).text(this.label));
	});
	$("#detail_todo_status").val(form.todoStatus);
	$("#detail_todo_status").selectmenu("refresh");
	
	$("#detail_todo_title").text(form.title);
	$("#detail_todo_tag").text(defaultString4Init(form.tag, "　"));
	$("#detail_todo_content").html(defaultString4Init(data.result.contentView, "　"));
	$("#detail_todo_period").text(defaultString4Init(formatDateyyyyMMdd(form.period), "　"));

	$("#detail_todo_versionNo").val(form.versionNo);
	
	//コメント再描画
	renderTodoCommentList(data.result.commentList);

	//TODO詳細をWebストレージに格納
	sessionStorage.setItem("todo_detail", JSON.stringify(data));
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

		var $tr = $("<tr />");
		$tr.append($("<td />").html(comment))
			.append($("<td />").text(createdAt).attr({width:"80px"}));
		$tbody.append($tr)
	});
	$("#comment_table").append($tbody);
}

//ステータス変更
function changeTodoStatus() {
	
	var params = {};
	params["todoStatus"] = $("#detail_todo_status").val();
	params["versionNo"] = $("#detail_todo_versionNo").val();
	params["keyToString"] = $("#keyToString").val();
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
				return back();
			}
			infoCheck(data);
			setTimeout('refresh()', 2000);
		}
	);
}

//TODO削除
function deleteTodo() {
	var name = $("#detail_todo_title").text();
	if(window.confirm("TODO「" + name + "」を削除します。本当によろしいですか？") == false) {
		return;
	}
	
	var params = {};
	params["keyToString"] = $("#keyToString").val();
	params["versionNo"] = $("#detail_todo_versionNo").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/todo/ajax/todoDelete",
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
	moveUrl("/mobile/bts/todo/detail?keyToString=" + keyToString + "&dashbord=" + dashbord);
}

//ダッシュボードからの遷移の場合、ダッシュボード画面へ遷移します
//一覧からの遷移の場合、一覧画面へ遷移します
function back() {
	var dashbord = $("#dashbord").val();
	var keyToString = $("#keyToString").val();
	if(dashbord == 'true') {
		moveUrl("/");
	} else {
		moveUrl("/mobile/bts/todo/list");
	}
}