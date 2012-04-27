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
  <li id="sub_menu1"><a href="#" title="プロジェクトに参加するメンバーを管理します">プロジェクトメンバー</a></li>
  <li class="nav-header disabled"></li>
  <li id="sub_menu7" class="disabled"><a href="#" title="高/中/低のようなチケットの優先度を管理します。チケットを分類する際に使用します">優先度</a></li>
  <li id="sub_menu6" class="disabled"><a href="#" title="チケットのステータスを管理します。チケットを分類する際に使用します">ステータス</a></li>
  <li id="sub_menu2" class="disabled"><a href="#" title="要望/バグ/タスクのようなチケットの種別を管理します。チケットを分類する際に使用します">種別</a></li>
  <li id="sub_menu3" class="disabled"><a href="#" title="インフラ/要件定義/テストのようなチケットのカテゴリを管理します。チケットを分類する際に使用します">カテゴリ</a></li>
  <li id="sub_menu4" class="disabled"><a href="#" title="プロジェクトの区切りを管理します。チケットを分類する際に使用します">マイルストーン</a></li>
  <li id="sub_menu5" class="disabled"><a href="#" title="対象バージョンを管理します。チケットを分類する際に使用します">バージョン</a></li>
</ul>

<script type="text/javascript">
<!--
$(function(){
	$("#sub_menu1").click(function(){
		moveUrl("/project/management/member");
	});
	$("#sub_menu2").click(function(){
		moveUrl("/project/management/kind");
	});
	$("#sub_menu3").click(function(){
		moveUrl("/project/management/category");
	});
	$("#sub_menu4").click(function(){
		moveUrl("/project/management/milestone");
	});
	$("#sub_menu5").click(function(){
		moveUrl("/project/management/version");
	});
	$("#sub_menu6").click(function(){
		moveUrl("/project/management/status");
	});
	$("#sub_menu7").click(function(){
		moveUrl("/project/management/priority");
	});
	
	//プロジェクトメンバーであれば表示
	if(projectMember) {
		$(".disabled").show();
	}
});
//-->
</script>

<input type="hidden" id="selected_main_menu" value="main_menu3" />
