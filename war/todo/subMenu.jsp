<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<ul class="nav nav-list">
  <li class="nav-header"></li>
  <li id="sub_menu1"><a href="#" title="あなたの「TODO」を管理します">TODO管理</a></li>
</ul>      

<script type="text/javascript">
<!--
$(function(){
	$("#sub_menu1").click(function(){
		moveUrl("/todo/");
	});
});
//-->
</script>

<input type="hidden" id="selected_main_menu" value="main_menu5" />
