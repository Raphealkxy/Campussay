define(['lib/jquery', 'util/funcTpl'], function($, funcTpl) {
	var popTpl = {
		init: function() {
			return funcTpl(popTpl.tpl);
		},
		tpl: function() {
			/*
			<!--logo和切换学校 -->
	    <div class="sign-in-warp">
	        <!--登陆 -->
	        <form id="sign-in" class="sign-register " name="false" style="display: block">
	            <a href="javascript:;"><h3 class="h3-onfocus">登录</h3></a>
	            <a href="javascript:;"><h3>注册</h3></a>

	            <div class="id-pwd">
	                <input type="text" id="sign-in-id"  placeholder="已注册手机">
	                <input type="password" id="sign-in-pwd" placeholder="登录密码" maxlength="20">
	                <span><a href="/user/pwdFound/1">忘记密码</a></span>
	                <span class="error"></span>
	            </div>
	            <div class="validate">
	                <input name="type3" type="text" id="type3" placeholder="验证码">
	                <img src="../code/getAuthCode?type=type3" class="validate-img"  alt="验证码">
	                <i style="float: right;width: 15px;margin-right:20px ">
	                    <a href="#"><img src="/static/img/page/pwd_found/wait-icon.png" alt="刷新" title="刷新" ></a>
	                </i>
	                <span class="validate-error"></span>
	            </div>
	            <!-- <input type="checkbox" class="auto-sign-in" name=""/>
	             <span>下次自动登录</span>-->
	            <input type="button" class="sign-in-btn" name="sign-in" value="登录">
	        </form>
	        <!--注册方式选择 -->
	        <form id="register" class="sign-register" style="display:none;">
	            <a href="javascript:;"><h3>登录</h3></a>
	            <a href="javascript:;"><h3 class="h3-onfocus">注册</h3></a>
	            <ul>
	                <li class="sign-in-QQ" style="display: none">
	                    <a href="#">使用QQ注册</a>
	                </li>
	                <li class="sign-in-phone">
	                    <a href="#">使用手机注册</a>
	                </li>
	                <li class="sign-in-mail">
	                    <a href="#">使用邮箱注册</a>
	                </li>
	            </ul>
	        </form>

	        <!--手机注册 -->
	        <form id="register-form-phone" class="sign-register" style="display: none">
	            <a href="javascript:;"><h3>登录</h3></a>
	            <a href="javascript:;"><h3 class="h3-onfocus">注册</h3></a>
	            <a href="javascript:;" class="change-mail" style="position: relative;left: -10000px;">切换至邮箱注册</a>
	            <div class="register-validate">
	                <input type="text" id="phoneNum" name="account" class="sign-phone" placeholder="输入您的手机号"/>
	                <input type="text" id="validateNum" class="phone-validate" placeholder="请输入手机验证码">
	                <button class="get-validate">获取验证码(<span>60</span>s)</button>
	            </div>
	            <div class="register-pwd">
	                <span class="pwd_error"></span>
	                <input class="register-pwd-1" id="pwd1" type="password" placeholder="密码（6--20字符）" maxlength="20">
	                <input class="register-pwd-2" id="pwd2" type="password" placeholder="再次输入你的密码" maxlength="20">
	                <span class="pwd_error"></span>
	                <span class="pwd_error_info"></span>
	            </div>
	            <div class="validate">
	                <input name="type1" type="text"  id="type1" placeholder="验证码"">
	                <img src="../code/getAuthCode?type=type1" class="validate-img" alt="验证码">
	                <i>
	                    <a href="#"><img src="/static/img/page/pwd_found/wait-icon.png" alt="刷新" title="刷新" ></a>
	                </i>
	                <span class="validate-error"></span>
	            </div>
	            <input type="checkbox" class="auto-sign-in" id="agree-phone"/>
	            <label for="agree-phone">我已同意校园说</label>
	            <a href="#">用户协议</a>
	            <input type="button" class="sign-in-btn" name="sign-up-phone" value="注册">
	            <a href="#" class="change-qq" style="display: none">< 返回QQ注册</a>
	        </form>

	        <!--邮箱注册 -->
	        <form id="register-form-mail" class="sign-register" style="display: none" >
	            <a href="javascript:;"><h3>登录</h3></a>
	            <a href="javascript:;"><h3 class="h3-onfocus">注册</h3></a>
	            <a href="javascript:;" class="change-phone">切换至手机注册</a>

	            <div class="register-validate">
	                <input type="text" class="sign-phone" name="account" placeholder="输入您的邮箱号"/>
	                <input type="text" class="phone-validate" placeholder="请输入邮箱验证码">
	                <button class="get-validate">获取验证码(<span>60</span>s)</button>
	                <a href="#" target="_blank">去邮箱查阅></a>
	            </div>
	            <div class="register-pwd">
	                <input class="register-pwd-1" type="password" placeholder="密码（6--20字符）"  maxlength="20">
	                <input class="register-pwd-2" type="password" placeholder="再次输入你的密码"
	                       maxlength="20">
	            </div>
	            <div class="validate">
	                <input  name="type2" type="text"  placeholder="验证码">
	                <img src="../code/getAuthCode?type=type2" class="validate-img" alt="验证码">
	                <i>
	                    <a href="#"><img src="/static/img/page/pwd_found/wait-icon.png" alt="刷新" title="刷新" ></a>
	                </i>
	            </div>
	            <input type="checkbox" class="auto-sign-in" id="agree-mail"/>
	            <label for="agree-mail">我已同意校园说</label><a href="#">用户协议</a>
	            <input type="button" class="sign-in-btn" name="sign-up-mail" value="注册">
	            <a href="#" class="change-qq" style="display: none">< 返回QQ注册</a>
	        </form>

	    </div>
			 */
		}
	};

	return popTpl.init;
	
});