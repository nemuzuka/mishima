var g_searchParams;

$(function(){
	
	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});
	
	initDialog();
	
	$("#searchProjectBtn").click(function(){
		searchProject();
	});
	
	$("#addProjectBtn").click(function(){
		openEditDialog("");
	});
	
});

//ダイアログ初期化
function initDialog(){
	$("#projectDialog").dialog({
		modal:true,
		autoOpen:false,
		width:600,
		resizable:false,
		open:function(event) {
			document.body.style.overflow = "hidden";
		},
		close:function(event) {
			document.body.style.overflow = "visible";
		}
	});
	
	$("#projectDialog-add").click(function(){
		execute();
	});
	
	$("#projectDialog-cancel").click(function(){
		$("#projectDialog").dialog("close");
	});
}

//プロジェクト検索
function searchProject() {
	var params = createSearchParams();
	g_searchParams = params;
	searchAndRender(params);
}

//検索＆レンダリング
function searchAndRender(params) {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/management/ajax/projectList",
		data: params
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
	infoCheck(data);
	//tokenの設定
	$("#token").val(data.token);
	
	var result = data.result;
	$("#listCnt").val(result.length);
	if(result.length == 0) {
		return;
	}

	var $msgDiv = $("<div />");
	if(result.length >= 1000) {
		$msgDiv.append($("<span />").text("1000件以上存在します").addClass("label label-warning"));
	} else {
		$msgDiv.append($("<span />").text("該当件数：" + result.length + "件").addClass("label label-info"));
	}
	
	//一覧をレンダリング
	var $table = $("<table />").addClass("table table-bordered result_table");
	var $thead = $("<thead />").append($("<tr />")
				.append($("<th />").text("プロジェクト名"))
				.append($("<th />").text("プロジェクト識別子"))
				.append($("<th />").text("プロジェクト概要"))
				.append($("<th />").text("").attr({width:"50px"}))
			);
	$table.append($thead);
	
	var $tbody = $("<tbody />");
	$.each(result, function(){
		var keyToString = this.model.keyToString;
		var projectName = this.model.projectName;
		var projectId = this.model.projectId;
		var versionNo = this.model.version
		var projectSummaryView = this.projectSummaryView;

		var $delBtn = $("<input />").attr({type:"button", value:"削"}).addClass("btn btn-danger btn-mini");
		$delBtn.click(function(){
			deleteProject(projectName, keyToString, versionNo);
		});
		
		var $a = $("<a />").attr({href:"javascript:void(0)"}).text(projectName);
		$a.click(function(){
			openEditDialog(keyToString);
		});
		var $tr = $("<tr />");
		$tr.append($("<td />").append($a))
			.append($("<td />").text(projectId))
			.append($("<td />").html(projectSummaryView))
			.append($("<td />").append($delBtn));
		$tbody.append($tr)
	});
	$table.append($tbody);
	
	$("#result_area").append($("<hr />")).append($msgDiv).append($table);
}

//登録・更新ダイアログ表示
function openEditDialog(keyToString) {
	
	var title = "";
	var buttonLabel = "";
	if(keyToString == '') {
		title = "プロジェクト登録";
		buttonLabel = "登録する";
		$("#project_admin_area").show();
	} else {
		title = "プロジェクト変更";
		buttonLabel = "変更する";
		$("#project_admin_area").hide();
	}
	$("#ui-dialog-title-projectDialog").empty();
	$("#ui-dialog-title-projectDialog").append(title);
	$("#projectDialog-add").attr({value:buttonLabel});
	
	var params = {};
	params["keyToString"] = keyToString;
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/management/ajax/projectEditInfo",
		data: params
	});
	
	//後処理の登録
	//表示対象データが存在せず、再検索を行える場合、再検索をします。
	task.pipe(
		function(data) {
			
			if(errorCheck(data) == false) {
				if(data.status == -6) {
					//表示対象データが存在しない場合、再検索して表示
					return reSearchAndRender();
				}
				return;
			}
			
			//tokenの設定
			$("#token").val(data.token);
			
			//form情報の設定
			var form = data.result;
			
			$("#edit_project_name").val(form.projectName);
			$("#edit_project_id").val(form.projectId);
			$("#edit_project_summary").val(form.projectSummary);
			
			$("#project_admin").empty();
			$.each(form.memberList, function(){
				$("#project_admin").append($('<option>').attr({ value: this.value }).text(this.label));
			});
			$("#project_admin").val(form.adminMemberId);
			
			$("#edit_versionNo").val(form.versionNo);
			$("#edit_keyToString").val(form.keyToString);
			
			$("#projectDialog").dialog("open");
			return;
		}
	);
}

//メンバー登録・更新
function execute() {
	var params = createExecuteParams();
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/management/ajax/projectExecute",
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
					$("#projectDialog").dialog("close");
					return reSearchAndRender();
				}
				return;
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			$("#projectDialog").dialog("close");
			return reSearchAndRender();
		}
	);
}

//プロジェクト削除
function deleteProject(name, keyToString, version) {
	if(window.confirm("プロジェクト「" + name + "」を削除します。本当によろしいですか？") == false) {
		return;
	}
	
	var params = {};
	params["keyToString"] = keyToString;
	params["versionNo"] = version;
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/management/ajax/projectDelete",
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
			return reSearchAndRender();
		}
	);
}

//検索条件パラメータ設定
function createSearchParams() {
	var params = {};
	params["projectName"] = $("#search_projectName").val();
	return params;
}

//登録パラメータ設定
function createExecuteParams() {
	var params = {};
	params["projectName"] = $("#edit_project_name").val();
	params["projectId"] = $("#edit_project_id").val();
	params["projectSummary"] = $("#edit_project_summary").val();
	params["adminMemberId"] = $("#project_admin").val();
	params["versionNo"] = $("#edit_versionNo").val();
	params["keyToString"] = $("#edit_keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	return params;
}

//再検索判断処理
//一覧に表示されている場合、再検索する、と判断します。
function isReRender() {
	if($("#listCnt").val() != '0') {
		return true;
	}
	return false;
}

//再検索処理
function reSearchAndRender() {
	if(isReRender()) {
		return searchAndRender(g_searchParams);
	}
	return "";
}

