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
  <li id="sub_menu2"><a href="#" title="あなたに割り当てられているチケットやTODOの状態を参照します">ダッシュボード</a></li>
  <li id="sub_menu1"><a href="#" title="あなたの「TODO」を管理します">TODO</a></li>
  <li id="sub_menu3" class="disabled"><a href="#" title="選択プロジェクトに紐付くチケットを管理します">チケット</a></li>
  <li id="sub_menu5" class="disabled"><a href="#" title="選択プロジェクトのチャートを表示します">チャート</a></li>
  <li id="sub_menu4" class="disabled"><a href="#" title="選択プロジェクトメンバーを参照します">プロジェクトメンバー</a></li>
</ul>      

<script type="text/javascript">
<!--
$(function(){
	$("#sub_menu2").click(function(){
		moveUrl("/bts/dashboard/");
	});
	$("#sub_menu1").click(function(){
		moveUrl("/bts/todo/");
	});
	$("#sub_menu3").click(function(){
		moveUrl("/bts/ticket/");
	});
	$("#sub_menu4").click(function(){
		moveUrl("/bts/member/");
	});
	$("#sub_menu5").click(function(){
		moveUrl("/bts/gantt/");
	});
	
	//プロジェクトを選択している場合、表示
	if(projectMember) {
		$(".disabled").show();
	}
});
//-->
</script>

<input type="hidden" id="selected_main_menu" value="main_menu1" />
