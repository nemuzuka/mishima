/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * 引数の文字列が、空文字でなければDateオブジェクトに変換し
 * yyyy/MM/dd変換します。
 * 前提条件として、引数の文字列はyyyyMMddフォーマットである必要があります。
 */
function formatDateyyyyMMdd(str) {

	return executeFormatDateyyyyMMdd(str, "yyyyMMdd", "yyyy/MM/dd");
}

/**
 * 引数の文字列が、空文字でなければDateオブジェクトに変換し
 * yyyy年MM月dd日変換します。
 * 前提条件として、引数の文字列はyyyyMMddフォーマットである必要があります。
 */
function formatDateyyyyMMdd4JP(str) {

	return executeFormatDateyyyyMMdd(str, "yyyyMMdd", "yyyy年MM月dd日");
}

/**
 * 日付フォーマット変換メイン処理
 */
function executeFormatDateyyyyMMdd(str, formatPattern1, formatPattern2) {
	if(str == null) {
		return '';
	}
	if(str == '') {
		return str;
	}

	var target = unFormatDate(str);
	var regex = /^\d{8}$/;
	if (regex.test(target) == false) {
		return target;
	}

	//日付フォーマット
	var dataFormat = new DateFormat(formatPattern1);
	var dataFormat2 = new DateFormat(formatPattern2);
	var date;
	date = dataFormat.parse(target);
	return dataFormat2.format(date);
}


/**
 * 引数の文字列が、空文字でなければDateオブジェクトに変換し
 * yyyy/MM変換します。
 * 前提条件として、引数の文字列はyyyyMMフォーマットである必要があります。
 */
function formatDateyyyyMM(str) {
	return executeFormatDateyyyyMM(str, "yyyyMM", "yyyy/MM")
}

/**
 * 引数の文字列が、空文字でなければDateオブジェクトに変換し
 * yyyy年MM月変換します。
 * 前提条件として、引数の文字列はyyyyMMフォーマットである必要があります。
 */
function formatDateyyyyMMJP(str) {
	return executeFormatDateyyyyMM(str, "yyyyMM", "yyyy年MM月")
}

/**
 * 日付フォーマット変換メイン処理
 */
function executeFormatDateyyyyMM(str, formatPattern1, formatPattern2) {
	if(str == null) {
		return str;
	}
	if(str == '') {
		return str;
	}

	var target = unFormatDate(str);
	var regex = /^\d{6}$/;
	if (regex.test(target) == false) {
		return target;
	}

	//日付フォーマット
	var dataFormat = new DateFormat(formatPattern1);
	var dataFormat2 = new DateFormat(formatPattern2);
	var date;
	date = dataFormat.parse(target);
	return dataFormat2.format(date);
}


/**
 * 引数の文字列が、空文字でなければDateオブジェクトに変換し
 * MM/dd変換します。
 * 前提条件として、引数の文字列はMMddフォーマットである必要があります。
 */
function formatDateMMdd(str) {

	if(str == null) {
		return str;
	}
	if(str == '') {
		return str;
	}

	var target = unFormatDate(str);
	var regex = /^\d{4}$/;
	if (regex.test(target) == false) {
		return target;
	}

	//日付フォーマット
	var dataFormat = new DateFormat("MMdd");
	var dataFormat2 = new DateFormat("MM/dd");
	var date;
	date = dataFormat.parse(target);
	return dataFormat2.format(date);
}


/**
 * 「/」除去
 */
function unFormatDate(str) {
	if(str == null) {
		return str;
	}
	if(str == '') {
		return str;
	}

	return replaceAll(str, '/', '');
}


/**
 * 引数の文字列が、空文字でなければDateオブジェクトに変換し
 * HH:mm変換します。
 * 前提条件として、引数の文字列はHHmmフォーマットである必要があります。
 */
function formatTimehhmm(str) {
	if(str == null) {
		return str;
	}
	if(str == '') {
		return str;
	}

	var target = unFormatTime(str);
	var regex = /^\d{4}$/;
	if (regex.test(target) == false) {
		return target;
	}

	//時刻フォーマット
	var timeFormat = new DateFormat("HHmm");
	var timeFormat2 = new DateFormat("HH:mm");
	var date;
	date = timeFormat.parse(target);
	return timeFormat2.format(date);
}

/**
 * 「:」除去
 */
function unFormatTime(str) {
	if(str == null) {
		return str;
	}
	if(str == '') {
		return str;
	}
	return replaceAll(str, ':', '');
}
