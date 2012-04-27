/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

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

//読み込み中のメッセージを表示
function viewLoadingMsg() {
	viewMsg("読み込み中...");
}

//メッセージ表示
function viewMsg(msg) {
	$.blockUI({
		message: msg,
		fadeIn: 0,
		fadeOut: 0,
		showOverlay: true,
		centerY: false,
		centerX: false,
		css: {
			top: '85px', 
			left: '', 
			right: '10px',
			border: 'none',
			padding: '5px',
			width: '120px',
			backgroundColor: '#F00',
			'-webkit-border-radius': '10px',
			'-moz-border-radius': '10px',
			opacity: .6,
			color: '#fff'
		},
		overlayCSS:  {
			backgroundColor: 'transparent',
			opacity:         0.6
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
		var t = $.toaster({showTime:1000, centerX:true, centerY:true});
		t.toast(msg);
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
 * 指定したオブジェクトのonblur/onfocusに
 * 日付フォーマットを組み込みます。
 */
function setDateFormatEvent($target) {

	var val = $target.val();

	$target.focusin(function(event){
		//データが入力されていれば、
		//yyyyMMddフォーマットに変換する
		var tgt = event.target;
		$(tgt).val(unFormatDate($(tgt).val()));
	}).focusout(function(event) {
		//データが入力されていれば、
		//yyyy/MM/ddフォーマットに変換する
		var tgt = event.target;
		$(tgt).val(formatDateyyyyMMdd($(tgt).val()));
	});
}

/**
 * 指定したオブジェクトのonblur/onfocusに
 * 日付フォーマットを組み込みます。
 */
function setYyyyMmFormatEvent($target) {

	var val = $target.val();

	$target.focusin(function(event){
		//データが入力されていれば、
		//yyyyMMフォーマットに変換する
		var tgt = event.target;
		$(tgt).val(unFormatDate($(tgt).val()));
	}).focusout(function(event) {
		//データが入力されていれば、
		//yyyy/MMフォーマットに変換する
		var tgt = event.target;
		$(tgt).val(formatDateyyyyMM($(tgt).val()));
	});
}

/**
 * 指定したオブジェクトのonblur/onfocusに
 * 日付フォーマットを組み込みます。
 */
function setMMddFormatEvent($target) {

	var val = $target.val();

	$target.focusin(function(event){
		//データが入力されていれば、
		//MMddフォーマットに変換する
		var tgt = event.target;
		$(tgt).val(unFormatDate($(tgt).val()));
	}).focusout(function(event) {
		//データが入力されていれば、
		//MM/ddフォーマットに変換する
		var tgt = event.target;
		$(tgt).val(formatDateMMdd($(tgt).val()));
	});
}


/**
 * 指定したオブジェクトのonblur/onfocusに
 * 時刻フォーマットを組み込みます。
 */
function setTimeFormatEvent($target) {
	var val = $target.val();

	$target.focusin(function(event){
		//データが入力されていれば、
		//hhmmフォーマットに変換する
		var tgt = event.target;
		$(tgt).val(unFormatTime($(tgt).val()));
	}).focusout(function(event) {
		//データが入力されていれば、
		//hh:mmフォーマットに変換する
		var tgt = event.target;
		$(tgt).val(formatTimehhmm($(tgt).val()));
	});
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
	if(target == null) {
		return "";
	}
	return target
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

/**
 * 指定したURLに移動します。
 */
function moveUrl(url) {
	viewLoadingMsg();
	document.location.href = url;
}

/**
 * 指定IDの要素を非表示にします。
 */
function blockLink(disableId) {
	$(disableId).css('display', 'none');
}

/**
 * 指定IDの要素を表示にします。
 */
function unBlockLink(unBlockId) {
	$(unBlockId).css('display', 'inline');
}

/**
 * リスト用画面位置設定
 * 指定したidのオブジェクトを画面のtopにします。
 */
function moveListTop(id) {
	$('body,html').animate({
		scrollTop: getOffsetTop(id)
	}, 0);
	return false;
}

/**
 * ダイアログ用画面位置設定
 * 指定したidのオブジェクトをダイアログのtopにします。
 * @param id1 ダイアログ直下のdivのid
 * @param id2 styleが指定してあるdivのid
 * @param id3 結果一覧のdivのid
 * @param point ダイアログからデータコンテンツまでの高さ
 */
function moveListTop4Dialog(id1, id2, id3, point) {

	//IEだとスクロールバーの位置が変わらないので、強制的に0に置き換える
	//このことで検索結果の位置が変わってしまうことを防ぐ
	$("#" + id2).scrollTop(0);

	var offset1 = getOffsetTop(id1);
	var offset3 = getOffsetTop(id3);
	var offset = offset3 - offset1 - point;
	$("#" + id2).scrollTop(offset);
	return false;
}

/**
 * 指定オブジェクトy軸取得.
 * 指定したidのoffset.topを返却します。
 */
function getOffsetTop(id) {
	return $('#'+id).offset().top;
}

/**
 * 年齢を計算します。
 */
function calcAge(birthDay) {
	if(birthDay == null || birthDay == '') {
		return "";
	}

	var targetDate = unFormatDate(birthDay);
	var regex = /^\d{8}$/;
	if (regex.test(targetDate) == false) {
		return "";
	}

	var dataFormat = new DateFormat("yyyyMMdd");
	var nowDateInt = parseInt(dataFormat.format(new Date()));

	var targetDateInt = parseInt(targetDate);

	return "" + Math.floor((nowDateInt - targetDateInt) / 10000);
}

/**
 * 年齢描画.
 * 生年月日IDを元に年齢を算出し、年齢出力先IDへ出力します。
 */
function writeAge(birthDayId, ageId) {
	var age = calcAge($("#" + birthDayId).val());
	renderAge(age, ageId);
}

/**
 * 年齢描画.
 * 指定の年齢を年齢出力先IDへ出力します。
 */
function renderAge(age, ageId) {
	$("#" + ageId).text("");
	if(age != '') {
		$("#" + ageId).text("(" + age + "歳)");
	}
}

/**
/* 指定値が"1"の場合、マーク文字列を返します
 * "1"でない場合、空文字を返します。
 * @param source チェック値
 * @param mark マーク文字列
 */
function getMark(source, mark) {

	if(source == null || source != '1') {
		return "";
	}
	return mark;
}

/**
 * ダミー用text追加.
 * ダミー用のspan領域を追加し、textを追加し、要素に追加します。
 * @param id 配置id
 */
function prependDummyText(id) {
	$("#" + id).prepend($("<span />").attr({id:id + "-dummy_area"}));
	$("#" + id + "-dummy_area").append($("<input />").attr({type:"text"}).css({width:"1px",height:"1px"}));
}

/**
 * ダミー用text削除.
 * ダミー用のspan領域を削除します。
 * @param id 配置id
 */
function removeDummyText(id) {
	$("#" + id + "-dummy_area").remove();
}

