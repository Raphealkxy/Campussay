/**
 * 话题社-领域中心
 * @author: wangxinyu
 * @date: 2016/1/23
 * @last-modified: 2016/1/23
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'util/funcTpl', 'util/request', 'modules/page/page','modules/topic-pop/commonPop','modules/popup/signPop', 'lib/juicer', 'modules/page/page','modules/baseMoudle'],
    function($, funcTpl, request, pager,tip,pop) {
    var field = {
        _api: {
            getAllFirstTag: '/talkingType/getAllFirstTalkType',
            getAllSecondTag: '/talkingType/getAllSecondTalkType',
            getPartSecondTag: '/talkingType/getSecondTalkType',
            getUser: '/topic/getUserDetailMsgById',
            setFollow: '/follow/addAConcernField',
            cancelFollow: '/follow/cancelConcernField'
        },
        init:function () {
            field.getUser();
            field.getFirstTag();
            field.getSecondTag(field._api.getAllSecondTag, {});

            // init nav style
            var $nav = $('.ht-common-switch li');
            $nav.eq(0).removeClass('switch-on');
            $nav.eq(1).addClass('switch-on');
            //获取翻页钩子函数->回调为翻页函数
            var initPage = pager.init('#page', function (num) {
                field.getSecondTag(field._api.getAllSecondTag, {
                    "page":num
                });
            });
            //初始化
            $('body').one('page:set', function (ev, size, totalList) {
                initPage(size, totalList);
            });
        },
        tag_tpl:function () {
            /*
            {@each data as tag}
             <span class="item-tag" data-id="${tag.talking_type_id}">${tag.talking_type_name}</span>
            {@/each}
            */
        },

        tag_detail_tpl:function () {
            /*
             {@each list as tag}
             <div class="item-field" data-id="${tag.talking_type_id}">
                 <div class="item-field-logo">
                     <a href="/topic/answer?id=${tag.talking_type_id}"><img class="item-field-logo-icon" src="${tag.talking_type_picture}" alt="" width="86" height="81"></a>
                 </div>
                 <div class="item-field-content">
                     <div class="item-field-title">
                         <a href="/topic/answer?id=${tag.talking_type_id}" class="item-field-name f18">${tag.talking_type_name}</a>
                         <a href="javascript:;" class="item-field-operation f12" data-follow="${tag.follow}">
                             {@if tag.follow == 0}
                                 + 关注
                             {@else if tag.follow == 1}
                                 + 已关注
                             {@else}
                                 <i class=""></i>
                             {@/if}
                         </a>
                     </div>
                     <div class="item-field-desc f12">${tag.talking_type_description}</div>
                 </div>
             </div>
             {@/each}
            */
        },
        person_tpl:function () {
            /*
           <div class="person-info-content clearfix">
               <div class="person-name">
                   <a href="/topic/htIndex?user=0"><img src="${user_photo}" alt=""/></a>
                       <div>
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
                   <li><a href="/topic/htIndex?user=0&type=1"><span>${answerNum}</span>回答</a></li>
                   <li><a href="/personalCenter/publish" target="_blank"><span>${talkingNum}</span>Talking</a></li>
                   <li><a href="/personalCenter/circle?type=1" target="_blank"><span>${attentionNum}</span>粉丝</a></li>
               </ul>
           */
        },

        getFirstTag:function () {
            request.post(field._api.getAllFirstTag, {}, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    var tag_tpl = juicer(funcTpl(field.tag_tpl), res);
                    $('.field-tag-watch').append(tag_tpl);

                    field.event();
                }
            });
        },

        getSecondTag:function (url, req) {
            request.post(url, req, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    var tag_detail_tpl = juicer(funcTpl(field.tag_detail_tpl), res.data);
                    $('.field-tag-unwatch').html(tag_detail_tpl).hide().fadeIn();

                    field.follow();

                    //触发翻页初始化
                    $('body').trigger('page:set', [10, res.data.rows]);
                    // hover img
                    $('.item-field-operation').hover(function () {
                        $(this).find('i').addClass('selected');
                    }, function () {
                        $(this).find('i').removeClass('selected');
                    });
                }
            });
        },

        getUser:function () {
            request.post(field._api.getUser, {
                "userId": 0
            }, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    var person_tpl = juicer(funcTpl(field.person_tpl), res.data);
                    $('.person-info').html(person_tpl);
                }
                // field.follow(res.code);
            });
        },

        follow: function () {
            $('.item-field-operation').hover(function() {
                var _this = $(this),
                    follow = $(this).attr('data-follow');

                var text = (follow==1) ? 'x 取消关注' 
                                       : '+ 关注';
                _this.html(text);
            }, function () {
                var _this = $(this),
                    follow = $(this).attr('data-follow');

                var text = (follow==1) ? '+ 已关注' 
                                       : '+ 关注';
                _this.html(text);
            });

            $('.field-tag-unwatch').on('click', '.item-field-operation', function () {
                var _this = $(this),
                    follow = $(this).attr('data-follow'),
                    id = $(this).parents('.item-field').attr('data-id');

                // 关注 & 取关
                if (follow == 0) {
                    request.post(field._api.setFollow, {"field_id": id}, function (res) {
                        res = JSON.parse(res);
                        if (res.code == 1) {
                            _this.attr('data-follow', 1);
                            _this.html('+ 已关注');
                            tip.info({ tip: '关注成功', time: '1500' });
                        } else if (res.code == -1) {
                            pop();
                        }
                    });
                } else {
                    request.post(field._api.cancelFollow, {"field_id": id}, function (res) {
                        res = JSON.parse(res);
                        if (res.code == 1) {
                            _this.attr('data-follow', 0);
                            _this.html('+ 关注');
                            tip.info({ tip: '取消关注成功', time: '1500' });
                        } else if (res.code == -1) {
                            pop();
                        }
                    });
                }
            });
        },

        event: function () {
            // 根据一级分类查询二级
            $('.field-tag-watch').on('click', '.item-tag', function () {
                var tag_id = $(this).attr('data-id');
                var url = '', req = {};

                // style 
                $(this).siblings().removeClass('active');
                $(this).addClass('active');

                // request
                if (tag_id) {
                    url = field._api.getPartSecondTag;
                    req = { "page": 1, "parent": tag_id };
                } else {
                    url = field._api.getAllSecondTag;
                    req = {};
                }
                field.getSecondTag(url, req);

                //获取翻页钩子函数->回调为翻页函数
                var initPage = pager.init('#page', function (num) {
                    field.getSecondTag(field._api.getPartSecondTag, {
                        "page": num,
                        "parent": tag_id
                    });
                });
                //初始化
                $('body').one('page:set', function (ev, size, totalList) {
                    initPage(size, totalList);
                });
            });
        }
    };

    field.init();
});