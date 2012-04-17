<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<script type="text/javascript" src="/js/todo/todoDialog.js"></script>

<%-- TODOダイアログ --%>
<div id="todoDialog" title="TODO" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>
	<div class="control-group">
		<label class="control-label" for="edit_todo_status">ステータス</label>
		<div class="controls">
			<select class="input-xlarge" id="edit_todo_status"></select>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_todo_title">件名</label>
		<div class="controls">
			<input type="text" class="input-xlarge required-input" id="edit_todo_title">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_todo_content">内容</label>
		<div class="controls">
			<textarea class="input-xlarge" id="edit_todo_content"></textarea>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_todo_period">期限</label>
		<div class="controls">
			<input type="text" class="input-xlarge required-input" id="edit_todo_period">
		</div>
	</div>

	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="todoDialog-add" value="登録する" />
		<input type="button" class="btn" id="todoDialog-cancel" value="キャンセル" />
		<input type="hidden" id="edit_todo_versionNo" />
		<input type="hidden" id="edit_todo_keyToString" />
	</div>
</fieldset>
</div>
</div>

<%-- TODOコメントダイアログ --%>
<div id="todoCommentDialog" title="TODOコメント" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>
	<div class="control-group">
		<label class="control-label" for="edit_todo_comment_status">ステータス</label>
		<div class="controls">
			<select class="input-xlarge" id="edit_todo_comment_status"></select>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_todo_comment">内容</label>
		<div class="controls">
			<textarea class="input-xlarge" id="edit_todo_comment"></textarea>
		</div>
	</div>

	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="todoCommentDialog-add" value="登録する" />
		<input type="button" class="btn" id="todoCommentDialog-cancel" value="キャンセル" />
		<input type="hidden" id="edit_todo_comment_versionNo" />
		<input type="hidden" id="edit_todo_comment_keyToString" />
	</div>
</fieldset>
</div>
</div>
