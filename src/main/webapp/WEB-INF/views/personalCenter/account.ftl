
<content tag="title">个人中心-交流干货,分享人生</content>
<content tag="css">/personal-center/account</content>
<content tag="javascript">/personal-center/account</content>

<!-- 个人中心通用头部导航 -->
<div class="personal-main-nav">
    <!-- personal-center/common/header.js render here -->
</div>
<!-- 个人中心通用头部导航结束 -->

<div class="account global-center clearfix">
    <!-- 左侧菜单栏 -->
    <div class="personal-aside">
        <!-- personal-center/common/aside.js render here -->
    </div>
    <!-- 左侧菜单栏结束 -->

    <!-- 个人中心主要内容 -->
    <div class="personal-content account-content clearfix">
        <!-- 详细列表 -->
        <div class="account-content">
            <!-- 提现申请 -->
            <div class="item-content item-money-out clearfix">
                <span class="item-breadcrumb f16">您当前的校园说余额为：<span id="J_accountMoney"></span></span>
                <div class="item item-operation">
                    <a href="javascript:;" class="operation submit" id="J_cashSubmit">提现</a>
                </div>

                <!-- 
                <div class="item">
                    <label><span> * </span>提现金额：</label>
                    <input type="text" id="J_cash" class="item-input item-text-input">
                    <div class="account-info" style="display:inline-block">
                        <i class="icon-info"></i>
                        <span>目前只提供支付宝提现方式</span>
                    </div>
                </div>

                <div class="item item-operation">
                    <a href="javascript:;" class="operation submit" id="J_cashSubmit">申请提现</a>
                </div> 
                -->
            </div>

            <!-- 账户信息 -->
            <div class="item-content item-money-account clearfix" style="height:260px">
                <span class="item-breadcrumb f18">如果出现退款情况，将退到您这个账号：</span>
                <div class="item item-zhifubao">
                    <label><span> * </span>收款人支付宝：</label>
                    <input type="text" name="zhifubao" class="item-input item-text-input">
                    <div class="account-info">
                        <i class="icon-info"></i>
                        <span>目前只提供支付宝一种结算方式</span>
                    </div>
                </div>
                <div class="item item-realname">
                    <label><span> * </span>收款人的姓名：</label>
                    <input type="text" name="realname" class="item-input item-text-input">
                    <div class="account-info">
                        <i class="icon-info"></i>
                        <span>请务必与身份证的姓名一致</span>
                    </div>
                </div>
                <div class="item item-agreement">
                    <label></label>
                    <input type="checkbox" name="agreement">我已同意校园说<a href="javascript:;" id="agreement" style="color:#00caab;">支付协议</a>
                </div>
                <div class="item item-operation">
                    <a href="javascript:;" class="operation submit J_submit">提交</a>
                </div>
            </div>

            <div class="item-content item-trade-record clearfix">
                <span class="item-breadcrumb f18">我的交易记录：</span>
                <table class="item-trade-table">
                    <thead>
                        <td>交易时间</td>
                        <td>支付类型</td>
                        <td>交易详情</td>
                        <td>金额（元)</td>
                        <td>交易状态</td>
                    </thead>
                    <tbody id="J_record">
                        <!-- personal-center/account.js render here -->

                    </tbody>
                </table>
            </div>

        </div>

        <!-- 翻页-->
        <div id="page"></div>
    </div>
    <!-- 个人中心主要内容结束 -->
</div>