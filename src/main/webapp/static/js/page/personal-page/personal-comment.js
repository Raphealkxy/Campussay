/**
 * Created by zhujing on 2016/1/17.
 */

require.config({
    baseUrl: CP.STATIC_ROOT
});
require(["lib/jquery","util/request","util/funcTpl","modules/page/page","modules/popup/signPop","lib/juicer",'modules/baseMoudle'], function ($,request,funcTpl,page,pop) {
    var index = {

        init: function () {
            user_id = index._param().user;
            index._showComment($(".talking"));
            index._render(user_id);
            index._forward();
            //获取翻页钩子函数->回调为翻页函数
            var initPage = page.init('#page', function (num) {
                index._data.pageCount = num;
                index._showComment($(".talking"));
            });
            //初始化
            $('body').one('page:set', function (ev, size, totalList) {
                //console.log(size+"-"+totalList);
                initPage(size, totalList);
            });
        },
        _data : {
            pageCount :1
        },
        _Api:{//接口信息
            getUserInfo:'/user/getBasicInfo',
            getUserFans:'/user/getAttentionCount',
            getPublishTalk:'/talking/getUserTalking',
            getJoinTalk:'/talking/getUserBuyTalking',
            addLove:'/user/attentionUser',
            getComment :　'/comment/getAllCommentByUser'
        },
        //_templatetiao : function () {
        //  /*
        //   <li class="personal-menu-item J-index"><a href="/user/personalIndex?user="+user_id>主页</a></li>
        //   <li class="personal-menu-item J-info"><a href="/user/persoanl-info?user="+user_id>个人资料</a></li>
        //   <li class="personal-menu-item current-menu-item J-comment">评价</li>
        //     */
        //},
        _param: function () {//获取参数
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
        _tpl: function () {
            /*
             <ul class="personal-panel-aside-big">
             <li class="personal-panel-item">
             <img src="${user_photo}" alt="header"/>
             </li>
             <li class="personal-panel-item person-panel-info">
             <h2 class="personal-panel-title">
             <span class="personal-panel-name">${user_name}</span>
             </h2>
             <p class="personal-panel-student">${user_campus_name}
             {@if user_student_check_result!=0}
             | ${user_academe} | ${user_major}
             {@/if}
             </p>
             {@if user_student_check_result!=0}
             <p>擅长领域 :
             {@each skill as item,index}
             {@if index!=0}、${item.skill_name} {@else} ${item.skill_name}{@/if}
             {@/each}
             </p>
             {@/if}
             </li>
             <li class="personal-panel-item personal-panel-dec">
             <p>简介：${user_description}</p>
             </li>
             </ul>
             <div class="personal-panel-aside-small">
             {@if attention==0}
             <button class="care" data-state="${attention}">+ 关注</button>
             {@else if attention==-1}
             <button class="care"><a href="/personalSetting/person">编辑</a></button>
             {@else}
             <button class="cancel-care" data-state="${attention}">× 取消关注</button>
             {@/if}
             </div>
             */
        },
        _commonRender: function (url,param,dom,tpl,fn) {
            request.post(url,param, function (res) {
                res = JSON.parse(res)||eval(res);
                if(1 == res.code){
                    var htmlT = juicer(tpl,res.data);
                    $(dom).html(htmlT);
                    fn&&fn(res.data);
                }
            });
        },
        _render: function (userId) {
            index._commonRender(
                index._Api.getUserInfo, {userId:userId}, '.personal-panel', funcTpl(index._tpl), function (data) {
                    if(!!data.user_student_check_result){
                    //    $('.J_student_ident').removeClass('no-student-identify')
                    //        .addClass('identify-xueli');
                    }
                    index._addLove(userId);
                }
            );
        },
        _addLove: function (userId) {
            $('.personal-panel-aside-small').on('click','button', function () {
                console.log(userId);
                var state = $(this).data('state');
                var hasState = (state != undefined);
                if(hasState){
                    state = state==0?1:0;
                    request.post(index._Api.addLove,{userId:userId,attention:state}, function (res) {
                        res = JSON.parse(res)||eval(res);
                        if(1 == res.code){
                            if(state){
                                $('.personal-panel-aside-small').html(' <button class="cancel-care" data-state=1>× 取消关注</button>');
                            }else{
                                $('.personal-panel-aside-small').html(' <button class="care" data-state=0>+ 关注</button>');
                            }
                        }else{
                            pop();
                        }
                    });
                }
            });
        },
        //显示评论
        _showComment: function ($ele) {
            request.get(index._Api.getComment, {"userId": user_id, "page": index._data.pageCount}, function (res) {
                    res = JSON.parse(res);
                    if (res.code == 1) {
                        var count = res.data.count; //获取评论条数
                        //显示评价数目
                        var tmpList = function () {
                            /*
                             {@each rows as item,index}
                             <li class="talking-container">
                             <div class="selfImg-list">
                             <img src="/static/img/page/personal-page/icon-dog.png">
                             <div class="show-name">${item.user_name}</div>
                             </div>
                             <div class="talking-show">
                             <h1>${item.talking_title}</h1>
                             <div class="cstar">
                             <div>综合评价：</div>
                             <ul>
                                $${item.talking_comment_grade |showstar}
                             </ul>
                             </div>
                             <p>${item.talking_comment_content}</p>
                             <div class="dd">${item.talking_comment_time|formDate}</div>
                             </div>
                             <div class="bottom-line"></div>
                             </li>
                             {@/each}
                             */
                        };
                        //循环输出星星
                        var showstar = function (num) {
                           var child = '<li><img src="/static/img/page/personal-page/star1.png"></li>';
                            var child_gray= '<li><img src="/static/img/page/personal-page/star-gray.png"></li>';
                            var html = "";
                            for(var i=0;i<num;i++){
                                html += child;
                            }
                            if(num<5){
                                for(var j=0;j<5-num;j++){
                                    html+=child_gray;
                                }
                            }
                            return html;
                        };
                        if(count !=0){
                            $(".talking-bar span").text(count);
                        }else{
                            $(".talking-bar span").text("0");
                        }

                        //时间戳转日期
                        var formDate = function (unixTime, isFull, timeZone) {
                            if (typeof (timeZone) == 'number') {
                                unixTime = parseInt(unixTime) + parseInt(timeZone) * 60 * 60;
                            }
                            var time = new Date(unixTime);
                            var ymdhis = "";
                            ymdhis += time.getUTCFullYear() + "-";
                            ymdhis += (time.getUTCMonth() + 1) + "-";
                            ymdhis += time.getUTCDate();
                            if (isFull === true) {
                                ymdhis += " " + time.getUTCHours() + ":";
                                ymdhis += time.getUTCMinutes() + ":";
                                ymdhis += time.getUTCSeconds();
                            }
                            return ymdhis;
                        };
                        juicer.register("formDate", formDate);
                        juicer.register("showstar",showstar);
                        var temp = juicer(funcTpl(tmpList), res.data);
                        $ele.html(temp);
                        $(".talking li:last-child .bottom-line").css("display", "none");
                        //触发翻页初始化
                        $('body').trigger('page:set',[10,count]);
                    }
                }
            );
        },
        //跳转页面
        _forward : function(){
            $(".J-info").on("click", function (e) {
                window.location.href = "/user/personal-info?user="+user_id;
            });
            $(".J-index").on("click",function () {
                window.location.href = "/user/personalIndex?user="+user_id;
            });
        }
    };
     return index.init();
});