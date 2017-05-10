/**
 * Created by liangbijie on 2015/12/14.
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});
require(['lib/jquery', 'util/request'], function ($, request) {
    var pwd_found = {
        init: function () {
            pwd_found.deleteNav();
            pwd_found.step_1();
            pwd_found.step2();
            pwd_found.validate_btn('.get-validate');
            pwd_found.step3();
            pwd_found.validate();
        },
        //全局数据
        found_info: {
            step1_acount: false,
            step1_validate: false,
            step2_validate: false,
            step2_phone:0,
            step2_same:false
        },
        deleteNav:function(){
            $('.main-nav').empty();
        },
        //输入账号信息
        step_1: function () {
            var _this = $('#mail-or-phone');
            _this.blur(function () {
                var mail_phone = _this.val();
                if (telRuleCheck2(/^1[34578]\d{9}$/, mail_phone,_this)) {
                    pwd_found.error_border($('#mail-or-phone'),true);
                }
                else {  //这里代表格式正确但是账号不存在而报错
                    pwd_found.error_border($('#mail-or-phone'),false);
                    console.log('asdfasdfasdfasdfsdasfdfasf')
                }
            });
            //验证格式函数
            var telRuleCheck2 = function (pattern, string,$dom) {
                var error_text = $('.mail-or-phone span');
                if (pattern.test(string)) {
                    pwd_found.error_text(error_text,'');
                    return true;
                }else{
                    if($($dom).val().trim()==''){
                        pwd_found.error_text(error_text,'请输入手机号码');
                    }else{
                        pwd_found.error_text(error_text,'请输入正确的手机号码');
                    }
                    return false;
                }
            };
            $('#next-step').click(function (event) {
                event.preventDefault();
                var mail_phone = _this.val();
                var mailOrPhone = $('#mail-or-phone');

                var dtd = $.Deferred();
                $.when(pwd_found.validate(dtd)).done(function(){
                    console.log(pwd_found.found_info.step1_acount+'123123')
                    if (pwd_found.found_info.step1_validate) {
                        if(pwd_found.found_info.step1_acount){
                            request.get('../../user/findPassword1', {account: mail_phone}, function (data) {
                                var data1 = JSON.parse(data);
                                if (data1.code == 1) {
                                    pwd_found.error_border(mailOrPhone, true);
                                    window.location = '/user/pwdFound/3?acount=' + $('#mail-or-phone').val();
                                } else {
                                    pwd_found.error_border($('#mail-or-phone'), false);
                                    pwd_found.found_info.step1_validate=false;
                                    pwd_found.error_text($('.mail-or-phone span'),data1.msg);
                                    console.log(data1.msg);
                                }
                            })
                        }
                    }else{
                        pwd_found.error_border($('#validate'), false);
                    }
                });

            });

        },
        step2:function(){
            //将第一步骤的手机号码填入
            pwd_found.geturl();
            $('.pwd_found form p span').html(pwd_found.found_info.phone);
            $('.get-validate').on('click',function(){
                if(pwd_found.found_info.step2_validate){
                    console.log('s');
                    request.get('../../user/findPassword1', {account: pwd_found.found_info.phone}, function (data) {
                        var data1 = JSON.parse(data);
                    })
                }
                pwd_found.validate_btn('.get-validate');
            });
            $('#test-phone').on('click',function(event){
                event.preventDefault();
                pwd_found.same_pwd();
                  if(pwd_found.found_info.step2_same){
                      request.post('../../user/findPassword2',{code:$('#test-phone-text').val(),userPassword:$('#new-pwd-2').val()},function(data){
                          var data1 = JSON.parse(data);
                          if (data1.code){
                              window.location='/user/pwdFound/5';
                          }else{
                              pwd_found.error_text($('.new-pwd span'),data1.msg)
                          }
                      })
                  }
            })
        },
        step3:function(){
            $('#pwd-found-finish').on('click',function(event){
                event.preventDefault();
                window.location='/user/sign-in-web'
            })
        },
        validate: function (dtd) {
            //获取step1输入框里面的验证码,并且验证
                var _this = $('#validate');
                if($(_this).val()!=''){

                    var validateNum = $(_this).val();
                    $.get('../../code/checkAuthcode', {type:'step1', code: validateNum}, function (data) {
                        var data1 = JSON.parse(data);
                        if (data1.code == 1) {
                            pwd_found.found_info.step1_validate = true;
                            pwd_found.error_border($(_this), true);
                            getValdate();//输入错误重新刷新验证码
                            $(_this).val('');
                            console.log(pwd_found.found_info.step1_validate+'第一步')
                            dtd.resolve(); // 改变deferred对象的执行状态
                        } else {
                            pwd_found.found_info.step1_validate = false;
                            pwd_found.error_border($(_this), false);
                            getValdate();//输入错误重新刷新验证码
                            $(_this).val('');
                            console.log(pwd_found.found_info.step1_validate+'第一步')
                            dtd.resolve(); // 改变deferred对象的执行状态
                        }
                    })
                }
            //刷新按钮
            $('.validate i img').on('click', function () {
                getValdate();
            });
            $('.validate img').on('click', function () {
                getValdate();
            });
            //获取验证码
            getValdate = function() {
                var type = 'step1';
                var date = new Date();
                var addressUrl = '../../code/getAuthCode?type=' + type + '&date=' + date;
                $('.validate img')[0].src = addressUrl;
            };
            return dtd;
    },
        //验证按钮倒计时
        validate_btn:function($btn){
            pwd_found.found_info.step2_validate=false;
                var oldcount, count = 60, _this = $btn;
                oldcount = count; //保存原始时间，以便刷新
                if ($(_this).find('span').html() =='60'||$(_this).html()=='重新发送验证码') {
                    $(_this).html("已发送(<span>" + count + "</span>s)").attr("disabled", true);
                    var countdown = setInterval(CountDown, 1000);
                }
                function CountDown(){
                    count--;
                    $(_this).attr("disabled", true).children().html(count);
                    if (count == 0) {
                        $(_this).val("Submit").removeAttr("disabled")
                            .html("重新发送验证码");
                        pwd_found.found_info.step2_validate=true;
                        clearInterval(countdown);
                        count = oldcount;
                    }
                }
        },
        //错误提示框
        error_border: function ($box, TF) {
            if (TF) {
                $box.css('border', '');
                pwd_found.found_info.step1_acount = true;
                console.log(pwd_found.found_info.step1_acount+'+')
            } else {
                $box.css('border', '1px solid #f06060');
                pwd_found.found_info.step1_acount = false;
                console.log(pwd_found.found_info.step1_acount+'+')
            }
        },
        //错误提示信息
        error_text:function($dom,text){
            $dom.html(text);
        },
        //获取url里面的account值
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
            pwd_found.found_info.phone = $.getUrlVar('acount');
        },
        same_pwd:function(){
            var pwd1 =$('#new-pwd-1');
            var pwd2 =$('#new-pwd-2');
            if(pwd1.val()==pwd2.val()&&pwd1.val()!=''&&pwd2.val()!=''){
                if(pwd2.val().length<6||pwd2.val().length>20){
                    pwd_found.found_info.step2_same=false;
                    pwd_found.error_border(pwd2,false);
                    pwd_found.error_text($('.new-pwd span'),'请输入6-20个数字或字母')
                }else{
                    pwd_found.found_info.step2_same = true;
                    pwd_found.error_border(pwd2,true);
                    pwd_found.error_text($('.new-pwd span'),'')
                }
            }else{
                pwd_found.found_info.step2_same=false;
                pwd_found.error_border(pwd2,false);
                pwd_found.error_text($('.new-pwd span'),'两次输入密码不一样')
            }
        }
    };
    pwd_found.init();
});