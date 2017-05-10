/**
 * Created by liangbijie on 2016/3/10.
 */
/**
 * Created by liangbijie on 2016/1/10.
 */

require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'util/request'], function ($, request) {
    var sign_in = {
        init: function () {
            sign_in.changeWay();
            sign_in.sendVerification();
            sign_in.validateSignIn();
            sign_in.pwdSame();
            sign_in.signUp();
            sign_in.geturl();
            if(sign_in.signIn_info.signUp==1){
                $('.sign-register').css('display', 'none');
                $('#register-form-phone').css('display', 'block');
            }
        },
        signIn_info: {
            url: null,
            signUp:null
        },
        validateSignIn: function () {
            $("input[name='sign-in']").click(function () {
                console.log('1')
                if($('#sign-in').attr('name')=='true'){
                    $(this).val('正在登录中......').attr('disabled','true');
                    signIn();
                }
            });
            $(document).keydown(function() {
                console.log('2')
                if (event.keyCode == "13"&&$('#sign-in').attr('name')=='true') {//keyCode=13是回车键
                    $(this).val('正在登录中......').attr('disabled','true');
                    signIn();
                }
            });

            var signIn = function () {

                var id = $('#sign-in-id').val().trim();
                var pwd = $('#sign-in-pwd').val();
                request.post('../user/userLogin', {
                    account: id,
                    password: pwd
                }, function (data) {
                    var data1 = JSON.parse(data);
                    if (data1.code == 1) {
                        sign_in.error_info($('.error'),'登陆成功',true);
                        if(sign_in.signIn_info.url){
                            window.location=sign_in.signIn_info.url;
                        }else{
                            window.location='/';
                        }
                    }
                    else {
                        sign_in.error_info($('.error'),data1.msg,false);
                        $("input[name='sign-in']").val('登录').removeAttr('disabled');
                    }
                })
            }
        },
        signUp: function () {
            $("input[name='sign-up-phone']").click(function () {
                var _this = $('#register-form-phone');
                var id = _this.find('.sign-phone').val().trim();
                var pwd = _this.find('.register-pwd-2').val();
                var num = _this.find('.phone-validate').val().trim();
                var TF = _this.find('.error-text');
                if($('#agree-phone').is(':checked')){
                    if (TF.length == 0 && _this.attr('name') == 'true' ) {
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
                }else{
                    sign_in.error_info($('.validate-error'),'请同意用户协议',false);
                }
            });
        },
        changeWay: function () {
            //登陆和注册切换
            $('.sign-register h3').on('click', function () {
                $('.sign-register').css('display', 'none');
                if ($(this).html() == '登录') {
                    $('#sign-in').css('display', 'block');
                }
                else {
                    $('#register-form-phone').css('display', 'block');
                }
            });
            //选择注册方式
            $('.sign-in-QQ').on('click', function () {
                $('.sign-register').css('display', 'none');
                $('#register-form').css('display', 'block');
            });
            $('.sign-in-phone').on('click', function () {
                $('.sign-register').css('display', 'none');
                $('#register-form-phone').css('display', 'block');
            });
            $('.sign-in-mail').on('click', function () {
                $('.sign-register').css('display', 'none');
                $('#register-form-mail').css('display', 'block');
            });
            //手机注册切换到邮箱注册
            $('.change-mail').on('click', function () {
                $('.sign-register').css('display', 'none');
                $('#register-form-mail').css('display', 'block')
            });
            //邮箱注册切换到手机注册
            $('.change-phone').on('click', function () {
                $('.sign-register').css('display', 'none');
                $('#register-form-phone').css('display', 'block')
            });
        },
        //验证输入的两次密码是否相等
        pwdSame: function () {
            var pwd_Same = function (phone_mail) {
                $('.register-pwd-1,.register-pwd-2').blur(function () {
                    var pwd = $(phone_mail);
                    var pwd1 = pwd.find('.register-pwd-1');
                    var pwd2 = pwd.find('.register-pwd-2');
                    console.log(pwd1.val());
                    console.log(pwd2.val());
                    if (pwd1.val() != pwd2.val() && pwd2.val() != '') {
                        pwd2.addClass('error-text');
                        pwd2.next().css('display', 'block').addClass('error-info');
                        setTimeout(function () {
                            pwd2.next().css('display', 'none').removeClass('error-info');
                        }, 1500);
                    } else if (pwd1.val().length < 6 || pwd1.val().length > 20) {
                        pwd2.addClass('error-text');
                        pwd2.next().next().css('display', 'block').addClass('error-info');
                        setTimeout(function () {
                            pwd2.next().next().removeClass('error-info').css('display', 'none');
                        }, 1500);
                    } else {
                        pwd2.removeClass('error-text');
                    }
                })
            };
            pwd_Same('#register-form-phone');
            pwd_Same('#register-form-mail');
        },
        //----------------------------------//
        // ------------手机注册函数----------//
        // ----------------------------------//

        //手机发送验证码按钮
        sendVerification: function () {
            var __this1 = $("input[name='account']");
            //失焦事件，用来验证手机格式
            $(__this1).blur(function () {
                var phoneNum = __this1.val();
                if (telRuleCheck2(/^1[34578]\d{9}$/, phoneNum)) {
                    truePhone('#register-form-phone'); //手机格式正确时候调用的函数
                }
                else {
                    falsePhone('#register-form-phone');//手机格式错误时候调用的函数
                }
            });
            //发送手机验证码并且核对格式

            $('#register-form-phone').find('.get-validate').on('click', function (event) {
                event.preventDefault();
                var phoneNum = __this1.val();
                if (telRuleCheck2(/^1[34578]\d{9}$/, phoneNum)) {
                    register(phoneNum);
                }
                else {
                    falsePhone();
                }
            });




            //验证格式函数
            var telRuleCheck2 = function (pattern, string) {
                if (pattern.test(string)) {
                    return true;
                }
                return false;
            };
            //格式正确
            var truePhone = function (phone_mail) {
                var _this = $(phone_mail).find("input[name=account]");
                _this.removeClass('error-text');
            };
            //格式错误
            var falsePhone = function (phone_mail) {
                var _this = $(phone_mail).find("input[name=account]");
                var old_text = _this.val();
                _this.val('请输入正确的格式').addClass('error-text error-info');
                setTimeout(function () {
                    _this.val(old_text).removeClass('error-info');
                }, 1500);
            };

            //验证码倒计时实现



            //注册请求
            var register = function (account1) {
                $.get('../user/register1', {account: account1}, function (data) {
                    var data1 = JSON.parse(data);
                    if (data1.code == 1) {
                        sign_in.error_info($('.pwd_error'),'',true);
                        btn($('.get-validate'));
                        var btn = function (btn) {
                            var oldcount, count = 60, _this = btn;
                            oldcount = count; //保存原始时间，以便刷新
                            if ($(_this).html() == "重新发送验证码") {
                                $(_this).html("获取验证码(<span>" + count + "</span>)s").attr("disabled", true);
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
                    } else {
                        sign_in.error_info($('.pwd_error'),data1.msg,false);
                    }
                })
            };
            //核对验证码
            var validateNum = function (phone_mail_validateNum) {
                var _this = $(phone_mail_validateNum);
                var typeNum = _this.find('.validate input').attr('name');
                _this.find('.validate input').blur(function () {
                    var num = _this.find('.validate input').val();
                    console.log(typeNum);
                    if(num){
                        $.get('../code/checkAuthcode', {type: typeNum, code: num}, function (date) {
                            var date1 = JSON.parse(date);
                            if (date1.code == 1) {
                                $(phone_mail_validateNum).attr('name', true);
                                sign_in.error_info($(phone_mail_validateNum).find('.validate-error'),'',true)
                            } else {
                                sign_in.error_info($(phone_mail_validateNum).find('.validate-error'),'验证码错误',false);
                                $(phone_mail_validateNum).attr('name', false);
                                //刷新验证码
                                var date = new Date();
                                var addressUrl = '../code/getAuthCode?type=' + typeNum + '&date=' + date;
                                _this.find('.validate-img').attr('src', addressUrl);
                            }
                        });
                    }
                });
                //刷新验证码
                var refresh = $('.validate').find('i img');
                refresh.on('click', function (event) {
                    event.preventDefault();
                    var date = new Date();
                    var addressUrl = '../code/getAuthCode?type=' + typeNum + '&date=' + date;
                    _this.find('.validate-img').attr('src', addressUrl);
                })
            };
            validateNum('#register-form-phone');
            //validateNum('#register-form-mail');
            validateNum('#sign-in');
        },
        //获取url

        geturl: function () {
            $.extend({
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
                    return $.getUrlVars()[name];
                }
            });

//获取参数对象
            //      alert($.getUrlVars());
//获取参数a的值
            sign_in.signIn_info.url = $.getUrlVar('url');
            sign_in.signIn_info.signUp = $.getUrlVar('type');
        },
        error_info:function($dom,text,TF){
            if(TF){
                $dom.html(text);
            }else{
                $dom.html(text);
            }
        }

    };
    sign_in.init();
});
