<%-- 
/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
 --%>

<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<div data-role="footer">
	<h1>Mishima(mobile)</h1>
	<a href="#" id="backPC" class="ui-btn-left" data-theme="b">PC版へ</a>
	<a href="#" id="logout" class="ui-btn-right" data-theme="b">ﾛｸﾞｱｳﾄ</a>
</div>

<script type="text/javascript">

$(function(){

	$(document).on("vclick", "#backPC", function(e){
		moveUrl("/");
	});

	$(document).on("vclick", "#logout", function(e){
		moveUrl(logoutUrl);
	});
});
</script>

