<%-- 
/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
 --%>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Navbar
    ================================================== -->
<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container-fluid">
      
      <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </a>
      
      <a class="brand" href="/">Mishima</a>
      <div class="nav-collapse" style="float: right;">
        <ul class="nav">
          <li class="" id="main_menu1">
            <a href="javascript:void(0)" id="dash_board_menu" title="あなたのTODOやプロジェクトのチケットを参照します">課題管理</a>
          </li>
          <li class="divider-vertical"></li>
          <li style="display:none" id="main_menu3">
            <a style="display:none" href="javascript:void(0)" id="project_menu" title="プロジェクトに関する設定を行います">プロジェクト設定</a>
          </li>
          <li style="display:none" id="main_menu4">
            <a style="display:none" href="javascript:void(0)" id="admin_menu" title="システムに関する設定を行います">システム管理</a>
          </li>

          <li class="divider-vertical"></li>
          <li class="" style="height: 40px;">
            <div style="margin-top: 5px;">
              <select id="targetProjects">
              <c:forEach var="project" items="${userInfo.projectList}">
                <option value="${f:h(project.value)}">${f:h(project.label)}</option>
              </c:forEach>
              </select>
            </div>
          </li>
          <li class="">
            <a href="javascript:void(0)" id="personal_settings" title="個人の情報を設定します">個人設定</a>
          </li>
          <li>
            <a href="javascript:void(0)" id="logout_link" title="ログアウトしてシステムを終了します">ログアウト</a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
<!--

var selectedProject = "${f:h(userInfo.selectedProject)}";
var projectManager = ${f:h(userInfo.projectManager)};
var projectMember = ${f:h(userInfo.projectMember)};
var systemManager = ${f:h(userInfo.systemManager)};
var logoutUrl = "${f:h(logoutURL)}";

$(function(){
	var selectedMainMenu = $("#selected_main_menu").val();
	$("#" + selectedMainMenu).addClass("active");

	var selectedSubMenu = $("#selected_sub_menu").val();
	$("#" + selectedSubMenu).addClass("active");

	$("#logout_link").click(function(){
		moveUrl(logoutUrl);
	});

	$("#project_menu").click(function(){
		moveUrl("/project/management/");
	});
	
	$("#admin_menu").click(function(){
		moveUrl("/management/");
	});

	$("#main_menu1").click(function(){
		moveUrl("/bts/");
	});

	$("#personal_settings").click(function(){
		openPersonalDialog();
	});
	
	$("#targetProjects").val(selectedProject);
	$("#targetProjects").change(function() {
		var targetProject = $("#targetProjects").val();
		moveUrl("/changeProject?projectKey=" + targetProject);
	});
	//削除されたプロジェクトを選択している際の処置
	//プロジェクトを選択しているにもかかわらず、現在のselectの選択値が空文字の場合、
	//削除されたプロジェクトを選択している状態と判断
	if(selectedProject != '') {
		if($("#targetProjects").val() == '') {
			$("#targetProjects").change();
		}
	}

	//システムマネージャでなければ、各種管理は参照できない
	if(systemManager == true) {
		$("#main_menu4").show();
		$("#admin_menu").show();
	}

	//プロジェクト設定メニューの設定
	if(selectedProject != "" && projectManager == true) {
		//プロジェクトを選択しており、かつ、プロジェクト管理者であれば、プロジェクト設定は参照できる
		$("#main_menu3").show();
		$("#project_menu").show();
	}

});
//-->
</script>

