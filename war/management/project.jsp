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
<c:import url="/management/subMenu.jsp" />
</c:param>
  
<c:param name="content">

<script type="text/javascript" src="/js/management/project.js"></script>

<div class="widget">
<form class="form-horizontal">

	<h2 class="title">プロジェクト検索</h2>
	<table class="search_table">
	<tbody>
	<tr>
		<th>プロジェクト名</th>
		<td><input type="text" id="search_projectName" /></td>
	</tr>
	</tbody>
	</table>
	<div class="search_ctrl">
		<input type="button" class="btn btn-primary" id="searchProjectBtn" value="検索" />
		<input type="button" class="btn" value="新規登録" id="addProjectBtn" />
	</div>
	
	<div id="result_area" class="result">
	</div>

<%-- 一覧表示時の件数. --%>
<input type="hidden" id="listCnt" value="0" />

</form>
</div>


<%-- プロジェクトダイアログ --%>
<div id="projectDialog" title="プロジェクト" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>
	<div class="control-group">
		<label class="control-label" for="edit_project_name">プロジェクト名</label>
		<div class="controls">
			<input type="text" class="input-xlarge required-input" id="edit_project_name">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_project_id">識別子</label>
		<div class="controls">
			<input type="text" class="input-xlarge required-input" id="edit_project_id">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_project_summary">概要</label>
		<div class="controls">
			<textarea class="input-xlarge" cols="30" rows="10" id="edit_project_summary"></textarea>
		</div>
	</div>

	<%-- 新規の時だけ表示 --%>
	<div class="control-group" id="project_admin_area">
		<label class="control-label" for="project_admin">管理者</label>
		<div class="controls">
			<select id="project_admin">
			</select>
		</div>
	</div>
	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="projectDialog-add" value="登録する" />
		<input type="button" class="btn" id="projectDialog-cancel" value="キャンセル" />
		<input type="hidden" id="edit_versionNo" />
		<input type="hidden" id="edit_keyToString" />
	</div>
</fieldset>
</div>
</div>

<input type="hidden" id="selected_sub_menu" value="sub_menu1" />

</c:param>

</c:import>