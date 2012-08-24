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
	<div data-role="header" data-position="inline">
		<a href="#" data-icon="back" data-theme="b" id="back">戻る</a>
		<h1 id="header_title"></h1>
	</div>
</c:param>

<c:param name="content">
<script type="text/javascript" src="/js/m/bts/ticket/edit.js"></script>

<form class="widget">
	<fieldset data-role="fieldcontain">
		<label for="todo_title">件名</label>
		<input type="text" class="required-input" id="ticket_title">
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_status">ステータス</label>
		<select id="ticket_status"></select>
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_content">内容</label>
		<textarea id="ticket_content"></textarea>
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_endCondition">終了条件</label>
		<textarea id="ticket_endCondition"></textarea>
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_startDate">開始日</label>
		<input id="ticket_startDate" type="date" data-role="datebox"
				data-options='{"mode": "calbox"}' placeholder="開始日">
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_period">期限</label>
		<input id="ticket_period" type="date" data-role="datebox"
				data-options='{"mode": "calbox"}' placeholder="期限">
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_priority">優先度</label>
		<select id="ticket_priority"></select>
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_kind">種別</label>
		<select id="ticket_kind"></select>
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_category">カテゴリ</label>
		<select id="ticket_category"></select>
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_targetVersion">バージョン</label>
		<select id="ticket_targetVersion"></select>
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_milestone">マイルストーン</label>
		<select id="ticket_milestone"></select>
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_targetMember">担当者</label>
		<select id="ticket_targetMember"></select>
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="ticket_parentKey">親チケットNo</label>
		<span id="ticket_project_id"></span><input type="text" id="ticket_parentKey">
	</fieldset>

</form>

<nav data-role="navbar">
<ul id="footer_navbar">
	<li><a href="#" id="nav_back">戻る</a></li>
	<li><a href="#" id="nav_save" data-theme="b">変更する</a></li>
</ul>
</nav>

<input type="hidden" id="keyToString" value="${f:h(keyToString)}" />
<input type="hidden" id="dashbord" value="${f:h(dashbord)}" />
<input type="hidden" id="edit_ticket_versionNo" />

<input type="hidden" id="selected_main_menu" value="main_menu3" />
</c:param>

</c:import>