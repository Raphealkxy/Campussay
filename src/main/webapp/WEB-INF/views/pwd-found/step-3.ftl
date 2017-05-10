<content tag="title">手机验证</content>
<content tag="css">/pwd-found/step-3</content>
<content tag="javascript">/pwd-found/step</content>
<div class="global-center">
    <!--logo和标题 -->
    <a href="/"><img class="pwd-found-logo" src="/static/img/page/pwd_found/logo.png" alt="校园说logo"></a>
    <h2 class="pwd-found-title">找回密码</h2>
    <div class="pwd_found">
        <!--进度条 -->
        <ul class="progress-bar">
            <li>输入验证信息</li>
            <li class="progress-bar_center">重置信息</li>
            <li>立即登录</li>
        </ul>
        <!--手机验证码表单 -->
        <form autocomplete="off">
            <h3>请验证你的手机</h3>
            <p>已验证手机号 : <span></span></p>
            <div class="test-phone-num">
                <label for="test-phone-text">手机验证码</label>
                <div class="get-validate">发送验证码(<span>60</span>s)</div>
                <input type="text" id="test-phone-text" >
                <p>短信已发送，请输入短信中的验证码</p>
            </div>
            <div class="new-pwd">
                <label class="new-pwd-1" for="new-pwd-1"  >新密码</label>
                <input style="display:none"><!-- for disable autocomplete on chrome -->
                <input type="password" id="new-pwd-1" placeholder="6-20个数字或英文字母" maxlength="20" autocomplete="off" />
                <label class="new-pwd-2" for="new-pwd-2" >确认密码</label>
                <input type="password" id="new-pwd-2" placeholder="再次输入你的密码" maxlength="20" autocomplete="off" />
                <span style="margin-left: 90px;margin-top: 10px;display: block;color:#f06060;height: 12px"></span>
            </div>
            <input type="submit" value="重置密码" id="test-phone">
        </form>
    </div>
</div>