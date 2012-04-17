var g_searchParams;

$(function(){
	
	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});
	
	initTodoDialog();
	
	$("#searchTodoBtn").click(function(){
		searchTodo();
	});
	
	$("#addTodoBtn").click(function(){
		openEditTodoDialog("");
	});
	
	initTodo();
});

//初期表示時処理
function initTodo() {
	setAjaxDefault();
	return $.ajax({
		type: "POST",
		url: "/todo/ajax/todoSearchInfo"
	}).then(
		function(data) {
			renderSearchInfo(data);
		}
	);
}

//検索条件設定
function renderSearchInfo(data) {
	//共通エラーチェック
	if(errorCheck(data) == false) {
		return;
	}
	
	var form = data.result;
	
	$("#status_area").empty();
	var $statusDiv = $("<div />");
	$.each(form.statusList, function(){
		var $label = $("<label />").addClass("checkbox inline");
		var $checkbox = $("<input />").attr({type:"checkbox", name:"search_status", value:this.value});
		var $span = $("<span />").text(this.label);
		$label = $label.append($checkbox).append($span);
		$statusDiv.append($label);
	});
	$("#status_area").append($statusDiv);
	
	$.datepicker.setDefaults($.extend($.datepicker.regional['ja']));
	$("#search_fromPeriod").datepicker();
	$("#search_toPeriod").datepicker();

}


//TODO検索
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
		url: "/todo/ajax/todoList",
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
	
	//TODO記述
	//一覧をレンダリング
	var $table = $("<table />").addClass("table table-bordered result_table");
	var $thead = $("<thead />").append($("<tr />")
				.append($("<th />").text("氏名"))
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

