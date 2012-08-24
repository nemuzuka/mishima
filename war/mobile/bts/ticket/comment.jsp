<%-- 
/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
 --%>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<c:import url="/mobile/layout-m.jsp">
<c:param name="title" value="ITS [Mishima]"/>

<c:param name="pageHeader">
	<div data-role="header" data-position="inline">
		<a href="#" data-icon="back" data-theme="b" id="back">戻る</a>
		<h1>チケットコメント</h1>
	</div>
</c:param>

<c:param name="content">
<script type="text/javascript" src="/js/m/bts/ticket/comment.js"></script>

<div class="widget">
	<dl class="detail">
		<dt class="dt">ステータス</dt>
		<dd class="dd"><select id="ticket_status"></select></dd>
		
		<dt class="dt">コメント</dt>
		<dd class="dd">
			<textarea class="required-input" id="ticket_comment"></textarea>
		</dd>
	
	</dl>
</div>

<nav data-role="navbar">
<ul>
	<li><a href="#" id="nav_execute" data-theme="b">追加する</a></li>
</ul>
</nav>

<input type="hidden" id="keyToString" />
<input type="hidden" id="dashbord" />
<input type="hidden" id="detail_ticket_versionNo" />

<input type="hidden" id="selected_main_menu" value="main_menu3" />
</c:param>

</c:import>