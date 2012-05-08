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
				<input type="text" class="input-xxlarge required-input" id="edit_ticket_title">
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
			<label class="control-label" for="edit_ticket_startDate">開始日</label>
			<div class="controls">
				<input type="text" class="input-xlarge" id="edit_ticket_startDate">
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
					<span id="edit_ticket_project_id"></span><input type="text" class="input-mini" id="edit_ticket_parentKey">
					<p>※親のチケットNoを入力することでチケットに関連付けを行うことができます。<br />
					※プロジェクトをまたいで登録することはできません</p>
				</div>
			</div>
		</div>
	</div>

	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="ticketDialog-add" value="登録する" />
		<input type="button" class="btn" id="ticketDialog-cancel" value="キャンセル" />
		<input type="hidden" id="edit_ticket_versionNo" />
		<input type="hidden" id="edit_ticket_keyToString" />
		<input type="hidden" id="edit_ticket_base_keyToString" />
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
			<dl>
				<dt>チケットNo</dt>
				<dd><div id="detail_ticket_no"></div><input type="hidden" id="detail_ticket_no_val" /></dd>
				<dt>ステータス</dt>
				<dd><select id="detail_ticket_status"></select></dd>
				<dt>件名</dt>
				<dd><div id="detail_ticket_title"></div></dd>
				<dt>内容</dt>
				<dd><div id="detail_ticket_content"></div></dd>
				<dt>終了条件</dt>
				<dd><div id="detail_ticket_endCondition"></div></dd>
				<dt>開始日</dt>
				<dd><div id="detail_ticket_startDate"></div></dd>
				<dt>期限</dt>
				<dd><div id="detail_ticket_period"></div></dd>
			</dl>
			
			<div style="float:left;">
				<dl style="float:left;">
					<dt class="detail_dt">優先度</dt>
					<dd><div id="detail_ticket_priority"></div></dd>
					<dt class="detail_dt">カテゴリ</dt>
					<dd><div id="detail_ticket_category"></div></dd>
				</dl>
			</div>
			<div style="float:left; margin-left:15px">
				<dl class="detail_dl" style="float:left;">
					<dt class="detail_dt">種別</dt>
					<dd><div id="detail_ticket_kind"></div></dd>
					<dt class="detail_dt">バージョン</dt>
					<dd><div id="detail_ticket_targetVersion"></div></dd>
				</dl>
			</div>
			<div style="float:left; margin-left:15px">
				<dl class="detail_dl" style="float:left;">
					<dt class="detail_dt">マイルストーン</dt>
					<dd><div id="detail_ticket_milestone"></div></dd>
					<dt class="detail_dt">担当者</dt>
					<dd><div id="detail_ticket_targetMember"></div></dd>
				</dl>
			</div>
			
			<div id="ticket_upload_file_list" style="clear:left;" class="result"></div>
			
			<div id="detail_ticket_conn_area" style="clear:left;"></div>
			
			<div class="detail_ctrl" >
				<input type="button" class="btn" id="ticketDetail-edit" value="内容変更" />
				<input type="button" class="btn" id="ticketDetail-Comment-add" value="コメント登録" />
				<input type="button" class="btn" id="ticketDetail-child-add" value="子チケット作成" />
				<input type="button" class="btn" id="ticketDetail-copy-add" value="コピー新規" />
				<input type="button" class="btn" id="ticketDetail-add-file" value="ファイル追加" />
				<input type="hidden" id="detail_ticket_versionNo" />
				<input type="hidden" id="detail_ticket_keyToString" />
			</div>
		</div>

		<div id="ticket_comment_list" class="result"></div>
	</div>

	<div class="detail_close">
		<input type="button" class="btn" id="ticketDetailDialog-cancel" value="閉じる" />
	</div>
</div>
</div>

<%-- チケット概要ダイアログ --%>
<div id="ticketSummaryDialog" title="チケット概要" class="dialog-widget">
<div class="dialog-container form-horizontal dialog-detail" >

	<div class="scroll_area">
		<div class="dialog-area">
			<dl>
				<dt>チケットNo</dt>
				<dd><div id="detail_ticket_summary_no"></div></dd>
				<dt>件名</dt>
				<dd><div id="detail_ticket_summary_title"></div></dd>
				<dt>内容</dt>
				<dd><div id="detail_ticket_summary_content"></div></dd>
				<dt>終了条件</dt>
				<dd><div id="detail_ticket_summary_endCondition"></div></dd>
				<dt>開始日</dt>
				<dd><div id="detail_ticket_summary_startDate"></div></dd>
				<dt>期限</dt>
				<dd><div id="detail_ticket_summary_period"></div></dd>
			</dl>

			<div class="detail_ctrl" >
				<input type="button" class="btn" id="ticketSummary-detail" value="詳細表示" />
				<input type="hidden" id="detail_summary_versionNo" />
				<input type="hidden" id="detail_summary_keyToString" />
			</div>
		</div>
	</div>

	<div class="detail_close">
		<input type="button" class="btn" id="ticketSummaryDialog-cancel" value="閉じる" />
	</div>
</div>
</div>

<%-- ファイルアップロードダイアログ --%>
<div id="ticketFileUploadDialog" title="ファイルアップロード" class="dialog-widget">
<form id="fileUploadForm" method="post" enctype="multipart/form-data" target="fileUploader">
<input type="hidden" id="fileUploadToken" name="jp.co.nemuzuka.token" />
<input type="hidden" id="ticket_file_upload_keyToString" name="keyToString" />
<div class="dialog-container form-horizontal dialog-detail" >

<fieldset>
	<div class="control-group">
		<label class="control-label" for="ticket_upload_file">ファイル</label>
		<div class="controls">
			<input type="file" id="ticket_upload_file" name="uploadFile" />
		</div>
	</div>

	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="ticketFileUploadDialog-add" value="アップロード" />
		<input type="button" class="btn" id="ticketFileUploadDialog-cancel" value="キャンセル" />
	</div>
</fieldset>
</div>
</form>
</div>
<%-- アップロード結果取得用iframe --%>
<iframe name="fileUploader" src="" style="width:0px;height:0px;border:0px;"></iframe>
<%-- ダウンロード取得用frame --%>
<form name="fileDownloader" id="fileDownloader" method="post" target="_self" style="width:0px;height:0px;border:0px;">
	<input type="hidden" id="uploadFileKeyString" name="keyString" />
	<input type="hidden" id="uploadFileTicketKeyString" name="ticketKeyString" />
</form>
