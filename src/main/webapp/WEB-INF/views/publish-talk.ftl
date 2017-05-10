<content tag="title">发布talking-校园说-交流干货,分享人生</content>
<content tag="css">/publish-talk</content>
<content tag="javascript">/publish-talk</content>

<div class="publish-talk-content global-center clearfix">
    <div class="publish-list-wrap">
        <h2 class="what-say">今天你想说什么?</h2>
        <ul class="publish-list">
            <li class="publish-item">
                <ul class="publish-input-list">
                    <li class="publish-input-item">
                        <label for="class-title">
                            <span class="input-title must-title">标题&nbsp;&nbsp;:</span>
                            <input type="text" id="class-title" class="input-big" autocomplete="off"/>
                            <span class="input-tip">还可以输入<span class="J_title_num">20</span>字</span>
                        </label>
                    </li>
                    <li class="publish-input-item">
                        <label for="class-calssify">
                            <span class="input-title must-title class-classify">分类&nbsp;&nbsp;:</span>
                        </label>

                        <div class="inline-block input-middle mn-select" id="class-calssify" data-id="">
                            <span class="inline-block" id="J_talking_type_first">请选择</span>
                            <ul class="inline-block" id="J_category">
                            </ul>
                        </div>
                        <div class="inline-block input-middle mn-select" id="class-sub-classify" data-id="">
                            <span class="inline-block J_sub" id="J_talking_type_two">请选择</span>
                            <ul class="inline-block" id="J_subCategory">
                            </ul>
                        </div>
                        <p class="input-tip warm-tip">如果没有找到你的分类，请选择“其他”，我们后续将创建对应的分类</p>
                    </li>
                    <li class="publish-input-item">
                        <label for="student-num">
                            <span class="input-title must-title">人数&nbsp;&nbsp;:</span>
                            <input type="text" id="student-num" class="input-middle" placeholder="最多参与人数" autocomplete="off"/>
                            <span class="input-tip">&nbsp;&nbsp;人</span>
                        </label>
                    </li>
                    <li class="publish-input-item">
                        <label for="class-price">
                            <span class="input-title must-title">价格&nbsp;&nbsp;:</span>
                            <input type="text" id="class-price" class="input-middle" autocomplete="off"/>
                            <span class="input-tip">&nbsp;&nbsp;元/人</span>
                        </label>
                    </li>
                </ul>
            </li>
            <li class="publish-item">
                <ul class="publish-input-list">
                    <li class="publish-input-item">
                        <label for="class-way">
                            <span class="input-title must-title">模式&nbsp;&nbsp;:</span>
                            <input type="radio" id="class-way" checked="checked" name="class-way" value="-1" autocomplete="off"/>
                            <span class="inline-block class-way"></span>
                            <span class="input-tip">&nbsp;&nbsp;线下</span>
                        </label>
                        <label for="class-online" class="class-online">
                            <input type="radio" id="class-online" name="class-way" value="1" autocomplete="off"/>
                            <span class="inline-block class-way"></span>
                            <span class="input-tip">&nbsp;&nbsp;线上</span>
                        </label>

                        <div class="input-address-wrap">
                            <input type="text" class="input-small hide" id="online-style" placeholder="QQ/微信/YY" autocomplete="off"/>
                            <input type="text" class="input-small hide" id="online-num" placeholder="请填写号码" autocomplete="off"/>
                            <input type="text" class="input-big" id="class-address" placeholder="请填写具体地址\可商议地址" autocomplete="off"/>
                        </div>
                    </li>
                    <li class="publish-input-item clearfix">
                        <span class="input-title contact-information">联系方式&nbsp;&nbsp;:</span>
                        <ul class="class-info inline-block">
                            <!--<li>abcdef@163.com</li>
                            <li>18883862825</li>-->
                            <li>&nbsp;</li>
                        </ul>
                        <p class="input-tip warm-tip">交流开始前半小时，系统将您联系方式发送给对方</p>
                    </li>
                </ul>
            </li>
            <li class="publish-item">
                <ul class="publish-input-list">
                    <li class="publish-input-item">
                        <span class="input-title must-title">上传图片&nbsp;&nbsp;:</span>

                        <div class="upload-img">
                            <img src="" alt="view" class="view hide" id="view">
                            <label for="input-img" class="inline-block" id="input-wrap">
                                <input type="file" id="input-img" accept="image/gif,image/jpeg,image/png"
                                       name="input-img" autocomplete="off"/>
                            </label>
                        </div>
                        <p class="input-tip warm-tip">上传一张图片</p>
                    </li>
                </ul>
            </li>
            <li class="publish-item">
                <ul class="publish-input-list">
                    <li class="publish-input-item">
                        <span class="input-title must-title">交流产品介绍&nbsp;&nbsp;:</span>
                    </li>
                    <li class="publish-input-item">
                        <span class="input-title">交流安排&nbsp;&nbsp;:</span>
                        <label for="class-time" class="class-time">
                            &nbsp;&nbsp;
                            <span class="input-title must-title">交流时间&nbsp;&nbsp;:</span>
                            <input type="text" class="input-middle" id="class-time" placeholder="YY-MM-DD" autocomplete="off"/>
                        </label>
                        <input type="text" id="class-on" class="input-time-up" placeholder="上课时间" autocomplete="off"/>
                        <span class="input-tip to inline-block">至</span>
                        <input type="text" id="class-off" class="input-time-down" placeholder="下课时间" autocomplete="off"/>

                        <div class="talk-content">
                            <span class="input-title must-title">交流目的&nbsp;&nbsp;:</span>
                            <input type="text" class="input-content" autocomplete="off"/>
                        </div>
                    </li>
                    <li class="publish-input-item clearfix">
                        <span class="input-title talk-detail must-title">交流详情&nbsp;&nbsp;:</span>

                        <div class="editor-wrap clearfix">
                            <div id="myEditor" style="width: 590px; height: 200px;"></div>
                            <button class="publish-talk" id="J_submit">发布</button>
                        </div>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
    <!--侧边步骤-->
    <ul class="publish-tip">
        <li class="publish-process process1">
        <span class="publish-process-txt">
            让大家知道你想讲什么吧
        </span>
        </li>
        <li class="publish-process process2">
        <span class="publish-process-txt">
            用几张图表达你的想法吧
        </span>
        </li>
        <li class="publish-process process3">
        <span class="publish-process-txt">
            详细说说吧
        </span>
        </li>
    </ul>
</div>

<div class="talkingBox" id='step1' style="display: none">
    <div  >
        <img src="/static/img/page/publish-talk/talktingStep1.png">
        <button class="stepBtn">我知道了</button>
    </div>
</div>
<div class="talkingBox"   id='step2' style="display: none">
    <div >
        <img src="/static/img/page/publish-talk/talktingStep2.png">
        <button class="stepBtn">我知道了</button>
    </div>
</div>
<div class="talkingBox" id='step3' style="display: none">
    <div  >
        <img src="/static/img/page/publish-talk/talktingStep3.png">
        <button class="stepBtn">我知道了</button>
    </div>
</div>