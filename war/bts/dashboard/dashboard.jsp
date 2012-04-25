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

<script type="text/javascript" src="/js/bts/dashbord.js"></script>

<div class="widget">
<form class="form-horizontal">

	<h2 class="title">ダッシュボード<span id="selectedProjectName"></span></h2>
	<div id="result_area">
	</div>

</form>
</div>

<input type="hidden" id="selected_sub_menu" value="sub_menu2" />

</c:param>

</c:import>