define(['lib/jquery', 'util/request'], function ($, request) {
    var sign_in = {
        init: function () {
            //登陆和注册切换
            sign_in.changeWay();
            sign_in.geturl();
            sign_in.signInEvent();
            sign_in.signUpEvent();
            sign_in.getPhoneValidate();
            sign_in.refresh('#sign-in','type3');
            sign_in.refresh('#register-form-phone','type1');
            sign_in.signInOrsignUp();
        },
        signIn_info: {
            signInId:false, // 验证账号是否为空
            signInPwd:false, // 验证密码是否为空
            signInIdTF:false, //验证账号格式是否格式正确
            validateNum:false,//验证验证码是否存在
            validateNumTF:false,//验证验证码是否正确
            url: null,
            signUp:null
        },
        signUp_info:{
            phoneNum:false,//手机号码是否为空
            phoneNumTF:false,//手机号码格式是否正确
            phoneValidate:false,//手机验证码是否为空
            phoneValidateTF:false,//手机验证码是否正确
            pwdFirst:false,//第一个密码是否为空
            pwdSecond:false,//第二个密码是否为空
            pwdSame:false,//两个密码是否一样
            pwdSecondTF:false,//第二个密码格式是否正确
            Validate:false,//验证码是否有值
            ValidateTF:false,//验证码是否正确
            agreement:false//协议是否勾选
        },
        signInOrsignUp:function(){
            if(sign_in.signIn_info.signUp==1){
                $('#sign-in').css('display', 'none');
                $('#register-form-phone').css('display', 'block');
            }
        },
        //登陆按钮事件绑定
        signInEvent:function(){
            $("input[name='sign-in']").on('click',function(){
                sign_in.signIn();
            });
            document.onkeydown=function(event)  
            {  
                e = event ? event :(window.event ? window.event : null);  
                var currKey=0;  
                currKey=e.keyCode||e.which||e.charCode;  
                if(currKey==13){  
                    //执行的方法  
                    sign_in.signIn();  
                    //alert('回车检测到了');  
                }  
        } 
        },
        //注册按钮事件绑定
        signUpEvent:function(){
            $("input[name='sign-up-phone']").on('click',function(){
                sign_in.signUp();
            });
            $(document).keydown(function() {
                if (event.keyCode == "13") {//keyCode=13是回车键
                    sign_in.signUp();
                }
            });
        },
        //登陆和注册切换
        changeWay:function(){
            $('.sign-register h3').on('click', function () {
                $('.sign-register').css('display', 'none');
                if ($(this).html() == '登录') {
                    $('#sign-in').css('display', 'block');
                }
                else {
                    $('#register-form-phone').css('display', 'block');
                }
            });
        },
        //登陆
        signIn:function(){

            //验证账号密码是否有值和格式
            if(sign_in.valTF('#sign-in-id')){
                sign_in.signIn_info.signInId = true;

                //验证账号是否格式正确
                if(sign_in.valPhoneTF('#sign-in-id')){
                    sign_in.signIn_info.signInIdTF = true;
                }else{
                    sign_in.signIn_info.signInIdTF = false;
                    //接下来 给账号框添加 错误框
                    //接下来提示信息 请输入正确手机号码
                    sign_in.error_info($('.error'),'请输入正确手机号码',false);
                }
            }else{
                sign_in.signIn_info.signInId = false;
                //接下来给账号框添加 错误框
                //接下来提示信息 请输入账号
                sign_in.error_info($('.error'),'请输入账号',false);
            }
            if(sign_in.valTF('#sign-in-pwd')){
                sign_in.signIn_info.signInPwd = true;
                //接下来 去掉提示信息
            }else{
                sign_in.signIn_info.signInPwd = false;
                //接下来 给密码框添加 错误框
                //接下来提示信息 请输入密码
                sign_in.error_info($('.error'),'请输入密码',false);
            }
            if(sign_in.signIn_info.signInId&&
                sign_in.signIn_info.signInPwd&&
                sign_in.signIn_info.signInIdTF){
                sign_in.error_info($('.error'),' ',true);
            }


            if(sign_in.valTF('#type3')){
                sign_in.signIn_info.validateNum = true;

                var dtd = $.Deferred(); // 新建一个deferred对象
                $.when(sign_in.validateNum('#type3','type3',dtd)).done(function(){
                    console.log(sign_in.signIn_info.validateNumTF)
                    if(sign_in.signIn_info.signInId&&
                        sign_in.signIn_info.signInPwd&&
                        sign_in.signIn_info.signInIdTF&&
                        sign_in.signIn_info.validateNum&&
                        sign_in.signIn_info.validateNumTF){
                        $('#sign-in .sign-in-btn').val('正在登录中......').attr('disabled','true');
                        var id = $('#sign-in-id').val().trim();
                        var pwd = $('#sign-in-pwd').val();
                        request.post('../user/userLogin', {
                            account: id,
                            password: pwd
                        }, function (data) {
                            data = JSON.parse(data);
                            if (data.code == 1) {
                                sign_in.error_info($('.error'),'',true);
                                if(sign_in.signIn_info.url&&sign_in.signIn_info.signUp!=1){
                                    window.location=sign_in.signIn_info.url;
                                }else if(window.location.pathname == "/user/sign-in-web"){
                                    window.location='/';
                                }else {
                                    window.location.reload();
                                }
                            }else if(data.code == -5){
                                sign_in.error_info($('.error'),'系统繁忙，请稍后再试',true);
                                $("input[name='sign-in']").val('登录').removeAttr('disabled');
                            }
                            else {
                                sign_in.error_info($('.error'),data.msg,false);
                                $("input[name='sign-in']").val('登录').removeAttr('disabled');
                            }
                        })
                    }
                });
                //清空验证码
                $('#type3').val('');
                //更新验证码
                sign_in.refreshValidateNum('#type3','type3')
            }else{
                sign_in.signIn_info.validateNum = false;
                //接下来 给验证码框添加 错误框
                //接下来提示信息 请输入验证码
                sign_in.error_info($('#sign-in .validate-error'),'请填写验证码',false);
            }


        },
        signUp:function(){
            //验证手机号码是否有值
            if(sign_in.valTF('#phoneNum')){
                sign_in.signUp_info.phoneNum = true;
                console.log('手机号码有值')
                //验证手机号码格式是否正确
                if(sign_in.valPhoneTF('#phoneNum')){
                    sign_in.signUp_info.phoneNumTF = true;
                    console.log('手机号码格式正确')
                    //验证手机验证码是否有值
                    if(sign_in.valTF('#validateNum')){
                        sign_in.signUp_info.phoneValidate = true;
                        console.log('手机验证码有值')
                        //提示信息清空
                        sign_in.error_info($('.pwd_error'),'',true);
                        //发送手机验证码
                    }else{
                        sign_in.signUp_info.phoneValidate = false;
                        console.log('手机验证码没有值')
                        //提示信息：请输入验证码
                        sign_in.error_info($('.pwd_error'),'请输入验证码',false);
                        //手机验证码框显示错误框
                    }
                }else{
                    sign_in.signUp_info.phoneNumTF = false;
                    console.log('手机号码格式错误')
                    //提示信息：请输入正确的手机格式
                    sign_in.error_info($('.pwd_error'),'请输入正确的手机格式',false);
                    //手机输入框显示错误框
                }
            }else{
                sign_in.signUp_info.phoneNum = false;
                console.log('手机号码没有值')
                //提示信息：请输入手机号码
                sign_in.error_info($('.pwd_error'),'请输入手机号码',false);
                //手机输入框显示错误框
            }
            //判断第一个密码框是否有值
            if(sign_in.valTF('#pwd1')){
                sign_in.signUp_info.pwdFirst = true;
                console.log('第一个密码框有值');
                //判断第二个密码框是否有值
                if(sign_in.valTF('#pwd2')){
                    sign_in.signUp_info.pwdSecond = true;
                    console.log('第二个密码框有值');
                    //判断两个密码是否一样
                    if(sign_in.pwdSame('#pwd1','#pwd2')){
                        sign_in.signUp_info.pwdSame = true;
                        console.log('两个密码一样');
                        //判断密码值是否为6-18位
                        if(sign_in.pwdVal('#pwd2')){
                            sign_in.signUp_info.pwdSecondTF = true;
                            console.log('密码格式正确');
                            //提示信息：清空
                            sign_in.error_info($('.pwd_error_info'),'',true);
                        }else{
                            sign_in.signUp_info.pwdSecondTF = false;
                            console.log('密码格式错误');
                            //提示信息：请输入6-20个字符的密码
                            sign_in.error_info($('.pwd_error_info'),'请输入6-20个数字或英文字母的密码',false);
                            //第二个密码框显示错误框
                        }
                    }else{
                        sign_in.signUp_info.pwdSame = false;
                        console.log('两个密码不一样');
                        //提示信息：两次输入密码不一样
                        sign_in.error_info($('.pwd_error_info'),'两次输入密码不一样',false);
                        //第二个密码框显示错误框
                    }
                }else{
                    sign_in.signUp_info.pwdSecond = false;
                    console.log('第二个密码框没有值');
                    //提示信息：请输入密码
                    sign_in.error_info($('.pwd_error_info'),'请输入密码',false);
                    //第二个密码框显示错误框
                }

            }else{
                sign_in.signUp_info.pwdFirst = false;
                console.log('第一个密码框没有值');
                //提示信息：请输入密码
                sign_in.error_info($('.pwd_error_info'),'请输入密码',false);
                //第一个密码框显示错误框
            }
            if($('#agree-phone').is(':checked')){
                sign_in.signUp_info.agreement = true;
                console.log('已经勾选协议');
                //判断验证码是否有值
                if(sign_in.valTF('#type1')){
                    sign_in.signUp_info.Validate = true;

                    console.log('验证码有值')

                    var dtd = $.Deferred(); // 新建一个deferred对象
                    $.when(sign_in.validateNum('#type1','type1',dtd)).done(function(){
                        console.log(sign_in.signUp_info.phoneNum);
                        console.log(sign_in.signUp_info.phoneNumTF);
                        console.log(sign_in.signUp_info.phoneValidate);
                        console.log(sign_in.signUp_info.pwdFirst);
                        console.log(sign_in.signUp_info.pwdSecond);
                        console.log(sign_in.signUp_info.pwdSame);
                        console.log(sign_in.signUp_info.pwdSecondTF);
                        console.log(sign_in.signUp_info.Validate);
                        console.log(sign_in.signUp_info.agreement);
                        console.log(sign_in.signUp_info.validateNumTF);
                        if(sign_in.signUp_info.phoneNum&&
                            sign_in.signUp_info.phoneNumTF&&
                            sign_in.signUp_info.phoneValidate&&
                            sign_in.signUp_info.pwdFirst&&
                            sign_in.signUp_info.pwdSecond&&
                            sign_in.signUp_info.pwdSame&&
                            sign_in.signUp_info.pwdSecondTF&&
                            sign_in.signUp_info.Validate&&
                            sign_in.signUp_info.validateNumTF&&
                            sign_in.signUp_info.agreement){
                            var dtd = $.Deferred(); // 新建一个deferred对象
                                var _this = $('#register-form-phone');
                                var id = _this.find('.sign-phone').val().trim();
                                var pwd = _this.find('.register-pwd-2').val();
                                var num = _this.find('.phone-validate').val().trim();
                                        sign_in.signUp_info.ValidateTF =true;
                                        sign_in.error_info($('.validate-error'),'',true);
                                        $("input[name='sign-up-phone']").val('注册中......').attr('disabled','disabled');
                                        request.post('../user/register2', {
                                            code: num,
                                            userName: id,
                                            userPassword: pwd
                                        }, function (data) {
                                            var data1 = JSON.parse(data);
                                            if(data1.code){
                                                sign_in.error_info($('.pwd_error'),'',true);
                                                request.post('../user/userLogin', {
                                                    account: id,
                                                    password: pwd
                                                },function(){
                                                    window.location = '/personalSetting/person'
                                                })
                                            }else{
                                                $("input[name='sign-up-phone']").val('注册').removeAttr('disabled');
                                                sign_in.error_info($('.pwd_error'),data1.msg,false)
                                            }
                                        })
                        }
                    });
                    //清空验证码
                    $('#type1').val('');
                    //更新验证码
                    sign_in.refreshValidateNum('#type1','type1')
                }else{
                    sign_in.signUp_info.Validate = false;
                    console.log('验证码没有值')
                    //接下来 给验证码框添加 错误框
                    //接下来提示信息 请输入验证码
                    sign_in.error_info($('#register-form-phone .validate-error'),'请填写验证码',false);
                }
            }else{
                sign_in.signUp_info.agreement = false;
                console.log('没有勾选协议')
                //接下来提示信息 请同意用户协议
                sign_in.error_info($('#register-form-phone .validate-error'),'请同意用户协议',false);
            }


        },
        getPhoneValidate:function(){
            $('.get-validate').on('click',function(event){
                event.preventDefault();
                //验证手机号码是否有值
                if(sign_in.valTF('#phoneNum')){
                    sign_in.signUp_info.phoneNum = true;
                    console.log('手机号码有值')
                    //验证手机号码格式是否正确
                    if(sign_in.valPhoneTF('#phoneNum')){
                        sign_in.signUp_info.phoneNumTF = true;
                        console.log('手机号码格式正确')
                            //提示信息清空
                            sign_in.error_info($('.pwd_error'),'',true);
                            //发送手机验证码
                            sign_in.register($('#phoneNum').val().trim());
                    }else{
                        sign_in.signUp_info.phoneNumTF = false;
                        console.log('手机号码格式错误')
                        //提示信息：请输入正确的手机格式
                        sign_in.error_info($('.pwd_error'),'请输入正确的手机格式',false);
                        //手机输入框显示错误框
                    }
                }else{
                    sign_in.signUp_info.phoneNum = false;
                    console.log('手机号码没有值')
                    //提示信息：请输入手机号码
                    sign_in.error_info($('.pwd_error'),'请输入手机号码',false);
                    //手机输入框显示错误框
                }
            })

        },
        //核对验证码
        validateNum:function ($dom,type,dtd) {
                var num = $($dom).val();
                var typeNum =type;
                $.get('../code/checkAuthcode', {type: typeNum, code: num}, function (date) {
                    var date1 = JSON.parse(date);
                    if (date1.code == 1) {
                        if(type=='type3'){
                            sign_in.signIn_info.validateNumTF = true;
                        }else{
                            sign_in.signUp_info.validateNumTF = true;
                        }
                        //接下来 去除提示信息
                        sign_in.error_info($($dom).siblings('.validate-error'),'',true);
                        //接下来 去除验证码框错误框
                        dtd.resolve(); // 改变deferred对象的执行状态
                    } else {
                        if(type=='type3'){
                            sign_in.signIn_info.validateNumTF = false;
                        }else{
                            sign_in.signUp_info.validateNumTF = false;
                        }
                        //接下来提示信息 验证码错误
                        sign_in.error_info($($dom).siblings('.validate-error'),'验证码错误',false);
                        //接下来 给验证码框添加 错误框
                        dtd.resolve(); // 改变deferred对象的执行状态
                    }
                });

                sign_in.refreshValidateNum($dom,type);
                return dtd;

        },
        //验证输入框是否有值
        valTF:function($dom){
            var val = $($dom).val();
            if(val){
                return true;
            }else{
                return false;
            }
        },
        //坚持两个密码是否一样
        pwdSame:function($dom1,$dom2){
            var pwd1 = $($dom1).val();
            var pwd2 = $($dom2).val();
            if(pwd1==pwd2){
                return true;
            }else{
                return false;
            }
        },
        pwdVal:function($dom){
            var pwd = $($dom).val().trim();
            if(pwd.length < 6 || pwd.length > 20){
                return false;
            }else{
                return true;
            }
        },
        //错误提示信息
        error_info:function($dom,text,TF){
            if(TF){
                $dom.html(text);
            }else{
                $dom.html(text);
            }
        },
        //刷新验证码
        refreshValidateNum:function($dom,type){
            var date = new Date();
            var addressUrl = '../code/getAuthCode?type=' + type + '&date=' + date;
            $($dom).next().attr('src', addressUrl);
            console.log('22222')
        },
        //验证手机格式
        valPhoneTF:function($dom){
            var string = $($dom).val();
            //验证格式函数
                if (/^1[34578]\d{9}$/.test(string)) {
                    var i;
                    return true;
                }
                return false;
        },
        register :function (account1) {
            $.get('../user/register1', {account: account1}, function (data) {
                var data1 = JSON.parse(data);
                if (data1.code == 1) {
                    sign_in.error_info($('.pwd_error'),'',true);
                    var btn = function (btn) {
                        var oldcount, count = 60, _this = btn;
                        oldcount = count; //保存原始时间，以便刷新$
                        $(_this).html("已发送(<span>" + count + "</span>)s").attr("disabled", true);
                        if ($(_this).html() == "重新发送验证码") {
                            $(_this).html("已发送(<span>" + count + "</span>)s").attr("disabled", true);
                        }
                        var countdown = setInterval(CountDown, 1000);

                        function CountDown() {
                            count--;
                            $(_this).attr("disabled", true).children().html(count);
                            if (count == 0) {
                                $(_this).val("Submit").removeAttr("disabled")
                                    .html("重新发送验证码");
                                clearInterval(countdown);
                                count = oldcount;
                            }
                        }
                    };
                    btn($('.get-validate'));
                } else {
                    sign_in.error_info($('.pwd_error'),data1.msg,false);
                }
            })
        },
        refresh:function($dom,typeNum){
            var refresh = $('.validate').find('i img');
            var refreshImg = $('.validate img');
            refresh.on('click', function () {
                var date = new Date();
                var addressUrl = '../code/getAuthCode?type=' + typeNum + '&date=' + date;
                $($dom).find('.validate-img').attr('src', addressUrl);
            });
            refreshImg.on('click', function () {
                var date = new Date();
                var addressUrl = '../code/getAuthCode?type=' + typeNum + '&date=' + date;
                $($dom).find('.validate-img').attr('src', addressUrl);
            })
        },
        geturl: function () {
            $.extend({
                //url参数约定search表示问号，$表示等于号
                getUrlVars: function () {
                    var vars = [], hash;
                    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
                    for (var i = 0; i < hashes.length; i++) {
                        hash = hashes[i].split('=');
                        vars.push(hash[0]);
                        vars[hash[0]] = hash[1];
                    }
                    return vars;
                },
                getUrlVar: function (name) {
                    var url = $.getUrlVars()[name];
                    if(name == 'url' && url){
                       url = url.replace('search',"?").replace('$','=');
                    }
                    return url;
                }
            });

//获取参数对象
            //      alert($.getUrlVars());
//获取参数a的值
            sign_in.signIn_info.url = $.getUrlVar('url');
            sign_in.signIn_info.signUp = $.getUrlVar('type');
        }
    };
    return sign_in.init;
});