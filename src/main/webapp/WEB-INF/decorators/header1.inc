<!DOCTYPE html>
 <#include "../views/utils.ftl">
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<head>
	<meta charset="utf-8" />
	<title>
	 <#if page.properties['page.title']?exists>
          ${page.properties['page.title']}
    <#else>
      
     </#if>
     </title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />

    <link rel="stylesheet" href="${staticPath}/css/global.css"/>

    <!--当前页面的引入的样式 start-->
    <#if page.properties['page.css']?exists>
        <link href="${staticPath}/css/page${page.properties['page.css']}.css" rel="stylesheet" />
    </#if>

	<!--<link rel="shortcut icon" href="/static/image/favicon.ico" />-->

    <!--[if lt IE 9]>
    <script src="/static/js/lib/basic/excanvas.min.js"></script>
    <script src="/static/js/lib/basic/respond.min.js"></script>
    <![endif]-->

    <!-- 页面的启动脚本配置文件，需要给定个变量判断是否开发环境-->
    <script type="text/javascript">
        var MT = {};
        MT.BOOTSTAMP = new Date().getTime();
        MT.STATIC_ROOT = "${staticPath}";
    </script>
    <!-- 页面的启动脚本配置文件 -->
</head>
<body>



