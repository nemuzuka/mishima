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
<c:param name="title" value="ITS [Mishima]"/>

<c:param name="subMenu">
<c:import url="/project/management/subMenu.jsp" />
</c:param>
  
<c:param name="content">

<script type="text/javascript" src="/js/project/management/member.js"></script>

<div class="widget">
<form class="form-horizontal">

	<h2 class="title">プロジェクトメンバー設定<span id="selectedProjectName"></span></h2>
	
	<div id="result_area" class="result">
	</div>
	<div class="search_ctrl">
		<input type="button" class="btn btn-primary" id="memberSettingBtn" value="設定" />
	</div>

</form>
</div>

<input type="hidden" id="selected_sub_menu" value="sub_menu1" />

</c:param>

</c:import>