(function(){
	"use strict"
	var headerCss = document.getElementsByTagName('script'),
		path,cssPath,headTitle = "";
	for(var i = 0; i < headerCss.length; i++){
	    cssPath = headerCss[i].getAttribute('data-css');
		headTitle = headerCss[i].getAttribute('data-title');
		if(cssPath != null && cssPath != undefined){
			cssPath = '<link rel="stylesheet" href="/static/css/page/'+cssPath+'.css"/>'
			break;	
		}
	}
	var html = '<!DOCTYPE html>'+
				'<html lang="en">'+
				'<head>'+
					'<meta charset="UTF-8">'+
					'<title>'+headTitle+'</title>'+
					'<link rel="stylesheet" href="/static/css/global.css"/>'
					+cssPath+
					'<script>'+
						'var CP = {};'+
						'CP.STATIC_ROOT = "/static/js"'+
					'</script>'+
				'</head>'+
				'<body>';
				
    var headerTpl = function(){
		/*
		<div class="common-header">
			<div class="global-center clearfix">
				<ul class="common-header-list">
					<li class="common-header-item index">
						<a href="#">
							<i class="home-pic inline-block"></i>
							<span class="index-font">首页</span>
						</a>
					</li>
				</ul>
				<ul class="common-header-aside list-unstyled">
					<li class="common-header-item what-campus">
						<a href="#">
							什么是校园说？
						</a>
					</li>
					<li class="common-header-item login-or-register">
						<a href="#">登录</a>
						/
						<a href="#">注册</a>
					</li>
				</ul>
			</div>
		</div>
		*/
	};

	var  header = html + headerTpl.toString().replace(/^[^\/]+\/\*!?/, '').replace(/\*\/[^\/]+$/, '');
	document.write(header);
})();
