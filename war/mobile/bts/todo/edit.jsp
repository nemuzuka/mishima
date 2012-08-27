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
<script type="text/javascript" src="/js/m/bts/todo/edit.js"></script>

<form class="widget">
	<fieldset data-role="fieldcontain">
		<label for="todo_title">件名</label>
		<input type="text" class="required-input" id="todo_title">
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="todo_status">ステータス</label>
		<select id="todo_status"></select>
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="todo_tag">タグ</label>
		<input type="text" id="todo_tag">
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="todo_period">期限</label>
		<input id="todo_period" type="date" data-role="datebox"
				data-options='{"mode": "calbox"}' placeholder="期限">
	</fieldset>

	<fieldset data-role="fieldcontain">
		<label for="todo_content">内容</label>
		<textarea id="todo_content"></textarea>
	</fieldset>

</form>

<nav data-role="navbar">
<ul id="footer_navbar">
	<li><a href="#" id="nav_save" data-theme="b">変更する</a></li>
	<li><a href="#" id="nav_back">戻る</a></li>
</ul>
</nav>

<input type="hidden" id="keyToString" value="${f:h(keyToString)}" />
<input type="hidden" id="dashbord" value="${f:h(dashbord)}" />
<input type="hidden" id="edit_todo_versionNo" />

<input type="hidden" id="selected_main_menu" value="main_menu2" />
</c:param>

</c:import>