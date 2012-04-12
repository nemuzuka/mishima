
/* fromに指定したidの選択状態のoptionをtoに指定したidのoptionにコピーします. */
function addItems(fromSelectId,toSelectId) {

	var checkedArray = new Array();

	$("#" + fromSelectId + " option:selected").each(function() {
		var appendFlg = true;
		var fromValue = $(this).val();
		$("#"+ toSelectId + " option").each(function() {
			if(fromValue == $(this).val()) {
				appendFlg = false;
				return false;
			}
		});
		if(appendFlg) {
			$("#" + toSelectId).append($("<option />").attr({value:$(this).val()}).text($(this).text()));
			checkedArray.push($(this).val());
		}
	});
	reWriteSelect(toSelectId, checkedArray);
}

/** 指定したidのselectタグを再描画します. */
//IEだと幅調整ができないので、elementで直接指定します。
function reWriteSelect(targetId, checkedArray) {
	var element=document.getElementById(targetId);
	var arrayOption = new Array();

	var count = 0;
	$("#"+ targetId + " option").each(function() {
		var tmpOption = new Option();
		tmpOption.text = $(this).text();
		tmpOption.value = $(this).val();
		arrayOption[count] = tmpOption;
		count++;
	});

	$("#" + targetId).css({width:"auto"});
	$("#" + targetId).empty();

	for(var i = 0; i < arrayOption.length; i++) {
		element.options[i] = arrayOption[i];
	}
	//指定したvalueのoptionをselectedにする
	$("#" + targetId).val(checkedArray);

	//selectに何も設定されていない場合、幅を設定
	if(count == 0) {
		$("#" + targetId).css({width:"100px"});
	}

}


/* 指定したidの選択状態のoptionを削除します. */
function removeItems(targetId) {
	$("#" + targetId + " option:selected").each(function() {
		$(this).remove();
	});
	reWriteSelect(targetId, new Array());
}


/* 指定したidの選択状態のoptionを上に移動します. */
function upItems(targetId) {
	var checkedArray = new Array();

	$("#" + targetId + " option:selected").each(function() {
		checkedArray.push($(this).val());
		var prev = $(this).first().prev();
		$(this).insertBefore(prev);
	});
	reWriteSelect(targetId, checkedArray);
}

/* 指定したidの選択状態のoptionを下に移動します. */
function downItems(targetId) {
	var checkedArray = new Array();

	$($("#" + targetId + " option:selected").get().reverse()).each(function() {
		checkedArray.push($(this).val());
		var next = $(this).last().next();
		$(this).insertAfter(next);
	});
	reWriteSelect(targetId, checkedArray);
}

/* 指定したidの構成情報のvalueを取得します. */
function getSelectArray(targetId) {
	var count = 0;
	var retArray = [];
	$("#"+ targetId + " option").each(function() {
		retArray[count] = $(this).val();
		count++;
	});
	return retArray;
}
