<!DOCTYPE html>
 <#include "../views/utils.ftl">
<head>
	<meta charset="utf-8" />
	<title>
	 <#if page.properties['page.title']?exists>
          ${page.properties['page.title']}
    <#else>
      校园说
     </#if>
     </title>
	
	<meta content="" name="author" />
    <meta name="keywords" content="校园说 校园社交 校园知识 校园经验 重邮创业 勤奋蜂">
    <meta name="description" content="campussay xiaoyuanshuo 大学生精英社交 大学生知识经验分享 校园在行 大学迷茫怎么办 大学怎么过 大学规划 大学职业规划 面试经验 就业经验 求职经验  留学经验  简历 高考志愿 重庆高校 创业 学长帮忙 学姐帮忙 知识分享 经验分享 校园App 校园招聘 社交推广 拓宽人际 猎头 优秀毕业生 奖学金 竞赛 社团 主席 干事 策划 大长腿 高颜值 学霸 保研怎么 留学 咨询 学渣 逆袭 转专业">
    <meta name="baidu-site-verification" content="YaFwLaBiL8" />
	<link rel="SHORTCUT ICON" href="${staticPath}/img/common/defaultUserImg.png">
    <link rel="stylesheet" href="${staticPath}/css/global.css"/>
    <!--当前页面的引入的样式 start-->
    <#if page.properties['page.css']?exists>
        <link href="${staticPath}/css/page${page.properties['page.css']}.css" rel="stylesheet" />
    </#if>


    <!--当前页面的引入的字体样式 start-->
    <#if page.properties['page.topic']?exists>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css"> 
    </#if>

    <!-- 页面的启动脚本配置文件，需要给定个变量判断是否开发环境-->
    <script type="text/javascript">
        var CP = {};
        CP.BOOTSTAMP = new Date().getTime();
        CP.STATIC_ROOT = "${staticPath}"+'/js';
        CP.imgServer = 'http://103.37.148.131:7000/';
    </script>
    <!-- 页面的启动脚本配置文件 -->
</head>  
    <!--  判断用户是否登录 -->
    <#if Session.user?exists> 
       <div class="head">
            <div class="container">
                <div class="left">
                    <span class="user-name" ><a href="/personalCenter/order">${Session.user.userName}</a></span>
                    <span class="user-exit" id="J_quit" >退出</span>
                    <span class="user-infor">
                        <i class="icon icon-user-infor"></i>
                        <a href="/message" id="J_msgNum" style="position:relative">
                        <img src="/static/img/common/messageHover.png" style="display:none;position:absolute;top:4px;left:0">
                        <span>消息<span>
                        </a>
                    </span>
                </div>
                <div class="right">
                    <a href="/download">下载App</a>
                    <a href="/about">什么是校园说</a>
                    <a href="/">校园说首页</a>
                </div>
            </div>
        </div>
        
    <#else>
       <div class="common-header">
            <div class="global-center clearfix">
                <ul class="common-header-list">
                    <li class="common-header-item index">
                        <a href="/">
                            <i class="home-pic inline-block"></i>
                            <span class="index-font"><!--${staticPath}-->首页</span>
                        </a>
                    </li>
                </ul>
                <ul class="common-header-aside list-unstyled">
                    <li class="common-header-item">
                        <a href="/download">下载App</a>
                    </li>
                    <li class="common-header-item what-campus">
                        <a href="/about">
                            什么是校园说？
                        </a>
                    </li>
                    <li class="common-header-item login-or-register J_tophandle">
                        <a data-type="login" href="/user/sign-in-web">登录</a>
                        /
                        <a href="/user/sign-in-web?url='/'&type=1" data-type="reginster">注册</a>
                    </li>
                </ul>
            </div>
        </div>
    </#if>  

<!--判断引入公共导航或者话题社导航 -->
    <#if page.properties['page.topic']?exists>
        <!-- 话题社通用头部导航 -->
        <div class="ht-common-bar">
            <div class="global-center clearfix ht-bar">
                <a href="/" class="logo-to-index">
                    <img src="/static/img/page/listen-talking-list/logo.png" alt="logo"/>
                </a>
                <h1 class="ht-bar-title"><a href="/topic/hot?userId=0">话题社</a></h1>
                <ul class="ht-common-switch clearfix">
                    <li class="switch-on">
                        <a href="/topic/hot?userId=0">
                            首页
                        </a>
                    </li>
                    <li>11
                        <a href="/topic/field">
                            领域
                        </a>
                    </li>
                </ul>
                <div class="action-wrap">
                    <a class="ask-question J_ask">我要提问</a>
                    <a href="/topic/htIndex?user=0" class="my-ht">我的话题社</a>
                </div>
            </div>
        </div>
    <#else>
        <div class="main-nav">
            <div class="global-center clearfix">
                <a href="/" class="link-index">
                    <img src="/static/img/page/listen-talking-list/logo.png" alt="logo" title="校园说网"/>
                </a>
                <div class="address">
                    <h1 id="J_schoolName" data-id=""></h1>


                    <#if Session.uri?exists>
                    <#if Session.uri == "/" ||  Session.uri == "/talking/ttIndex"  >

                    <button id="J_switchschool">切换学校</button>
                    </#if>
                    </#if>

                </div>
                <ul class="nav-list" id="J_nav">
                    <!--<li><a href="/talking/listenList" class="J_nav-1">Talking分类</a></li>-->
                    <li><a href="/" class="J_nav-2">首页</a></li>
                    <li><a href="/talking/ttIndex"  class="J_nav-3">听听堂</a></li>
                    <li><a href="/talking/publish" class="J_nav-4">发布Talking</a></li>
                    <li><a href="/personalCenter/order" class="J_nav-5">我的校园说</a></li>
                    <li><a href="/topic/hot" target="_blank" class="J_nav-6">话题社</a></li>
                </ul>
            </div>
        </div>
    </#if>



