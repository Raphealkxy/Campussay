(function(){
	"use strict"
	var footerScript = document.getElementsByTagName('script'),
		path;
	for(var i = footerScript.length; i > 0; i--){
	    path = footerScript[i-1].getAttribute('data-js');
		if(path != null){
			break;	
		}
	}
	var footerTpl = function(){
		/*
		<div class="footer">
            <div class="global-center footer-wrap clearfix">
                <ul class="link-wrap-list inline-block">
                    <li class="link-wrap-item inline-block">
                        <a href="#" class="inline-block">
                            <img src="/static/img/common/logo.png" alt="同学说" class="footer-logo"/>
                        </a>
                    </li>
                    <li class="link-wrap-item inline-block">
                        <a href="#" class="inline-block">
                            <img src="/static/img/common/web.png" alt="微博" class="third-party"/>
                        </a>
                    </li>
                    <li class="link-wrap-item inline-block">
                        <a href="#" class="inline-block">
                            <img src="/static/img/common/weixin.png" alt="微信" class="third-party"/>
                        </a>
                    </li>
                </ul>
                <div class="footer-aside">
                    <ul class="about-us-list">
                        <li class="inline-block">
                            <a href="#">关于校园说</a>
                        </li>
                        <li class="inline-block">
                            <a href="#">帮助中心</a>
                        </li>
                        <li class="inline-block">
                            <a href="#">联系我们</a>
                        </li>
                    </ul>
                    <p>
                        Copyrights &copy; 2015-2016   CampusSay  Co.,Ltd. All Rights Reserved.
                    </p>
                    <p>
                        <a href="#">渝ICP备16001027号</a>
                    </p>
                </div>
            </div>
        </div>
		*/
	}
	var footer = footerTpl.toString().replace(/^[^\/]+\/\*!?/, '').replace(/\*\/[^\/]+$/, '')+
				'<script src="/static/js/lib/r.js" data-main="/static/js/page/'+path+'"></script>'+
				'</body>'+
			  '</html>';
	document.write(footer);
})();