<content tag="title">校园说消息系统</content>
<content tag="css">/message/message</content>
<content tag="javascript">/message/message</content>


<div class="global-center">
    <div class="nav-header clearfix">
        <h1>消息系统(<span class="dataCount"></span>)</h1>

        <ul id="filter-wrap" class="clearfix">
            <li>
                筛选：
            </li>
            <li>
                <button data-type="">全部</button>
            </li>
            <li>
                <button data-type="1" class="has-line">动态</button>
            </li>
            <li>
                <button data-type="2" class="has-line">活动</button>
            </li>
            <li>
                <button data-type="3" class="has-line">系统提醒</button>
            </li>
            <li>
                <button data-type="4" class="has-line">话题社</button>
            </li>
        </ul>
    </div>


    <ul id="message-list" style="color: #777777">
    </ul>

    <div class="clearfix">
        <ul class="option-wrap">
            <li class="option-item">
                <button class="delD del">清空全部消息</button>
            </li>
            <li class="option-item">
                <button class="chooseD read">全部标记为已读</button>
            </li>
        </ul>
    </div>

    <div id="page"></div>
</div>

