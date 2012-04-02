<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<ul class="nav nav-list">
  <li class="nav-header"></li>
  <li id="sub_menu1"><a href="#">プロジェクト管理</a></li>
  <li id="sub_menu2"><a href="#">メンバー管理</a></li>
</ul>      

<script type="text/javascript">
<!--
$(function(){
	$("#sub_menu1").click(function(){
		moveUrl("/management/project");
	});
	$("#sub_menu2").click(function(){
		moveUrl("/management/member");
	});
});
//-->
</script>

<input type="hidden" id="selected_main_menu" value="main_menu4" />
