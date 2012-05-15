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
<c:import url="/layout.jsp">
<c:param name="title" value="ITS [Mishima]"/>

<c:param name="subMenu">
<c:import url="/bts/subMenu.jsp" />
</c:param>
  
<c:param name="content">

<script type="text/javascript" src="/js/date.js"></script>
<script type="text/javascript" src="/js/jquery.ganttView.js"></script>
<script type="text/javascript" src="/js/bts/gantt.js"></script>
<script type="text/javascript" src="/js/bts/ticket_status.js"></script>
<link href="/css/jquery.ganttView.css" rel="stylesheet">

<div class="widget">
<form class="form-horizontal">

	<h2 class="title">チャート出力<span id="selectedProjectName"></span></h2>
	<table class="search_table">
	<tbody>
	<tr>
		<th>ステータス</th>
		<td id="status_area" class="controls" colspan="3"></td>
	</tr>
	<tr>
		<th>マイルストーン</th>
		<td><select id="search_milestone"></select></td>
		<th>担当者</th>
		<td><select id="search_targetMember"></select></td>
	</tr>
	</tbody>
	</table>
	<div class="search_ctrl">
		<input type="button" class="btn btn-primary" id="exportGantttBtn" value="チャート出力" />
	</div>
	
	<div id="result_area" class="result">
	</div>

<%-- 一覧表示時の件数. --%>
<input type="hidden" id="listCnt" value="0" />

</form>
</div>

<c:import url="/bts/ticket/ticketDialog.jsp"/>

<input type="hidden" id="selected_sub_menu" value="sub_menu5" />

</c:param>

</c:import>