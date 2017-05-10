<content tag="title">个人设置-校园说-交流干货,分享人生</content>
<content tag="css">/personal-setting/security-setting</content>
<content tag="javascript">/personal-setting/security-setting</content>

<!-- 个人中心通用头部导航 -->
<div class="personal-main-nav">
    <!-- personal-center/common/header.js render here -->
</div>
<!-- 个人中心通用头部导航结束 -->

<div class="security-setting global-center clearfix">
    <!-- 左侧菜单栏 -->
    <div class="personal-aside">
        <div class="personal-aside-item personal-info"></div>
        <div></div>
    </div>
    <!-- 左侧菜单栏结束 -->

    <!-- 个人中心主要内容 -->
    <div class="personal-content security-setting-content clearfix">
        <!-- 菜单切换列表-->
        <ul class="personal-content-tab security-setting-tab clearfix">
            <li class="tab-item"><a href="/personalSetting/person#basic">个人设置</a></li>
            <li class="tab-item"><a href="/personalSetting/security">安全设置</a></li>
            <li class="tab-item"><a href="/personalSetting/authentication">认证设置</a></li>
        </ul>
        <!-- 详细列表 -->
        <div class="security-setting-content-list">
            <div class="security-setting-list-item">
                <div style="margin-top: 34px;" class="f24">暂未开放，敬请期待...</div>
                <!--<div class="item-content" id="label-1">
                    <div class="item-binding-phone binding-done" style="display: none;">
                        <div class="item-title">
                            <i class="icon icon-phone done"></i>
                            <span class="item-title-main">已绑定手机号</span>
                            <span class="item-title-sub">156****1945</span>
                            <span class="item-cancel-binding"><a href="javascript:;">解除绑定</a></span>
                        </div>
                        <div class="item-body">
                            <div class="item-binding binding-phone">
                                <label>当前绑定手机号：</label>
                                <span class="binding-phone-number">156****1945</span>
                            </div>
                            <div class="item-binding-code binding-phone-code">
                                <label>手机验证码</label>
                                <input type="text" name="phone-code" class="item-binding-input">
                                <a href="javascript:;" class="get-phone-code">获取验证码<span class="countdown">(60s)</span></a>
                            </div>
                            <div class="item-binding-tip">短信已发送，请输入短信中的验证码</div>
                            <div class="item-binding-operation">
                                <a href="javascript:;" class="operation next-step">下一步</a>
                                <a href="javascript:;" class="cancel-binding">取消</a>
                            </div>
                        </div>
                    </div>
                    <div class="item-binding-phone binding-todo">
                        <div class="item-title">
                            <i class="icon icon-phone done"></i>
                            <span class="item-title-main">绑定手机号</span>
                            <span class="item-title-sub">绑定手机更安全，更方便</span>
                        </div>
                        <div class="item-body">
                            <div class="item-binding binding-phone">
                                <input type="text" name="phone" placeholder="输入您的手机号" class="item-binding-input">
                                <a href="javascript:;">绑定手机并发送验证码 &gt;</a>
                            </div>
                            <div class="item-binding-code binding-phone-code">
                                <input type="text" name="phone-code" placeholder="请输入手机验证码" class="item-binding-input">
                                <a href="javascript:;">立即激活</a>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="item-content" id="label-2">
                    <div class="item-binding-mail binding-done" style="display: none;">

                    </div>
                    <div class="item-binding-mail binding-todo">
                        <div class="item-title">
                            <i class="icon icon-mail done"></i>
                            <span class="item-title-main">绑定邮箱账号</span>
                            <span class="item-title-sub">绑定邮箱更安全，更方便</span>
                            <span class="item-cancel-binding"><a href="javascript:;">解除绑定</a></span>
                        </div>
                        <div class="item-body">
                            <div class="item-binding binding-mail">
                                <input type="text" name="mail" placeholder="输入您的邮箱号" class="item-binding-input">
                                <a href="javascript:;">绑定邮箱并发送验证码 &gt;</a>
                            </div>
                            <div class="item-binding-code binding-mail-code">
                                <input type="text" name="mail-code" placeholder="请输入邮箱验证码" class="item-binding-input">
                                <a href="javascript:;">立即激活</a>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="item-content" id="label-3">
                    <div class="item-binding-qq binding-done" style="display: none;">
                    </div>
                    <div class="item-binding-qq binding-todo">
                        <div class="item-title">
                            <i class="icon icon-qq"></i>
                            <span class="item-title-main">绑定QQ账号</span>
                            <span class="item-title-sub">绑定QQ更安全，更方便</span>
                            <span class="item-cancel-binding"><a href="javascript:;">解除绑定</a></span>
                        </div>
                    </div>
                </div>

                <div class="item-content" id="label-4">
                    <div class="item-binding-password binding-done">
                        <div class="item-title">
                            <i class="icon icon-password done"></i>
                            <span class="item-title-main">找回密码或修改</span>
                            <span></span>
                        </div>
                        <div class="item-body">
                            <div class="item-modify-password old-password">
                                <label>当前登录密码</label>
                                <input type="password" name="old-password" class="item-binding-input">
                                <a href="javascript:;">找回登录密码</a>
                            </div>
                            <div class="item-modify-password new-password">
                                <label>新密码</label>
                                <input type="password" name="new-password" class="item-binding-input">
                            </div>
                            <div class="item-modify-password confirm-new-password">
                                <label>确认密码</label>
                                <input type="password" name="confirm-new-password" class="item-binding-input">
                            </div>
                            <div class="item-binding-operation">
                                <a href="javascript:;" class="operation save">保存</a>
                                <a href="javascript:;" class="cancel-binding">取消</a>
                            </div>
                        </div>
                    </div>
                </div>

            </div>-->

        </div>
    </div>
    <!-- 个人中心主要内容结束 -->
</div>