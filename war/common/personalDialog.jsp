<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<script type="text/javascript" src="/js/common/personalDialog.js"></script>

<%-- 個人設定ダイアログ --%>
<div id="personalDialog" title="個人設定" class="dialog-widget">
<div class="dialog-container form-horizontal" >
<fieldset>
	<div class="control-group">
		<label class="control-label" for="edit_person_name">ニックネーム</label>
		<div class="controls">
			<input type="text" class="input-xlarge required-input" id="edit_person_name">
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="edit_person_memo">メモ</label>
		<div class="controls">
			<textarea class="input-xlarge" cols="30" rows="8" id="edit_person_memo"></textarea>
		</div>
	</div>

	<div class="edit_ctrl">
		<input type="button" class="btn btn-primary" id="personalDialog-update" value="変更する" />
		<input type="button" class="btn" id="personalDialog-cancel" value="キャンセル" />
		<input type="hidden" id="edit_person_versionNo" />
		<input type="hidden" id="edit_person_keyToString" />
	</div>
</fieldset>
</div>
</div>

