<content tag="title">输入验证消息</content>
<content tag="css">/pwd-found/step_1</content>
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
        <!--填写邮箱手机号和验证码的表单 -->
        <form>
            <h3>请填写你需要找回的账号信息</h3>
            <div class="mail-or-phone">
                <label for="mail-or-phone">邮箱/手机号</label>
                <input type="text" id="mail-or-phone" name="account" class="" placeholder="输入账号使用的邮箱或手机号码">
                <span style="color: #f06060"></span>
            </div>
            <div class="validate">
                <label for="validate">验证码</label>
                <input type="text" id="validate" name="validate-num"  placeholder="验证码">
                <img src="../../code/getAuthCode?type=step1" alt="验证码">
                <i><a href="#"><img src="/static/img/page/pwd_found/wait-icon.png" alt="刷新" title="刷新"></a></i>
            </div>
            <input type="submit" value="下一步" id="next-step">
        </form>
    </div>
</div>

