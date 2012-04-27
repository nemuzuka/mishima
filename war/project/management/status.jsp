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
<c:param name="title" value="Mishima BTS/ITS"/>

<c:param name="subMenu">
<c:import url="/project/management/subMenu.jsp" />
</c:param>
  
<c:param name="content">

<script type="text/javascript" src="/js/project/management/status.js"></script>

<div class="widget">
<form class="form-horizontal">

	<h2 class="title">ステータス管理<span id="selectedProjectName"></span></h2>
	
	<table class="edit_main_table">
	<tbody>
	<tr>
		<th>ステータス</th>
		<td style="padding-right: 30px;">
			<p>ステータス</p>
			<textarea id="edit_status_name" cols="30" rows="10"></textarea>
		</td>
		<td>
			<p>完了とみなすステータス</p>
			<textarea id="edit_close_status_name" cols="30" rows="10"></textarea>
		</td>
	</tr>
	</tbody>
	</table>
	<p>改行で区切ることで複数の項目を設定することができます</p>
	<div class="edit_main_ctrl">
		<input type="button" class="btn btn-primary" id="status-add" value="登録する" />
		<input type="button" class="btn" id="init-get" value="初期値取得" />
	</div>

	<input type="hidden" id="edit_versionNo" />
	<input type="hidden" id="edit_keyToString" />

</form>
</div>

<input type="hidden" id="selected_sub_menu" value="sub_menu6" />

</c:param>

</c:import>