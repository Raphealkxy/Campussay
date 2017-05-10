require.config({
    baseUrl: CP.STATIC_ROOT
});
require(['lib/jquery', 'util/request', 'util/funcTpl', 'modules/baseMoudle', 'modules/topic-pop/commonPop','modules/popup/signPop', 'lib/juicer'],
    function($, request, funcTpl, baseMdl, commonpop,pop) {
        var index = {
            init: function() {


                //index._checkLogin();
                //tid表示课程id，orderid表示订单id
                var orderid = this.getUrlParam("orderid");
                var talkid = this.getUrlParam("tid");
                this.order.id = orderid;
                this.order.talkingId = talkid;

                if (!orderid) {
                    //1.支付第一步：提交订单。tid表示课程id
                    this._stepone();
                } else {
                    //2.支付第二步：选择支付方式。orderid表示订单编号
                    var promise = this._getTalk();
                    promise.then(this._steptwo);
                }

            },
            _checkLogin:function(){
            var ajax = request.get('/user/isLogin',{},function(rsp){
                rsp = JSON.parse(rsp);

                if(rsp.code != -1){
                   pop();
                }
            });

            return ajax;
            },
            order: {
                id: "", //订单id
                talkingId: null, //课程id
                talk: null
            },
            //支付第一步：提交订单.tid表示订单。orderid表示跳转
            _stepone: function() {
                var courseId = this.getUrlParam("tid"),
                    self = this;

                this.order.talkingid = courseId;

                request.get('/talking/getTalkingInfoForCreateOrder', { talkingId: courseId }, function(rsp) {
                    rsp = JSON.parse(rsp);
                    if (rsp.code == 1) {
                        var tmpl,
                            talk,
                            tplData = {},
                            dom = $("#J-step-one-content");

                        tplData.talk = rsp.data;
                        tplData.talk.id = self.order.talkingid;
                        talk = rsp.data;
                        self.order.talk = rsp.data;

                        self._handleTalkData(rsp.data);
                        tmpl = juicer(funcTpl(self._courseTpl), tplData);
                        dom.append(tmpl);
                        
                        //页面滚动到顶部 为什么没生效
                        //console.log($(document).scrollTop());
                        //$("document").animate({scrollTop:"0px"},1000);

                        $("#J-input-phone").val(talk.now_user_phone);
                        $("#J-input-username").val(talk.now_user_name);

                        self._bindStepOne();

                    }else if(rsp.code == -1){
                        var path = window.location.pathname+"search";
                        var key = window.location.search.substr(1).replace('=','$');
                        window.location.href = "/user/sign-in-web?url="+path+key;
                    }else{
                        baseMdl.infoNotreload("出错了"+rsp.msg);
                    }
                });
            },
            _handleTalkData: function(talk) {
                var month,
                    day,
                    startTime,
                    endTime,
                    minunte,
                    talkDate = new Date(talk.talking_end_time);

                month = talkDate.getMonth() + 1;
                day = talkDate.getDate();
                startTime = talk.talking_start_time.split(/\s+/)[1].split(":").slice(0, 2).join(":");
                minunte = talkDate.getMinutes();
                minunte = minunte % 10 === minunte ? "0" + talkDate.getMinutes() : talkDate.getMinutes();
                endTime = talkDate.getHours() + ":" + minunte;

                talk.date = month + "月" + day + "日";
                talk.currentTime = startTime + "—" + endTime;
            },
            //支付第一步：提交订单
            _bindStepOne: function() {
                var self = this,
                    clickTime;

                $("#J-infor-submit").on('click', function() {
                    var order = {},
                        orderParam;

                    //加上5分钟的时间戳，让button在5分钟之内,不可以点击
                    if (!clickTime) {
                        clickTime = new Date().getTime() + 60 * 1 * 1000;
                    } else if (clickTime > new Date().getTime()) {
                        return
                    }


                    order.phone = $("#J-input-phone").val();
                    if (!order.phone) {
                        $("#J-input-phone").attr("placeholder", "电话不能为空");
                        return;
                    }

                    //检测电话号码，13位数字非法格式
                    order.user = $("#J-input-username").val();
                    if (!order.user) {
                        $("#J-input-username").attr("placeholder", "名字不能为空");
                        return;
                    }
                    order.msg = $("#J-input-message").val();

                    orderParam = {
                        talkingId: self.order.talkingid,
                        order_user_tel: order.phone,
                        order_user_realname: order.user,
                        order_user_extr_info: order.msg
                    };

                    $(this).html("正在提交");
                    request.post("/order/createOrderInfo", orderParam, function(rsp) {
                        rsp = JSON.parse(rsp);
                        //下单成功
                        if (rsp.code == 1) {
                            self.order.id = rsp.data.orderId;
                            self._steptwo();
                        } else if (rsp.code == 100009) {
                            commonpop.payTip({msg:"您已经拥有该课程"});
                            setTimeout(function(){
                                window.location.href = '/personalCenter/order';
                            },1000);
                        }
                    });

                });
            },
            //支付第二步：选择支付方式
            _steptwo: function() {
                $("#J_stepbg").removeClass("step-1").addClass("step-2");

                var tmpl = juicer(funcTpl(index._stepTwoTpl), index.order);
                $("#J-pay-content").html(tmpl);
                index._bindStepTwo();
            },
            _bindStepTwo: function() {
                //确认支付
                var self = this;
                $("#J_confirmPay").on('click', function() {
                    var param = {
                        orderid: self.order.id
                    };
                    var form;
                    var newPageWindow = window.open('/talking/ttIndex');

                    var ajax = request.get('/order/gotoAliPay', param, function(rsp) {
                        rsp = JSON.parse(rsp);
                        if (rsp.code == 1) {
                            var form = '';
                            window.form = rsp.data.sHtmlText;
                            //bug,新开弹窗会被拦截

                            //var newPageWindow = window.open('/talking/ttIndex');

                            //window.test = newPageWindow;
                            //setTimeout(function(){
                                //this.test.document.write(rsp.data.sHtmlText)},1000);

                        }
                    });

                    ajax.done(function(){
                        var x = window.form;
                        newPageWindow.document.write(window.form);
                    });
                });
            },
            _getTalk: function() {
                var self = this;
                var promise = request.get('/talking/getTalkingInfoForCreateOrder', { talkingId: this.order.talkingId }, function(rsp) {
                    rsp = JSON.parse(rsp);
                    if (rsp.code == 1) {
                        self.order.talk = rsp.data;
                        self._handleTalkData(rsp.data);
                    }
                });
                return promise;
            },
            _courseTpl: function() {
                /*
                <!--确认产品信息-->
        <div class="confirm">
            <h1 class="cf-head">确认产品信息：</h1>
            <div class="cf-body">
                <ul class="infor-title">
                    <li>交流主题</li>
                    <li>说友</li>
                    <li>学校</li>
                    <li>交流时间</li>
                    <li>小记（元）</li>
                </ul>

                <ul class="infor-body">
                    <li class="infor-body-topic">
                        <a href="/talking/classDetail?tk=${talk.id}" target="_black"><img src=${talk.talking_main_picture}></a>
                        <a href="/talking/classDetail?tk=${talk.id}" target="_black"><p>${talk.talking_title}</p><a>
                    </li>
                    <li class="sayer">
                        <span style="">${talk.user_name}</span>
                    </li>
                    <li class="school">${talk.campus_name}</li>
                    <li class="time">
                        <span style="margin-top:44px">${talk.date}</span>
                        <span style="margin-top:30px">${talk.currentTime}</span>
                    </li>
                    <li class="money">${talk.talking_price}</li>
                </ul>

                <div class="total-money">
                    犒劳总额&nbsp;:&nbsp;<span>¥&nbsp;&nbsp;${talk.talking_price}</span>
                </div>
                <div class="infor-submit">
                    <a class="infor-button" id="J-infor-submit">提交</a>
                </div>
            </div>
        </div>
    </div>
            */
            },
            _stepTwoTpl: function() {
                /*
            <!--支付第二步，选择支付方式-->
    <div class="pay-content">
        <!--订单信息-->
        <div class="orderform">
            <h1 class="orderform-hd"> <span></span>订单提交成功</h1>
            <div class="orderform-bd">
                <div class="orderform-bd-left">您需要犒劳小伙伴&nbsp;&nbsp;:&nbsp;&nbsp;<span class="money">¥&nbsp;&nbsp;${talk.talking_price}</span></div>
                <div class="orderform-bd-right">
                    <p>订单号：<span>${id}</span></p>
                    <p>${talk.talking_title}<span style="margin-left:30px;">${talk.user_name}</span></p>
                    <p>交流时间&nbsp;&nbsp;:&nbsp;&nbsp;<span>${talk.date}</span>&nbsp;&nbsp;<span>${talk.currentTime}</span></p>
                </div>
            </div>
        </div>
        <!--选择支付方式-->
        <div class="select-pay">
            <h1 class="sp-hd">支付方式 ：</h1>
            <div class="sp-bd">
                <div class="select-pay-method">
                    <a class="zhifubao"></a>
                </div>
                <div><a class="seletpay-button" id="J_confirmPay">确认支付</a></div>
            </div>
        </div>
    </div>
            */
            },
            getUrlParam: function(name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
                var r = window.location.search.substr(1).match(reg); //匹配目标参数
                if (r != null) return unescape(r[2]);
                return null; //返回参数值
            },

        }

        index.init();
    });
