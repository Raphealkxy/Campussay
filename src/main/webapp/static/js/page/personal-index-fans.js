/**
 * 个人设置  首页 => 粉丝 关注
 *
 * author: 王欣瑜
 * date: 2015-12-30
 */
require.config({
    baseUrl: CP.STATIC_ROOT,
    shim:{
        kkpager:{
            deps:['lib/jquery'],
            exports:'kkpager'
        }
    },
    paths:{
        kkpager:"modules/pager/kkpager"
    }
});
require(['lib/jquery', 'util/request', 'util/funcTpl', 'kkpager', 'lib/juicer'],
    function($, request, funcTpl, pager) {
    var personalFans = {
        init: function () {
            personalFans.getFansList();
            personalFans.event();
        },


        fans_tpl: function() {
            /*{@each data as ulItem}
             <div class="circle-list-item" data-id="${ulItem.user_id}">
                 <ul class="item-content clearfix">
                     <li class="item item-describe">
                         <div class="item-describe-pic">
                             <a href="#"><img src="${ulItem.user_photo}"></a>
                         </div>
                         <div class="item-describe-info">
                             <div class="item-care-name f18 clearfix">${ulItem.user_name}</div>
                             <div class="item-basic-info f12">
                                 <ul>
                                     <li class="care-person-school">${ulItem.user_campus_name}</li>
                                     <li class="split-bar">|</li>
                                     <li class="care-person-fans">粉丝<span>6666</span></li>
                                     <li class="split-bar">|</li>
                                     <li class="care-person-care">关注<span>13</span></li>
                                 </ul>
                             </div>
                             <div class="item-field item-good-field f12">
                                 <span>擅长领域：</span>
                                 <div class="item-label-info tag f12">
                                     {@each ulItem.circle_tag1 as tag}
                                         <div class="item-tag tag-commerce">
                                             <span class="tag-rect">${tag.tag_name}</span>
                                         </div>
                                     {@/each}
                                 </div>
                             </div>
                         </div>
                     </li>
                     <li class="item item-care">
                         <div class="item-field item-care-field f12">
                             <span>关注领域：</span>
                             <div class="item-label-info tag f12">
                                 {@each ulItem.circle_tag1 as tag}
                                     <div class="item-tag tag-commerce">
                                         <span class="tag-rect">${tag.tag_name}</span>
                                     </div>
                                 {@/each}
                             </div>
                         </div>
                     </li>
                     <li class="item item-operation">
                         {@if ulItem.circle_relationship == 0}
                             <div class="operation cancel-care">
                                 <i class="icon icon-false"></i>取消关注
                             </div>
                         {@else if ulItem.circle_relationship == 1}
                             <div class="operation care-each">
                                 <i class="icon icon-each"></i>相互关注
                             </div>
                         {@else}
                             <div class="operation"></div>
                         {@/if}
                     </li>
                 </ul>
              </div>
             {@/each}
            */
        },

        getFansList: function () {
            var reqData = {
                user_id: 123,
                listType: 1,    // 1是ta粉丝，0是ta关注
                page: 1         // 非必需，为空时是1
            };

            request.post('/user/getAttentionList', reqData, function (res) {
                res = JSON.parse(res);

                if (res.code == 0) {
                    alert(res.msg);
                } else {
                    var tmp = juicer(funcTpl(personalFans.fans_tpl), res);
                    $(".circle-content-list").empty().append(tmp);
                }

                personalFans.event();
            });
        },

        getWatchList: function () {
            var reqData = {
                user_id: 123,
                listType: 0,    // 1是ta粉丝，0是ta关注
                page: 1         // 非必需，为空时是1
            };

            request.post('/user/getAttentionList', reqData, function (res) {
                res = JSON.parse(res);

                if (res.code == 0) {
                    alert(res.msg);
                } else {
                    var tmp = juicer(funcTpl(personalFans.fans_tpl), res);
                    $(".circle-content-list").empty().append(tmp);
                }

                personalFans.event();
            });
        },

        // switch tab
        tab:function () {
            $('.personal-care-tab').on('click', '.personal-care-tab-item', function () {
                // style: switch tab
                if ($(this).hasClass('current-tab-item')) {
                    return;
                } else {
                    $(this).addClass('current-tab-item');
                    $(this).siblings().removeClass('current-tab-item');
                }

                // TODO: 判断渲染哪个
            });
        },

        event: function () {
            // cancel watch
            $('.cancel-care').on('click', function () {
                var reqData = {};
                var thisItem = $(this).parents('.circle-list-item');
                reqData.user_id = thisItem.data('id');

                request.post('', reqData, function () {});

                thisItem.fadeOut(300);
            });
        }
    };

    personalFans.init();

});