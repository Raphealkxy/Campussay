<content tag="title">个人设置-校园说-交流干货,分享人生</content>
<content tag="css">/personal-setting/person-setting</content>
<content tag="javascript">/personal-setting/person-setting</content>

<!-- 个人中心通用头部导航 -->
<div class="personal-main-nav">
    <!-- personal-center/common/header.js render here -->
</div>
<!-- 个人中心通用头部导航结束 -->

<div class="person-setting global-center clearfix">
    <!-- 左侧菜单栏 -->
    <div class="personal-aside">
        <div class="personal-aside-item personal-info"></div>
        <div></div>
    </div>
    <!-- 左侧菜单栏结束 -->

    <!-- 个人中心主要内容 -->
    <div class="personal-content person-setting-content clearfix">
        <!-- 菜单切换列表-->
        <ul class="personal-content-tab security-setting-tab clearfix">
            <li class="tab-item"><a href="/personalSetting/person#basic">个人设置</a></li>
            <li class="tab-item"><a href="/personalSetting/security">安全设置</a></li>
            <li class="tab-item"><a href="/personalSetting/authentication">认证设置</a></li>
        </ul>
        <!-- 详细列表 -->
        <div class="person-setting-content-list">
            <div class="person-setting-list-item">

                <div class="item-content item-basic">
                    <div class="item-title">
                        <i class="icon icon-big-basic selected"></i>
                        <span class="item-title-main f20">基本信息</span>
                        <span class="item-title-sub">填写基本信息，让大家都看好你</span>
                    </div>
                    <div class="item-body" data-label="basic">
                        <div class="item item-real-name">
                            <label><span> * </span>姓名：</label>
                            <input type="text" name="real-name" class="item-input item-text-input">
                            <span>请填写真实姓名，方便联系哦</span>
                        </div>
                        <div class="item item-title-name">
                            <label><span> * </span>头衔：</label>
                            <input type="text" name="title" class="item-input item-text-input" placeholder="如：XX实验室负责人">
                            <span></span>
                        </div>
                        <div class="item item-school">
                            <label><span> * </span>学校：</label>
                            <div class="select select-school">
                                <input type="text" name="school" class="item-input item-select-input" readonly>
                                <ul class="select-content"></ul>
                            </div>
                        </div>
                        <div class="item item-institute">
                            <label><span> * </span>学院及专业：</label>
                            <div class="select select-institute">
                                <input type="text" name="institute" class="item-input item-select-input" style="background:none">
                                <ul class="select-content"></ul>
                            </div>
                            <div class="select select-major">
                                <input type="text" name="major" class="item-input item-select-input" style="background:none">
                                <ul class="select-content"></ul>
                            </div>
                        </div>
                        <div class="item item-sexual">
                            <label><span> * </span>性别：</label>
                            <div class="radio radio-sexual">
                                <input id="female" type="radio" name="radio" value="0" >
                                <span class="radio-outer"> <span class="radio-inner"> </span> </span> 女
                            </div>
                            <div class="radio radio-sexual">
                                <input id="male" type="radio" name="radio" value="1">
                                <span class="radio-outer"> <span class="radio-inner"> </span> </span> 男
                            </div>
                        </div>
                        <div class="item item-good-field">
                            <label>擅长领域：</label>
                            <div class="select select-good-field-main">
                                <input type="text" name="good-field-main" class="item-input item-select-input" readonly>
                                <ul class="select-content">
                                </ul>
                            </div>
                            <div class="select select-good-field-sub">
                                <input type="text" name="good-field-sub" class="item-input item-select-input" readonly>
                                <ul class="select-content">
                                </ul>
                            </div>
                            <div class="select select-sub-info">
                                <span class="left">限十个领域标签</span>
                                <span class="right add-tag"><a href="javascript:;">添加</a> </span>
                            </div>
                            <div class="select-result">
                                <div class="tag">
                                </div>
                            </div>
                        </div>
                        <div class="item item-care-field">
                            <label>关注领域：</label>
                            <div class="select select-care-field-main">
                                <input type="text" name="care-field-main" class="item-input item-select-input" readonly>
                                <ul class="select-content">
                                </ul>
                            </div>
                            <div class="select select-care-field-sub">
                                <input type="text" name="care-field-sub" class="item-input item-select-input" readonly>
                                <ul class="select-content">
                                </ul>
                            </div>
                            <div class="select select-sub-info">
                                <span class="left"></span>
                                <span class="right add-tag"><a href="javascript:;">添加</a> </span>
                            </div>
                            <div class="select-result">
                                <div class="tag"></div>
                            </div>
                        </div>
                        <div class="item item-desc-self">
                            <label style="vertical-align:top">自我介绍：</label>
                            <textarea class="select-result" placeholder="如：数一大神149" style="margin:0"></textarea>
                        </div>
                        <div class="item item-operation">
                            <label></label>
                            <a href="javascript:;" class="operation save">保存</a>
                            <a href="javascript:;" class="cancel-binding">取消</a>
                        </div>
                    </div>
                </div>

                <div class="item-content item-skill">
                    <div class="item-title">
                        <i class="icon icon-info selected"></i>
                        <span class="item-title-main f20">技能证书</span>
                        <span class="item-title-sub">技能证书能更好的展示你的实力</span>
                    </div>
                    <div class="item-body" data-label="skill">
                        <!--<div class="item-body-con">
                            <div class="item item-name">
                                <label>名称：</label>
                                <input type="text" name="certification-name" class="item-input item-text-input" readonly>
                                <div class="select select-sub-info">
                                    <span class="left"><a href="javascript:;">添加图片</a> </span>
                                </div>
                            </div>
                        </div>
                        <span class="add"><a href="javascript:;">添加</a></span>
                        <div class="item item-operation">
                            <label></label>
                            <a href="javascript:;" class="operation save">保存</a>
                            <a href="javascript:;" class="cancel-binding">取消</a>
                        </div>-->
                        <div style="margin-top: 34px;" class="f24">暂未开放，敬请期待...</div>
                    </div>
                </div>

                <div class="item-content item-education">
                    <div class="item-title">
                        <i class="icon icon-big-education selected"></i>
                        <span class="item-title-main f20">教育经历</span>
                        <span class="item-title-sub">说说自己来自哪里，找到校友更容易</span>
                    </div>
                    <div class="item-body" data-label="education">
                        <span class="add"><a href="javascript:;">添加</a></span>
                        <div class="item item-operation">
                            <label></label>
                            <a href="javascript:;" class="operation save">保存</a>
                            <a href="javascript:;" class="cancel-binding">取消</a>
                        </div>
                    </div>
                </div>

                <div class="item-content item-awards">
                    <div class="item-title">
                        <i class="icon icon-big-awards selected"></i>
                        <span class="item-title-main f20">获奖成果</span>
                        <span class="item-title-sub">讲讲自己都有哪些厉害的经历吧</span>
                    </div>
                    <div class="item-body" data-label="awards">
                        <span class="add"><a href="javascript:;">添加</a></span>
                        <div class="item item-operation">
                            <label></label>
                            <a href="javascript:;" class="operation save">保存</a>
                            <a href="javascript:;" class="cancel-binding">取消</a>
                        </div>
                    </div>
                </div>

                <div class="item-content item-practice">
                    <div class="item-title">
                        <i class="icon icon-big-practice selected"></i>
                        <span class="item-title-main f20">实践经历</span>
                        <span class="item-title-sub">实战很重要，让你更具吸引力</span>
                    </div>
                    <div class="item-body" data-label="practice">
                        <span class="add"><a href="javascript:;">添加</a></span>
                        <div class="item item-operation">
                            <label></label>
                            <a href="javascript:;" class="operation save">保存</a>
                            <a href="javascript:;" class="cancel-binding">取消</a>
                        </div>
                    </div>
                </div>

                <div class="item-content item-job">
                    <div class="item-title">
                        <i class="icon icon-big-job selected"></i>
                        <span class="item-title-main f20">工作经验</span>
                        <span class="item-title-sub">分享工作经历体会，职场达人就是你</span>
                    </div>
                    <div class="item-body" data-label="job">
                        <span class="add"><a href="javascript:;">添加</a></span>
                        <div class="item item-operation">
                            <label></label>
                            <a href="javascript:;" class="operation save">保存</a>
                            <a href="javascript:;" class="cancel-binding">取消</a>
                        </div>
                    </div>
                </div>

            </div>

        </div>
    </div>
    <!-- 个人中心主要内容结束 -->
</div>