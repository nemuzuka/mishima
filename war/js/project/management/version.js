$(function(){
	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});

	initDialog();

	$("#addVersionBtn").click(function(){
		openEditDialog("");
	});

	$("#searchVersionBtn").click(function(){
		searchVersion();
	});
});

//ダイアログ初期化
function initDialog(){
	$("#versionDialog").dialog({
		modal:true,
		autoOpen:false,
		width:500,
		resizable:false,
		open:function(event) {
			document.body.style.overflow = "hidden";
		},
		close:function(event) {
			document.body.style.overflow = "visible";
		}
	});

	$("#versionSortDialog").dialog({
		modal:true,
		autoOpen:false,
		width:500,
		resizable:false,
		open:function(event) {
			document.body.style.overflow = "hidden";
		},
		close:function(event) {
			document.body.style.overflow = "visible";
		}
	});

	$("#versionDialog-add").click(function(){
		execute();
	});
	
	$("#versionDialog-cancel").click(function(){
		$("#versionDialog").dialog("close");
	});
	

	//ソートダイアログ
	$("#sortVersionBtn").click(function(){
		openSortDialog();
	});
	$("#sort_up").click(function(){
		upItems("version_to");
	});
	$("#sort_down").click(function(){
		downItems("version_to");
	});
	$("#versionSortDialog-execute").click(function(){
		executeSort();
	});
	$("#versionSortDialog-cancel").click(function(){
		$("#versionSortDialog").dialog("close");
	});
}

//バージョン検索
function searchVersion() {
	searchAndRender();
}

//検索＆レンダリング
function searchAndRender() {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/project/management/ajax/versionList"
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
				.append($("<th />").text("バージョン"))
				.append($("<th />").text("").attr({width:"50px"}))
			);
	$table.append($thead);
	
	var $tbody = $("<tbody />");
	$.each(result, function(){
		var keyToString = this.keyToString;
		var versionName = this.versionName;
		var versionNo = this.version

		var $delBtn = $("<input />").attr({type:"button", value:"削"}).addClass("btn btn-danger btn-mini");
		$delBtn.click(function(){
			deleteVersion(versionName, keyToString, versionNo);
		});
		
		var $a = $("<a />").attr({href:"javascript:void(0)"}).text(versionName);
		$a.click(function(){
			openEditDialog(keyToString);
		});
		var $tr = $("<tr />");
		$tr.append($("<td />").append($a))
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
		title = "バージョン登録";
		buttonLabel = "登録する";
	} else {
		title = "バージョン変更";
		buttonLabel = "変更する";
	}
	$("#ui-dialog-title-versionDialog").empty();
	$("#ui-dialog-title-versionDialog").append(title);
	$("#versionDialog-add").attr({value:buttonLabel});
	
	var params = {};
	params["keyToString"] = keyToString;
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/project/management/ajax/versionEditInfo",
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
			
			$("#edit_version_name").val(form.versionName);

			$("#edit_versionNo").val(form.versionNo);
			$("#edit_keyToString").val(form.keyToString);
			
			$("#versionDialog").dialog("open");
			return;
		}
	);
}

//バージョン登録・更新
function execute() {
	var params = createExecuteParams();
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/project/management/ajax/versionExecute",
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
					$("#versionDialog").dialog("close");
					return reSearchAndRender();
				}
				return;
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			$("#versionDialog").dialog("close");
			return reSearchAndRender();
		}
	);
}

//バージョン削除
function deleteVersion(name, keyToString, version) {
	if(window.confirm("バージョン「" + name + "」を削除します。本当によろしいですか？") == false) {
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
		url: "/project/management/ajax/versionDelete",
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

//登録パラメータ設定
function createExecuteParams() {
	var params = {};
	params["versionName"] = $("#edit_version_name").val();
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
		return searchAndRender();
	}
	return "";
}

//ダイアログ処理
function openSortDialog() {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/project/management/ajax/versionList"
	}).then(
		function(data) {
			renderSortDialog(data);
		}
	);
}

//ソートダイアログデータ設定 & Open
function renderSortDialog(data) {
	$("#version_to").empty();

	//共通エラーチェック
	if(errorCheck(data) == false) {
		return;
	}
	infoCheck(data);
	//tokenの設定
	$("#token").val(data.token);
	
	var result = data.result;
	if(result.length == 0) {
		return;
	}

	$.each(result, function(){
		var keyToString = this.keyToString;
		var versionName = this.versionName;
		$("#version_to").append($('<option />').attr({value:keyToString }).text(versionName));
	});
	reWriteSelect("version_to", new Array());
	$("#versionSortDialog").dialog("open");
}

//ソート情報変更
function executeSort() {
	var params = {};
	params["sortedVersionKeys"] = getSelectArray("version_to");
	params["jp.co.nemuzuka.token"] = $("#token").val();

	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/project/management/ajax/versionSort",
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
			}

			//メッセージを表示して、ダイアログを閉じて、再検索
			infoCheck(data);
			$("#versionSortDialog").dialog("close");
			return reSearchAndRender();
		}
	);

}