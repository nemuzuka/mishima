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

	$("#version-add").click(function(){
		execute();
	});
	
	var selectedProjectName =  $("#targetProjects option:selected").text();
	$("#selectedProjectName").text("(" + selectedProjectName + ")");

	//初期データ取得
	getInitData();
});

//初期データ取得
function getInitData() {

	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/project/management/ajax/versionEditInfo"
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
			
			$("#edit_version_name").val(form.versionName);

			$("#edit_versionNo").val(form.versionNo);
			$("#edit_keyToString").val(form.keyToString);
			
			if($("#edit_keyToString").val() != '') {
				//ボタンラベル変更
				$("#version-add").val("変更する");
			}
			return;
		}
	);
}

//バージョン登録・更新
function execute() {
	var params = createExecuteParams();
	
	if(validate(params) == false) {
		return;
	}
	
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
	params["versionName"] = $("#edit_version_name").val();
	params["versionNo"] = $("#edit_versionNo").val();
	params["keyToString"] = $("#edit_keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	return params;
}

//登録validate
function validate(params) {
	var v = new Validate();
	v.addRules({value:params["versionName"],option:'required',error_args:"バージョン"});
	v.addRules({value:params["versionName"],option:'maxLength',error_args:"バージョン", size:1024});
	return v.execute();
}
