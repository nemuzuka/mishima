/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
$(function(){
	
	$("#personalDialog").dialog({
		modal:true,
		autoOpen:false,
		width:500,
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
	
	$("#personalDialog-update").click(function(){
		updatePersonal();
	});
	
	$("#personalDialog-cancel").click(function(){
		$("#personalDialog").dialog("close");
	});
});

//更新ダイアログ表示
function openPersonalDialog() {

	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/ajax/memberEditInfo"
	});
	
	//後処理の登録
	//表示対象データが存在せず、再検索を行える場合、再検索をします。
	task.pipe(
		function(data) {
			
			if(errorCheck(data) == false) {
				return;
			}
			
			//tokenの設定
			$("#token").val(data.token);
			
			//form情報の設定
			var form = data.result;
			
			$("#edit_person_name").val(form.name);
			$("#edit_person_memo").val(form.memo);
			$("#edit_person_versionNo").val(form.versionNo);
			$("#edit_person_keyToString").val(form.keyToString);
			
			prependDummyText("personalDialog");
			$("#personalDialog").dialog("open");
			removeDummyText("personalDialog");
			return;
		}
	);
}

//更新
function updatePersonal() {
	var params = {};
	params["name"] = $("#edit_person_name").val();
	params["memo"] = $("#edit_person_memo").val();
	params["versionNo"] = $("#edit_person_versionNo").val();
	params["keyToString"] = $("#edit_person_keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	var v = new Validate();
	v.addRules({value:params["name"],option:'required',error_args:"ニックネーム"});
	v.addRules({value:params["name"],option:'maxLength',error_args:"ニックネーム", size:64});
	v.addRules({value:params["memo"],option:'maxLength',error_args:"メモ", size:1024});
	if(v.execute() == false) {
		return;
	}
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/ajax/memberExecute",
		data: params
	});
	
	//後処理の登録
	//
	task.pipe(
		function(data) {
			//共通エラーチェック
			if(errorCheck(data) == false) {
				if(data.status == -1 || data.status == -5) {
					//validate or 入力値による一意制約エラーの場合、tokenを再発行
					return reSetToken();
				}
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			$("#personalDialog").dialog("close");
		}
	);
}
