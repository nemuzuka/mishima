<%-- 
/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
 --%>

<!DOCTYPE html>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html lang="ja">
<head>
<meta charset="utf-8" >
<meta name="viewport" content="width=device-width,user-scalable=no,initial-scale=1,maximum-scale=1" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>${param.title}</title>

<link href="/css/m/theme/mishima.min.css" rel="stylesheet">
<link href="/css/m/jquery.mobile.structure-1.1.1.min.css" rel="stylesheet">
<link href="/css/m/jquery.mobile.datebox.min.css" rel="stylesheet">
<link href="/css/m/common.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>

<script type="text/javascript">
<!--
$(document).bind("mobileinit", function(){

	$.mobile.loadingMessage = '読込み中';
	$.mobile.pageLoadErrorMessage = '読込みに失敗しました';
	$.mobile.page.prototype.options.backBtnText = '戻る';
	$.mobile.dialog.prototype.options.closeBtnText = '閉じる';
	$.mobile.selectmenu.prototype.options.closeText= '閉じる';
	$.mobile.listview.prototype.options.filterPlaceholder = '検索文字列...';
	$.mobile.selectmenu.prototype.options.hidePlaceholderMenuItems = false;

	$.extend( $.mobile , {
		ajaxEnabled: false,
		pushStateEnabled: false,
		ajaxLinksEnabled: false,
		ajaxFormsEnabled: false,
		hashListeningEnabled: false,
	});
});

$(function(){
	var selectedMenu = $("#selected_main_menu").val();
	$("#" + selectedMenu).addClass("ui-btn-active");

});

function hideAdBar(){
	setTimeout("scrollTo(0,1)", 100);
}

//-->
</script>

<script type="text/javascript" src="/js/m/jquery.mobile-1.1.1.min.js"></script>
<script type="text/javascript" src="/js/m/jquery.mobile.datebox.min.js"></script>
<script type="text/javascript" src="/js/m/jquery.mobile-common.js"></script>
<script type="text/javascript" src="/js/common.js" ></script>
<script type="text/javascript" src="/js/dateformat.js" ></script>
<script type="text/javascript" src="/js/format-utils.js" ></script>
<script type="text/javascript" src="/js/jquery.blockUI.js" ></script>
<script type="text/javascript" src="/js/jquery.toaster.min.js"></script>
<script type="text/javascript" src="/js/validate-utils.js"></script>
<script type="text/javascript" src="/js/i18n/datebox-ja.js"></script>


</head>
<body onLoad="hideAdBar()" onOrientationChange="hideAdBar()">
<div data-role="page" id="main_page" data-theme="a">

<!-- メニュー -->
<c:import url="/mobile/menu-m.jsp"/>

<!-- 本文 -->
<div data-role="content">
<input type="hidden" id="token" />
${param.content}
</div>

<!-- メニュー -->
<c:import url="/mobile/footer-m.jsp"/>


</div>

</body>
</html>