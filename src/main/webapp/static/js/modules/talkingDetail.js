/**
 * @description 课程详情
 * @author 吴伦毅
 */
define(['lib/jquery', 'util/request', 'util/funcTpl', 'modules/page/page', 'modules/topic-pop/commonPop','modules/popup/signPop', 'lib/juicer', 'modules/baseMoudle'], function ($, request, funcTpl, page, tip,pop) {
    'use strict';
//    定义对象
    var talkingDetail = {
        init: function () {
            var talkId = talkingDetail._param().tk;
            if (!talkId) {
                return;
            }
            //获取翻页钩子函数->回调为翻页函数
            var initPage = page.init('#page', function (num) {
                talkingDetail._renderTalkingCommit(talkId, num);
            });
            //初始化
            $('body').one('page:set', function (ev, size, totalList) {
                initPage(size, totalList);
            }).one('get:other', function (ev, userId) {
                talkingDetail._renderOtherTalking(userId);//其它课程
            });
            talkingDetail._renderDetail(talkId);//课程详情
            talkingDetail._renderTalkingCommit(talkId, 1);//评价
        },
        _param: function () {
            var paramSearch = window.location.search.slice(1),
                paramArr = [],
                oParam = {};
            paramArr = paramSearch.split('&');
            for (var i = 0; i < paramArr.length; i++) {
                var inArr = paramArr[i].split('=');
                oParam[inArr[0]] = parseInt(inArr[1]);
            }
            return oParam;
        },
        _Api: {
            talkingInfo_api: '/talking/talkingshow',
            talkingComments_api: '/talking/talkingcomment',
            joinTalking_api: '/talking/jointalking',
            otherTalking_api: '/talking/getothertalking'
        },
        _render: function (api, param, tpl, dom, config, fn) {
            request.post(api, param, function (res) {
                res = JSON.parse(res) || eval(res);
                if (1 == res.code) {
                    var template = '';
                    //如果该说友没有其他课程，删除这个节点
                    if(dom =='.recommend-list'&&res.data.length ==0){
                        $('#listen-aside-content').remove();
                    }

                    if (config.data) {
                        template = juicer(tpl, res.data);
                    } else {
                        template = juicer(tpl, res);
                    }
                    if (config.append) {
                        $(dom).append(template).css('display','none').fadeIn();
                    } else {
                        $(dom).css('display','none').html(template).fadeIn();
                    }
                    fn && fn(res.data);
                }
            });
        },
        _detailTpl: function () {
            /*
             <!--
             {@helper getDate}
             function(time) {
             var oTime = new Date(time);
             return [oTime.getMonth()+1+"月",oTime.getDate()+"日"].join('');
             }
             {@/helper}
             {@helper getHour}
             function(time) {
             var oTime = new Date(time);
             function toNormal(num){
             if(num>9){
             return num;
             }else{
             return "0"+num;
             }
             }
             return [toNormal(oTime.getHours()),toNormal(oTime.getMinutes())].join(':');
             }
             {@/helper}
             -->
             <div class="class-pic-show">
             <img src="${talkingshow.talking_main_picture}" alt="pic" class="current-show-pic" onerror="this.src = window.pub.classImg"/>
             </div>
             <!-- 文字描述-->
             <div class="class-info-context">
             <h2>${talkingshow.talking_title}</h2>
             <ul class="class-info-list clearfix">
             <li class="class-info-item"><span class="info-item-title">酬劳</span>&nbsp;&nbsp;&nbsp;&nbsp;<span class="class-price">&yen;${talkingshow.talking_price}</span></li>
             <li class="class-info-item"><span class="info-item-title">人数</span>&nbsp;&nbsp;&nbsp;&nbsp;已报<span class="has-join">${talkingshow.talking_now_persion}</span>人/共${talkingshow.talking_max_persion}人</li>
             <li class="class-info-item"><span class="info-item-title">模式</span>&nbsp;&nbsp;&nbsp;&nbsp;{@if talkingshow.talking_address==0}线上{@else}线下{@/if}</li>
             <li class="class-info-item"><span class="info-item-title">时间</span>&nbsp;&nbsp;&nbsp;&nbsp;${talkingshow.talking_start_time|getDate} ${talkingshow.talking_start_time|getHour}-${talkingshow.talking_end_time|getHour}</li>

             <li class="class-info-item last-info-item">
             {@if talkingshow.talking_address==0}
             <span class="info-item-title">方式</span>&nbsp;&nbsp;&nbsp;&nbsp;${talkingshow.talking_tool}
             {@else}
             <span class="info-item-title">地点</span>&nbsp;&nbsp;&nbsp;&nbsp;${talkingshow.talking_address}
             {@/if}
             </li>
             </ul>
             <div class="class-option">
             {@if talkingshow.talking_state != 100}
                 {@if talkingshow.talking_now_persion!=talkingshow.talking_max_persion}
                  <div id="J_joincourse"> <a href=/pay?tid=${talkingshow.talking_id} style="color:#ffffff;display:block;padding-top:9px;padding-bottom:9px;">立即参加</a></div>
                 {@else}
                 <button id="J_joincourse" disabled="disabled" style="background-color:#dddddd;cursor:no-drop;height: 39px;width: 113px;font-size: 16px;">
                 已满员
                 {@/if}
             {@else}
             <button id="J_joincourse" disabled="disabled" style="background-color:#dddddd;cursor:no-drop;height: 39px;width: 113px;font-size: 16px;">
             已过期
             {@/if}
             </button>
             <a href="javascript:;" class="share"><i class="inline-block share-to"></i>分享</a>
             </div>
             </div>
             <!-- 作者-->
             <div class="class-author">
             <a href="/user/personalIndex?user=${talkingshow.talking_user}" class="inline-block"><img src="${UserDetails.user_photo}" alt="pic" onerror='this.src = window.pub.userimg'/></a>
             <dl>
             <dt>${UserDetails.user_name}</dt>
            <!-- <dd>
             {@each i in range(0,parseInt(UserDetails.avg))}
             <i class="teacher-star inline-block star2"></i>
             {@/each}
             {@each  i in range(parseInt(UserDetails.avg) ,5)}
             <i class="teacher-star inline-block star1"></i>
             {@/each}
             <span>${UserDetails.avg}</span>
             </dd>-->
             </dl>
             </div>*/
        },
        _OtherTalkingTpl: function () {
            /*
             {@each data as item,index}
             {@if index!=0}
             <li class="row-line"></li>
             {@/if}
             <li class="recommend-item">
             <a href="/talking/classDetail?tk=${item.talking_id}" class="inline-block">
             <img src="${item.talking_main_picture}" alt="" class="recommend-item-pic" onerror="this.src = window.pub.classImg"/>
             </a>

             <h3 class="recommend-item-title">${item.talking_title}</h3>
             <ul class="recommend-item-dec clearfix">
             <li class="recommend-item-dec-att"><span class="recommend-item-dec-in">${item.talking_now_persion}</span><span>/${item.talking_max_persion}参加</span>
             </li>
             <li class="recommend-item-dec-price">￥<span class="recommend-item-dec-price-big">${item.talking_price}</span></li>
             </ul>
             </li>
             {@/each}
             */
        },
        _talkingCommit: function () {
            /*
             <!--
             {@helper getTime}
             function(time) {
             var oTime = new Date(time);
             return [oTime.getFullYear(),oTime.getMonth()+1,oTime.getDate()].join('-');
             }
             {@/helper}
             -->
             {@each list as item}
             <li class="commit-detail-item clearfix">
             <dl class="teacher-name">
             <dt><a href="/user/personalIndex?user=${item.talking_comment_user}" class="inline-block"><img src="${item.user_photo}" alt="pic" onerror="this.src = window.pub.userimg"/> </a></dt>
             <dd>${item.user_name}</dd>
             </dl>
             <p class="commit-dec">
             ${item.talking_comment_content}
             </p>
             <p class="commit-time">${item.talking_comment_time|getTime}</p>
             </li>
             {@/each}
             */
        },
        _renderDetail: function (talkId) {
            talkingDetail._render(
                talkingDetail._Api.talkingInfo_api,
                {TalkingId: talkId},
                funcTpl(talkingDetail._detailTpl),
                '.class-info',
                {data: true, append: false},
                function (data) {
                    $('.share').on('click', tip.share);
                    $('body').trigger('get:other', [data.talkingshow.talking_user]);
                    $('.J_talking_target').html(data.talkingshow.talking_target);
                    $('.J_talking_info').html(data.talkingshow.talking_info);
                    $('#talking-detail').slideDown("slow");

                    if (0 != data.talkingshow.talking_address) {
                        $('.J_talking_address').html('地点 : ' + data.talkingshow.talking_address);
                    } else {
                        $('.J_talking_address').html('线下交流方式 : ' + data.talkingshow.talking_tool);
                    }
                    function str(num) {
                        return num > 9 ? num : 0 + '' + num;
                    }

                    var start_time = new Date(data.talkingshow.talking_start_time);
                    var end_time = new Date(data.talkingshow.talking_end_time);
                    var tTime = start_time.getFullYear() + '年' + (start_time.getMonth() + 1) + '月' +
                        start_time.getDate() + '日&nbsp;&nbsp;' + str(start_time.getHours()) + ':' +
                        str(start_time.getMinutes()) + '-' + str(end_time.getHours()) + ':' + str(end_time.getMinutes());
                    $('.J_class_time').css('display','none').html(tTime).fadeIn();
                    $('.J_class_author').css('display','none').html(data.UserDetails.user_name).fadeIn();
                    $('.J_class_content').css('display','none').html(data.talkingshow.talking_target).fadeIn();
                    $('#talking-plan-content').slideDown('slow');
                }
            );
        },
        _renderOtherTalking: function (userId) {
            talkingDetail._render(
                talkingDetail._Api.otherTalking_api,
                {user_id: userId},
                funcTpl(talkingDetail._OtherTalkingTpl),
                '.recommend-list',
                {data: false, append: false}
            )
        },
        _renderTalkingCommit: function (talkId, pageNum) {
            talkingDetail._render(
                talkingDetail._Api.talkingComments_api,
                {TalkingId: talkId, page: pageNum},
                funcTpl(talkingDetail._talkingCommit),
                '.commit-detail-list',
                {data: true, append: false},
                function (data) {
                    $('.J-all-comments').html(data.all);
                    setTimeout(function(){
                        $('#commit-detail-list-content').slideDown('slow');},1000);
                    $('body').trigger('page:set', [10, data.all]);
                }
            )
        }
    };

    return talkingDetail
});
