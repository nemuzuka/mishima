<%-- 
/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
 --%>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<c:import url="/mobile/layout-m.jsp">
<c:param name="title" value="ITS [Mishima]"/>

<c:param name="pageHeader">
	<div data-role="header">
		<h1>チケット検索</h1>
	</div>
</c:param>

<c:param name="content">
<script type="text/javascript" src="/js/m/bts/ticket/search.js"></script>

<form class="widget">
	<fieldset data-role="fieldcontain">
		<label for="ticket_status">ステータス</label>
		<select id="ticket_status" multiple="multiple"></select>
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_title">件名</label>
		<input type="text" id="ticket_title">
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="targetMember">担当者</label>
		<select id="targetMember"></select>
	</fieldset>

	<fieldset data-role="fieldcontain" data-position="inline">
		<label>期限From</label>
		<input id="search_fromPeriod" type="date" data-role="datebox"
				data-options='{"mode": "calbox"}'>
		<label>期限To</label>
		<input id="search_toPeriod" type="date" data-role="datebox"
				data-options='{"mode": "calbox"}'>
	</fieldset>

</form>

<nav data-role="navbar">
<ul id="footer-navbar">
	<li><a href="#" id="nav_search">検索</a></li>
	<li><a href="#" id="nav_create">新規登録</a></li>
</ul>
</nav>

<input type="hidden" id="selected_main_menu" value="main_menu3" />
</c:param>

</c:import>