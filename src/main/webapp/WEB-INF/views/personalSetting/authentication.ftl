<content tag="title">个人设置-校园说-交流干货,分享人生</content>
<content tag="css">/personal-setting/authentication-setting</content>
<content tag="javascript">/personal-setting/authentication-setting</content>

<!-- 个人中心通用头部导航 -->
<div class="personal-main-nav">
    <!-- personal-center/common/header.js render here -->
</div>
<!-- 个人中心通用头部导航结束 -->

<div class="authentication-setting global-center clearfix">
    <!-- 左侧菜单栏 -->
    <div class="personal-aside">
        <div class="personal-aside-item personal-info"></div>
        <div></div>
    </div>
    <!-- 左侧菜单栏结束 -->

    <!-- 个人中心主要内容 -->
    <div class="personal-content authentication-setting-content clearfix">
        <!-- 菜单切换列表-->
        <ul class="personal-content-tab security-setting-tab clearfix">
            <li class="tab-item"><a href="/personalSetting/person#basic">个人设置</a></li>
            <li class="tab-item"><a href="/personalSetting/security">安全设置</a></li>
            <li class="tab-item"><a href="/personalSetting/authentication">认证设置</a></li>
        </ul>
        <!-- 详细列表 -->
        <div class="authentication-setting-content-list">
            <div class="authentication-setting-list-item">

                <div class="item-content" id="label-1">
                    <div class="item-binding-identity-card binding-done" style="display: none;">
                        <div class="item-title">
                            <i class="icon icon-user-identity-done"></i>
                            <span class="item-title-main">已认证身份证</span>
                            <span class="item-title-sub">5116201992****7273</span>
                            <span class="item-cancel-binding"><a href="javascript:;">修改</a></span>
                        </div>
                    </div>
                    <div class="item-binding-identity-card binding-todo" style="display: none;">
                        <div class="item-title">
                            <i class="icon icon-user-identity"></i>
                            <span class="item-title-main">身份证认证</span>
                            <span class="item-title-sub">通过身份认证，才能进行线下交流和发布Talking哦</span>
                        </div>
                        <div class="item-body">
                            <div class="item item-upload-img">
                                <button class="select-img-btn"></button>
                                <div class="upload-info">
                                    <i class="icon-warn"></i>
                                    <span>身份证照片或扫描件</span>
                                </div>
                            </div>
                            <div class="item item-operation">
                                <a href="javascript:;" class="operation upload">确定</a>
                                <a href="javascript:;" class="cancel-binding">取消</a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="item-content" id="label-2">
                    <div class="item-binding-student-card binding-done" style="display: none;">
                        <div class="item-title">
                            <i class="icon icon-student-card-done"></i>
                            <span class="item-title-main">已认证学生证</span>
                            <span class="item-title-sub">重庆大学</span>
                            <span class="item-modify-binding"><a href="javascript:;">修改</a></span>
                        </div>
                    </div>
                    <div class="item-binding-student-card binding-todo">
                        <div class="item-title">
                            <i class="icon icon-student-card"></i>
                            <span class="item-title-main">学生证认证</span>
                            <span class="item-title-sub">通过学生证认证，能吸引更多朋友</span>
                        </div>
                        <div class="item-body">
                            <div class="item item-upload-img">
                                <a class="select-img-btn student-card-upload" data-file="student-card-upload">
                                    <img src="" class="preview" width="148" height="94">
                                    <i class="icon-upload"></i>
                                </a>
                                <input type="file" name="morePicMainFile" id="student-card-upload" style="display:none" value=""/>
                                <div class="upload-info">
                                    <i class="icon-warn"></i>
                                    <span>学生证照片或扫描件</span>
                                </div>
                            </div>
                            <div class="item item-operation">
                                <a href="javascript:;" class="operation upload">确定</a>
                                <a href="javascript:;" class="cancel-binding">取消</a>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="item-content" id="label-3">
                    <div class="item-binding-degree binding-done" style="display: none;">
                        <div class="item-title">
                            <i class="icon icon-degree-done"></i>
                            <span class="item-title-main">已认证学历</span>
                            <span class="item-title-sub">重庆邮电大学管理学学士学位</span>
                            <span class="item-modify-binding"><a href="javascript:;">修改</a></span>
                        </div>
                    </div>
                    <div class="item-binding-degree binding-todo" style="display: none;">
                        <div class="item-title">
                            <i class="icon icon-student-card"></i>
                            <span class="item-title-main">学历认证</span>
                            <span class="item-title-sub">添加学历认证，显得更加靠谱</span>
                            <span class="item-modify-binding"><a href="javascript:;">添加</a> </span>
                        </div>
                        <div class="item-body">
                            <div class="item item-upload-img">
                                <button class="select-img-btn"></button>
                                <div class="upload-info">
                                    <i class="icon-warn"></i>
                                    <span>学历照片或扫描件</span>
                                </div>
                            </div>
                            <div class="item item-operation">
                                <a href="javascript:;" class="operation upload">确定</a>
                                <a href="javascript:;" class="cancel-binding">取消</a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="item-content" id="label-4">
                    <div class="item-binding-skill binding-done" style="display: none;">
                        <div class="item-title">
                            <i class="icon icon-skill-done"></i>
                            <span class="item-title-main">已认证技能</span>
                            <span class="item-title-sub">CET6</span>
                            <span class="item-cancel-binding"><a href="javascript:;">修改</a></span>
                        </div>
                    </div>
                    <div class="item-binding-skill binding-todo" style="display: none;">
                        <div class="item-title">
                            <i class="icon icon-student-card"></i>
                            <span class="item-title-main">技能证书认证</span>
                            <span class="item-title-sub">添加资格认证，展示大牛实力</span>
                            <span class="item-modify-binding"><a href="javascript:;">添加</a> </span>
                        </div>
                        <div class="item-body">
                            <div class="item item-upload-img">
                                <button class="select-img-btn"></button>
                                <div class="upload-info">
                                    <i class="icon-warn"></i>
                                    <span>资格证书照片或扫描件</span>
                                </div>
                            </div>
                            <div class="item item-operation">
                                <a href="javascript:;" class="operation upload">确定</a>
                                <a href="javascript:;" class="cancel-binding">取消</a>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

        </div>
    </div>
    <!-- 个人中心主要内容结束 -->
</div>