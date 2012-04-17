function initTodoDialog() {
	$("#todoDialog").dialog({
		modal:true,
		autoOpen:false,
		width:500,
		resizable:false,
		open:function(event) {
			document.body.style.overflow = "hidden";
		},
		close:function(event) {
			document.body.style.overflow = "visible";
		}
	});
	$("#todoCommentDialog").dialog({
		modal:true,
		autoOpen:false,
		width:500,
		resizable:false
	});

	//textareaの自動拡張
	$("#edit_todo_content").elastic();
	$("#edit_todo_comment").elastic();

	//TODO 登録するとかその他諸々のボタン設定
}

//TODOダイアログオープン
function openEditTodoDialog(key) {
	
}
