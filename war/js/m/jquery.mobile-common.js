/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Mobile用の共通JavaScript定義
 */

//読み込み中のメッセージを表示
function viewLoadingMsg() {
	$.blockUI({
		message: '処理中...',
		fadeIn: 0,
		fadeOut: 0,
		showOverlay: true,
		centerY: false,
		baseZ:10000,
		css: {
			width: '80px',
			top: '100px',
			left: '',
			right: '10px',
			border: 'none',
			padding: '5px',
			backgroundColor: '#458B74',
			'-webkit-border-radius': '10px',
			'-moz-border-radius': '10px',
			opacity: 1.0,
			color: '#fff'
		},
		overlayCSS:  {
			backgroundColor: 'transparent',
			opacity:         0.2
		}
	});
}

//処理結果メッセージを表示
function viewInformMsg(msg) {
	$.blockUI({
		message: msg,
		fadeIn: 0,
		fadeOut: 0,
		showOverlay: true,
		centerY: true,
		centerX: true,
		baseZ:10000,
		css: {
			border: '3px solid #aaa',
			padding: '15px',
			backgroundColor: '#000',
			'-webkit-border-radius': '10px',
			'-moz-border-radius': '10px',
			opacity: 0.6,
			color: '#fff',
			'font-size':'70%'
		},
		overlayCSS:  {
			backgroundColor: 'transparent',
			opacity:         0.6
		}
	});
}

//メッセージtoast表示
function viewToastMsg(msg) {
	var t = $.toaster({showTime:1000, centerY:true});
	t.toast(msg);
}


/**
 * 指定したURLに移動します。
 */
function moveUrl(url) {
	viewLoadingMsg();
	document.location.href = url;
}

//先頭移動
function moveTop() {
	$('html,body').animate({ scrollTop: 0 }, 0);
}

/* 指定したidの選択状態のvalueを取得します. */
function getSelectArray(targetId) {
	var retArray = new Array();
	$("#" + targetId + " option:selected").each(function() {
		retArray.push($(this).val());
	});
	return retArray;
}

