<%-- 
/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
 --%>
<!DOCTYPE html>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<html lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta charset="utf-8">

<script type="text/javascript">
<!--
window.onload = function() {

	if(window.parent) {

		var msg = "";
		<c:forEach var="msg" items="${errorMsgs}" >
			msg += "${f:h(msg)}" + "\n";
		</c:forEach>
		if(msg != '') {
			alert(msg);
		}
		
		msg = "";
		<c:forEach var="msg" items="${infoMsgs}" >
			msg += "${f:h(msg)}" + "\n";
		</c:forEach>
		
		setTimeout(
			window.parent.afterUpload(msg),
			2000
		);
	}

}
//-->
</script>
</head><body></body></html>
