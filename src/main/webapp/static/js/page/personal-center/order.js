/**
 * 个人中心  首页  我的订单
 *
 * @author: wangxinyu
 * @date: 2015/12/30
 * @last-modified: 2016/1/24
 *
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'util/funcTpl', 'util/request', 'util/getLocalTime', 'page/personal-center/common/layout',
        'modules/page/page', 'modules/topic-pop/popPanel', 'modules/topic-pop/orderPop', 'lib/juicer','modules/baseMoudle'],
    function($, funcTpl, request, getTime, layout, pager, pop, orderpop) {

        var order = {
            //页面全局对象:state,表示订单不同状态（null:全部订单；1:未付款订单；）
            pageState: {
                state: null
            },
            init: function() {
                var opt = {
                    tab: '',
                    menu: 'order',
                    callback: order._getFirstOrders //初始化会调用此回调函数
                };

                //初始化，右边导航列表
                layout(opt);
                //orderpop.confirmSuccess("33");

            },
            //初始化第一页订单
            _getFirstOrders: function(pageNum, state) {
                //1.获取第一页的全部订单
                order._getOrders({ pageNum: 1 });

                //2.获取翻页钩子函数->回调为翻页函数
                var initPage = pager.init('#page', function(num) {
                    var param = {
                        pageNum: num,
                        state: order.pageState.state
                    };
                    order._getOrders(param);
                });
                //初始化,one方法只监听一次
                $('body').one('page:one', function(ev, size, totalList) {
                    initPage(size, totalList);
                });

                //3.点击切换不同状态订单
                order._tapOrder();
            },
            /*获取订单
              参数：{}
              pageNum 页码
              state   查询不同订单的状态。
            */
            _getOrders: function(cfg) {
                var self = this,
                    param = {};

                if (cfg.pageNum) {
                    param.pageNum = cfg.pageNum
                }
                if (cfg.state) {
                    param.state = cfg.state
                }

                request.get('/order/selectUserOrderByStatus', param, function(rsp) {
                    rsp = JSON.parse(rsp);
                    if (rsp.code == 1) {
                        var tmpl,
                            tplData = {
                                orderlist : rsp.data.resultList,
                            };
                        //将返回的系统时间，转换为毫秒。好做对比     
                        if(rsp.data.system_time){
                            tplData.systemtime = new Date(rsp.data.system_time).getTime();
                        }    

                        tmpl = juicer(funcTpl(order.order_tpl), tplData);
                        $('.order-content-list').html(tmpl);

                        //1.触发分页
                        $('body').trigger('page:one', [10, rsp.data.dataCount]);
                        //2.添加事件
                        order._event();
                    }
                    //100011表示暂无数据
                    if (rsp.code == 100011) {

                        $('.order-content-list').html($.emptyContentImg());
                    }
                });
            },
            _tapOrder: function() {
                var self = this;

                $(".tab-item").click(function() {
                    var x = $(this).attr("action-state");
                    self.pageState.state = $(this).attr("action-state");
                    self._getOrders({ pageNum: 1, state: self.pageState.state });
                });
            },
            _event: function() {
                //1.立即支付跳转
                $('.item-pay-now-link').on('click', function() {
                    // TODO: 跳转
                });

                //2.确认课程完成
                var confimFlag = true; //标志，可以确认状态
                $(".J_order_confirm").on('click', function() {
                    var orderid = $(this).attr('data-orderid'),
                        self = this;

                    if (!orderid && !confimFlag) {
                        return;
                    }

                    confimFlag = false;
                    request.get('/order/confirmTakingFinish', { orderId: orderid }, function(rsp) {
                        rsp = JSON.parse(rsp);

                        if (rsp.code == 1) {
                            orderpop.confirmSuccess(orderid);
                        } else {
                            orderpop.confirmFail();
                        }
                        confimFlag = true;
                    });
                });

                //3.课程评价（弹窗）
                $('.item-comment').on('click', 'a', function() {
                    var orderId = $(this).attr('data-orderid');
                    var talkTitle = $(this).attr('data-title');

                    if(!orderId||!talkTitle){
                        return
                    }
                    orderpop.comment({head:talkTitle,orderId:orderId});
                    /*pop.init({
                        width: '870px',
                        height: '452px',
                        content: tpml,
                        type:"comment-pop"
                    });*/

                    // 提交评价

                    /*$('#J_submit').on('click', function() {
                        var comment = $('#J_des').val().trim();

                        if (!comment) {
                            $('#J_des').attr('placeholder', '评价不能为空哦~');
                            return;
                        }
                        if (!orderId) {
                            return;
                        }

                        request.get('/comment/lpySaveTalkingComment', {
                            "orderId": orderId,
                            "strTalkingComment": comment
                        }, function(res) {
                            res = JSON.parse(res);
                            if (res.code == 1) {
                                $('#J_des').val('评价成功~');
                                setTimeout(pop.destory, 1000);
                                thisdom.remove();
                            } else {
                                $('#J_des').val('评价失败~');
                                setTimeout(pop.destory, 1000);
                            }
                        });
                    });*/
                });

                //4.申请退款
                $(".J_order_refund").click(function() {

                    var orderid = $(this).attr("data-orderid");

                    if (orderid) {
                        window.order = {
                            rebateOrderId: orderid
                        }
                    }
                    //退款申请的弹窗
                    orderpop.rebate();


                });
            },

            // 订单模板
            order_tpl: function() {
                /*
                 {@each orderlist as order}
                 <div class="order-list-item" data-id="${order.order_id}">
                     <div class="item-title f12">
                         <span class="order-time">${order.order_creat_time|getTime}</span>
                         <span class="order-number">订单 ID：${order.order_id}</span>
                         <span class="order-talker">说友：${order.user_name}</span>
                     </div>
                     <ul class="item-content clearfix">
                         <li class="item item-describe">

                             <div class="item-describe-pic">
                                 <a href="/talking/classDetail?tk=${order.talking_id}"><img src="${order.talking_main_picture}" alt="我的订单" width="160" height="97"></a>
                             </div>


                             <div class="item-describe-info">
                                 <div class="item-basic-info f16">${order.talking_title}</div>
                                 <div class="item-money-info">
                                     <span class="item-price f24">${order.order_price}</span>
                                 </div>
                             </div>    
                             </li>
                             <li class="item item-progress">
                                 <div class="item-progress-show f16" data = ${order.order_creat_time    }>
                                     {@if order.order_state == 1}待支付
                                     {@else if order.order_state == 0}已作废
                                     {@else if order.order_state == 2}
                                        {@if systemtime > order.talking_start_time && systemtime <order.talking_end_time}
                                            正在上课
                                        {@else}
                                            课程暂未开始   
                                        {@/if}           
                                     {@else if order.order_state == 3}待确认
                                     {@else if order.order_state == 4}待评价
                                     {@else}
                                      已结束
                                     {@/if}
                                 </div>
                             </li>
                             <li class="item item-operation">

                                 <div class="table-cell">
                               
                                 {@if order.order_state == 1}
                                     <div class="link item-view-detail"><a href="/talking/classDetail?tk=${order.talking_id}">查看详情</a></div>
                                     <div class="link item-pay-now-link"><a href="/pay?orderid=${order.order_id}&tid=${order.talking_id}">立即支付</a></div>
                                 {@else if order.order_state == 0}
                                     <div class="link item-view-detail"><a href="/talking/classDetail?tk=${order.talking_id}">查看详情</a></div>    
                                 {@else if order.order_state == 2}
                                     {@if systemtime > order.order_creat_time && systemtime < order.talking_end_time}
                                        <div class="link item-view-detail"><a href="/talking/classDetail?tk=${order.talking_id}">查看详情(已付款)</a></div>
                                                
                                     {@else}   
                                        <div class="link item-view-detail"><a href="/talking/classDetail?tk=${order.talking_id}">查看详情(已付款)</a></div>
                                     {@/if}
                                 {@else if order.order_state == 3}
                                     <div class="link item-view-detail"><a href="/talking/classDetail?tk=${order.talking_id}">查看详情</a></div>
                                     <div class="link"><a  class="J_order_confirm" href="javascript:;" data-orderid=${order.order_id}>确认付款</a></div>
                                     <div class="link"><a  class="J_order_refund" href="javascript:;" data-orderid=${order.order_id}>申请退款</a></div>
                                 {@else if order.order_state == 4}
                                     <div class="link item-view-detail"><a href="/talking/classDetail?tk=${order.talking_id}">查看详情</a></div>
                                     <div class="link item-comment"><a href="javascript:;" data-orderid=${order.order_id} data-title=${order.talking_title}>立即评价</a></div>
                                 {@else if order.order_state == 5}
                                     <div class="link item-view-detail"><a href=/talking/classDetail?tk=${order.talking_id}>查看评价</a></div>    
                                 {@else if order.order_state == 6}
                                     <div class="link item-view-detail"><a>退款审核中</a></div>
                                 {@else if order.order_state == 7}
                                     <div class="link item-view-detail"><a>成功退款</a></div>
                                 {@else if order.order_state == 8}
                                     <div class="link item-view-detail"><a>退款失败</a></div>          
                                 {@else}
                                     <div class="link">&nbsp;没有专题</div>
                                     <div class="link item-view-detail"><a href="/talking/classDetail?tk=${order.talking_id}"></a></div>
                                 {@/if}
                                 </div>
                                 </div>
                             </li>
                         </ul>
                 </div>
                 {@/each}
                 */
            },

            // 评价弹窗模板
            comment_tpl: function() {
                /*
                 <div class="comment-pop-content">
                 <div class="row">
                    <h1>ddd</h1>
                 </div>
                 <div class="row">
                     <div class="row-left">订单评价 <span class="colon">:</span> </div>
                     <div class="row-right">
                         <textarea placeholder="亲，给个好评吧~" class="ask-detail" id="J_des"></textarea>
                     </div>
                 </div>
                 <div class="row">
                     <div class="row-right row-button">
                         <a class="ask-button ask-button-pub" id="J_submit">确定</a>
                         <a class="ask-button ask-button-cancle" data-type="close">取消</a>
                     </div>
                 </div>
                 </div>
                */
            }
        };

        order.init();

    });
