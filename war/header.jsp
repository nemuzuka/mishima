<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
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
      
      <a class="brand" href="http://twitter.github.com/bootstrap/index.html">Mishima</a>
      <div class="nav-collapse" style="float: right;">
        <ul class="nav">
          <li style="color:#FFF;height: 40px;margin:auto;line-height: 40px;" id="selected_project_name">
            【プロジェクト名】
          </li>
          <li class="divider-vertical"></li>
          <li class="" style="line-height: 40px;">
            <select id="targetProjects">
              <option>--プロジェクトを選択--</option>
              <option>XXXプロジェクト</option>
              <option>YYYプロジェクト</option>
            </select>
          </li>
          <li class="">
            <a href="#" id="personal_settings">個人設定</a>
          </li>
          <li class="">
            <a href="${f:h(logoutURL)}">logout</a>
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
            <a href="#">ダッシュボード</a>
          </li>
          <li class="" id="main_menu2">
            <a href="#">チケット</a>
          </li>
          <li class="divider-vertical"></li>
          <li class="" id="main_menu3">
            <a href="#">プロジェクト設定</a>
          </li>
          <li class="" id="main_menu4">
            <a href="#">各種管理</a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
<!--
$(function(){
	var selectedMainMenu = $("#selected_main_menu").val();
	$("#" + selectedMainMenu).addClass("active");

	var selectedSubMenu = $("#selected_sub_menu").val();
	$("#" + selectedSubMenu).addClass("active");
	
	$("#main_menu4").click(function(){
		moveUrl("/management/");
	});
	
	
});
//-->
</script>

