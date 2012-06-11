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

<script type="text/javascript" src="/js/management/member.js"></script>

<div class="widget">
<form class="form-horizontal">

	<h2 class="title">メンバー検索</h2>
	<table class="search_table">
	<tbody>
	<tr>
		<th>ニックネーム</th>
		<td><input type="text" id="search_name" /></td>
		<th>メールアドレス</th>
		<td><input type="text" id="search_mail" /></td>
	</tr>
	</tbody>
	</table>
	<div class="search_ctrl">
		<input type="button" class="btn btn-primary" id="searchMemberBtn" value="検索" />
		<input type="button" class="btn" value="新規登録" id="addMemberBtn" />
	</div>
	
	<div id="result_area" class="result">
	</div>

<%-- 一覧表示時の件数. --%>
<input type="hidden" id="listCnt" value="0" />

</form>
</div>


<%-- メンバーダイアログ --%>
<div id="memberDialog" title="メンバー" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>
	<div class="control-group">
		<label class="control-label" for="edit_name">ニックネーム</label>
		<div class="controls">
			<input type="text" class="input-xlarge required-input" id="edit_name">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="edit_mail">メールアドレス</label>
		<div class="controls">
			<input type="text" class="input-xlarge required-input" id="edit_mail">
			<p class="help-block">googleの認証で使用するメールアドレスです。同じメールアドレスは登録できません</p>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="edit_timeZone">タイムゾーン</label>
		<div class="controls">
			<select class="input-xlarge required-input" id="edit_timeZone"></select>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="edit_authority">権限</label>
		<div class="controls">
			<label class="radio inline"><input type="radio" name="authority" value="admin">管理者</label>
			<label class="radio inline"><input type="radio" name="authority" value="normal">一般</label>
		</div>
	</div>
	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="memberDialog-add" value="登録する" />
		<input type="button" class="btn" id="memberDialog-cancel" value="キャンセル" />
		<input type="hidden" id="edit_versionNo" />
		<input type="hidden" id="edit_keyToString" />
	</div>
</fieldset>
</div>
</div>

<input type="hidden" id="selected_sub_menu" value="sub_menu2" />

</c:param>

</c:import>