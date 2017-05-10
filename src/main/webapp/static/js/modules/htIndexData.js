define(['lib/jquery', 'util/request', 'util/funcTpl', 'modules/page/page','modules/topic-pop/commonPop', 'modules/popup/signPop',
 'lib/juicer','modules/dotdotdot/jquery.dotdotdot', 'modules/formatTime'], function ($, request, funcTpl, page, pop, popup) {
    'use strict';
    var htData = {
        init: function () {
            var userId = htData._param().user;
            //获取翻页钩子函数->回调为翻页函数
            var initPage = page.init('#page', function (num) {
                htData._renderList(userId,num);
            });
            //初始化
            function setPage(){
                $('body').one('page:set', function (ev, size, totalList) {
                    initPage(size, totalList);
                });
            }
            if(userId!=undefined){
                setPage();
                htData._renderPanel(userId);
                htData._currentListTpl = funcTpl(htData._tplList);
                htData._renderList(userId,1);
                htData._renderOther(userId);
                htData._switchTab(userId,setPage);
                htData._addLove(userId);
            }
        },
        _userId:0,
        _Api: {
            getAllQuestion: '/topic/getAllTopicByUserId',
            getAllAnswer: '/answer/getAllAnswerByUserId',
            getUserInfo: '/topic/getUserDetailMsgById',
            getOtherTalking: '/talking/getothertalking',
            addLove:'/user/attentionUser',
            getMoreMsg:'/Comments/getCommentsByAnswerId',
            addAConcernTopic:'/followtopic/addAConcernTopic',
            cancelConcernTopic:'/followtopic/cancelConcernTopic'
        },
        _currentListApi:'/topic/getAllTopicByUserId',
        _currentListTpl:'',
        _param: function () {
            var paramSearch = window.location.search.slice(1),
                paramArr = [],
                oParam = {};
            paramArr = paramSearch.split('&');
            for(var i=0;i<paramArr.length;i++){
                var inArr = paramArr[i].split('=');
                oParam[inArr[0]] = parseInt(inArr[1]);
            }
            return oParam;
        },
        _tplPersonal: function () {
            /*
                <a href="javascript:;" class="personal-url">
                    <img src="${user_photo}" alt="head" class="personal-head"/>
                </a>
                <ul class="personal-info">
                <li class="personal-name">
                    <span>${user_name}</span>
                    {@if attention!=-1}
                    {@if attention==0}
                    <button class="J_love" data-state="${attention}">+关注</button>
                    {@else if attention==1}
                    <button class="J_love" data-state="${attention}" style="width:80px;">×取消关注</button>
                    {@/if}
                    {@/if}
                </li>
                <li class="personal-school">
                    <span>${user_campus_name}</span>
                    {@if academy}
                        <i></i>
                        <span>${academy}</span>
                    {@/if}
                </li>
                <li class="good-at">
                    <span class="at-name">擅长领域&nbsp;&nbsp;:</span>
                    {@each skillsName as item}
                        <span class="good-at-item inline-block tip at-item">${item.talking_type_name}<i class="inline-block"></i></span>
                    {@/each}
                </li>
                <li class="about-at">
                    <span class="at-name">关注领域&nbsp;&nbsp;:</span>
                     {@each concernName as item}
                        <span class="good-at-item inline-block tip at-item">${item.talking_type_name}<i class="inline-block"></i></span>
                     {@/each}
                </li>
            </ul>
            <ul class="look-personal">
             {@if attention!=-1}
                <li><a href="/user/personalIndex?user=${user_id}">查看TA的个人资料</a></li>
             {@else}
             <li><a href="/personalSetting/person#basic">编辑个人资料</a></li>
             {@/if}
                <li><!--<span class="read-num">100</span>--><span class="up-num">${islike}</span></li>
            </ul>
            */
        },
        _tplList: function () {
            /*
             <!--{@helper getDate}
                 function(time) {
                     var oTime = new Date(time);
                     return [oTime.getFullYear(),oTime.getMonth()+1,oTime.getDate()].join('-');
                 }
             {@/helper}-->
            {@each list as item}
                <li class="qa-item clearfix">
                    <div>
                        <h2>${item.tile}?</h2>
                        <p>
                            <span>${item.create_time|getDate}</span>
                            <span>${item.answerNum}条回复</span>
                        </p>
                    </div>
                   <ul>
                        <li class="followTopic" data-topicid=${item.id}>
                        {@if item.isFollow == 1}<button data-follow=${item.isFollow}> ×取消关注</button>{@/if}
                        {@if item.isFollow == 0}<button data-follow=${item.isFollow}> +关注话题</button>{@/if}
                        </li>
                        <li><span class="about-num">${item.followNum}</span><span>人关注</span></li>
                    </ul>
                </li>
             {@/each}
           */
        },
        _tplAnswerList: function () {
            /*
            <!--
                {@helper getTime}
                    function(date){
                         var oDate = new Date(date).getTime();
                         var nowDate = new Date().getTime();
                         var seconds = (nowDate - oDate)/1000;
                         var oHtml = '';
                         if(Math.floor(seconds/60/60/24/30)){
                             oHtml += Math.floor(seconds/60/60/24/30)+'月';
                             seconds = seconds - Math.floor(seconds/60/60/24/30)*60*60*24*30;
                         }
                         if(Math.floor(seconds/60/60/24)){
                              oHtml += Math.floor(seconds/60/60/24)+'日';
                              seconds = seconds - Math.floor(seconds/60/60/24)*60*60*24;
                         }
                         if(Math.floor(seconds/60/60)){
                            oHtml += Math.floor(seconds/60/60)+'小时';
                            seconds = seconds - Math.floor(seconds/60/60)*60*60;
                         }
                         if(Math.floor(seconds/60)){
                            oHtml +=Math.floor(seconds/60)+'分钟';
                         }
                        return oHtml;
                    }
                {@/helper}
            -->
            {@each list as item}
            <li class="answer-item clearfix">
                <h2 class="answer-title clearfix"><a href="/topic/answerdetail?topicid=${item.topicId}">${item.tile} </a><span class="answer-time">${item.time|handleTime}</span></h2>
                <div class="clearfix">
                    <button class="add-good"><i class="add-good-icon"></i>${item.islike}</button>
                    <div class="answer-content">
                        <h3 class="answer-content-title">${item.usersname}在 <span>${item.talkingTypeName}</span>中回答</h3>
                        <p class="answer-info" data-content="${item.context}">
                         $${item.context}
                         </p>
                    </div>
                </div>
                <div class="answer-option-wrap">
                    <button data-id="${item.answerId}" class='J_getMsg' data-num="${item.commentsNum}"><i class="msg-icon"></i>${item.commentsNum}</button>
                    <button class="share-btn" data-url="/topic/answerdetail?topicid=${item.topicId}"><i class="share-icon"></i>分享</button>
                </div>
            </li>
            {@/each}
            */
        },
        _tplOther: function () {
            /*
            {@each data as item}
                <li class="my-talking-item clearfix">
                    <a href="/talking/classDetail/?tk=${item.talking_id}"><img src="${item.talking_main_picture}" alt="pic"/></a>
                    <div class="talking-info">
                        <h3>${item.talking_title}</h3>
                        <p><span>${item.talking_type_name}</span><i></i><span>${item.user_name}</span></p>
                    </div>
                    <div class="look-talking">
                        <p>&yen;${item.talking_price}</p><a href="/talking/classDetail/?tk=${item.talking_id}" class="go-btn">去看看</a>
                    </div>
                </li>
             {@/each}
            */
        },
        _tplSubAnswerList: function () {
            /*
            <ul class="answer-all-list">
                {@each list as item}
                <li class="answer-all-item clearfix">
                    <a href="/topic/htIndex?user=${item.user_id}" class="inline-block">
                    <img src="${item.user_photo}" alt="pic">
                    </a>
                    <p><span class="answer-name">${item.user_name}</span>${item.context}</p>
               <!-- <div class="answer-detail-option">
                    <button>回复</button>
                    <button><i class="add-good-icon"></i>${item.islike}</button>
                </div>-->
                </li>
                {@/each}
            </ul>
            */
        },
        _commonRender: function (url, param, tpl, dom,config ,fn) {
            request.post(url,param, function (res) {
                res = JSON.parse(res)||eval(res);
                if(1 == res.code){
                    var template = '';
                    if(config.data){
                         template = juicer(tpl,res.data);
                    }else{
                         template = juicer(tpl,res);
                    }
                    $(dom).html(template);
                    fn&&fn(res.data);
                }else if(-1 == res.code){
                    popup();
                }
            });
        },
        _renderPanel: function (userId) {
            htData._commonRender(htData._Api.getUserInfo,{userId:userId},funcTpl(htData._tplPersonal),'.personal-panel',{data:true}, function () {
                htData._addLove(userId);
            });
        },
        _renderList: function (userId,page) {
            htData._commonRender(htData._currentListApi,{userId:userId,page:page},htData._currentListTpl,'.qa-list',{data:true}, function (data) {
                $('body').trigger('page:set',[10,data.rows]);
                htData._getMoreMsg();
                htData._share();
                htData._follow();
                //添加查看更多
                var $info = $('.answer-info');
                if($info.length>0){
                    $info.each(function (index) {
                        if(this.scrollHeight > 60){
                            while (this.scrollHeight > 60) {
                                $(this).text($(this).text().replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, ""));
                            }
                            $(this).text($(this).text().slice(0,-20));
                            var lookMore = $('<span>');

                            var content = $(this).data('content');
                            var $that = $(this);
                            lookMore.html('...查看更多').css({'color':'#00caab','font-size':'14px','cursor':'pointer'}).on('click', function (ev) {
                                $that.css({'height':'auto'}).html(content);
                            });
                            $that.append(lookMore);
                        }
                    });
                }
            });
        },
        _renderOther: function (userId) {
            htData._commonRender(htData._Api.getOtherTalking,{user_id:userId},funcTpl(htData._tplOther),'.my-talking-list',{data:false});
        },
        _switchTab: function (userId,fn) {
            $('.switch-bar li').on('click', function (ev) {
                if(!$(this).hasClass('checked')){
                    htData._currentListApi = $(this).data('id')==0?htData._Api.getAllQuestion:htData._Api.getAllAnswer;
                    htData._currentListTpl = $(this).data('id')==0?funcTpl(htData._tplList):funcTpl(htData._tplAnswerList);
                    $('.checked').toggleClass('checked');
                    $(this).toggleClass('checked');
                    fn&&fn();
                    htData._renderList(userId,1);
                }
            });
            //跳转过来页面 如果type=1 则选中展示回答的页面
            function GetQueryString(name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
                var r = window.location.search.substr(1).match(reg);
                if (r!=null) return (r[2]); return null;
            }
            var sname = GetQueryString("type");
            if(sname!=null)
            {
                $('.switch-bar li').eq(1).trigger('click');
            }
        },
        _addLove: function (userId) {
            $('.J_love').on('click', function (ev) {
                var state = $(ev.target).data('state');
                var hasState = (state != undefined);
                if(ev.target.tagName == 'BUTTON'&&hasState){
                    state = state==0?1:0;
                    request.post(htData._Api.addLove,{userId:userId,attention:state}, function (res) {
                        res = JSON.parse(res)||eval(res);
                        if(1 == res.code){
                            if(state){
                                $('.J_love').html('× 取消关注').data('state',state).attr({'style':'width:80px;'});
                            }else{
                                $('.J_love').html('+ 关注').data('state',state).attr({'style':'width:58px;'});
                            }
                        }
                    });
                }
            });
        },
        _getMoreMsg: function () {
            $('.J_getMsg').on('click', function (ev) {
                var num = $(this).data('num');
                if(num){
                    if(!$(this).find('i').hasClass('open-msg-icon')){
                        var insertPlace = this.parentNode.parentNode;
                        var id = $(this).data('id');

                        request.post(htData._Api.getMoreMsg,{answerId:id}, function (res) {
                            res = JSON.parse(res)||eval(res);
                            if(res.code == 1){
                                var tpl = juicer(funcTpl(htData._tplSubAnswerList),res.data);
                                if(insertPlace){
                                    $(insertPlace).after(tpl);
                                }else{
                                    $('.qa-list').append(tpl);
                                }
                            }
                        });

                        $('.J_getMsg').find('i').removeClass('open-msg-icon');
                        $(this).find('i').toggleClass('open-msg-icon');
                    }else{
                        $(this).find('i').removeClass('open-msg-icon');
                        $('.answer-all-list').remove(); //去除上一个选中
                    }
                }
            });
        },
        _share: function () {
            $('.share-btn').on('click',function () {
                var $config = {
                    url: $(this).attr('data-url'),
                    source: 'www.campussay.com'
                };
                pop.share($config);
            });
        },
        _follow: function () {
            $('.followTopic').on('click',function () {
                var self = $(this);
                var id = $(this).attr('data-topicid');
                var follow = $(this).find('button').attr('data-follow');
                var reqUrl = (follow == 0) ? htData._Api.addAConcernTopic : htData._Api.cancelConcernTopic;
                var tpl = (follow == 0) ? '<button data-follow="1"> ×取消关注</button>' : '<button data-follow="0"> +关注话题</button>'

                request.post(reqUrl,{topicId:id}, function (res) {
                    res = JSON.parse(res);
                    if(res.code == 1){
                        self.html(tpl);
                        pop.info({ tip: res.msg, time: '1500' });
                    } else {
                        pop.info({ tip: res.msg, time: '1500' });
                    }
                });
            });
        }
    };

    return htData;
});