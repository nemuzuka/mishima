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
			<select class="required-input" id="edit_todo_status"></select>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_todo_title">件名</label>
		<div class="controls">
			<input type="text" class="input-xlarge required-input" id="edit_todo_title">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_todo_period">期限</label>
		<div class="controls">
			<input type="text" class="input-xlarge" id="edit_todo_period">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_todo_content">内容</label>
		<div class="controls">
			<textarea class="input-xlarge" cols="30" rows="8" id="edit_todo_content"></textarea>
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
<div id="todoCommentDialog" title="TODOコメント登録" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>
	<div class="control-group">
		<label class="control-label" for="edit_todo_comment_status">ステータス</label>
		<div class="controls">
			<select class="input-xlarge" id="edit_todo_comment_status"></select>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_todo_comment">コメント</label>
		<div class="controls">
			<textarea class="input-xlarge" cols="30" rows="8" id="edit_todo_comment"></textarea>
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

<%-- TODO詳細ダイアログ --%>
<div id="todoDetailDialog" title="TODO詳細" class="dialog-widget">
<div class="dialog-container form-horizontal dialog-detail" >

	<div class="scroll_area">
		<div class="dialog-area">
			<dl class="detail_dl">
				<dt class="detail_dt">ステータス</dt>
				<dd><select id="detail_todo_status"></select></dd>
				<dt>件名</dt>
				<dd><div id="detail_todo_title"></div></dd>
				<dt>期限</dt>
				<dd><div id="detail_todo_period"></div></dd>
				<dt>内容</dt>
				<dd><div id="detail_todo_content"></div></dd>
			</dl>
		
			<div class="detail_ctrl">
				<input type="button" class="btn" id="todoDetail-edit" value="TODO内容変更" />
				<input type="button" class="btn" id="todoDetail-Comment-add" value="コメント登録" />
				<input type="hidden" id="detail_todo_versionNo" />
				<input type="hidden" id="detail_todo_keyToString" />
			</div>
		</div>

		<div id="todo_comment_list" class="result"></div>
	</div>

	<div class="detail_close">
		<input type="button" class="btn" id="todoDetailDialog-cancel" value="閉じる" />
	</div>
</div>
</div>

