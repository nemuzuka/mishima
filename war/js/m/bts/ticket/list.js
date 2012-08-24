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
	$("#back").on("click", function(){
		back();
	});

	$("#nav_back").on("click", function(){
		back();
	});

	init();
});

//初期表示時処理
function init() {
	//検索条件初期値をWebstorageより取得
	var params = JSON.parse(sessionStorage.getItem("ticket_search_param"));
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/bts/ticket/ajax/ticketList",
		data: params
	}).then(
		function(data) {
			var $writeList = writeTicketList(data.result);

			//1件も表示するデータが存在しない場合、メッセージを表示
			if($writeList == null) {
				var $div = $("<div />").text("表示するデータは存在しません。");
				$("#result_area").append($div);
			} else {
				$("#result_area").append($writeList);
				$writeList.listview();
			}
		}
	);
}

//チケット一覧描画
function writeTicketList(list) {
	if(list.length == 0) {
		return null;
	}
	
	var $ul = $("<ul />").attr({"data-role":"listview","data-inset":"true" });
	
	$.each(list, function(index){

		var keyToString = this.model.keyToString;
		var status = this.model.status;
		var title = this.model.title;
		var memberName = this.targetMemberName;
		var no = this.model.no;
		var viewNo = this.viewNo;
		var versionNo = this.model.version;
		var period = this.period;
		var createdAt = this.createdAt;
		var periodStatusLabel = this.periodStatusLabel;
		var periodStatusCode = this.periodStatusCode;

		var $a = $("<a />").attr({href: "#"});
		$a.on("click", function(){
			moveUrl("/mobile/bts/ticket/detail?keyToString=" + keyToString + "&dashbord=false");
		})
		
		var $statusSpan = $("<span />").text(status).addClass("label label-info");
		var $periodStatusSpan = $("<span />");
		if(periodStatusCode != '') {
			$periodStatusSpan.text(periodStatusLabel);
			if(periodStatusCode == '1') {
				$periodStatusSpan = $periodStatusSpan.addClass("label label-warning");
			} else {
				$periodStatusSpan = $periodStatusSpan.addClass("label label-important");
			}
		}
		var $periodSpan = "";
		if(period != null && period != "") {
			$periodSpan = $("<span />").text(formatDateyyyyMMdd(period));
		}

		var $memberNameSpan = "";
		if(memberName != null && memberName != "") {
			$memberNameSpan = $("<span />").text(memberName);
		}
		
		var $h1 = $("<h1 />").text(title);
		var $p = $("<p />").append($statusSpan).append("　").append($periodStatusSpan);
		var $p_aside = $("<p />").append($periodSpan).append("<br />").append($memberNameSpan).addClass("ui-li-aside");
		$a.append($h1).append($p).append($p_aside);

		$ul.append($("<li />").append($a));
	});
	return $ul;
}

function back() {
	moveUrl("/mobile/bts/ticket/");
}
