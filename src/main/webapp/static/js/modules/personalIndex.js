define(['lib/jquery', 'util/request', 'util/funcTpl', 'modules/page/page', "modules/popup/signPop", 'page/personal-page/time-line', 'lib/juicer', 'modules/baseMoudle'], function ($, request, funcTpl, page, pop, timeLine) {
    'use strict';

    var user = {
        init: function () {
            var user_id = user._param().user;//获取传参

            //获取翻页钩子函数->回调为翻页函数
            var initPage = page.init('#page', function (num) {
                user._talkOrOtherTalk(user_id, num);
            });

            function toInit() {
                $('body').one('page:set', function (ev, size, totalList) {
                    debugger
                    initPage(size, totalList);
                });
            }


            user._watchMenuBar(user_id);//初始化导航切换
            toInit(); //初始化翻页
            user._render(user_id);
            user._attention(user_id);
            user._talkOrOtherTalk(user_id, 1);
            user._switch(user_id, toInit);//监听切换
        },
        _Api: {//接口信息
            getUserInfo: '/user/getBasicInfo',
            getUserFans: '/user/getAttentionCount',
            getPublishTalk: '/talking/getUserTalking',
            getJoinTalk: '/talking/getUserBuyTalking',
            addLove: '/user/attentionUser',
            getComment: '/comment/getAllCommentByUser'
        },
        _fans: 0,
        _currentListApi: '/talking/getUserTalking',//默认请求的列表地址
        _param: function () {//获取参数
            var paramSearch = window.location.search.slice(1),
                paramArr = [],
                oParam = {};
            paramArr = paramSearch.split('&');
            for (var i = 0; i < paramArr.length; i++) {
                var inArr = paramArr[i].split('=');
                oParam[inArr[0]] = parseInt(inArr[1]);
            }
            return oParam;
        },
        _tpl: function () {
            /*
             <ul class="personal-panel-aside-big">
             <li class="personal-panel-item">
             <img src="${user_photo}" alt="header" onerror='this.src = window.pub.userimg'/>
             </li>
             <li class="personal-panel-item person-panel-info">
             <h2 class="personal-panel-title">
             <span class="personal-panel-name">${user_name}</span>
             <span class="personal-panel-tip inline-block">${user_title}</span>
             </h2>
             <p class="personal-panel-student" style="white-space:nowrap;overflow:hidden;text-overflow:ellipsis;" title="${user_major}">${user_campus_name}
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
             <div class="care"><a href="/personalSetting/person#basic" class="edit-self">编辑</a></div>
             {@else}
             <button class="cancel-care" data-state="${attention}">× 取消关注</button>
             {@/if}
             </div>
             */
        },
        _tplAttention: function () {
            /*
             <li> <dl><dt><a href="/personalCenter/circle">${attentionCount}</a></dt><dd><a href="/personalCenter/circle">关注</a></dd></dl></li>
             <li><dl><dt><a href="/personalCenter/circle?type=1">${fansCount}</a></dt><dd><a href="/personalCenter/circle?type=1">粉丝</a></dd></dl></li>
             <li><dl><dt><a href="/personalCenter/publish">${talkingCount}</a></dt><dd><a href="/personalCenter/publish">Talking</a></dd></dl></li>
             */
        },
        templateEducation: function () {
            /*
             {@each education as item}
             <ul class="school-warp">
             <li class="colls">${item.education_campus_name}【${item.education_time}】
             <ul class="show-list">
             <li>${item.education_academe}</li>
             <li>${item.education_major}【${item.education_degree}】</li>
             <li>专业排名前${item.education_ranking}%</li>
             </ul>
             </li>
             </ul>
             {@/each}
             */
        },
        _tplTalking: function () {
            /*
             <!--
             {@helper getTime}
             function(time) {
             var oTime = new Date(time);
             return [oTime.getMonth()+1+"月",oTime.getDate()+"日"].join('');
             }
             {@/helper}
             {@helper getHour}
             function(time) {
             var oTime = new Date(time);
             function toNormal(num){
             if(num>10){
             return num;
             }else{
             return "0"+num;
             }
             }
             return [toNormal(oTime.getHours()),toNormal(oTime.getMinutes())].join(':');
             }
             {@/helper}
             -->
             {@each rows as item}
             <li class="personal-dec-item">
             <a href="/talking/classDetail?tk=${item.talking_id}" class="personal-dec-item-pic">
             <img src="${item.talking_main_picture}" alt="pic" onerror="this.src='/static/img/common/talking.png'"/>
             </a>
             <div class="personal-dec-panel">
             <a href="/talking/classDetail?tk=${item.talking_id}" class="inline-block">
             <h2 class="personal-dec-title">${item.talking_title}</h2>
             </a>
             <ul class="personal-dec-info-list">
             <li class="personal-dec-info-item">
             <i class="personal-pic inline-block"></i>
             <span> 已报${item.talking_now_persion}人/共${item.talking_max_persion}人</span>
             </li>
             <li class="personal-dec-info-item">
             <i class="time inline-block"></i>
             <span>${item.talking_start_time|getTime} ${item.talking_start_time|getHour}-${item.talking_end_time|getHour}</span>
             </li>
             <li class="personal-dec-info-item">
             <i class="address-pic inline-block "></i>
             <span>
             {@if item.talking_address==0}
             QQ/YY/微信<i class="inline-block style-way">线上</i>
             {@else}
             ${item.talking_address}<i class="inline-block style-way">线下</i>
             {@/if}
             </span>
             </li>

             </ul>
             </div>
             <ul class="class-info">
             <li class="class-info-price"><span>&yen;</span> <strong>${item.talking_price}</strong> </li>
             <li class="class-info-add">
             {@if item.talking_state == 100}
                <a href="#" class="inline-block" style="background-color:#dddddd;cursor:no-drop;">已过期</a>
             {@else}
                  {@if item.talking_now_persion<item.talking_max_persion}
                 <a href=/pay?tid=${item.talking_id} class="inline-block">立即参加</a>
                 {@else}
                 <a href="#" class="inline-block" style="background-color:#dddddd;cursor:no-drop;"> 已满员</a>
                 {@/if}  
             {@/if}
             </li>
             </ul>
             </li>
             {@/each}
             */
        },
        //获奖成果
        templateAwards: function () {
            /*
             {@each prize as item}
             <li><span>${item.prize_time}</span>${item.prize_title}</li>
             {@/each}
             */
        },
        //社团经历
        templateSocial: function () {
            /*
             {@each campusExperience as item}
             <div class="social-exp-wrap-list">
             <ul>
             <li class="wid">${item.campus_experience_time}</li>
             <li>${item.campus_experience_title}</li>
             <li>${item.campus_experience_role}</li>
             </ul>
             <p>${item.campus_experience_descript}</p>
             </div>
             {@/each}
             */
        },
        //工作经历
        templateWork: function () {
            /*
             {@each workExperience as item}
             <div class="work-exp-wrap-list">
             <ul>
             <li class="wid">${item.work_experience_time}</li>
             <li>${item.work_experience_place}</li>
             <li>${item.work_experience_role}</li>
             </ul>
             <p>${item.work_experience_descript}</p>
             </div>
             {@/each}
             */
        },
        _commonRender: function (url, param, dom, tpl, fn) {
            request.post(url, param, function (res) {
                res = JSON.parse(res);
                if (1 == res.code) {
                    var htmlT = juicer(tpl, res.data);
                    $(dom).html(htmlT);
                    fn && fn(res.data);
                }
            });
        },
        _render: function (userId) {
            user._commonRender(
                user._Api.getUserInfo,
                {userId: userId},
                '.personal-panel',
                funcTpl(user._tpl),
                function (data) {
                    if (!!data.user_student_check_result) {
                        $('.J_student_ident').removeClass('no-student-identify')
                            .addClass('identify-xueli');
                    }
                    user._addLove(userId);
                }
            );
        },
        _attention: function (userId) {
            user._commonRender(
                user._Api.getUserFans,
                {userId: userId},
                '.personal-about-list',
                funcTpl(user._tplAttention),
                function (data) {
                    user._fans = data.fansCount;
                }
            );
        },
        _talkOrOtherTalk: function (userId, page) {
            user._commonRender(
                user._currentListApi,
                {userId: userId, page: page, state: 10},
                '.personal-dec-list',
                funcTpl(user._tplTalking),
                function (data) {
                    $('body').trigger('page:set', [10, data.count])
                }
            );
        },
        _switch: function (user_id, fn) {
            $('.personal-care-tab-item').on('click', function (ev) {
                $(this).addClass('current-care-item').siblings().removeClass('current-care-item');
                if($(this).hasClass('getPublishTalk')){
                    user._currentListApi =user._Api.getPublishTalk;
                }else{
                    user._currentListApi =user._Api.getJoinTalk;
                }
                user._talkOrOtherTalk(user_id, 1);
                fn();
            });
        },
        _addLove: function (userId) {
            $('.personal-panel-aside-small').on('click', 'button', function () {
                var state = $(this).data('state');
                var hasState = (state != undefined);
                if (hasState) {
                    state = state == 0 ? 1 : 0;
                    request.post(user._Api.addLove, {userId: userId, attention: state}, function (res) {
                        res = JSON.parse(res) || eval(res);
                        if (1 == res.code) {
                            if (state) {
                                $('.personal-panel-aside-small').html(' <button class="cancel-care" data-state=1>× 取消关注</button>');
                                user._fans += 1;
                            } else {
                                $('.personal-panel-aside-small').html(' <button class="care" data-state=0>+ 关注</button>');
                                user._fans -= 1;
                            }
                            $('.personal-about-list li:eq(1) dt').html(user._fans);
                        } else {
                            pop();
                        }
                    });
                }
            });
        },
        _watchMenuBar: function (user_id) {//监听导航按钮
            var $currentTarget = $('#J_index');

            $('#J_user_index,#J_user_info,#J_user_comment').on('click', function () {
                $('.current-menu-item').removeClass('current-menu-item');//删除高亮

                $($(this).parent('.personal-menu-item')).addClass('current-menu-item');//添加高亮

                $currentTarget.addClass('hide');//隐藏之前显示

                //个人资料
                if (this.id == 'J_user_info') {
                    $currentTarget = $('#J_info');
                    $currentTarget.removeClass('hide');

                    if (!$currentTarget.hasClass('has')) {
                        $currentTarget.addClass('has');
                        user._showPersonalInfo($(".school-warp"), $(".wrap-award"), $(".social-exp-wrap"), $(".work-exp-wrap"), user_id);
                    }
                }

                //个人首页
                if (this.id == 'J_user_index') {
                    $currentTarget = $('#J_index');
                    $currentTarget.removeClass('hide');
                }

                //个人评价
                if (this.id == 'J_user_comment') {
                    $currentTarget = $('#J_comment');
                    $currentTarget.removeClass('hide');
                    user._showComment($('.talking'), user_id, 0);

                }
            })
        },
        //显示评论
        _showComment: function ($ele, user_id, page) {
            request.get(user._Api.getComment, {"userId": user_id, "page": page}, function (res) {
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
                             <h1>Talking 主题：${item.talking_title}</h1>
                             <p class="talking-comment-content">${item.talking_comment_content}</p>
                             <div class="comment-time">${item.talking_comment_time|formDate}</div>
                             </div>
                             <div class="bottom-line"></div>
                             </li>
                             {@/each}
                             */
                        };
                        //循环输出星星
                        var showstar = function (num) {
                            var child = '<li><img src="/static/img/page/personal-page/star1.png"></li>';
                            var child_gray = '<li><img src="/static/img/page/personal-page/star-gray.png"></li>';
                            var html = "";
                            for (var i = 0; i < num; i++) {
                                html += child;
                            }
                            if (num < 5) {
                                for (var j = 0; j < 5 - num; j++) {
                                    html += child_gray;
                                }
                            }
                            return html;
                        };
                        if (count != 0) {
                            $(".talking-bar span").text(count);
                        } else {
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
                        juicer.register("showstar", showstar);
                        var temp = juicer(funcTpl(tmpList), res.data);
                        $ele.html(temp);
                        $(".talking li:last-child .bottom-line").css("display", "none");
                        //触发翻页初始化
                        debugger
                        $('body').trigger('page:set', [10, count]);
                    }
                }
            );
        },
        //读取用户的简历
        _showPersonalInfo: function ($eleedu, $eleaward, $elesocial, $elework, user_id) {
            request.get("../user/getUserResume", {userId: user_id}, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    //教育经历
                    var tmpPersonalInfo = juicer(funcTpl(user.templateEducation), res.data);
                    $eleedu.html(tmpPersonalInfo);
                    //获奖成果
                    var tmpAwards = juicer(funcTpl(user.templateAwards), res.data);
                    $eleaward.html(tmpAwards);
                    //社团经历
                    var temSocial = juicer(funcTpl(user.templateSocial), res.data);
                    $elesocial.html(temSocial);
                    //工作经历
                    var temWork = juicer(funcTpl(user.templateWork), res.data);
                    $elework.html(temWork);
                    timeLine.setTimeLine();
                }
            });
        }
    };

    return user;
});