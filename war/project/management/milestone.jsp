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
<c:import url="/project/management/subMenu.jsp" />
</c:param>
  
<c:param name="content">

<script type="text/javascript" src="/js/project/management/milestone.js"></script>

<div class="widget">
<form class="form-horizontal">

	<h2 class="title">マイルストーン管理<span id="selectedProjectName"></span></h2>
	<div class="search_ctrl">
		<input type="button" class="btn btn-primary" id="searchMilestoneBtn" value="検索" />
		<input type="button" class="btn" value="新規登録" id="addMilestoneBtn" />
		<input type="button" class="btn" value="表示順変更" id="sortMilestoneBtn" />
	</div>
	
	<div id="result_area" class="result">
	</div>

<%-- 一覧表示時の件数. --%>
<input type="hidden" id="listCnt" value="0" />

</form>
</div>


<%-- マイルストーンダイアログ --%>
<div id="milestoneDialog" title="マイルストーン" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>
	<div class="control-group">
		<label class="control-label" for="edit_milestone_name">マイルストーン名</label>
		<div class="controls">
			<input type="text" class="input-xlarge required-input" id="edit_milestone_name">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="edit_start_date">開始日</label>
		<div class="controls">
			<input type="text" class="input-xlarge" id="edit_start_date">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="edit_end_date">終了日</label>
		<div class="controls">
			<input type="text" class="input-xlarge" id="edit_end_date">
		</div>
	</div>

	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="milestoneDialog-add" value="登録する" />
		<input type="button" class="btn" id="milestoneDialog-cancel" value="キャンセル" />
		<input type="hidden" id="edit_versionNo" />
		<input type="hidden" id="edit_keyToString" />
	</div>
</fieldset>
</div>
</div>

<%-- 表示順変更ダイアログ --%>
<div id="milestoneSortDialog" title="表示順変更" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>
	<div class="control-group">
		<label class="control-label" for="milestone_to">マイルストーン</label>
		<div class="controls">
			<div class="sort_to">
				<select id="milestone_to" size="10" multiple="multiple"></select>
			</div>
			<div class="sort_action_area">
				<div  style="margin-bottom: 50px;">
					<input type="button" class="btn sort_up_button" id="sort_up" value="↑" />
				</div>
				<div>
					<input type="button" class="btn" id="sort_down" value="↓" />
				</div>
			</div>
		</div>
	</div>

	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="milestoneSortDialog-execute" value="変更する" />
		<input type="button" class="btn" id="milestoneSortDialog-cancel" value="キャンセル" />
	</div>
</fieldset>
</div>
</div>


<input type="hidden" id="selected_sub_menu" value="sub_menu4" />

</c:param>

</c:import>