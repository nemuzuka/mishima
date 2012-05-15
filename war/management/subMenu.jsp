<%-- 
/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
 --%>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<ul class="nav nav-list">
  <li class="nav-header"></li>
  <li id="sub_menu1"><a href="javascript:void(0)" title="複数ユーザで情報を共有する為の「プロジェクト」を管理します">プロジェクト管理</a></li>
  <li id="sub_menu2"><a href="javascript:void(0)" title="本システムにログインできる「メンバー」を管理します">メンバー管理</a></li>
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
