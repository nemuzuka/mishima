$(function(){
	initDialog();
	
	$("#searchMemberBtn").click(function(){
		searchMember();
	});
	
	$("#addMemberBtn").click(function(){
		openEditDialog("");
	});
	
});

//ダイアログ初期化
function initDialog(){
	$("#memberDialog").dialog({
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
	
	$("#memberDialog-add").click(function(){
		execute();
	});
	
	$("#memberDialog-cancel").click(function(){
		$("#memberDialog").dialog("close");
	});
}

//メンバー検索
function searchMember() {
	var params = createSearchParams();
	setAjaxDefault();
	$.ajax({
		type: "POST",
		url: "/management/ajax/memberList",
		data: params,
		success: function(data, status){

			$("#result_area").empty();

			//共通エラーチェック
			if(errorCheck(data) == false) {
				return;
			}
			infoCheck(data);
			//tokenの設定
			$("#token").val(data.token);
			
			var result = data.result;
			$("#listCnt").val(result.length);
			if(result.length == 0) {
				return;
			}
			
			//一覧をレンダリング
			var $table = $("<table />").addClass("table table-bordered result_table");
			var $thead = $("<thead />").append($("<tr />")
						.append($("<th />").text("氏名"))
						.append($("<th />").text("メールアドレス"))
						.append($("<th />").text("権限"))
						.append($("<th />").text(""))
					);
			$table.append($thead);
			
			var $tbody = $("<tbody />");
			$.each(result, function(){
				var keyToString = this.keyToString;
				var name = this.name;
				var mail = this.mail;
				var authorityLabel = this.authorityLabel;
				var version = this.version

				var $delBtn = $("<input />").attr({type:"button", value:"削"}).addClass("btn btn-danger btn-mini");
				$delBtn.click(function(){
					deleteMember(name, keyToString, version);
				});
				
				var $a = $("<a />").attr({href:"javascript:void(0)"}).text(name);
				$a.click(function(){
					openEditDialog(keyToString);
				});
				var $tr = $("<tr />");
				$tr.append($("<td />").append($a))
					.append($("<td />").text(mail))
					.append($("<td />").text(authorityLabel))
					.append($("<td />").append($delBtn));
				$tbody.append($tr)
			});
			$table.append($tbody);
			
			$("#result_area").append($("<hr />")).append($table);
		}
	});
}

//登録・更新ダイアログ表示
function openEditDialog(keyToString) {
	
	$("#ui-dialog-title-memberDialog").empty();
	if(keyToString == '') {
		$("#ui-dialog-title-memberDialog").append("メンバー登録");
		$("#memberDialog-add").attr({value:"登録する"});
	} else {
		$("#ui-dialog-title-memberDialog").append("メンバー変更");
		$("#memberDialog-add").attr({value:"変更する"});
	}
	
	var params = {};
	params["keyToString"] = keyToString;
	setAjaxDefault();
	$.ajax({
		type: "POST",
		url: "/management/ajax/memberEditInfo",
		data: params,
		success: function(data, status){
			
			//共通エラーチェック
			if(errorCheck(data) == false) {
				if(data.result == -6) {
					//変更で該当レコード存在しない場合、一覧を再表示
					backView();
				}
				return;
			}
			
			//tokenの設定
			$("#token").val(data.token);
			
			//form情報の設定
			var form = data.result;
			
			$("#edit_name").val(form.name);
			$("#edit_mail").val(form.mail);
			$("input[type='radio'][name='authority']").val([form.authority]);
			$("#edit_versionNo").val(form.versionNo);
			$("#edit_keyToString").val(form.keyToString);
			
			$("#memberDialog").dialog("open");
		}
	});
}

//メンバー登録・更新
function execute() {
	var params = createExecuteParams();
	setAjaxDefault();
	$.ajax({
		type: "POST",
		url: "/management/ajax/memberExecute",
		data: params,
		success: function(data, status){
			
			//共通エラーチェック
			if(errorCheck(data) == false) {
				if(data.status == -1) {
					//validateのエラーの場合、tokenを再発行
					reSetToken();
				}
				return;
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			$("#memberDialog").dialog("close");
			backView();
		}
	});
}

//メンバー削除
function deleteMember(name, keyToString, version) {
	if(window.confirm("メンバー「" + name + "」を削除します。本当によろしいですか？") == false) {
		return;
	}
	
	var params = {};
	params["keyToString"] = keyToString;
	params["version"] = version;
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	setAjaxDefault();
	$.ajax({
		type: "POST",
		url: "/management/ajax/memberDelete",
		data: params,
		success: function(data, status){
			
			//共通エラーチェック
			errorCheck(data);
			
			//メッセージを表示して、戻る
			infoCheck(data);
			backView();
		}
	});
}

//検索条件パラメータ設定
function createSearchParams() {
	var params = {};
	params["name"] = $("#search_name").val();
	params["mail"] = $("#search_mail").val();
	return params;
}

//登録パラメータ設定
function createExecuteParams() {
	var params = {};
	params["name"] = $("#edit_name").val();
	params["mail"] = $("#edit_mail").val();
	params["authority"] = $("input[type='radio'][name='authority']:checked").val();
	params["versionNo"] = $("#edit_versionNo").val();
	params["keyToString"] = $("#edit_keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	return params;
}

//再検索
//一覧に1件以上表示されている場合、再検索します。
function backView() {
	if($("#listCnt").val() != '0') {
		searchMember();
	}
}


