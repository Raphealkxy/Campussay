<content tag="title">个人中心-交流干货,分享人生</content>
<content tag="css">/personal-center/order</content>
<content tag="javascript">/personal-center/order</content>

<!-- 个人中心通用头部导航 -->
<div class="personal-main-nav">
    <!-- personal-center/common/header.js render here -->
</div>
<!-- 个人中心通用头部导航结束 -->

<div class="order global-center clearfix">
    <!-- 左侧菜单栏 -->
    <div class="personal-aside">
        <!-- personal-center/common/aside.js render here -->
    </div>
    <!-- 左侧菜单栏结束 -->

    <!-- 个人中心主要内容 -->
    <div class="personal-content order-content clearfix">
        <!-- 菜单切换列表-->
        <ul class="personal-content-tab order-tab clearfix">
            <li class="tab-item current-tab-item">全部</li>
            <li class="tab-item" action-state="1">待付款</li>
            <li class="tab-item" action-state="2">进行中</li>
            <li class="tab-item" action-state="3">待确认</li>
        </ul>
        <!-- 详细列表 -->
        <div class="order-content-list order-all-list clearfix">
            <!-- personal-center/order.js render here -->
        </div>

        <!-- 翻页-->
        <div id="page"></div>
    </div>
    <!-- 个人中心主要内容结束 -->
</div>