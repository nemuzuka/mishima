<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<c:import url="/layout.jsp">
<c:param name="title" value="Mishima BTS/ITS"/>

<c:param name="subMenu">
<c:import url="/todo/subMenu.jsp" />
</c:param>
  
<c:param name="content">

<script type="text/javascript" src="/js/todo/todo.js"></script>

<div class="widget">
<form class="form-horizontal">

	<h2 class="title">TODO検索</h2>
	<table class="search_table">
	<tbody>
	<tr>
		<th>ステータス</th>
		<td id="status_area" class="controls"></td>
	</tr>
	<tr>
		<th>件名</th>
		<td><input type="text" id="search_title" class="input-xlarge" /></td>
	</tr>
	<tr>
		<th>期限</th>
		<td><input type="text" id="search_fromPeriod" />　〜　<input type="text" id="search_toPeriod" /></td>
	</tr>
	</tbody>
	</table>
	<div class="search_ctrl">
		<input type="button" class="btn btn-primary" id="searchTodoBtn" value="検索" />
		<input type="button" class="btn" value="新規登録" id="addTodoBtn" />
	</div>
	
	<div id="result_area" class="result">
	</div>

<%-- 一覧表示時の件数. --%>
<input type="hidden" id="listCnt" value="0" />

</form>
</div>

<c:import url="/todo/todoDialog.jsp"/>

<input type="hidden" id="selected_sub_menu" value="sub_menu1" />

</c:param>

</c:import>