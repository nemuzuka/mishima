$(function(){

	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});

	$("#back").on("click", function(){
		back();
	});

	$("#nav_back").on("click", function(){
		back();
	});

	$("#nav_save").on("click", function(){
		executeTodo();
	});

	executeSearch();
});

//表示する情報を取得し、表示する
function executeSearch() {
	var key = $("#keyToString").val();
	var title = "";
	var buttonLabel = "";
	if(key == '') {
		title = "TODO登録";
		buttonLabel = "登録する";
	} else {
		title = "TODO変更";
		buttonLabel = "変更する";
	}
	$("#header_title").text(title);
	$("#nav_save span.ui-btn-inner span").text(buttonLabel);
	
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
					return back();
				}
				return;
			}
			
			//tokenの設定
			$("#token").val(data.token);
			
			//form情報の設定
			var form = data.result;
			
			$("#todo_status").empty();
			$.each(form.statusList, function(){
				$("#todo_status").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#todo_status").val(form.todoStatus);
			$("#todo_status").selectmenu("refresh");

			$("#todo_title").val(form.title);
			$("#todo_tag").val(form.tag);
			$("#todo_content").val(form.content);
			$('#todo_content').keyup();
			$("#todo_period").val(formatDateyyyyMMdd(form.period));

			$("#edit_todo_versionNo").val(form.versionNo);
			return;
		}
	);

}

//TODO登録・更新
function executeTodo() {
	var params = createExecuteTodoParams();
	
	if(todoValidate(params) == false) {
		return;
	}
	
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
				}
				return back();
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			setTimeout('back()', 2000);
		}
	);
}

//TODO登録パラメータ設定
function createExecuteTodoParams() {
	var params = {};
	params["todoStatus"] = $("#todo_status").val();
	params["title"] = $("#todo_title").val();
	params["tag"] = $("#todo_tag").val();
	params["content"] = $("#todo_content").val();
	params["period"] = unFormatDate($("#todo_period").val());
	params["versionNo"] = $("#edit_todo_versionNo").val();
	params["keyToString"] = $("#keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	return params;
}

//TODO登録validate
function todoValidate(params) {
	var v = new Validate();
	v.addRules({value:params["title"],option:'required',error_args:"件名"});
	v.addRules({value:params["title"],option:'maxLength',error_args:"件名", size:128});
	v.addRules({value:params["tag"],option:'maxLength',error_args:"タグ", size:500});
	v.addRules({value:params["period"],option:'date',error_args:"期限"});
	v.addRules({value:params["content"],option:'maxLength',error_args:"内容", size:1024});
	return v.execute();
}

//詳細画面からの遷移の場合、詳細画面へ遷移します
//新規登録の場合、TODOメニューへ遷移します
function back() {
	var dashbord = $("#dashbord").val();
	var keyToString = $("#keyToString").val();
	if(keyToString != null && keyToString != '') {
		moveUrl("/mobile/bts/todo/detail?keyToString=" + keyToString + "&dashbord=" + dashbord);
	} else {
		moveUrl("/mobile/bts/todo/");
	}
}