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
	
	var selectedProjectName =  $("#targetProjects option:selected").text();
	$("#selectedProjectName").text("(" + selectedProjectName + ")");

	searchAndRender();
});


//検索＆レンダリング
function searchAndRender() {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/bts/member/ajax/memberList"
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

	//tokenの設定
	$("#token").val(data.token);
	
	var result = data.result;
	if(result.length == 0) {
		return;
	}

	//一覧をレンダリング
	var $table = $("<table />").addClass("table table-bordered result_table");
	var $thead = $("<thead />").append($("<tr />")
				.append($("<th />").text("ニックネーム"))
				.append($("<th />").text("メモ"))
				.append($("<th />").text("権限"))
			);
	$table.append($thead);
	
	var $tbody = $("<tbody />");
	$.each(result, function(){
		
		var name = this.member.name;
		var memo = this.memo;
		var authorityName = this.authorityName;
		
		var $tr = $("<tr />");
		$tr.append($("<td />").text(name))
			.append($("<td />").html(memo))
			.append($("<td />").text(authorityName));
		$tbody.append($tr)
	});
	$table.append($tbody);
	
	$("#result_area").append($table);
}
