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
          <li class="divider-vertical"></li>
          <li class="" style="line-height: 40px;">
            <select id="targetProjects">
            <c:forEach var="project" items="${userInfo.projectList}">
              <option value="${f:h(project.value)}">${f:h(project.label)}</option>
            </c:forEach>
            </select>
          </li>
          <li class="">
            <a href="#" id="personal_settings" title="個人の情報を設定します">個人設定</a>
          </li>
          <li class="">
            <a href="#" id="logout_link" title="ログアウトしてシステムを終了します">logout</a>
          </li>
        </ul>
      </div>
    </div>
  </div>
  <!-- 各機能に対するメニューバー(2段目) -->
  <div class="navbar-inner">
    <div class="container">
      <div class="nav-collapse">
        <ul class="nav">
          <li class="" id="main_menu1">
            <a href="#" id="dash_board_menu" title="あなたに割り当てられているチケットやTODOの状態を参照します">ダッシュボード</a>
          </li>
          <li class="" id="main_menu5">
            <a href="#" id="todo_menu" title="あなたのTODOを参照します">TODO</a>
          </li>
          <li class="" id="main_menu2">
            <a href="#" id="ticket_menu" title="プロジェクト毎のチケットを参照します">チケット</a>
          </li>
          <li class="divider-vertical"></li>
          <li class="" id="main_menu3">
            <a href="#" id="project_menu" title="プロジェクトに関する設定を行います">プロジェクト設定</a>
          </li>
          <li class="" id="main_menu4">
            <a href="#" id="admin_menu" title="システムに関する設定を行います">システム管理</a>
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

	$("#todo_menu").click(function(){
		moveUrl("/todo/");
	});
	$("#personal_settings").click(function(){
		openPersonalDialog();
	});
	
	$("#targetProjects").val(selectedProject);
	$("#targetProjects").change(function() {
		var targetProject = $("#targetProjects").val();
		if(targetProject == '') {
			return;
		}
		moveUrl("/changeProject?projectKey=" + targetProject);
	});

	//システムマネージャでなければ、各種管理は参照できない
	if(systemManager == false) {
		$("#admin_menu").hide();
	}

	//プロジェクトを選択していなければ、チケット、プロジェクト設定は参照できない
	if(selectedProject == "") {
		$("#ticket_menu").hide();
		$("#project_menu").hide();
	}

	//プロジェクト管理者でなければ、プロジェクト設定は参照できない
	if(projectManager == false) {
		$("#project_menu").hide();
	}

	//プロジェクトメンバーでなければチケットは参照できない
	if(projectMember == false) {
		$("#ticket_menu").hide();
	}
});
//-->
</script>

