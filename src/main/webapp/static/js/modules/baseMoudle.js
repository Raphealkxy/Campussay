/*
FZQ 全局公共js模块：可以返回公共模块和对象，以及执行公共js
*/

define(['lib/jquery', 'modules/ask', 'util/request', 'util/funcTpl','modules/switchSchool/schoolIndex', 'modules/topic-pop/commonPop','lib/juicer'],
    function($, ask, request, funcTpl,schoolindex,commonpop) {
        var index = {
            init: function() {
                this._loginJump();

                //1.话题社，提问弹窗
                this._ask();

                //2.退出功能
                this._quit();

                //3.获取未读消息
                this._getMsg();

                //4.设置公共的对象userimg
                this.initPub();

                //5.顶部切换学校功能
                this._switchSchool();

                //6.添加jqery&juicer的全局方法
                this._setFunction();

                //高亮当前导航
                this._indexNav();

            },
            //高亮当前导航
            _indexNav:function(){

                var path = window.location.pathname;
                var index;

                var navPath = {
                    "/":0,
                    "/talking/ttIndex":1,
                    "/talking/publish":2,
                    "/personalCenter/order":3,
                    "/topic/hot":4
                };
                var navlist = $("#J_nav li>a"); 
                if(path){
                    index = navPath[path];
                }
                if(index !== 'undefined'){
                   $(navlist[index]).addClass("nav-active");   
                }
                
            },
            //图片
            picLoad:function(){
                $('.pic1').each(function() {
                    $(this).load(function () {

                        $(this).attr('visibility','visible').fadeIn();
                    })
                });
            },
            _loginJump: function() {
                $(".J_tophandle").on('click', function(e) {
                    var type = $(e.target).attr("data-type");
                    switch (type) {
                        case 'login':
                            var index = '/user/sign-in-web?url=';
                            window.location.href = index + window.location.pathname;
                    }
                })
            },
            //我要提问的弹窗
            _ask: function() {
                $(".J_ask").on('click', function() {
                    ask.init();
                });
            },
            //切换学校的弹窗
            _switchSchool: function() {
                
                var pathname = window.location.pathname;

                //只有首页和听听堂可以切换学校
                if(pathname === "/" || pathname === "/talking/ttIndex"){
                    $("#J_switchschool").click(function(){
                        schoolindex.chooseSchool();
                    });
                }else{
                    $("#J_switchschool").fadeOut();
                }

                var schoolName = $.cookie("schoolName");
                var oldSchoolName = $("#J_schoolName").html();
                if(oldSchoolName){
                    oldSchoolName = $("#J_schoolName").html().trim();
                }

                //schoolname存在并且和  
                schoolName && oldSchoolName !== schoolName && $("#J_schoolName").html(schoolName);
                
            },
            //退出弹窗
            _quit: function() {
                console.log(commonpop)
                $("#J_quit").on("click", function() {
                    //退出弹窗
                    commonpop.quitPop();
                })
            },
            //获取未读消息数量
            _getMsg: function() {

                var msgAjax = function() {
                    request.get('/information/getuserInformationList', { informationStatus: 0 }, function(rsp) {
                        rsp = JSON.parse(rsp);
                        var _this = $("#J_msgNum");
                        if (rsp.code == 1 && rsp.data && rsp.data.dataCount) {
                            _this.find('span').html("消息" + rsp.data.dataCount);
                            var show = setInterval(function(){
                                _this.css('background','none');
                                _this.find('img').fadeOut().fadeIn();
                            },1000);
                            setTimeout(function(){
                                clearInterval(show);
                            },5000)
                        }

                    });
                };
                //延迟发送，避免首页请求太多
                setTimeout(msgAjax, 5000)

            },
            _setFunction:function(){
                //1.防止重复点击。一段时间内，只会触发第一次点击
                $.extend({
                    clickOnce:function(dom,fn,time){
                        var lazyTime = time || 3000;//默认延迟3s内点击无效
                        var clickTime;//可点击时间
                        if(!dom){return}  

                        var  x = $(dom);
                        $(dom).click(function(event){
                            var nowTime = new Date().getTime();
                            //第一次点击时，初始化点击时间,并且执行函数
                            if (!clickTime) {
                                clickTime = new Date().getTime()+ lazyTime;
                                var x  = event.target;
                                //记得修改处理函数的this指针，否则在fn内指向window
                                fn.apply($(this),[$(event.target)]);
                            };
                            //如果点击时间大于当前时间，则返回
                            if(clickTime>nowTime){
                                return
                            }else{
                                clickTime = null
                            }
                        });
                    }
                })

                //2.注册juicer的全局方法：返回默认头像
                var getDeafultUser = function(){
                    return window.pub.userimg;
                }

                juicer.register('userPhoto', getDeafultUser);

                //3.返回为空图片
                $.extend({
                    BlackImgbig:function(){
                        return '<img style="width:100%;" src="/static/img/common/global-nodata.jpg"/>'
                    },
                    BlackImgsm:function(){
                        return '<img style="width:100%;" src="/static/img/common/global-nodata-sm.jpg"/>'
                    },
                    emptyContentImg:function() {
                        return '<img style="margin-top:100px; margin-left:-160px;" src="/static/img/common/empty.png"/>'
                    }
                });

            },
            //挂载全局对象
            initPub:function(){
                
                window.pub = {
                    userimg : "/static/img/common/defaultUserImg.png",
                    imgServer:"http://103.37.148.131:7000/",
                    classImg: "/static/img/common/talking.png"//默认课程图片
                }
            }
        };

        var tmplData = {

        }

        //首先执行全局js
        index.init();

        //返回公共对象
        var pubMoudle = {
            getUrlParam: function(name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
                var r = window.location.search.substr(1).match(reg); //匹配目标参数
                if (r != null) return unescape(r[2]);
                return null; //返回参数值
            },
            /*
            参数：
                课程开始时间startTime 必填    
                课程结束时间endTime 
                   
            返回值： 
                 talk.data        x月x日 
                 talk.currentTime 表示14:00-16:00 若没有传结束时间，则不会有这个
            */
            _handleTalkData: function(talk) {

                if(!talk.startTime){
                    return
                }

                var month,
                    day,
                    startTime,
                    endTime,
                    minunte,
                    talkDate = new Date(talk.startTime);

                month = talkDate.getMonth() + 1;
                day = talkDate.getDate();
                minunte = talkDate.getMinutes();
                minunte = minunte % 10 === minunte ? "0" + talkDate.getMinutes() : talkDate.getMinutes();
                startTime = talk.startTime.split(/\s+/)[1].split(":").slice(0, 2).join(":");
                talk.date = month + "月" + day + "日";

                //课程开始时间存在
                if(talk.endTime ){
                    endTime = talkDate.getHours() + ":" + minunte;
                    talk.currentTime = startTime + "—" + endTime;
                }
            },
            //一句话提示框（不重刷页面）
            infoNotreload:function(msg){
                if(!msg){return}
                commonpop.tipAutoDestory({tip:msg});
            },
            // 校招
            share:function ($dom) {
                var p = {
                    url:location.href,
                    showcount:'0',/*是否显示分享总数,显示：'1'，不显示：'0' */
                    desc:'desc',/*默认分享理由(可选)*/
                    summary:'summary',/*分享摘要(可选)*/
                    title:'title',/*分享标题(可选)*/
                    site:'www.campussay.com',/*分享来源 如：腾讯网(可选)*/
                    pics:'', /*分享图片的路径(可选)*/
                    style:'201',
                    width:39,
                    height:39
                };
                var s = [];
                for(var i in p){
                    s.push(i + '=' + encodeURIComponent(p[i]||''));
                }
                $dom.append(['<a version="1.0" class="qzOpenerDiv" href="http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?',s.join('&'),'" target="_blank">QQ分享</a>'].join(''));
                $('head').append('<script src="http://qzonestyle.gtimg.cn/qzone/app/qzlike/qzopensl.js#jsdate=20111201" charset="utf-8"></script>');
            }
        };

        return pubMoudle;
    });
