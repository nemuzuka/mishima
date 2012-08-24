$(function(){

	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});

	$("#back").on("click", function(){
		back();
	});

	$("#nav_back").on("click", function(){
		back();
	});

	$("#nav_save").on("click", function(){
		executeTicket();
	});

	executeSearch();
});

//表示する情報を取得し、表示する
function executeSearch() {
	var key = $("#keyToString").val();
	var title = "";
	var buttonLabel = "";
	if(key == '') {
		title = "チケット登録";
		buttonLabel = "登録する";
	} else {
		title = "チケット変更";
		buttonLabel = "変更する";
	}
	$("#header_title").text(title);
	$("#nav_save span.ui-btn-inner span").text(buttonLabel);
	
	var params = {};
	params["keyToString"] = key;
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketEditInfo",
		data: params
	});
	
	//後処理の登録
	//表示対象データが存在せず、再検索を行える場合、再検索をします。
	task.pipe(
		function(data) {
			
			if(errorCheck(data) == false) {
				if(data.status == -6) {
					//表示対象データが存在しない場合、再検索して表示
					return back();
				}
				return;
			}
			
			//tokenの設定
			$("#token").val(data.token);
			
			//form情報の設定
			var form = data.result;
			
			$("#ticket_status").empty();
			$.each(form.ticketMst.statusList, function(){
				$("#ticket_status").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#ticket_status").val(form.status);
			$("#ticket_status").selectmenu("refresh");

			$("#ticket_title").val(form.title);
			$("#ticket_content").val(form.content);
			$('#ticket_content').keyup();
			$("#ticket_endCondition").val(form.endCondition);
			$('#ticket_endCondition').keyup();
			$("#ticket_startDate").val(formatDateyyyyMMdd(form.startDate));
			$("#ticket_period").val(formatDateyyyyMMdd(form.period));
			
			$("#ticket_priority").empty();
			$.each(form.ticketMst.priorityList, function(){
				$("#ticket_priority").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#ticket_priority").val(form.priority);
			$("#ticket_priority").selectmenu("refresh");

			$("#ticket_kind").empty();
			$.each(form.ticketMst.kindList, function(){
				$("#ticket_kind").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#ticket_kind").val(form.targetKind);
			$("#ticket_kind").selectmenu("refresh");

			$("#ticket_category").empty();
			$.each(form.ticketMst.categoryList, function(){
				$("#ticket_category").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#ticket_category").val(form.category);
			$("#ticket_category").selectmenu("refresh");

			$("#ticket_targetVersion").empty();
			$.each(form.ticketMst.versionList, function(){
				$("#ticket_targetVersion").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#ticket_targetVersion").val(form.targetVersion);
			$("#ticket_targetVersion").selectmenu("refresh");

			$("#ticket_milestone").empty();
			$.each(form.ticketMst.milestoneList, function(){
				$("#ticket_milestone").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#ticket_milestone").val(form.milestone);
			$("#ticket_milestone").selectmenu("refresh");

			$("#ticket_targetMember").empty();
			$.each(form.ticketMst.memberList, function(){
				$("#ticket_targetMember").append($("<option />").attr({value:this.value}).text(this.label));
			});
			$("#ticket_targetMember").val(form.targetMember);
			$("#ticket_targetMember").selectmenu("refresh");
			
			$("#ticket_parentKey").val(form.parentKey);
			$("#ticket_project_id").text("");
			if(form.projectId != null && form.projectId != '' ) {
				$("#ticket_project_id").text(form.projectId + "-");
			}
			
			$("#edit_ticket_versionNo").val(form.versionNo);
			return;
		}
	);

}

//チケット登録・更新
function executeTicket() {
	var params = createExecuteTicketParams();
	
	if(ticketValidate(params) == false) {
		return;
	}
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketExecute",
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

//Ticket登録パラメータ設定
function createExecuteTicketParams() {
	var params = {};
	params["status"] = $("#ticket_status").val();
	params["title"] = $("#ticket_title").val();
	params["content"] = $("#ticket_content").val();
	params["endCondition"] = $("#ticket_endCondition").val();
	params["startDate"] = unFormatDate($("#ticket_startDate").val());
	params["period"] = unFormatDate($("#ticket_period").val());
	
	params["priority"] = $("#ticket_priority").val();
	params["targetKind"] = $("#ticket_kind").val();
	params["category"] = $("#ticket_category").val();
	params["targetVersion"] = $("#ticket_targetVersion").val();
	params["milestone"] = $("#ticket_milestone").val();
	params["targetMember"] = $("#ticket_targetMember").val();
	params["parentKey"] = $("#ticket_parentKey").val();
	
	params["versionNo"] = $("#edit_ticket_versionNo").val();
	params["keyToString"] = $("#keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	return params;
}

//Ticket登録validate
function ticketValidate(params) {
	var v = new Validate();
	v.addRules({value:params["title"],option:'required',error_args:"件名"});
	v.addRules({value:params["title"],option:'maxLength',error_args:"件名", size:128});

	v.addRules({value:params["content"],option:'maxLength',error_args:"内容", size:1024});
	v.addRules({value:params["endCondition"],option:'maxLength',error_args:"終了条件", size:1024});

	v.addRules({value:params["startDate"],option:'date',error_args:"開始日"});
	v.addRules({value:params["period"],option:'date',error_args:"期限"});

	v.addRules({value:params["parentKey"],option:'number',error_args:"親チケットNo"});
	return v.execute();
}

//詳細画面からの遷移の場合、詳細画面へ遷移します
//新規登録の場合、TODOメニューへ遷移します
function back() {
	var dashbord = $("#dashbord").val();
	var keyToString = $("#keyToString").val();
	if(keyToString != null && keyToString != '') {
		moveUrl("/mobile/bts/ticket/detail?keyToString=" + keyToString + "&dashbord=" + dashbord);
	} else {
		moveUrl("/mobile/bts/ticket/");
	}
}