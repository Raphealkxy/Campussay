/**
 * Created by zhujing on 2016/1/25.
 */

require.config({
    baseUrl: CP.STATIC_ROOT
});
require(["lib/jquery","util/request","util/funcTpl","modules/page/page","modules/popup/signPop","modules/topic-pop/commonPop",'modules/baseMoudle',"lib/juicer"], function ($,request,funcTpl,page,tip,pop) {
    var index ={
        init : function(){
            var type_id = index._param().id; //获取领域的ID
            //this._addCare();
            this._addField(type_id);
            this._getPersonal(0);
            this._create(type_id,index._data.pageCount);
            this._prosecute_btn();
            $('.ht-common-switch li').on('click',function(){
                $(this).addClass('switch-on').siblings().removeClass('switch-on');
            });
            //获取翻页钩子函数->回调为翻页函数
            var initPage = page.init('#page', function (num) {
                // index._data.pageCount = num;
                index._create(type_id, num);
            });
            //初始化
            $('body').one('page:set', function (ev, size, totalList) {
                initPage(size, totalList);
            });
        },
        _data : {
            pageCount:1
        },
        _hotInfo:{
            _this_prosecute:null
        },
        _API :{
            getAllTopic : "/topic/getAllTopicByFieldID",
            getDetailMsg: "/topic/getUserDetailMsgById",
            getFollow : "/follow/isConcernField",
            addFollow : "/follow/addAConcernField",
            cancelFollow : "/follow/cancelConcernField"
        },
        _templateList : function(){
            /*
             {@each list as item}
                 <div class="li-info">
                     <h3><a href="/topic/answerdetail?topicid=${item.id}">${item.tile}</a><span>${item.update_time |handleTime}</span></h3>
                     <div class="li-tag">
                         <a href="/user/personalIndex?user=${item.user_id}">${item.user_name}</a>  |  ${item.user_campus_name}
                     </div>
                     <p>${item.context}</p>
                     <div class="li-num clearfix">
                         <div><span>${item.answerNum}</span>条回答</div>
                         <div class="prosecute-share">
                             <a href="javascript:;" class="prosecute">举报</a>
                             <a href="javascript:;" class="answer-share" data-url="/topic/answerdetail?topicid=${item.id}">分享</a>
                         </div>
                     </div>
                 </div>
             {@/each}
             <!--
                 {@helper handleTime}
                 function(startTime){
                     var currentTime = Date.parse(new Date()),
                         time        = currentTime-startTime,
                         day  = parseInt(time/(1000*60*60*24)),
                         hour = parseInt(time/(1000*60*60)),
                         min  = parseInt(time/(1000*60));

                     if(day){
                         return day+"天前"
                     }
                     if(hour){
                         return hour+"小时前"
                     }
                     if(min){
                         return min+"分钟前"
                     }
                 }
                 {@/helper}
             -->
             */
    },
        _templatePersonal : function () {
            /*
             <div class="person-info-content clearfix">
             <div class="person-name">
             <a href="/topic/htIndex?user=0"><img src="${user_photo}" alt=""/></a>
             <div >
             <h3><a href="/topic/htIndex?user=0">${user_name}</a></h3>
             <ul>
             <li>${user_campus_name}</li>
             <li>${academy}</li>
             </ul>
             </div>
             </div>
             <dl class="good-skill">
             <dt>擅长领域 : </dt>
             {@each skillsName as item}
             <dd><span><strong>${item.talking_type_name}</strong></span></dd>
             {@/each}
             </dl>
             <dl class="attention">
             <dt>关注领域 : </dt>
             {@each concernName as item}
             <dd><span><strong>${item.talking_type_name}</strong></span></dd>
             {@/each}
             </dl>
             </div>
             <ul class="person-num clearfix">
             <li><a href="/topic/htIndex?user=0"><span>${answerNum}</span>回答</a></li>
             <li><a href="/topic/htIndex?user=0"><span>${talkingNum}</span>Talking</a></li>
             <li><a href="#"><span>${attentionNum}</span>粉丝</a></li>
             </ul>
             */
        },
        _templateCare : function () {
            /*
             <button class="care">+ 关注</button>
             */
        },
        _param: function () {//获取传参
            var paramSearch = window.location.search.slice(1),
                paramArr = [],
                oParam = {};
            paramArr = paramSearch.split('&');
            for(var i=0;i<paramArr.length;i++){
                var inArr = paramArr[i].split('=');
                oParam[inArr[0]] = parseInt(inArr[1]);
            }
            return oParam;
        },
        _commonRender: function (url,param,dom,tpl,fn) {
            request.post(url,param, function (res) {
                res = JSON.parse(res)||eval(res);
                if(1 == res.code){
                    var htmlT = juicer(tpl,res.data);
                    $(dom).html(htmlT);
                    fn&&fn(res.data);
                    return res.data.size;
                }
            });
        },
        _templateHead : function () {

        },
        //关注领域
        _field : function () {
            /*
             <img src="${data.talking_type_picture}" width="53" height="49">
             <span class="f16">${data.talking_type_name}</span>
             <div class="btn" >
             {@if code==1}
             <button class="care">取消关注</button>
             {@else}
             <button class="care"><strong>+</strong> 关注</button>
             {@/if}
             </div>
             */
        },
        //显示领域,添加关注作为回调
        _addField : function (talkId) {
            request.get(index._API.getFollow,{taking_type_id:talkId}, function (res) {
                res = JSON.parse(res);
                var temp = juicer(funcTpl(index._field),res);
                $(".top").html(temp);
                follow = res.code;
                $(".btn").on("click", function () {
                    if(follow==-1){
                            tip();
                    } else if(follow==0){
                        request.post(index._API.addFollow,{field_id:talkId}, function (data) {
                            data = JSON.parse(data);
                            if(data.code==1){
                                $(".btn").html('<button class="care">取消关注</button>');
                                //$(".btn").html('<button class="care">+ 关注</button>');
                                //follow = $(".btn").data("bd",1);
                                follow=1;
                            }
                        });
                    }else if(follow==1){
                        console.log(res.msg);
                        //$(".btn").html('<button class="care" style="background-color:#00caab">取消关注</button>');
                        request.post(index._API.cancelFollow,{field_id:talkId}, function (data) {
                            //$(".btn").html('<button class="care">+ 关注</button>');
                            data  = JSON.parse(data);
                            if(data.code ==1){
                                //follow = $(this).data("code",1);
                                $(".btn").html('<button class="care">+ 关注</button>');
                                //$(".btn").html('<button class="care" style="background-color:#c2c2c2">取消关注</button>');
                                //$(".btn").data("bd",0);
                                follow=0;
                            }

                        });
                    }
                })
            });

        },
        //消息列表
        _create : function(fieldId,page){
            //var date=new Date();
            request.get(index._API.getAllTopic,{field_id:fieldId ,page:page}, function (res) {
                res = JSON.parse(res);
                if(res.code==1){
                    var count = res.data.rows;
                    var tem = juicer(funcTpl(index._templateList),res.data);
                    $(".content-list").html(tem);
                    $('body').trigger('page:set',[10,count]);

                    $(".content-list div:last-child").css("borderBottom","white");
                    index.prosecute();
                    index.share();

                }
            });
        },
        //获取个人面板的信息
        _getPersonal : function (userId){
             index._commonRender(index._API.getDetailMsg,{userId:userId},".person-info",funcTpl(index._templatePersonal), function () {

             });
        },
        //举报弹窗按钮
        prosecute:function(){
            $('.prosecute').on('click',function(){
                index._hotInfo._this_prosecute = this;
                if($(this).attr('class')=='prosecute'){
                    $('.MsgBack').css('display','block');
                    $('.alert').css('display','block');
                }
            })
        },
        //举报框 举报和取消按钮
        _prosecute_btn:function(){
            $('.report').on('click',function(){
                $(index._hotInfo._this_prosecute).attr('class','prosecuted').html('已举报');
                close();
            });
            $('.close').on('click',function(){
                close();
            });
            var close = function(){
                $('.MsgBack').css('display','none');
                $('.alert').css('display','none');
            };
            this.prosecute();
        },
        // 分享
        share:function () {
            $('.answer-share').on('click',function() {
                var $config = {
                    url: $(this).attr('data-url'),
                    source: 'www.campussay.com'
                };
                pop.share($config);
            });
        }
    };
            return index.init();
});