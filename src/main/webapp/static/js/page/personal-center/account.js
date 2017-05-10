/**
 * 个人中心  首页  收款账户
 *
 * @author: wangxinyu
 * @date: 2016-1-18
 * @last-modified: 2016-1-21
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'util/funcTpl', 'util/request', 'util/getLocalTime', 'page/personal-center/common/layout',
        'modules/page/page', 'modules/topic-pop/commonPop', 'modules/baseMoudle','lib/juicer'],
    function ($, funcTpl, request, getTime, layout, pager, pop,basemdl) {

        var account = {
            init:function () {
                var opt = {
                    tab: '',
                    menu: 'account',
                    callback: account.event
                };
                layout(opt);

                //1.获取账号信息
                account.initAliPayAccount();
                //2.获取收入纪录
                account.getTradeRecord();
                //3.翻页
                account.page();
            },
            // 账号是否已经设置
            _isSettled: 0,
            // 数据集合
            _data:{
                getTradeRecordUrl: '/order/getMyTalkingSaleInfo',
                getAliPayUrl: '/user/getUserPayAccountInfo',
                setAliPayUrl: '/user/setUserPayAccountInfo',
                agreement: '这里是支付协议'
            },
            _account: {},
            // 收入记录模板
            account_tpl:function () {
                /*
                {@each rows as record}
                  <tr> 
                    <td>${record.time|getTime}</td>
                    {@if record.stand == '1'}
                        <td>购买talk</td>
                        <td>订单编号：${record.id}
                            <a href=/talking/classDetail?tk=${record.talking_id}>${record.talking_title}</a>
                        </td>
                        <td>-${record.cash}</td>
                        {@if record.status == 1 }
                            <td>未付款</td>
                        {@else if record.status ==0 }    
                            <td>失败</td>
                        {@else}
                            <td>成功</td>
                        {@/if}    
                    {@/if}

                    {@if record.stand == '2' && record.status == '2' }
                        <td>收入</td>
                        <td>订单编号：${record.id}
                            <a href=/talking/classDetail?tk=${record.talking_id}>${record.talking_title}</a>
                        </td>
                        <td>+${record.cash}</td>
                        <td>成功</td>
                    {@/if}


                    {@if record.stand == '3'}
                        <td>退款</td>
                        <td>订单编号：${record.id}
                            <a href=/talking/classDetail?tk=${record.talking_id}>${record.talking_title}</a>
                        </td>
                        <td>+${record.cash}</td>
                        {@if record.status == 6 }
                            <td>后台审核中</td>
                        {@else if record.status == 7}    
                            <td>退款成功</td>
                        {@else if record.status == 8 }
                            <td>退款驳回</td>
                        {@/if}       
                    {@/if}
                    
                    {@if record.stand == '4'}
                        <td>提现</td>
                        <td>订单编号：${record.id}
                        </td>
                        <td>+${record.cash}</td>
                        {@if record.status == 0 }
                            <td>后台审核中</td>
                        {@else if record.status == 1}    
                            <td>成功</td>
                        {@else if record.status == 2 }
                            <td>失败</td>
                        {@/if}       
                    {@/if}
                  </tr>
                {@/each}
                */
            },
            // 获取收入记录
            getTradeRecord:function (page) {
                request.post(account._data.getTradeRecordUrl, {
                    "page":page
                }, function (res) {
                    res = JSON.parse(res);
                    if (res.code == 1) {
                        var account_tpl = juicer(funcTpl(account.account_tpl), res.data);
                        console.log(account_tpl);
                        //$('.item-trade-table tbody').empty().append(account_tpl);
                        $("#J_record").append(account_tpl);
                        //触发翻页初始化
                        $('body').trigger('page:set', [10, res.data.count]);
                    }
                });
            },
            switchAccountState:function($self) {
                if (account._isSettled) {
                    $('.item>input').attr('disabled', 'disabled');
                    $('.account-info').html('<a href="javascript:;" class="modifyAccount">修改</a>');
                    $('.item-agreement').hide();
                } else {
                    var $input = $self.parent().prev();
                    $input.removeAttr('disabled').focus();
                    $('.item-agreement input').attr('checked', 'checked');
                }
            },
            // 获取已设置的支付宝账号
            initAliPayAccount:function () {
                request.post(account._data.getAliPayUrl, {}, function (res) {
                    res = JSON.parse(res);
                    if (res.code == 1) {
                        if (res.data.userPayAccount) {
                            account._isSettled = 1;
                            account._account = res.data;
                            account.switchAccountState();
                            $('.item-zhifubao input').val(res.data.userPayAccount);
                            $('.item-realname input').val(res.data.userPayName);
                        }
                        $("#J_accountMoney").html(res.data.userBalance+'元');
                    }
                });
            },
            // 设置支付宝账号
            setAliPayAccount:function () {
                // $('.item-operation').off('click');
                $('.item-operation').on('click', '.J_submit', function () {
                    var aliAccount = $('.item-zhifubao input').val().trim(),
                        aliName = $('.item-realname input').val().trim(),
                        agree = $('.item-agreement input').is(':checked');

                    if (!agree) {
                        pop.info({tip:'你需要同意校园说支付协议'});
                        return;
                    }

                    if (aliAccount == "" && aliName == "") {
                        pop.info({tip:'请输入支付宝账号和姓名'});
                    } else {
                        request.post(account._data.setAliPayUrl, {
                            "userPayAccount":aliAccount,
                            "userPayName":aliName
                        }, function (res) {
                            var msg = '';
                            res = JSON.parse(res);
                            msg = (res.code==1) ? '保存成功' : '保存失败';

                            if(res.code == 1){
                                account._isSettled = 1;
                            }
                            pop.info({ tip: msg, time: 1500 });
                        });
                    }
                });
            },
            // 修改账号
            modifyAccount:function () {
                $('.account-info').on('click', '.modifyAccount', function () {
                    var _this = $(this);
                    account._isSettled = 0;
                    account.switchAccountState(_this);
                });
            },

            event:function () {
                // save aliPayAccount
                account.setAliPayAccount();
                account.modifyAccount();
                
                // 同意支付协议
                $('#agreement').on('click', function () {
                    pop.info({
                        tip:account._data.agreement
                    });
                });

                //3.提取现金弹窗
                $('#J_cashSubmit').on('click', function() {
                    //先检查，是否设置了支付宝账号
                    if(!account._isSettled){
                        pop.info({tip:"请先设置，支付宝账号"
                        ,time:1000});

                        return;
                    }


                    pop.getMoneyOut({
                        tip: juicer(funcTpl(account.getMoneyOutTpl), account._account),
                        addevent: confirmMoneyOut
                    });
                });

                //4.确认提取现金
                var confirmMoneyOut = function () {
                    var outMoney;

                    $('.operation').on('click', function() {
                        var $self = $(this).parents('.item-content'),
                            psw = '';
                        // 提现到支付宝
                        if ($self.hasClass('pop-step-1')) {
                            var $outMoney = $('#money-out');
                            var $error = $('#error-tip1');
                            outMoney = +$outMoney.val().trim();

                            if (outMoney == "" || isNaN(outMoney)) {
                                $error.html('请输入提现金额');
                                return;
                            }
                            if (outMoney>500 || outMoney>account._account.userBalance || outMoney<=0) {
                                $error.html('提现金额超过限制');
                                return;
                            }
                            account._account.moneyTake = outMoney;
                            $('#money-take').val(outMoney);
                            $self.slideUp().next().fadeIn(1000);

                        }
                        // 提现信息确认 
                        else {
                            var $psw = $('#login-password');
                            var $error = $('#error-tip2');
                            psw = $psw.val().trim();
            
                            if (psw == "") {
                                $error.html('密码不能为空');
                                return;
                            } else {

                                request.post('/user/applyToCash', {
                                    password: psw,
                                    cash: outMoney
                                }, function(res) {
                                    res = JSON.parse(res);
                                    if (res.code == 100016) {
                                        $self.html('您有提现申请正在审核中，暂不能重复申请，请见谅');
                                    } else {
                                        $error.html(res.msg); 
                                    }

                                });
                            }
                        }

                    });

                    
                };
            
                // juicer func
                var orderState = function (n) {
                    if (n == 1) {
                        return '失败';
                    } else {
                        return '成功';
                    }
                };
                juicer.register('orderState', orderState);
            },

            page:function () {
                //获取翻页钩子函数->回调为翻页函数
                var initPage = pager.init('#page', function (num) {
                    account.getTradeRecord(num);
                });
                //初始化
                $('body').one('page:set', function (ev, size, totalList) {
                    initPage(size, totalList);
                });
            },

            getMoneyOutTpl: function () {
                /*
                <div class="item-content item-money-pop pop-step-1 clearfix">
                    <span class="item-breadcrumb f18">提现到支付宝</span>
                    <div class="item item-money-now">
                        <label>现有金额:</label>
                        <input type="text" id="money-now" class="item-input item-text-input" value="${userBalance}元" disabled>
                    </div>
                    <div class="item item-money-take">
                        <label>提取金额：</label>
                        <form action="#" autocomplete="off" style="display:inline">
                        <input type="text" id="money-out" class="item-input item-text-input" autocomplete="off" />
                        </form>
                        <div class="account-info">
                            <label></label>
                            <i class="icon-info"></i>
                            <span>最高提现金额<span style="color:#fca825;">500.00</span>元</span>
                        </div>
                    </div>
                    <div class="item item-error">
                        <div class="account-info">
                            <label> </label>
                            <span id="error-tip1" style="color:#f06060;"></span>
                        </div>
                    </div>
                    <div class="item item-operation">
                        <a href="javascript:;" class="operation submit" id="J_nextstep">下一步</a>
                    </div>
                </div>
                <div class="item-content item-money-pop pop-step-2 clearfix" style="display:none;">
                    <span class="item-breadcrumb f18">提现信息确认</span>
                    <div class="item item-account-info">
                        <label>支付宝账户：</label>
                        <input type="text" id="money-account" class="item-input item-text-input" value="${userPayAccount}" disabled>
                    </div>
                    <div class="item item-money-take">
                        <label>提现金额：</label>
                        <input type="text" id="money-take" class="item-input item-text-input" disabled>
                        <span>元</span>
                    </div>
                    <div class="item item-login-passwod">
                        <label>登录密码：</label>
                        <input type="password" id="login-password" class="item-input item-text-input" autocomplete="off">
                    </div>
                    <div class="item item-error">
                        <div class="account-info">
                            <label> </label>
                            <span id="error-tip2" style="color:#f06060;"></span>
                        </div>
                    </div>
                    <div class="item item-operation">
                        <a href="javascript:;" class="operation submit" id="J_confirm">确认</a>
                    </div>
                </div>
                 */
            }
        };

        account.init();
    }
);