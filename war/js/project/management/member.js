/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
$(function(){
	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});

	searchMember();
});

//メンバー検索
function searchMember() {
	$("#memberSettingBtn").click(function(){
		execute();
	});
	
	var selectedProjectName =  $("#targetProjects option:selected").text();
	$("#selectedProjectName").text("(" + selectedProjectName + ")");
	
	searchAndRender();
}

//検索＆レンダリング
function searchAndRender() {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/project/management/ajax/memberList"
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

	var $msgDiv = $("<div />");
	if(result.length >= 1000) {
		$msgDiv.append($("<span />").text("1000件以上存在します").addClass("label label-warning"));
	} else {
		$msgDiv.append($("<span />").text("該当件数：" + result.length + "件").addClass("label label-info"));
	}
	
	//一覧をレンダリング
	var $table = $("<table />").addClass("table table-bordered result_table");
	var $thead = $("<thead />").append($("<tr />")
				.append($("<th />").html("プロジェクト<br />メンバー").attr({width:"80px"}))
				.append($("<th />").text("ニックネーム"))
				.append($("<th />").text("権限"))
			);
	$table.append($thead);
	
	var $tbody = $("<tbody />");
	$.each(result, function(){
		var keyToString = this.member.keyToString;
		var name = this.member.name;
		var projectMember = this.projectMember;
		var authorityCode = this.authorityCode;
		
		var $checkBox = $("<input />").attr({type:"checkbox", value:keyToString, name:"memberKey"});
		if(projectMember == true) {
			$checkBox.prop("checked", true);
		}
		
		var $select = $("<select />").attr({name:"authorityCode"});
		$select
			.append($("<option />").attr({value:"projectAdmin"}).text("プロジェクト管理者"))
			.append($("<option />").attr({value:"developer"}).text("開発者"))
			.append($("<option />").attr({value:"reporter"}).text("報告者"));
		$select.val(authorityCode);

		var $tr = $("<tr />");
		$tr.append($("<td />").append($checkBox).css({"text-align":"center"}))
			.append($("<td />").text(name))
			.append($("<td />").append($select));
		$tbody.append($tr)
	});
	$table.append($tbody);
	
	$("#result_area").append($msgDiv).append($table);
}

//プロジェクトメンバー設定
function execute() {
	var params = createExecuteParams();
	
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/project/management/ajax/memberSetting",
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
				}
				return;
			}
			//メッセージを表示して、終了
			infoCheck(data);
			return;
		}
	);
}

//登録パラメータ設定
function createExecuteParams() {
	var params = {};
	params["memberKeyArray"] = new Array();
	params["authorityCodeArray"] = new Array();
	params["jp.co.nemuzuka.token"] = $("#token").val();

	var count = 0;
	$("input[type='checkbox'][name='memberKey']").each(function(index){
		
		if($(this).prop("checked") == true) {
			params["memberKeyArray"].push($(this).val());
			params["authorityCodeArray"].push($($("select[name='authorityCode']").get()[index]).val());
			count++;
		}
	});
	return params;
}

