/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * 共通JavaScript定義
 */

//非同期通信をデフォルトで行います。
//Deferredを使用することが前提のsetupメソッドです。
function setAjaxDefault() {
	$.ajaxSetup({
		timeout: 30000,		//ミリ秒
		ifModified: true,
		async: true,		//非同期通信

		//通信前の処理を定義
		beforeSend: function(jqXHR, settings) {
			viewLoadingMsg();
		},
		//通信成功時の処理を定義
		success : function(data, dataType) {
			unBlockLoadingMsg();
		},
		// エラー・ハンドラを定義（エラー時にダイアログ表示）
		error: function(xhr, status, err) {
			unBlockLoadingMsg();
			alert('通信エラーが発生しました。');
		}
	});
}

//読み込み中メッセージを表示解除
function unBlockLoadingMsg() {
	$.unblockUI();
}

//メッセージ表示
function getMsgs(msgList) {
	var msg = "";
	for(var i = 0; i < msgList.length; i++) {
		msg = msg + msgList[i] + "\n";
	}
	return msg;
}

//Ajax戻り値共通エラーチェック
function errorCheck(data) {
	if(data.errorMsg.length != 0) {
		//エラーが存在する場合
		alert(getMsgs(data.errorMsg));
		
		//Sessionタイムアウトの場合、強制的にログアウトさせる
		if(data.status == -99) {
			moveUrl(logoutUrl);
		}
		return false;
	}
	return true;
}

//Ajaxメッセージ表示
function infoCheck(data) {
	if(data.infoMsg.length != 0) {
		//メッセージが存在する場合
		var msg = getMsgs(data.infoMsg);
		viewToastMsg(msg);
	}
	return;
}



//Token再設定
//idが'token'であるhiddenオブジェクトに対して、
//新たにサーバ側で設定したtokenを設定します。
function reSetToken() {
	return executeReSetToken("/ajax/token");
}

//Token設定メイン
function executeReSetToken(url) {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: url}).then(
			function(data){

				//共通エラーチェック
				if(errorCheck(data) == false) {
					return;
				}
	
				$("#token").val(data.token);
			}
		);
}

/**
 * 文字列置換.
 */
function replaceAll(expression, org, dest){
	return expression.split(org).join(dest);
}

/**
 * 引数の文字列がnullの場合、空文字に置き換えます
 */
function defaultString(target) {
	return defaultString4Init(target, "");
}

/**
 * 引数の文字列がnullか空文字の場合、指定文字列に置き換えます
 */
function defaultString4Init(target, replaseStr) {
	if(target == null || target == '') {
		return replaseStr;
	}
	return target;
}

/**
 * 値取得.
 * 指定したname属性のvalue値を配列として取得します。
 */
function getValues4Name(nameStr) {
	var count = 0;
	var retArray = {};
	$("input[name='" + nameStr + "']").each(function() {
		retArray[count] = $(this).val();
		count++;
	});
	return retArray;
}

