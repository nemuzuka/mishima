<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<c:import url="/layout.jsp">
<c:param name="title" value="Mishima BTS/ITS"/>

<c:param name="subMenu">
<c:import url="/management/subMenu.jsp" />
</c:param>
  
<c:param name="content">
<div class="widget">
<form class="form-horizontal">
	<h2 class="title">メンバー検索</h2>
	<table class="search_table">
	<tbody>
	<tr>
		<th>氏名</tf>
		<td><input type="text" id="search_name" /></td>
		<th>メールアドレス</tf>
		<td><input type="text" id="search_mail" /></td>
	</tr>
	</tbody>
	</table>
	<div class="search_ctrl">
		<input type="button" class="btn btn-primary" value="検索" />
		<input type="button" class="btn" value="新規登録" />
	</div>
	
	<div id="result_area" class="result">
		<hr noshade />
		<table class="table table-bordered result_table">
		<thead><tr>
			<th>氏名</th>
			<th>メールアドレス</th>
			<th>権限</th>
			<th></th>
		</tr></thead>
		<tbody>
		<tr>
			<td><a>片桐　一宗</a></td>
			<td>kkaz123@gmail.com</td>
			<td>管理者</td>
			<td><input type="button" class="btn btn-danger btn-mini" value="削"></td>
		</tr>
		<tr>
			<td><a>片桐　一宗2</a></td>
			<td>kkaz1234@gmail.com</td>
			<td>一般</td>
			<td><input type="button" class="btn btn-danger btn-mini" value="削"></td>
		</tr>
		</tbody>
		</table>
	<div>
</form>
</div>

<input type="hidden" id="selected_sub_menu" id="sub_menu2" />

</c:param>

</c:import>