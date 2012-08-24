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

<div data-role="header">
	<div data-role="navbar">
		<ul>
			<li><a id="main_menu1" href="#">ﾀﾞｯｼｭﾎﾞｰﾄﾞ</a></li>
			<li><a id="main_menu2" href="#">TODO</a></li>
			<li><a id="main_menu3" href="#">チケット</a></li>
		</ul>
	</div>
	<div data-role="navbar">
		<ul>
			<li>
				<div data-role="fieldcontain">
					<select id="targetProjects">
					<c:forEach var="project" items="${userInfo.projectList}">
						<option value="${f:h(project.value)}">${f:h(project.label)}</option>
					</c:forEach>
					</select>
				</div>
			</li>
		</ul>
	</div>
	${param.pageHeader}
</div>

<script type="text/javascript">

var selectedProject = "${f:h(userInfo.selectedProject)}";
var projectManager = ${f:h(userInfo.projectManager)};
var projectMember = ${f:h(userInfo.projectMember)};
var systemManager = ${f:h(userInfo.systemManager)};
var logoutUrl = "${f:h(logoutURL)}";

$(function(){

	$(document).on("vclick", "#main_menu1", function(e){
		moveUrl("/mobile/bts/dashboard/");
	});

	$(document).on("vclick", "#main_menu2", function(e){
		moveUrl("/mobile/bts/todo/");
	});

	if(selectedProject != null && selectedProject != '') {
		$(document).on("vclick", "#main_menu3", function(e){
			moveUrl("/mobile/bts/ticket/");
		});
	} else {
		$("#main_menu3").remove();
	}

	$("#targetProjects").on("change", function() {
		var targetProject = $("#targetProjects").val();
		moveUrl("/changeProject?projectKey=" + targetProject);
	});
	$("#targetProjects").val(selectedProject);
	$("#targetProjects").selectmenu("refresh");

});
</script>

