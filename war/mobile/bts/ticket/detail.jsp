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
		<h1>チケット詳細</h1>
	</div>
</c:param>

<c:param name="content">
<script type="text/javascript" src="/js/m/bts/ticket/detail.js"></script>

<div class="widget">
	<h3 id="detail_ticket_title"></h3>
	<h4 id="detail_ticket_no" align="right"></h4>
	<dl class="detail">
		<dt class="dt">ステータス</dt>
		<dd class="dd"><select id="detail_ticket_status"></select></dd>
	
		<dt class="dt">内容</dt>
		<dd class="dd" id="detail_ticket_content"></dd>

		<dt class="dt">終了条件</dt>
		<dd class="dd" id="detail_ticket_endCondition"></dd>

		<dt class="dt">開始日</dt>
		<dd class="dd" id="detail_ticket_startDate"></dd>

		<dt class="dt">期限</dt>
		<dd class="dd" id="detail_ticket_period"></dd>

		<div data-role="collapsible">
			<h3>詳細情報</h3>
			<dt class="dt">優先度</dt>
			<dd class="dd" id="detail_ticket_priority"></dd>
			<dt class="dt">カテゴリ</dt>
			<dd class="dd" id="detail_ticket_category"></dd>
			<dt class="dt">種別</dt>
			<dd class="dd" id="detail_ticket_kind"></dd>
			<dt class="dt">バージョン</dt>
			<dd class="dd" id="detail_ticket_targetVersion"></dd>
			<dt class="dt">マイルストーン</dt>
			<dd class="dd" id="detail_ticket_milestone"></dd>
			<dt class="dt">担当者</dt>
			<dd class="dd" id="detail_ticket_targetMember"></dd>
		</div>
	
		<div data-role="collapsible">
			<h3>コメント</h3>
			<div id="followViewArea">
				<table id="comment_table" class="comment_table"></table>
				<a href="#" id="addComment" data-role="button" data-inline="true" >コメント追加</a>
			</div>
		</div>
	
	</dl>
</div>

<nav data-role="navbar">
<ul id="footer-navbar">
	<li><a href="#" id="nav_back">戻る</a></li>
	<li><a href="#" id="nav_edit">内容変更</a></li>
	<li><a href="#" id="nav_delete" data-theme="b">削除する</a></li>
</ul>
</nav>

<input type="hidden" id="keyToString" value="${f:h(keyToString)}" />
<input type="hidden" id="dashbord" value="${f:h(dashbord)}" />
<input type="hidden" id="detail_ticket_versionNo" />

<input type="hidden" id="selected_main_menu" value="main_menu3" />
</c:param>

</c:import>