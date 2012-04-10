<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<ul class="nav nav-list">
  <li class="nav-header"></li>
  <li id="sub_menu1"><a href="#">プロジェクトメンバー</a></li>
  <li class="nav-header"></li>
  <li id="sub_menu2"><a href="#">種別</a></li>
  <li id="sub_menu3"><a href="#">カテゴリ</a></li>
  <li id="sub_menu4"><a href="#">マイルストーン</a></li>
  <li id="sub_menu5"><a href="#">バージョン</a></li>
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
	
	//プロジェクトメンバーでなければ非表示
	if(projectMember == false) {
		$("#sub_menu2").hide();
		$("#sub_menu3").hide();
		$("#sub_menu4").hide();
	}
});
//-->
</script>

<input type="hidden" id="selected_main_menu" value="main_menu3" />
