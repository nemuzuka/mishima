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

<script type="text/javascript" src="/js/project/management/kind.js"></script>

<div class="widget">
<form class="form-horizontal">

	<h2 class="title">種別管理</h2>
	<div class="search_ctrl">
		<input type="button" class="btn btn-primary" id="searchKindBtn" value="検索" />
		<input type="button" class="btn" value="新規登録" id="addKindBtn" />
		<input type="button" class="btn" value="表示順変更" id="sortKindBtn" />
	</div>
	
	<div id="result_area" class="result">
	</div>

<%-- 一覧表示時の件数. --%>
<input type="hidden" id="listCnt" value="0" />

</form>
</div>


<%-- 種別ダイアログ --%>
<div id="kindDialog" title="種別" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>
	<div class="control-group">
		<label class="control-label" for="edit_project_name">種別名</label>
		<div class="controls">
			<input type="text" class="input-xlarge" id="edit_kind_name">
		</div>
	</div>

	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="kindDialog-add" value="登録する" />
		<input type="button" class="btn" id="kindDialog-cancel" value="キャンセル" />
		<input type="hidden" id="edit_versionNo" />
		<input type="hidden" id="edit_keyToString" />
	</div>
</fieldset>
</div>
</div>

<%-- 表示順変更ダイアログ --%>
<div id="kindSortDialog" title="表示順変更" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>
	<div class="control-group">
		<label class="control-label" for="kind_to">種別</label>
		<div class="controls">
			<div class="sort_to">
				<select id="kind_to" size="10" multiple="multiple"></select>
			</div>
			<div class="sort_action_area">
				<input type="button" class="btn sort_up_button" id="sort_up" value="↑" style="float:left;margin-bottom: 50px;" /><br/>
				<input type="button" class="btn" id="sort_down" value="↓" />
			</div>
		</div>
	</div>

	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="kindSortDialog-execute" value="変更する" />
		<input type="button" class="btn" id="kindSortDialog-cancel" value="キャンセル" />
	</div>
</fieldset>
</div>
</div>


<input type="hidden" id="selected_sub_menu" value="sub_menu2" />

</c:param>

</c:import>