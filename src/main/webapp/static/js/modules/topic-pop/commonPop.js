define(['lib/jquery', 'modules/topic-pop/popPanel', "util/funcTpl", 'util/request', 'modules/share/js/share', 'lib/juicer'],

    function($, popPanel, funcTpl, request) {

        //公共弹窗组件：退出，切换学校
        var commonPop = {
            //1.退出弹窗
            quitPop: function() {
                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg'><span class='order-icon order-icon-fail'></span>确认退出？</div>" +
                    "<div class='order-button-row'>" +
                    "<a class='quit-button' data-type='quite-event' style='margin-right:20px'>确定</a>" +
                    "<a class='quit-button' data-type='close'>取消</a>" +
                    "</div>" +
                    "</div>";

                var opt = {
                    width: "310px",
                    height: "143px",
                    content: popTpl,
                    type: 'order-pop', //复用订单弹窗样式
                    bind: {
                        "quite-event": this._quitEvent //绑定确认退出事件
                    }
                }
                popPanel.init(opt);
            },
            //点击确认退出事件
            _quitEvent: function() {

                request.get('/user/signout', function(rsp) {
                    rsp = JSON.parse(rsp);

                    if (rsp.code == 1) {
                        window.location.href = '/';
                    } else {
                        $(".order-pop-content").html(commonPop._failquietTpl());
                    }

                });
            },
            //确认失败的面板
            _failquietTpl: function() {
                var tpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg'><span class='order-icon order-icon-fail'></span>退出失败</div>" +
                    "<div class='order-button-row'>" +
                    "<a class='order-button' data-type='close'>请稍后重试</a>" +
                    "</div>" +
                    "</div>";

                return tpl;
            },
            //2.切换学校的弹窗
            switchSchoolPop: function(data) {
                if (!data) {
                    return
                }

                var tplData = {
                    list: data
                };

                var popTpl = juicer(funcTpl(this._schoolTpl), tplData);
                var opt = {
                    isRepaind:true,//是否需要重绘弹窗
                    width: "460px",
                    height: "300px",
                    content: popTpl,
                    type: 'switchschool-pop', //复用订单弹窗样式
                    addevent: this._schoolEvent,
                    closeIcon: false //不需要关闭的按钮
                }

                popPanel.init(opt);
            },
            //切换学校弹窗，点击切换首页banner，牛人和精品专区
            _schoolEvent: function() {

                $(".J_school").click(function(){
                    var id = $(this).attr("data-id"); //学校id
                    var name = $(this).attr("data-schoolName");
                    if (id){
                        //在cookie中存下学校id和学校名字
                        $.cookie('schoolid', id, { expires: 365 }, "/");
                        $.cookie('schoolName', name, { expires: 365 }, "/");
                        $("#J_schoolName").html(name);
                        $(document).trigger('switchschool');
                        popPanel.destory();
                    }
                });
            },
            _schoolTpl: function() {

                /*
                <div class="school-pop-content">
                <ul class="school-list">
                    {@each list as item}
                        {@if item.campus_name === '重庆大学'}
                            <li class="school cq-school J_school" data-type='switch-event' data-id=${item.campus_id} data-schoolName="重庆大学">
                            <span class="s-icon icon-CQ"></span>
                            <p class="s-name">重庆大学</p>
                            </li>
                        {@else if item.campus_name == '重庆邮电大学'}
                             <li class="school cq-school J_school" data-type='switch-event' data-id=${item.campus_id} data-schoolName="重庆邮电大学">
                            <span class="s-icon icon-CQUPT"></span>
                            <p class="s-name">重庆邮电大学</p>
                            </li>
                        {@else if item.campus_name == '重庆工商大学'} 
                            <li class="school gs-school J_school" data-type='switch-event' data-id=${item.campus_id} data-schoolName="重庆工商大学">
                            <span class="s-icon icon-CQGS "></span>
                            <p class="s-name">重庆工商大学</p>
                            </li>
                            
                        {@else if item.campus_name == '其他大学'}    
                            <li class="school gs-school J_school" data-type='switch-event' data-id=${item.campus_id} data-schoolName="其他大学">
                            <span class="s-icon icon-another "></span>
                            <p class="s-name">其他大学</p>
                            </li>   
                        {@else}     
                        {@/if}    
                    {@/each}
                </ul>
                </div>
                */
            },
            //3.发布talking提示弹窗
            /*
            可设置width height tip time
            */
            pubtalkTip: function(options) {
                if (!options.tip) {
                    options.tip = null
                }
                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg'><span class='order-icon order-icon-fail'></span>" +
                    options.tip +
                    "</div>" +
                    "</div>";

                var opt = {
                    width: "310px",
                    height: "143px",
                    content: popTpl,
                    time: null,
                    type: 'order-pop', //复用订单弹窗样式
                    reload: false
                }

                //options 替换默认值     
                if (options) {
                    $.each(options, function(key, value) {
                        if (opt[key] === null || opt[key] || opt[key] === false) {
                            opt[key] = value;
                        }
                    });
                }
                popPanel.init(opt);

                $(".popup-mask-layer").remove();
                $(".popup-close").remove();
            },
            //5.支付流程的提示框
            payTip: function(opt) {
                if (!opt.msg) {
                    return
                }

                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg' style='text-align:center'><span class='order-icon order-icon-fail'></span>" + opt.msg + "</div>" +
                    "</div>";

                var opt = {
                    width: "310px",
                    height: "143px",
                    content: popTpl,
                    type: 'order-pop'
                }
                popPanel.init(opt);
            },
            // 6.基本设置的提示框 
            info: function(options) {
                if (!options.tip) {
                    options.tip = null
                }
                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg'><span class='order-icon order-icon-fail'></span>" +
                    options.tip +
                    "</div>" +
                    "</div>";

                var opt = {
                    width: "310px",
                    height: "143px",
                    content: popTpl,
                    time: null,
                    type: 'order-pop', //复用订单弹窗样式
                    reload: false
                }

                //options 替换默认值
                if (options) {
                    $.each(options, function(key, value) {
                        if (opt[key] === null || opt[key] || opt[key] === false) {
                            opt[key] = value;
                        }
                    });
                }
                popPanel.init(opt);
                // $(".popup-close").remove();
            },
            /*7.全局提示弹框：可自动销毁
             *
             *@param {string} tip 弹窗内容
             *@returns void
             */
            tipAutoDestory: function(opt) {
                if (!opt.tip) {
                    opt.tip = null
                }

                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg'><span class='order-icon order-icon-fail'></span>" + opt.tip + "</div>" +
                    "<div class='order-button-row'>" +
                    "<a class='order-button' data-type='close'>稍后再试</a>" +
                    "</div>" +
                    "</div>";

                var opt = {
                    width: "310px",
                    height: "143px",
                    content: popTpl,
                    type: 'order-pop',
                    time: 3000

                }
                popPanel.init(opt);
            },
            // 8.参看参加我的talking的人的信息弹窗
            joinTip: function(opt) {
                if (!opt.tip) {
                    opt.tip = null
                }
                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg'><span class='order-icon order-icon-fail'></span>" +
                    opt.tip + "</div>" +
                    "</div>";

                var opt = {
                    width: "510px",
                    height: "343px",
                    content: popTpl,
                    type: 'order-pop' //复用订单弹窗样式

                }
                popPanel.init(opt);
            },
            // 9.上传用户头像
            uploadPhoto: function(opt) {
                if (!opt.tip) {
                    opt.tip = null
                }
                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg'><span class='order-icon order-icon-fail'></span>" +
                    opt.tip + "</div>" +
                    "</div>";

                var opt = {
                    width: "340px",
                    height: "223px",
                    content: popTpl,
                    type: 'order-pop' //复用订单弹窗样式

                };
                popPanel.init(opt);
            },
            //9.提现之前，输入登录密码
            checkPass: function() {
                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg'><label name='password'>请输入登录密码：</label><input name='password' type='password'/> </div>" +
                    "<div class='order-button-row'>" +
                    "<a class='order-button' data-type='quite-event' style='margin-right:20px'>确定</a>" +
                    "<a class='order-button' data-type='close'>取消</a>" +
                    "</div>" +
                    "</div>";

                var opt = {
                    width: "310px",
                    height: "143px",
                    content: popTpl,
                    type: 'order-pop', //复用订单弹窗样式
                    bind: {
                        "quite-event": this._quitEvent //绑定确认退出事件
                    }
                }
                popPanel.init(opt);
            },
            //10.分享
            share: function(cfg) {
                var $config = cfg;
                var popTpl = '<div style="width:168px; margin:54px auto;"><div class="share-component social-share"></div></div>';
                var opt = {
                    width: "320px", 
                    height: "200px",
                    content: popTpl,
                    type: 'order-pop', //复用订单弹窗样式
                    closeIcon: true, //不需要关闭的按钮
                    addevent: commonPop._shareEvent
                };

                popPanel.init(opt);
                debugger
                $('.social-share').share($config);
                debugger
                // Domready after initialization
                /*$(function() {
                    $('.social-share').share($config);
                });*/
            },
            /*
            11.提示信息的弹窗：成功信息，失败信息
            type:error ,默认是成功的弹窗
            msg：提示内容
            */
            tip: function(opt) {
                //第一次初始化提示框
                var init = function(opt) {
                    $('head').append('<link rel="stylesheet" href="/static/js/modules/topic-pop/tipPop.css" />');
                    var tpl =

                        '<div class="pop-wrap" id="J_popTip">' +
                        '<i class="pop-icon"></i>' +
                        '<i class="pop-close"></i>' +
                        '<p class="pop-text">' +
                        '{@if type == "error"}' +
                        '<i class="error"></i>' +
                        '{@else}' +
                        '<i class="success"></i>' +
                        '{@/if}' +
                        '<span class="pop-content">${msg}</span></p>' +
                        '</div>';
                    ;
                    tpl = juicer(funcTpl(tpl), opt);
                    $('body').append(tpl);
                    autoClose();
                };

                //重新绘制
                var paint = function(opt) {
                    var tpl =
                        '{@if type == "error"}' +
                        '<i class="error"></i>' +
                        '{@else}' +
                        '<i class="success"></i>' +
                        '{@/if}' +
                        '<span class="pop-content">${msg}</span>' ;


                    $("#J_popTip .pop-text").html(juicer(funcTpl(tpl), opt));

                    $("#J_popTip").show();
                    autoClose();
                };
                //3S之后隐藏
                var autoClose = function() {
                    setTimeout(function() {
                        var pop = $("#J_popTip");

                        if (pop.length == 0) {
                            return;
                        }
                        $("#J_popTip").hide();
                    }, 3000);
                }


                var popTip = $("#J_popTip");

                if (popTip.length == 0) {
                    init(opt);
                } else {
                    paint(opt);
                }

                //绑定关闭事件
                $("#J_popTip .pop-close").click(function(){
                    $("#J_popTip").hide();
                  }
                );
            },
            //切换学校弹窗，点击切换首页banner，牛人和精品专区
            _shareEvent: function() {
                var sharePop = $('#sharePop');
                if (!sharePop.length) {
                    $('head').append('<link rel="stylesheet" href="/static/js/modules/share/css/share.min.css" id="sharePop" />');
                }
            },
            // 14.提现
            getMoneyOut: function(opt) {
                if (!opt.tip) {
                    opt.tip = null
                }
                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='account-content' id='account-pop'><span class='order-icon order-icon-fail'></span>" +
                    opt.tip + "</div>" +
                    "</div>";

                var opt = {
                    width: "460px",
                    height: "273px",
                    content: popTpl,
                    type: 'order-pop', //复用订单弹窗样式
                    addevent: opt.addevent
                };
                popPanel.init(opt);
            },
        };
        

        return commonPop
    });