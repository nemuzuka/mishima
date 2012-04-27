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
<c:import url="/layout.jsp">
<c:param name="title" value="Mishima BTS/ITS"/>

<c:param name="subMenu">
<c:import url="/bts/subMenu.jsp" />
</c:param>
  
<c:param name="content">

<script type="text/javascript" src="/js/bts/dashboard.js"></script>

<div class="widget">
<form class="form-horizontal">

	<div id="result_area">
	</div>

</form>
</div>

<c:import url="/bts/ticket/ticketDialog.jsp"/>
<c:import url="/bts/todo/todoDialog.jsp"/>

<input type="hidden" id="dashboard_limit_cnt" value="${f:h(userInfo.dashboardLimitCnt)}" />
<input type="hidden" id="selected_sub_menu" value="sub_menu2" />

</c:param>

</c:import>