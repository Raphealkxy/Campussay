define(['lib/jquery', 'modules/topic-pop/popPanel', "util/funcTpl", 'util/request', 'lib/juicer'],
    function($, popPanel, funcTpl, request) {

        var order = {
            //评价弹框
            //参数：head表示标题,orderId
            comment:function(opt){
                var popTpl = juicer(funcTpl(this.comment_tpl),opt);

                var opt = {
                    width: "870px",
                    height: "452px",
                    content: popTpl,
                    type: 'comment-pop',//覆盖，评价弹框样式
                    bind:{
                        "comment-event":this.commentSubmit
                    },
                    addevent:this.checkNumber,
                    reload:true
                };

                popPanel.init(opt);
            },
            //检查超出字数
            checkNumber:function(){

                $("#J_commentTxt").bind('input propertychange',function(){
                    var numConut = 200;//评价总字数

                    var commentNum  = $("#J_commentTxt").val().trim().length;//用户输入字数

                    var overNum = commentNum - numConut;

                    if(overNum > 0){
                        $(".tip-msg").html("已超过<span class='over-number'>"+overNum+"</span>字");
                    }
                });
            },
            comment_tpl: function() {
                /*
                 <div class="comment-pop-content">
                     <div class="row">
                        <h1>#${head}#</h1>
                     </div>
                     <div class="tip-msg">
                         可以输入200字的评价
                     </div>

                     <div class="comment-info">
                        <textarea placeholder="请输入你的评价吧" id="J_commentTxt"></textarea>
                     </div>

                     <div class="comment-button-row">
                             <a class="comment-button" data-type="comment-event" data-orderid=${orderId}>评价</a>
                     </div>
                 </div>
                */
            },
            //评论弹框，提交评论事件
            commentSubmit:function(e){
                var orderid = e.attr("data-orderid");
                var comment = $("#J_commentTxt").val().trim();

                if(!comment){
                    $("#J_commentTxt").attr("placeholder","请填写评价");
                }
                
                if(comment.length != 0){
                    var overNum = comment.length - 200 ;
                }


                if(overNum > 0){
                    $(".tip-msg").html("已超过<span class='over-number'>"+overNum+"</span>字");
                    return
                }

                if(!orderid){return}

                var param = {
                    orderId:orderid,
                    strTalkingComment:comment
                }    
                request.get('/comment/lpySaveTalkingComment',param,function(res){
                    res = JSON.parse(res);
                    if (res.code == 1) {
                        popPanel.destory();
                        window.location.reload();
                    }else{
                    $("#J_commentTxt").attr("placeholder","提交失败，请稍后重试");
                        setTimeout(popPanel.destory,2000);;    
                    }
                });    
            },
            //申请退款的弹窗
            rebate: function(orderid) {
                var popTpl = "<div class='order-pop-content order-rebate-content'>" +
                    "<div class='order-msg order-rebate-msg'>" +
                    "<p>退款原因：</p>" +
                    "<div><textarea class='order-rebate-textarea' id='J_rebatereason'></textarea></div>" +
                    "</div>" +
                    "<div class='order-button-row J_orderButton'>" +
                    "<a class='order-button' style='margin-right:45px' data-type='rebate-event'>确认</a>" +
                    "<a class='order-button rebate-button-cancle' data-type='close'>取消</a>" +
                    "</div>" +
                    "</div>";

                var opt = {
                    width: "310px",
                    height: "210px",
                    content: popTpl,
                    type: 'order-pop',
                    bind: {
                        'rebate-event': this.rebateEvent
                    },
                    reload:true
                }

                popPanel.init(opt);
            },
            //提交退款事件
            rebateEvent: function() {
                var reason = $("#J_rebatereason").val().trim();
                if (!reason) {
                    $("#J_rebatereason").attr('placeholder', "请填写退款原因");
                    return;
                }

                if (!window.order.rebateOrderId) {
                    $("#J_rebatereason").parent().html("出错，请稍后重试");
                    return;
                }

                //确保退款订单存在
                if (!window.order && !window.order.rebateOrderId) {
                    return
                }

                var param = {
                    orderId: window.order.rebateOrderId,
                    reason: reason
                }

                request.get('/order/requestRefund', param, function(rsp) {
                    rsp = JSON.parse(rsp);

                    if (rsp.code == 1) {
                        var successinfo = "<div class='order-msg'><span class='order-icon order-icon-fail'></span>申请已提交，我们会尽快处理</div>" +
                            "<div class='order-button-row'>" +
                            "<a class='order-button' data-type='close'>朕知道了</a>" +
                            "</div>" +
                            "</div>";

                        $(".order-pop-content").html(successinfo);
                        $(".popup").css('height', '143px');
                        //popPanel.autoDestory();

                    } else {
                        var failinfo =
                            "<div class='order-msg'><span class='order-icon order-icon-fail'></span>申请失败</div>" +
                            "<div class='order-button-row'>" +
                            "<a class='order-button' data-type='close'>稍后再试</a>" +
                            "</div>";
                        $(".order-pop-content").html(failinfo);
                        $(".popup").css('height', '143px');
                        //popPanel.autoDestory();

                    }

                });
            },
            //提交申请弹窗
            submitdemand: function() {
                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg'><span class='order-icon order-icon-fail'></span>申请已提交，我们会尽快处理</div>" +
                    "<div class='order-button-row'>" +
                    "<a class='order-button' data-type='close'>朕知道了</a>" +
                    "</div>" +
                    "</div>";

                var opt = {
                    width: "310px",
                    height: "143px",
                    content: popTpl,
                    type: 'order-pop'
                }
                popPanel.init(opt);
            },
            //确认成功弹窗
            confirmSuccess: function(orderid) {
                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg'><span class='order-icon order-icon-fail'></span>确认成功</div>" +
                    "<div class='order-button-row'>" +
                    "<a class='order-button' data-type='commit-event' data-orderid=" + orderid + ">立即评价</a>" +
                    "</div>" +
                    "</div>";

                var opt = {
                    width: "310px",
                    height: "143px",
                    content: popTpl,
                    type: 'order-pop',
                    bind: {
                        "commit-event": order.commentEvent
                    },
                    reload:true
                }
                popPanel.init(opt);
            },
            //点击确认弹框的，立即评价
            commentEvent: function(thisDom) {
                var orderid = thisDom.attr("data-orderid");

                if (!orderid) {
                    return
                }

                var commentTpl = 
                    "<div class='order-msg order-rebate-msg'>" +
                    "<p>评价：</p>" +
                    "<div><textarea class='order-rebate-textarea' id='J_commentText'></textarea></div>" +
                    "</div>" +
                    "<div class='order-button-row J_orderButton'>" +
                    "<a class='order-button' style='margin-right:45px' id='J_commentsubimt' >确认</a>" +
                    "<a class='order-button rebate-button-cancle' data-type='close'>取消</a>" +
                    "</div>";

                $(".order-pop-content").html(commentTpl);

                $("#J_commentsubimt").click(function() {
                    var comment = $("#J_commentText").val().trim();

                    if (!comment) {
                        $('#J_commentText').attr('placeholder', '评价不能为空哦~');
                        return;
                    }

                    request.get('/comment/lpySaveTalkingComment', {
                        "orderId": orderid,
                        "strTalkingComment": comment
                    }, function(res) {
                        res = JSON.parse(res);
                        if (res.code == 1) {

                            var successinfo = "<div class='order-msg'><span class='order-icon order-icon-fail'></span>评价成功</div>" +
                                "<div class='order-button-row'>" +
                                "<a class='order-button' data-type='close'>关闭</a>" +
                                "</div>" +
                                "</div>";

                            $(".order-pop-content").html(successinfo);
                            $(".popup").css('height', '143px');

                        } else {
                            var failinfo =
                                "<div class='order-msg'><span class='order-icon order-icon-fail'></span>提交失败</div>" +
                                "<div class='order-button-row'>" +
                                "<a class='order-button' data-type='close'>稍后再试</a>" +
                                "</div>";
                            $(".order-pop-content").html(failinfo);
                            $(".popup").css('height', '143px');
                        }
                    });
                });


            },
            //确认失败弹窗
            confirmFail: function() {
                var popTpl = "<div class='order-pop-content'>" +
                    "<div class='order-msg'><span class='order-icon order-icon-fail'></span>确认失败</div>" +
                    "<div class='order-button-row'>" +
                    "<a class='order-button' data-type='close'>稍后再试</a>" +
                    "</div>" +
                    "</div>";

                var opt = {
                    width: "310px",
                    height: "143px",
                    content: popTpl,
                    type: 'order-pop'
                }
                popPanel.init(opt);
            }
        }

        return order;
    });
