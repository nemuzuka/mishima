<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<script type="text/javascript" src="/js/bts/ticketDialog.js"></script>

<%-- チケット登録ダイアログ --%>
<div id="ticketDialog" title="チケット" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>

	<div class="scroll_area">

		<div class="control-group">
			<label class="control-label" for="edit_ticket_status">ステータス</label>
			<div class="controls">
				<select class="required-input" id="edit_ticket_status"></select>
			</div>
		</div>
	
		<div class="control-group">
			<label class="control-label" for="edit_ticket_title">件名</label>
			<div class="controls">
				<input type="text" class="input-xlarge required-input" id="edit_ticket_title">
			</div>
		</div>
	
		<div class="control-group">
			<label class="control-label" for="edit_ticket_content">内容</label>
			<div class="controls">
				<textarea class="input-xxlarge" cols="30" rows="6" id="edit_ticket_content"></textarea>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="edit_ticket_endCondition">終了条件</label>
			<div class="controls">
				<textarea class="input-xxlarge" cols="30" rows="6" id="edit_ticket_endCondition"></textarea>
			</div>
		</div>
	
	
		<div class="control-group">
			<label class="control-label" for="edit_ticket_period">期限</label>
			<div class="controls">
				<input type="text" class="input-xlarge" id="edit_ticket_period">
			</div>
		</div>
		
		<div>
			<div class="control-group" style="float:left;">
				<label class="control-label" for="edit_ticket_priority">優先度</label>
				<div class="controls">
					<select id="edit_ticket_priority"></select>
				</div>
			</div>
			<div class="control-group" style="float:left;">
				<label class="control-label" for="edit_ticket_kind">種別</label>
				<div class="controls">
					<select id="edit_ticket_kind"></select>
				</div>
			</div>
		</div>
		<div>
			<div class="control-group" style="float:left;">
				<label class="control-label" for="edit_ticket_category">カテゴリ</label>
				<div class="controls">
					<select id="edit_ticket_category"></select>
				</div>
			</div>
			<div class="control-group" style="float:left;">
				<label class="control-label" for="edit_ticket_targetVersion">バージョン</label>
				<div class="controls">
					<select id="edit_ticket_targetVersion"></select>
				</div>
			</div>
		</div>
		<div>
			<div class="control-group" style="float:left;">
				<label class="control-label" for="edit_ticket_milestone">マイルストーン</label>
				<div class="controls">
					<select id="edit_ticket_milestone"></select>
				</div>
			</div>
			<div class="control-group" style="float:left;">
				<label class="control-label" for="edit_ticket_targetMember">担当者</label>
				<div class="controls">
					<select id="edit_ticket_targetMember"></select>
				</div>
			</div>
		</div>
		
		<div style="clear:left;">
			<div class="control-group">
				<label class="control-label" for="edit_ticket_parentKey">親チケットNo</label>
				<div class="controls">
					<input type="text" id="edit_ticket_parentKey">
					<p>※親のチケットNoを入力することでチケットに関連付けを行うことができます</p>
				</div>
			</div>
		</div>
	</div>

	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="ticketDialog-add" value="登録する" />
		<input type="button" class="btn" id="ticketDialog-cancel" value="キャンセル" />
		<input type="hidden" id="edit_ticket_versionNo" />
		<input type="hidden" id="edit_ticket_keyToString" />
	</div>
</fieldset>
</div>
</div>

<%-- チケットコメントダイアログ --%>
<div id="ticketCommentDialog" title="コメント登録" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>
	<div class="control-group">
		<label class="control-label" for="edit_ticket_comment_status">ステータス</label>
		<div class="controls">
			<select class="input-xlarge" id="edit_ticket_comment_status"></select>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_ticket_comment">コメント</label>
		<div class="controls">
			<textarea class="input-xlarge required-input" cols="30" rows="8" id="edit_ticket_comment"></textarea>
		</div>
	</div>

	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="ticketCommentDialog-add" value="登録する" />
		<input type="button" class="btn" id="ticketCommentDialog-cancel" value="キャンセル" />
		<input type="hidden" id="edit_ticket_comment_versionNo" />
		<input type="hidden" id="edit_ticket_comment_keyToString" />
	</div>
</fieldset>
</div>
</div>

<%-- チケット詳細ダイアログ --%>
<div id="ticketDetailDialog" title="チケット詳細" class="dialog-widget">
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
				<input type="button" class="btn" id="todoDetail-edit" value="内容変更" />
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

