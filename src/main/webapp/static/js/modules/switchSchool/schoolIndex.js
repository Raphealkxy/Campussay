/*
fzq
切换学校，需要更新的模块
*/
define(['lib/jquery', 'util/request', 'util/funcTpl', 'modules/indexBanner', 'modules/topic-pop/commonPop', 'modules/cookie','modules/picFadeIn'],
    function($, request, funcTpl, banner, commonpop, cookie,picFadeIn) {

        var index = {
            schoolid: undefined, //这个没有用，schoolid是存在cookie里的
            //获取学校id
            initSchool: function() {
                //先从cookie中获取，若无则弹窗让用户选择学校
                var schoolid = $.cookie('schoolid');
                if (!schoolid) {
                    this.chooseSchool();
                } else {
                    //根据cookie中的id，获取首页配置（banner 精品 牛人）
                    this.initSchoolData()
                }
                 //自定义切换事件   
                //$(document).bind('switchschool', this.initSchoolData);

            },
            //用户切换学校
            chooseSchool: function() {
                var self = this;

                //1.获取学校信息
                var promise = request.get('/campus/getALLcampusname');

                //成功获取学校信息之后，弹窗用户切换学校
                var success = function(rsp) {
                    rsp = JSON.parse(rsp);
                    if (rsp.code == 1) {
                        commonpop.switchSchoolPop(rsp.data);
                    }
                };

                promise.done(success);

                //自定义切换事件   
                $(document).bind('switchschool', this.initSchoolData);
            },
            //获取学校的，banner图，大牛，精品talking
            initSchoolData: function(switchScoolID) {
                var schoolid = $.cookie("schoolid");
                    
                //如果切换学校的id 和当前id一样，就不发送请求
                if(switchScoolID && switchScoolID == schoolid){
                    return
                }


                //如果获取不到学校id，则弹窗让用户选择学校
                if (!schoolid) {
                    this.chooseSchool();
                }

                var param = {
                    //牛人接口
                    start: {
                        path: "/Star/Starindex",
                        tpl: index._startTpl,
                        dom: $("#J_start")
                    },
                    //精品课程接口
                    talk: {
                        path: "/talking/Specialtalking",
                        tpl: index._talkTpl,
                        dom: $("#J_talkIndex")
                    }
                }

                //注意~听听堂页面和首页切换的内容不一样
                var page = window.location.pathname;
                switch (page) {
                    case "/":
                        //1.获取牛人接口
                        index._getAjax(param.start);
                        //2.精品talk接口
                        var promiseTalk = index._getAjax(param.talk);
                        //promiseTalk.done(this._talkAnnimation); //回调成功之后，加上动画效果
                        
                        //3.banner图
                        banner(schoolid);
                        break;
                    case "/talking/ttIndex": //听听堂
                        banner(schoolid);
                        break;
                }
            },
            //精品talk的轮播动画
            _talkAnnimation: function() {
                var talkSlider = $("#J_talkIndex"), //轮播容器
                    niceTalk  = $("#J_talkContainer"),//精品talk的容器
                    talkItem = $("#J_talkIndex > .talk-item"), //每个图片
                    itemWidth = $(talkItem[0]).width(),
                    moveWidth = itemWidth + 20, //有20间距
                    current = 1, //当前轮播的标志位,从第四个开始
                    num = $("#J_talkIndex  li.talk-item").length; ///图片个数  15 
                //根据图片大小和图片个数，计算容器宽度
                talkSlider.css("width", (itemWidth + 20) * num);
                if (num <= 0) {
                    return } //只有四个及更少的时候返回

                //播放第n个元素    
                var change = function(index) {
                    var moveLeft = -index * moveWidth + "px";
                    talkSlider.animate({"left":moveLeft},400);
                };

                //当前轮播的位置
                var size = current % (num - 4+1);
                
                //自动轮播
                var autoShow = function(){
                    change(size);
                    current++;
                    size = current % (num - 4+1);
                }

                var timer = setInterval(autoShow, 3000);

                //鼠标移入停止，移除
                niceTalk.hover(function() {
                    clearInterval(timer);
                }, function() {
                    clearInterval(timer);
                    timer = setInterval(autoShow, 3000);
                });

                //点击左右箭头轮滑
                $(".arrow-left").click(function(){
                    current ++ ;
                    size = current % (num - 4);
                    change(size);
                });

                $(".arrow-right").click(function(){
                    current -- ;
                    size = current % (num - 4);
                    change(size);
                });

            },
            //1.大牛专区
            _startTpl: function() {
                /*
            <h1 class="com-title">大牛们都在这<a class="btn-more" href="/talking/listenList?tap=1#friends">更多>></a></h1>
                <ul class="person">
                {@each list as item,index}
                    {@if index == 1 || index == 4 || index ==7}
                        <li class="ml20 mr20 person-item">
                    {@else}
                        <li class="person-item">
                    {@/if}
                        <a href="/user/personalIndex?user=${item.user_id}" target="_blank"><img src="${item.user_photo}" class="pic1"></a>
                        <div class="person-details">
                            <div class="name">
                            <a href="/user/personalIndex?user=${item.user_id}" target="_blank">${item.user_name}</a>
                            </div>
                            <div class="school">
                                <span>${item.user_campus_name}</span>
                                <span class="line"></span>
                                <span>${item.academe}</span>
                            </div>
                            <div class="comments">
                                ${item.user_title}
                            </div>
                        </div>
                    </li>
                {@/each}    
                </ul>
            */
            },
            //2.精品talk专区
            _talkTpl: function() {
                /*
                {@each list as item,index}
                        <li class="talk-item"><a class="talk-item" href="/talking/classDetail?tk=${item.talking_id}" target="_blank"><img src=${item.talking_main_picture} /></a></li>
                {@/each} 
               
                */
            },
            /*
        切换学校，需要发起的后台请求
        参数:
             path:ajax路径,必填
             schoolid:学校id
             tpl:juicer模版，必填    
             dom:字符串插入元素
        返回值：返回字符串类型的html片段
        */
            _getAjax: function(cfg) {
                var self = this;
                if (!cfg.path) {
                    return
                }
                var schoolid = $.cookie('schoolid');

                var promise = request.get(cfg.path, { campus_id: schoolid }, function(rsp) {

                    rsp = JSON.parse(rsp);
                    if (rsp.code == 1) {
                        var tmpl,
                            tplData = {
                                list: rsp.data
                            };
                        tmpl = juicer(funcTpl(cfg.tpl), tplData);
                        cfg.dom.html(tmpl);
                        picFadeIn();
                    }
                    /*else if (rsp.data === null) {
                                           //后台返回null，表示传入的schoolid无效，可能是因为cookie里存的是无效数据
                                           self.chooseSchool();
                                       }*/
                });

                return promise;
            }
        }

        return index;
    });