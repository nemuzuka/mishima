$(function(){
	$(window).unload(function(){
		//画面を離れる場合
		unBlockLoadingMsg();
	});

	$("#status-add").click(function(){
		execute();
	});
	
	//初期データ取得
	getInitData();
});

//初期データ取得
function getInitData() {

	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/project/management/ajax/statusEditInfo"
	});
	
	//後処理の登録
	task.pipe(
		function(data) {
			
			if(errorCheck(data) == false) {
				return;
			}
			
			//tokenの設定
			$("#token").val(data.token);
			
			//form情報の設定
			var form = data.result;
			
			$("#edit_status_name").val(form.statusName);
			$("#edit_close_status_name").val(form.closeStatusName);

			$("#edit_versionNo").val(form.versionNo);
			$("#edit_keyToString").val(form.keyToString);
			
			if($("#edit_keyToString").val() != '') {
				//ボタンラベル変更
				$("#status-add").val("変更する");
			}
			return;
		}
	);
}

//ステータス登録・更新
function execute() {
	var params = createExecuteParams();
	setAjaxDefault();
	var task;
	task = $.ajax({
		type: "POST",
		url: "/project/management/ajax/statusExecute",
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
					reSetToken();
					return;
				}
				//再度画面情報取得する
				getInitData();
				return;
			}
			
			//メッセージを表示して、再度画面情報取得
			infoCheck(data);
			getInitData();
			return;
		}
	);
}

//登録パラメータ設定
function createExecuteParams() {
	var params = {};
	params["statusName"] = $("#edit_status_name").val();
	params["closeStatusName"] = $("#edit_close_status_name").val();
	params["versionNo"] = $("#edit_versionNo").val();
	params["keyToString"] = $("#edit_keyToString").val();
	params["jp.co.nemuzuka.token"] = $("#token").val();
	return params;
}