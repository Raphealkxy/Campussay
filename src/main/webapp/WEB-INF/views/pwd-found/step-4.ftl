<content tag="title">验证成功</content>
<content tag="css">/pwd-found/step-4</content>
<content tag="javascript">/sign-in/sign-in</content>
<div class="global-center">
    <!--logo和标题 -->
    <img class="pwd-found-logo" src="/static/img/page/pwd_found/logo.png" alt="校园说logo">
    <h2 class="pwd-found-title">找回密码</h2>
    <div class="pwd_found">
        <!--进度条 -->
        <ul class="progress-bar">
            <li>输入账号信息</li>
            <li class="progress-bar_center">验证信息</li>
            <li>重置密码</li>
        </ul>
        <!--重置密码表单 -->
        <form>
            <h3>恭喜，账号验证成功</h3>
            <p>请重置你的登录密码并妥善保管</p>
            <div class="new-pwd">
                <label class="new-pwd-1" for="new-pwd-1">新密码</label>
                <input type="text" id="new-pwd-1" />
                <label class="new-pwd-2" for="new-pwd-2">确认密码</label>
                <input type="text" id="new-pwd-2" />
            </div>
            <input type="submit" value="完成" id="pwd-found-finish">
        </form>
    </div>
</div>