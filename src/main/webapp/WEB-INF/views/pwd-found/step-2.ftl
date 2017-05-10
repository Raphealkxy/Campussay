<content tag="title">邮箱验证</content>
<content tag="css">/pwd-found/step-2</content>
<content tag="javascript">/sign-in/sign-in</content>
<div class="global-center">
    <!--logo和标题 -->
    <img class="pwd-found-logo  " src="/static/img/page/pwd_found/logo.png" alt="校园说logo">
    <h2 class="pwd-found-title">找回密码</h2>
    <div class="pwd_found">
        <!--进度条 -->
        <ul class="progress-bar">
            <li>输入验证信息</li>
            <li class="progress-bar_center">重置信息</li>
            <li>立即登录</li>
        </ul>
        <!--邮箱验证码表单 -->
        <form>
            <h3>请验证你的邮箱</h3>
            <p>已向你的邮箱<span>693947122@qq.com</span>发送了一封验证邮箱。</p>
            <p>请将邮件中的6位验证码输入下方的输入框内，完成邮箱认证。</p>
            <div class="test-mail-num">
                <label for="test-mail-text">邮箱验证码</label>
                <input type="text" id="test-mail-text" >
                <a href="#">去邮箱查阅></a>
            </div>
            <div class="new-pwd">
                <label class="new-pwd-1" for="new-pwd-1">新密码</label>
                <input type="text" id="new-pwd-1" />
                <label class="new-pwd-2" for="new-pwd-2">确认密码</label>
                <input type="text" id="new-pwd-2" />
            </div>
            <input type="submit" value="重置密码" id="test-mail">
        </form>
        <!--重置密码表单 -->
        <form>
        </form>
        <div class="mail-question">
            <h3>没有收到邮件</h3>
            <p>1.如果你没有收到验证邮件<a href="#">请点击此处重新发送验证邮件</a></p>
            <p>2.你的邮件系统可能会误将激活邮件识别为垃圾邮件，请到垃圾邮件目录找找</p>
        </div>
    </div>
</div>