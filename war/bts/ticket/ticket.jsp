<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<c:import url="/layout.jsp">
<c:param name="title" value="Mishima BTS/ITS"/>

<c:param name="subMenu">
<c:import url="/bts/subMenu.jsp" />
</c:param>
  
<c:param name="content">

<script type="text/javascript" src="/js/bts/ticket.js"></script>

<div class="widget">
<form class="form-horizontal">

	<h2 class="title">チケット検索</h2>
	<table class="search_table">
	<tbody>
	<tr>
		<th>ステータス</th>
		<td id="status_area" class="controls" colspan="3"></td>
	</tr>
	<tr>
		<th>件名</th>
		<td  colspan="3"><input type="text" id="search_title" class="input-xlarge" /></td>
	</tr>

	<tr>
		<th>優先度</th>
		<td><select id="search_priority"></select></td>
		<th>種別</th>
		<td><select id="search_kind"></select></td>
	</tr>

	<tr>
		<th>カテゴリ</th>
		<td><select id="search_category"></select></td>
		<th>バージョン</th>
		<td><select id="search_version"></select></td>
	</tr>

	<tr>
		<th>マイルストーン</th>
		<td><select id="search_milestone"></select></td>
		<th>担当者</th>
		<td><select id="search_targetMember"></select></td>
	</tr>

	<tr>
		<th>期限</th>
		<td  colspan="3"><input type="text" id="search_fromPeriod" />　〜　<input type="text" id="search_toPeriod" /></td>
	</tr>
	</tbody>
	</table>
	<div class="search_ctrl">
		<input type="button" class="btn btn-primary" id="searchTicketBtn" value="検索" />
		<input type="button" class="btn" value="新規登録" id="addTicketBtn" />
	</div>
	
	<div id="result_area" class="result">
	</div>

<%-- 一覧表示時の件数. --%>
<input type="hidden" id="listCnt" value="0" />

</form>
</div>

<c:import url="/bts/ticket/ticketDialog.jsp"/>

<input type="hidden" id="selected_sub_menu" value="sub_menu3" />

</c:param>

</c:import>