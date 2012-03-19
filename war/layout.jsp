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
<link href="/css/docs.css" rel="stylesheet">
<link href="/css/common.css" rel="stylesheet">

<script type="text/javascript" src="/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/js/bootstrap.min.js"></script>

</head>
<body data-spy="scroll" data-target=".subnav" data-offset="50" data-twttr-rendered="true">
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
        ${param.content}
      </div>
    </div>
  </div>
</div>
</body>
</html>