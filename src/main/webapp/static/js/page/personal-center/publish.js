/**
 * 个人中心  首页  我的发布
 *
 * @author: wangxinyu
 * @date: 2015-12-30
 * @last-modified: 2016-1-24
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'util/funcTpl', 'util/request','util/getLocalTime',
        'page/personal-center/common/layout','modules/page/page',
        'modules/topic-pop/commonPop','modules/baseMoudle','lib/juicer'],
    function ($, funcTpl, request,getTime, layout,pager,tip,baseMoudle) {

    var publish = {
        init:function () {
            var opt = {
                tab: '',
                menu: 'publish',
                callback: publish.tab
            };
            layout(opt);
            // publish.getPublish(1, 10);
        },

        publish_tpl:function () {
            /*
             {@each rows as publish}
             <div class="publish-list-item" data-id="${publish.talking_id}">
                 <div class="item-title f12">
                     <span class="publish-time">发布时间：${publish.talking_publish_time|getTime}</span>
                     <span class="publish-number">编号：${publish.talking_id}</span>
                     <span class="similar-talking"><i class="icon icon-publish"></i><a href="/talking/publish?tk=${publish.talking_id}">发布相似talking</a> </span>
                 </div>
                 <ul class="item-content clearfix">
                     <li class="item item-describe">
                         <div class="item-describe-pic">
                             <a href="/talking/classDetail?tk=${publish.talking_id}"><img src="${publish.talking_main_picture}" alt="我的发布" width="160" height="97"></a>
                         </div>
                         <div class="item-describe-info">
                             <div class="item-basic-info f16">${publish.talking_title}</div>
                             <!--<div class="item-label-info tag f12">
                             {@each publish.publish_tag as tag}
                                 <div class="item-tag tag-commerce">
                                     <span class="tag-rect">${tag.tag_name}</span>
                                 </div>
                             {@/each}
                             </div>-->
                             <div class="item-money-info">${publish.talking_price}</div>
                             <!--<div class="item-other-info f12">
                                 <ul>
                                     <li class="viewed-num">浏览<span>${publish.publish_view}</span></li>
                                     <li class="split-bar">|</li>
                                     <li class="message-num">留言<span>${publish.publish_comment}</span></li>
                                     <li class="split-bar">|</li>
                                     <li class="collect-num">收藏<span>${publish.publish_collect}</span></li>
                                 </ul>
                             </div>-->
                         </div>
                     </li>
                     <li class="item item-talking">
                         <div class="item-talking-info">
                             <div class="talking-time">交流时间：${publish.talking_start_time|getTime}</div>
                             <div class="talking-person-num f16">已参加 <span>${publish.talking_now_persion}</span>/${publish.talking_max_persion} 人</div>
                             <div class="talking-details">查看详情</div>
                         </div>
                     </li>
                     <li class="item item-operation">
                         <div class="table-cell">
                         {@if publish.talking_now_persion == 0 && publish.talking_state != 100}
                             <div class="link item-modify"><a href="/talking/publish?etk=${publish.talking_id}">修改</a></div>
                             <div class="link item-delete"><a href="javascript:;" class="disabled">删除</a></div>
                         {@else if publish.talking_now_persion > 0 || (publish.talking_now_persion == 0 && publish.talking_state == 100)}
                             <div class="link"> </div>
                             <div class="link item-delete"><a href="javascript:;" class="disabled">删除</a></div>
                         {@/if}
                         </div>
                     </li>
                 </ul>
             </div>
             {@/each}
             */
        },

        publish_delete_tpl:function () {
            /*
             {@each rows as publish}
             <div class="publish-list-item" data-id="${publish.talking_id}">
                 <div class="item-title f12">
                     <span class="publish-time">发布时间：${publish.talking_publish_time|getTime}</span>
                     <span class="publish-number">编号：${publish.talking_id}</span>
                     <span class="delete-type">${publish.delete_by}</span>
                 </div>
                 <ul class="item-content clearfix">
                     <li class="item item-describe">
                         <div class="item-describe-pic">
                             <a href="/talking/publish?tk=${publish.talking_id}"><img src="${publish.talking_main_picture}" alt="我的发布" width="160" height="97"></a>
                         </div>
                         <div class="item-describe-info">
                             <div class="item-basic-info f16">${publish.talking_title}</div>
                             <!--<div class="item-label-info tag f12">
                             {@each publish.publish_tag as tag}
                                 <div class="item-tag">
                                     <span class="tag-rect">${tag.talking_target}</span>
                                 </div>
                             {@/each}
                             </div>-->
                             <div class="item-money-info del">${publish.talking_price}</div>
                         </div>
                     </li>
                     <li class="item item-talking">
                         <div class="item-talking-info">
                             <div class="talking-time del">交流时间：${publish.talking_start_time|getTime}</div>
                         </div>
                     </li>
                     <li class="item item-operation">
                         <div class="table-cell">
                         <div class="link item-similar-talking"><a href="/talking/publish?tk=${publish.talking_id}">发布相似Talking</a></div>
                         </div>
                     </li>
                 </ul>
             </div>
             {@/each}
            */
        },
        // switch tab
        tab:function () {
            var state = 10;
            $('.personal-content-tab').on('click', '.tab-item', function(){
                var idx = $('.tab-item').index($(this));

                if (idx == 0) {
                    state = 10;
                    publish.getPublish(1, state);
                } else {
                    state = 1;
                    publish.getPublish(1, state);
                }

                //获取翻页钩子函数->回调为翻页函数
                var initPage = pager.init('#page', function (num) {
                    publish.getPublish(num, state);
                });
                //初始化
                $('body').one('page:set', function (ev, size, totalList) {
                    initPage(size, totalList);
                });
            });

            $('.tab-item:eq(0)').click();
        },

        // get publish and render tpl
        getPublish:function (page, state) {
            request.post('/talking/getUserTalking', {
                "userId":0,
                "page":page,
                "state":state
            }, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    if(state == 10) {
                        // 所有发布
                        var publish_tpl = juicer(funcTpl(publish.publish_tpl), res.data);
                        $('.publish-content-list').empty().append(publish_tpl);
                    }else{
                        // 删除的发布
                        var publish_delete_tpl = juicer(funcTpl(publish.publish_delete_tpl), res.data);
                        $('.publish-content-list').empty().append(publish_delete_tpl);
                    }
                    //触发翻页初始化
                    $('body').trigger('page:set', [10, res.data.count]);
                }else{
                    alert(res.msg);
                }

                publish.event();
            });
        },
        event:function () {

            // delete publish
            $('.item-delete').on('click', function () {
                if (confirm('确认删除？该操作不能撤销')) {
                    var thisItem = $(this).parents('.publish-list-item'),
                        publish_id = thisItem.data('id');
                    request.post('/talking/delTalking', {
                        "talkingId":publish_id
                    }, function (res) {
                        res = JSON.parse(res);
                        if (res.code == 1) {
                            thisItem.fadeOut(300);
                            alert('删除成功');
                        } else {
                            alert(res.msg);
                        }
                    });
                } else {
                    return;
                }
            });
            // 查看参加我发布的talking的人的信息
            $('.talking-details').on('click', function () {
                var thisItem = $(this).parents('.publish-list-item'),
                    publish_id = thisItem.data('id');
                if ($(this).text() == 0) {
                    tip.joinTip({tip: '暂时没有人参与哦~'});
                    return;
                }
                request.post('/order/getUserLine', {
                    "orderTalking": publish_id
                }, function (res) {
                    res = JSON.parse(res);
                    if (res.code == 1) {
                        var tmp = juicer(funcTpl(publish.popTpl), res);
                        tip.joinTip({tip: tmp});
                    } else {
                        tip.joinTip({tip: '嗷偶，数据加载错误了呢。error:'+ res.msg});
                    }
                });
            });
        },
        popTpl: function () {
            /*
            <div id="joinPeople">
                <table>
                    <thead>
                         <th>姓名</th>
                         <th>联系方式</th>
                         <th>购买时间</th>
                         <th>留言</th>
                    </thead>
                    <tbody>
                         {@each data as item}
                             <tr>
                                 <td>${item.order_user_realname}</td>
                                 <td>${item.order_user_tel}</td>
                                 <td>${item.order_user_confirm_time|getTime}</td>
                                 <td title="${item.order_user_extr_info}">${item.order_user_extr_info}</td>
                             </tr>
                         {@/each}
                    </tbody>
                </table>
            </div>
            */
        }
    };

    publish.init();

});


