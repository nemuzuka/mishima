<%-- 
/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
 --%>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<c:import url="/mobile/layout-m.jsp">
<c:param name="title" value="ITS [Mishima]"/>

<c:param name="pageHeader">
</c:param>

<c:param name="content">
<script type="text/javascript" src="/js/m/bts/dashboard.js?version=1.1"></script>

<div class="widget">
<form>
	<div id="result_area">
	</div>
</form>
</div>

<input type="hidden" id="selected_main_menu" value="main_menu1" />
</c:param>

</c:import>