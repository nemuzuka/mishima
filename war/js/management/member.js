/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
var g_searchParams;

$(function(){
	
	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});
	
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
		width:600,
		resizable:false,
		open:function(event) {
			openModalDialog();
		},
		close:function(event) {
			closeModelDialog();
		},
		show: 'clip',
        hide: 'clip'
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
	g_searchParams = params;
	searchAndRender(params);
}

//検索＆レンダリング
function searchAndRender(params) {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/management/ajax/memberList",
		data: params
	}).then(
		function(data) {
			render(data);
		}
	);
}

//一覧表示
function render(data) {
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

	var $msgDiv = $("<div />");
	if(result.length >= 1000) {
		$msgDiv.append($("<span />").text("1000件以上存在します").addClass("label label-warning"));
	} else {
		$msgDiv.append($("<span />").text("該当件数：" + result.length + "件").addClass("label label-info"));
	}
	
	//一覧をレンダリング
	var $table = $("<table />").addClass("table table-bordered result_table");
	var $thead = $("<thead />").append($("<tr />")
				.append($("<th />").text("ニックネーム"))
				.append($("<th />").text("メールアドレス"))
				.append($("<th />").text("権限"))
				.append($("<th />").text("").attr({width:"50px"}))
			);
	$table.append($thead);
	
	var $tbody = $("<tbody />");
	$.each(result, function(){
		var keyToString = this.keyToString;
		var name = this.name;
		var mail = this.mail;
		var authorityLabel = this.authorityLabel;
		var versionNo = this.version

		var $delBtn = $("<input />").attr({type:"button", value:"削"}).addClass("btn btn-danger btn-mini");
		$delBtn.click(function(){
			deleteMember(name, keyToString, versionNo);
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
	
	$("#result_area").append($("<hr />")).append($msgDiv).append($table);
}

//登録・更新ダイアログ表示
function openEditDialog(keyToString) {
	
	var title = "";
	var buttonLabel = "";
	if(keyToString == '') {
		title = "メンバー登録";
		buttonLabel = "登録する";
		$("#edit_mail").prop("readonly", false);
	} else {
		title = "メンバー変更";
		buttonLabel = "変更する";
		$("#edit_mail").prop("readonly", true);
	}
	$("#ui-dialog-title-memberDialog").empty();
	$("#ui-dialog-title-memberDialog").append(title);
	$("#memberDialog-add").attr({value:buttonLabel});
	
	var params = {};
	params["keyToString"] = keyToString;
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/management/ajax/memberEditInfo",
		data: params
	});
	
	//後処理の登録
	//表示対象データが存在せず、再検索を行える場合、再検索をします。
	task.pipe(
		function(data) {
			
			if(errorCheck(data) == false) {
				if(data.status == -6) {
					//表示対象データが存在しない場合、再検索して表示
					return reSearchAndRender();
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
			
			$("#edit_timeZone").empty();
			$.each(form.timeZoneList, function(){
				$("#edit_timeZone").append($('<option>').attr({ value: this.value }).text(this.label));
			});
			$("#edit_timeZone").val(form.timeZone);
			
			prependDummyText("memberDialog");
			$("#memberDialog").dialog("open");
			removeDummyText("memberDialog");
			return;
		}
	);
}

//メンバー登録・更新
function execute() {
	var params = createExecuteParams();
	
	if(validate(params) == false) {
		return;
	}
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/management/ajax/memberExecute",
		data: params
	});
	
	//後処理の登録
	//
	task.pipe(
		function(data) {
			//共通エラーチェック
			if(errorCheck(data) == false) {
				if(data.status == -1 || data.status == -5) {
					//validate or 入力値による一意制約エラーの場合、tokenを再発行
					return reSetToken();
				} else {
					//強制的にダイアログを閉じて、再検索
					$("#memberDialog").dialog("close");
					return reSearchAndRender();
				}
				return;
			}
			
			//メッセージを表示して、戻る
			infoCheck(data);
			$("#memberDialog").dialog("close");
			return reSearchAndRender();
		}
	);
}

//メンバー削除
function deleteMember(name, keyToString, version) {
	if(window.confirm("メンバー「" + name + "」を削除します。本当によろしいですか？") == false) {
		return;
	}
	
	var params = {};
	params["keyToString"] = keyToString;
	params["versionNo"] = version;
	params["jp.co.nemuzuka.token"] = $("#token").val();
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/management/ajax/memberDelete",
		data: params
	});
	
	//後処理の登録
	//
	task.pipe(
		function(data) {
			//共通エラーチェック
			errorCheck(data);
			//メッセージを表示して、戻る
			infoCheck(data);
			return reSearchAndRender();
		}
	);
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

//登録validate
function validate(params) {
	var v = new Validate();
	v.addRules({value:params["name"],option:'required',error_args:"ニックネーム"});
	v.addRules({value:params["name"],option:'maxLength',error_args:"ニックネーム", size:64});

	v.addRules({value:params["mail"],option:'required',error_args:"メールアドレス"});
	v.addRules({value:params["mail"],option:'maxLength',error_args:"メールアドレス", size:256});

	return v.execute();
}


//再検索判断処理
//一覧に表示されている場合、再検索する、と判断します。
function isReRender() {
	if($("#listCnt").val() != '0') {
		return true;
	}
	return false;
}

//再検索処理
function reSearchAndRender() {
	if(isReRender()) {
		return searchAndRender(g_searchParams);
	}
	return "";
}

