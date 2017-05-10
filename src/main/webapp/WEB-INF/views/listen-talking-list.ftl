<content tag="title">列表-校园说-交流干货,分享人生</content>
<content tag="css">/listen-talking-list/listen-talking-list</content>
<content tag="javascript">/listen-talking-list</content>
<!--地址导航栏-->
<div class="address-bar-wrap">
    <div class="address-bar global-center">
        <a href="/talking/ttIndex">首页</a>&gt;<span>全部</span>&gt;<span>全部</span>
    </div>
</div>
<!--搜索条件栏listen-search-condition-->
<ul class="talking-condition-list">
    <li class="filter-item clearfix">
        <span>分类 :</span>
        <ul class="clearfix" id="J_classification">
        </ul>
    </li>
    <li class="filter-item clearfix">
        <span>二级 :</span>
        <ul class="clearfix" id="J_subClassification">
        </ul>
    </li>
    <li class="filter-item clearfix">
        <span>城市 :</span>
        <ul class="clearfix" id="J_city">
        </ul>
    </li>
    <li class="filter-item clearfix" >
        <span>学校 :</span>
        <ul class="clearfix" id="J_campus">
        </ul>
    </li>
</ul>
<!--展示内容栏-->
<div class="listen-content-wrap global-center clearfix">
    <!-- talking or 说友-->
    <ul class="listen-main-content-tab-bar clearfix">
        <li class="listen-main-content-tab tab-active J_switch_tap">
            <a href="#talking">Talking</a>
        </li>
        <li class="listen-main-content-tab J_switch_tap">
            <a href="#friends">
                说友
            </a>
        </li>
        <!--<li class="listen-main-content-tab publish-time">
            <span>
                发布时间
            </span>
        </li>-->
        <li class="sex-input-wrap hide J_sex">
            <span>性别&nbsp;:&nbsp;</span>
            <label for="boy">
                <input type="checkbox" name="sex" class="sex-input" id="boy" checked="checked" value="1"/>
                <span class="inline-block sex-input-virtual"></span><span>&nbsp;男&nbsp;</span>
            </label>
            <label for="girl">
                <input type="checkbox" name="sex" class="sex-input" id="girl" checked="checked" value="0"/>
                <span class="inline-block sex-input-virtual"></span><span>&nbsp;女&nbsp;</span>
            </label>
        </li>
       <!-- <li class="sex-input-wrap J_style hide">
            <span>模式&nbsp;:&nbsp;</span>
            <label for="onLine">
                <input type="checkbox" name="style" class="sex-input" id="onLine" checked="checked" value="1"/>
                <span class="inline-block sex-input-virtual"></span><span>&nbsp;线上&nbsp;</span>
            </label>
            <label for="offLine">
                <input type="checkbox" name="style" class="sex-input" id="offLine" checked="checked" value="0"/>
                <span class="inline-block sex-input-virtual"></span><span>&nbsp;线下&nbsp;</span>
            </label>
        </li>-->
    </ul>
    <!-- Talking content-->
    <ul class="talking-list clearfix"></ul>
    <!-- 翻页-->
    <div id="page"></div>
</div>