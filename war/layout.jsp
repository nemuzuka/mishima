<%-- 
/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
 --%>
<!DOCTYPE html>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html lang="jp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta charset="utf-8">
<title>${param.title}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link href="/css/bootstrap.css" rel="stylesheet">
<link href="/css/bootstrap-responsive.css" rel="stylesheet">

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
  <script src="/js/html5.js"></script>
<![endif]-->

<link href="/css/docs.css" rel="stylesheet">
<link href="/css/jquery-ui-1.8.18.custom.css" rel="stylesheet">
<link href="/css/common.css?version=1.2" rel="stylesheet">

<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui-effects.min.js"></script>
<script type="text/javascript" src="/js/jquery-common.js?version=1.1"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/jquery.toaster.min.js"></script>
<script type="text/javascript" src="/js/jquery.elastic.js"></script>
<script type="text/javascript" src="/js/select-utils.js"></script>
<script type="text/javascript" src="/js/i18n/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="/js/dateformat.js"></script>
<script type="text/javascript" src="/js/format-utils.js"></script>
<script type="text/javascript" src="/js/validate-utils.js"></script>

</head>
<body>
<c:import url="/common/personalDialog.jsp"/>
<c:import url="/header.jsp"/>
<!-- Contents -->
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span3">
      <!--サイドコンテンツ-->
      <div class="well sidebar-nav">
        ${param.subMenu}
      </div>
    </div>
    <div class="span9">
      <!--メインコンテンツ-->
      <div class="well">
        <%-- token. --%>
        <input type="hidden" id="token" />
        ${param.content}
      </div>
    </div>
  </div>
</div>
</body>
</html>