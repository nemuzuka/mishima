/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
$(function(){
	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});

	$("#priority-add").click(function(){
		execute();
	});
	
	$("#init-get").click(function(){
		getInitDataSetting();
	});
	
	var selectedProjectName =  $("#targetProjects option:selected").text();
	$("#selectedProjectName").text("(" + selectedProjectName + ")");

	//初期データ取得
	getInitData();
});

//初期設定値取得
function getInitDataSetting() {
	var params = {};
	params["type"] = "priority";
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/project/management/ajax/initInfo",
		data:params
	});
	//後処理の登録
	task.pipe(
		function(data) {
			
			if(errorCheck(data) == false) {
				return;
			}
			//レスポンス情報の設定
			$("#edit_priority_name").val(data.result);
			return;
		}
	);
}

//初期データ取得
function getInitData() {

	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/project/management/ajax/priorityEditInfo"
	});
	
	//後処理の登録
	task.pipe(
		function(data) {
			
			if(errorCheck(data) == false) {
				return;
			}
			
			//tokenの設定
			$("#token").val(data.token);
			
			//form情報の設定
			var form = data.result;
			
			$("#edit_priority_name").val(form.priorityName);

			$("#edit_versionNo").val(form.versionNo);
			$("#edit_keyToString").val(form.keyToString);
			
			if($("#edit_keyToString").val() != '') {
				//ボタンラベル変更
				$("#priority-add").val("変更する");
			}
			return;
		}
	);
}

//優先度登録・更新
function execute() {
	var params = createExecuteParams();
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/project/management/ajax/priorityExecute",
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
					reSetToken();
					return;
				}
				//再度画面情報取得する
				getInitData();
				return;
			}
			
			//メッセージを表示して、再度画面情報取得
			infoCheck(data);
			getInitData();
			return;
		}
	);
}

//登録パラメータ設定
function createExecuteParams() {
	var params = {};
	params["priorityName"] = $("#edit_priority_name").val();
	params["versionNo"] = $("#edit_versionNo").val();
	params["keyToString"] = $("#edit_keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	return params;
}