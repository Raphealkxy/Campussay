<content tag="title">个人首页-校园说-交流干货,分享人生</content>
<content tag="css">/personal-index</content>
<content tag="javascript">/personal-index</content>

<!--个人信息面板-->
<div class="personal-panel global-center clearfix">
    <!--<ul class="personal-panel-aside-big">
        <li class="personal-panel-item">
            <img src="/static/img/page/personal-index/header.png" alt="header"/>
        </li>
        <li class="personal-panel-item person-panel-info">
            <h2 class="personal-panel-title">
                <span class="personal-panel-name">刘智鹏</span>
                <span class="personal-panel-tip inline-block">初级学霸</span>
                <span class="personal-panel-title-name"></span>
            </h2>

            <p class="personal-panel-student">重庆邮电大学 | 计算机学院</p>

            <p>擅长领域 : 产品设计、互联网、数据挖掘</p>
        </li>
        <li class="personal-panel-item personal-panel-dec">
            <p>
                简介：“重庆你好勤奋蜂科技”创始人， 曾被邀请参加“北京大学”创业精英班培 训，被“华中科技大学”创业组导师邀请 至创业PAR ”
            </p>
        </li>
    </ul>
    <div class="personal-panel-aside-small">
        <button class="care">
            关注
        </button>
    </div>-->
</div>
<!--个人相关信息菜单tab-bar-->
<ul class="personal-menu-bar global-center clearfix">
    <li class="personal-menu-item current-menu-item"><a href="javascript:;"  id="J_user_index">主页</a></li>
    <li class="personal-menu-item"><a href="javascript:;"  id="J_user_info">个人资料</a></li>
    <li class="personal-menu-item"><a href="javascript:;"  id="J_user_comment">评价</a></li>
</ul>
<!--展示区-->
<div id="J_index" class="personal-content global-center clearfix">
    <!-- talking列表-->
    <div class="personal-content-care">
        <!-- 菜单切换列表-->
        <ul class="personal-care-tab clearfix">
            <li class="personal-care-tab-item current-care-item getPublishTalk">TA发布的Talking</li>
            <li class="personal-care-tab-item">TA参与的Talking</li>
        </ul>
        <!-- 详细列表-->
        <ul class="personal-dec-list clearfix">
        </ul>
        <!-- 翻页-->
        <div id="page"></div>
    </div>
    <!-- 个人相关-->
    <div class="personal-content-care-aside">
        <ul class="personal-about-list clearfix">
        </ul>
        <ul class="personal-identify-list">
            <li class="personal-identify-item">
                <h3>
                    <i class="inline-block v-line"></i>
                    <span class="inline-block">身份认证</span>
                </h3>
                <ul class="personal-identify-info-list clearfix">
                    <li class="personal-identify-info-item hide">
                        <dl>
                            <dt>
                                <i class="inline-block identify-pic identify-xueli"></i>
                            </dt>
                            <dd>学历认证</dd>
                        </dl>
                    </li>
                    <li class="personal-identify-info-item">
                        <dl>
                            <dt>
                                <i class="inline-block identify-pic no-student-identify J_student_ident"></i>
                            </dt>
                            <dd>学生证认证</dd>
                        </dl>
                    </li>
                    <li class="personal-identify-info-item hide">
                        <dl>
                            <dt>
                                <i class="inline-block identify-pic identify"></i>
                            </dt>
                            <dd>身份认证</dd>
                        </dl>
                    </li>
                </ul>
            </li>
            <!--<li class="personal-identify-item">
                <h3>
                    <i class="inline-block v-line"></i>
                    <span class="inline-block">技能认证</span>
                </h3>
                <ul class="skill-list clearfix">
                    <li class="skill-item">CET-6</li>
                    <li class="skill-item">BBC中级</li>
                    <li class="skill-item">计算机二级C++</li>
                </ul>
            </li >-->
        </ul>
    </div>
</div>

<!-- 个人资料-->
<div class="container hide" id="J_info">
    <div class="resume">
        <ul class="nav-content">
            <li class="nav-title" id="li-edu">
                <div class="list-title">
                    <img class="set-pos" src="/static/img/page/personal-page/education.png">
                    <span>教育经历</span>
                    <img src="/static/img/page/personal-page/xuxian.png">
                </div>
                <ul class="school-warp">
                </ul>
            </li>
            <li class="nav-title" id="li-award">
                <div class="list-title">
                    <img class="set-pos" src="/static/img/page/personal-page/Awards.png">
                    <span>获奖成果</span>
                    <img src="/static/img/page/personal-page/xuxian.png">
                </div>
                <div class="awardz-warp">
                    <div class="line-award"></div>
                    <ul class="wrap-award">
                    </ul>
                </div>
            </li>
            <li class="nav-title" id="li-social">
                <div class="list-title">
                    <img class="set-pos" src="/static/img/page/personal-page/social.png">
                    <span>社团经历</span>
                    <img src="/static/img/page/personal-page/xuxian.png">
                </div>
                <div class="social-exp">
                    <div class="line"></div>
                    <div class="social-exp-wrap">
                    </div>
                </div>
            </li>
            <li class="nav-title" id="li-work">
                <div class="list-title">
                    <img class="set-pos" src="/static/img/page/personal-page/work_experience.png">
                    <span>工作经验</span>
                    <img src="/static/img/page/personal-page/xuxian.png">
                </div>
                <div class="work-exp">
                    <div class="line-work"></div>
                    <div class="work-exp-wrap">
                    </div>
                </div>
            </li>
        </ul>
    </div>
    <ul id="navi">
        <li><a href="#li-edu"><img src="/static/img/page/personal-page/bookmark.png">教育经历</a></li>
        <li><a href="#li-award" ><img src="/static/img/page/personal-page/star-gray.png">获奖成果</a></li>
        <li><a href="#li-social"><img src="/static/img/page/personal-page/flag.png">社团经历</a></li>
        <li><a href="#li-work"><img src="/static/img/page/personal-page/box.png">工作经验</a></li>
    </ul>
</div>

<!-- 评论-->
<div class="personal-content global-center hide" id="J_comment">
    <ul class="talking-bar clearfix">
        <li>全部评价（<span></span>）</li>
    </ul>


    <!--展示页-Talking主题-->
    <ul class="talking">
        <li class="talking-container">
        <div class="selfImg-list">
        <img src="/static/img/page/personal-page/icon-dog.png">
        <div class="show-name">董海亮</div>
        </div>
        <div class="talking-show">
        <h1>Talking主题：如何做好未来职业规划</h1>
        <div class="cstar">
        <div>评价分数</div>
        <ul>
        <li><img src="/static/img/page/personal-page/star-gray.png"></li>
        </ul>
        </div>
        <p>学长人很好交流，干货很多，也让我对自己的人生定位和规划有了进一步的目标，学长人很好交流，干货很多，也让我对自己的人生定位和规划有了进一步的目标学长人很好交流，干货很多，也让我对自己的人生定位和规划有了进一步的目标，学长人很好交流，干货很多</p>
        <div class="dd">2015-11-03</div>
        </div>
        <div class="bottom-line"></div>
        </li>
    </ul>
    <!-- 翻页-->
    <div id="page1"></div>
</div>