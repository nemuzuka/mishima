/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

//ステータスのcheckboxを設定します。
function setStatusCheckBoc($area, list) {
	$area.empty();
	var $statusDiv = $("<div />");
	$.each(list, function(){
		var $label = $("<label />").addClass("checkbox inline");
		var $checkbox = $("<input />").attr({type:"checkbox", name:"search_status", value:this.value});
		var $span = $("<span />").text(this.label);
		$label = $label.append($checkbox).append($span);
		$statusDiv.append($label);
	});
	$area.append($statusDiv);
}