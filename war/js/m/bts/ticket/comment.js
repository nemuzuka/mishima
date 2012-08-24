$(function(){

	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});

	$("#back").on("click", function(){
		back();
	});
	
	$("#nav_execute").on("click", function(){
		execute();
	});

	render();
});

//Comment情報表示
function render() {
	
	var data = JSON.parse(sessionStorage.getItem("ticket_detail"));
	
	//tokenの設定
	$("#token").val(data.token);
	
	//form情報の設定
	var form = data.result.form;
	
	$("#ticket_status").empty();
	$.each(form.ticketMst.statusList, function(){
		$("#ticket_status").append($("<option />").attr({value:this.value}).text(this.label));
	});
	$("#ticket_status").val(form.status);
	$("#ticket_status").selectmenu("refresh");
	
	$("#keyToString").val(form.keyToString);
	$("#detail_ticket_versionNo").val(form.versionNo);
	
	return;
}

//TODOコメント登録
function execute() {
	var params = {};
	params["status"] = $("#ticket_status").val();
	params["comment"] = $("#ticket_comment").val();
	params["versionNo"] = $("#detail_ticket_versionNo").val();
	params["keyToString"] = $("#keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	//validateチェック
	var v = new Validate();
	v.addRules({value:params["comment"],option:'required',error_args:"コメント"});
	v.addRules({value:params["comment"],option:'maxLength',error_args:"コメント", size:1024});
	if(v.execute() == false) {
		return;
	}
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketCommentExecute",
		data: params
	});
	
	//後処理の登録
	//
	task.pipe(
		function(data) {
			//共通エラーチェック
			if(errorCheck(data) == false) {
				if(data.status == -1 ) {
					//validateの場合、tokenを再発行
					return reSetToken();
				}
				return back();
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			setTimeout('back()', 2000);
		}
	);
}

//詳細画面へ遷移します
function back() {
	var dashbord = sessionStorage.getItem("dashbord");
	var keyToString = sessionStorage.getItem("keyToString");
	moveUrl("/mobile/bts/ticket/detail?keyToString=" + keyToString + "&dashbord=" + dashbord);
}