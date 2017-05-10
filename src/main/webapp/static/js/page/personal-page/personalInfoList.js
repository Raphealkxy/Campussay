/**
 * Created by zhujing on 2016/1/18.
 * 
 */

require.config({
    baseUrl: CP.STATIC_ROOT,
    paths : {
        "time-line" : "page/personal-page/time-line"
    }
});
require(["lib/jquery","util/request","time-line","util/funcTpl","modules/popup/signPop","lib/juicer",'modules/baseMoudle'],function($,request,timeLine,funcTpl,pop) {
    var personalInfo = {
        init: function () {
             user_id = personalInfo._param().user;//获取传参
            this._setScroll();
            this._showPersonalInfo($(".school-warp"),$(".wrap-award"),$(".social-exp-wrap"),$(".work-exp-wrap"),user_id);
            personalInfo._render();
            timeLine.setTimeLine();
            personalInfo._forward();
        },
        //导航栏随页面滚动
        _setScroll : function () {
            var scroller_anchor = $("#navi").offset().top;
            $(window).scroll(function() {
                    if ($(this).scrollTop() >= scroller_anchor && $('#navi').css('position') != 'fixed') {
                        $('#navi').css({'position': 'fixed','top': '0',"left": $("#navi").offset().left});
                    }
                if ($(this).scrollTop() < scroller_anchor && $('#navi').css('position') != 'relative') {
                        $("#navi").css({"position": 'relative', "top": 0,"left":0});
                    }
            });
        },
        _Api:{//接口信息
            getUserInfo:'/user/getBasicInfo',
            getUserFans:'/user/getAttentionCount',
            getPublishTalk:'/talking/getUserTalking',
            getJoinTalk:'/talking/getUserBuyTalking',
            addLove:'/user/attentionUser'
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
        //获奖成果
        templateAwards : function(){
            /*
            {@each prize as item}
             <li><span>${item.prize_time}</span>${prize_title}</li>
            {@/each}
            */
        },
        //社团经历
        templateSocial : function () {
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
        templateWork : function () {
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
        //添加关注
        _addLove: function (userId) {
            $('.personal-panel-aside-small').on('click','button', function () {
                var state = $(this).data('state');
                var hasState = (state != undefined);
                if(hasState){
                    state = state==0?1:0;
                    request.post(personalInfo._Api.addLove,{userId:userId,attention:state}, function (res) {
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
        _render: function () {
            personalInfo._commonRender(personalInfo._Api.getUserInfo, {userId:user_id}, '.personal-panel', funcTpl(personalInfo._tpl), function (data) {
                    //if(!!data.user_student_check_result){
                    //    $('.J_student_ident').removeClass('no-student-identify').addClass('student-identify');
                    //}
                    personalInfo._addLove(user_id);
                }
            );
        },
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
        //读取用户的简历
        _showPersonalInfo: function ($eleedu,$eleaward,$elesocial,$elework,user_id) {
            request.get("../user/getUserResume",{userId:user_id},function (res) {
                res = JSON.parse(res);
                if(res.code == 1){
                    //教育经历
                    var tmpPersonalInfo = juicer(funcTpl(personalInfo.templateEducation),res.data);
                    $eleedu.html(tmpPersonalInfo);
                    //获奖成果
                    var tmpAwards  = juicer(funcTpl(personalInfo.templateAwards),res.data);
                    $eleaward.html(tmpAwards);
                    //社团经历
                    var temSocial = juicer(funcTpl(personalInfo.templateSocial),res.data);
                    $elesocial.html(temSocial);
                    //工作经历
                    var temWork = juicer(funcTpl(personalInfo.templateWork),res.data);
                    $elework.html(temWork);
                    timeLine.setTimeLine();

                }
            });
        },
        //跳转页面
        //跳页
        _forward : function () {
            $(".J-comment").on("click",function (e) {
                window.location.href = "/user/personal-comment?user="+user_id;
            });
            $(".J-per").on("click", function () {
                window.location.href = "/user/personalIndex?user="+user_id;
            });
        }

    };
    return personalInfo.init();
});