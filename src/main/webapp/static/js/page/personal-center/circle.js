/**
 * 个人中心  首页  我的圈子
 *
 * @author: wangxinyu
 * @date: 2016-1-18
 * @last-modified: 2016-1-21
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'util/funcTpl', 'util/request', 'page/personal-center/common/layout','modules/page/page',
    'modules/topic-pop/commonPop','modules/baseMoudle','lib/juicer'],
    function ($, funcTpl, request, layout,pager,tip,baseMoudle) {

        var circle = {
            init:function () {
                var opt = {
                    tab: '',
                    menu: 'circle',
                    callback: circle.tab
                };
                layout(opt);
                // circle.tab();
            },
            // 数据集合
            _data:{
                fansUrl: '/user/getMyFansMsg',
                attentionUrl: '/user/getMyConcernMsg',
                getListNum: '/user/getMyFansAndConcernNum',
                attentionUserUrl: '/user/attentionUser'
            },
            // 获取粉丝、关注人列表
            getCircle:function (page, state) {
                request.post(circle._data[state], {
                    "page": page
                }, function (res) {
                    res = JSON.parse(res);
                    if (res.code == 1) {
                        var circle_tpl = juicer(funcTpl(circle.circle_tpl), res.data);
                        $('.circle-content-list').html(circle_tpl).attr('id', state);
                        //触发翻页初始化
                        $('body').trigger('page:set', [10, res.data.rows]);
                    } else {
                        alert(res.msg);
                    }

                    circle.bind();
                });
            },
            // 粉丝、关系切换
            tab:function () {
                // 获取粉丝和关注数量
                request.post(circle._data.getListNum, {}, function (res) {
                    res = JSON.parse(res);
                    if(res.code == 1){
                        $('.circle-tab span').eq(0).html("("+res.data.concernNum+")");
                        $('.circle-tab span').eq(1).html("("+res.data.fansNum+")");
                    }
                });
                // 分页
                $('.personal-content-tab').on('click', '.tab-item', function(){
                    var idx = $('.tab-item').index($(this));
                    var state = (idx == 0 ? 'attentionUrl' : 'fansUrl');
                    $('.circle-content-list').attr('id', state);
                    circle.getCircle(1, state);

                    //获取翻页钩子函数->回调为翻页函数
                    var initPage = pager.init('#page', function (num) {
                        circle.getCircle(num, state);
                    });
                    //初始化
                    $('body').one('page:set', function (ev, size, totalList) {
                        console.log("success");
                        initPage(size, totalList);
                    });
                });

                $('.tab-item:eq(0)').click();
            },
            // 关注 & 取关操作
            attentionUser: function (attentionUrl, attention) {
                var self = $(this),
                    thisItem = $(this).parents('.circle-list-item');
                request.post(attentionUrl, {
                    "userId": thisItem.attr('data-id'),
                    "attention": attention
                }, function (res) {
                    res = JSON.parse(res);
                    var message = '';
                    if (1 == res.code) {
                        var $concern = $('.circle-tab span').eq(0),
                            concernNum = +$concern.text().match(/\d+/)[0];
                        
                        if (attention == 1) {
                            // 我的粉丝 ：关注 -> 相互关注
                            self.find('span').text('相互关注');
                            self.removeClass('uncare').addClass('care-each');
                            message = '关注成功';

                            // tab 标签栏上 关注数和粉丝数改变
                            $concern.html("("+(concernNum + 1)+")");
                        } else {
                            var state = $('.circle-content-list').attr('id');
                            if (state == 'attentionUrl') {

                                // 我的关注 ：取消关注 -> 从我的关注中移除
                                thisItem.remove();
                            } else if (state == "fansUrl") {

                                // 我的粉丝 ：取消关注 -> 变为未关注
                                self.find('span').text('未关注');
                                self.removeClass('care-each').addClass('uncare');
                            } else {
                                message = '嗷噢，好像出错了，请联系管理员';
                            }
                            message = '取消关注成功';
                            
                            // tab 标签栏上 关注数和粉丝数改变
                            $concern.html("("+(concernNum - 1)+")");
                        }
                    } else {
                        message = res.msg;
                    }
                    tip.info({ tip: message, time: 1500 });
                });
            },
            bind: function () {
                /*
                 * 1.我的关注
                 * 状态：已关注、相互关注
                 * 操作：取消关注 -> 消失
                 * 2.我的粉丝
                 * 状态：未关注、相互关注
                 * 操作：关注 -> 相互关注、取消关注 -> 未关注
                 *
                 * 每次关注/取关一个人后，先解绑，再重新绑定
                 * */
                var text = '';
                var getClass = function (_this) {
                    return _this.attr('class').replace('operation ', '');
                };
                $('.operation').hover(function () {
                    var className = getClass($(this));
                    var $span = $(this).find('span');
                    if (className == 'uncare') {
                        $span.text('关注TA');
                    } else {
                        text = $(this).find('span').text();
                        $span.text('取消关注');
                    }
                }, function () {
                    var className = getClass($(this));
                    var $span = $(this).find('span');
                    if (className == 'uncare') {
                        $span.text('未关注');
                    } else {
                        $span.text(text);
                    }
                }).on('click', function () {
                    var className = getClass($(this));
                    if (className == 'uncare') {
                        circle.attentionUser.call(this, circle._data.attentionUserUrl, 1);
                    } else {
                        if (confirm('确认取消关注？')) {
                            circle.attentionUser.call(this, circle._data.attentionUserUrl, 0);
                        }
                    }
                });
            },
            // 粉丝、关注人列表模板
            circle_tpl:function () {
                /*
                 {@each list as circle}
                 <div class="circle-list-item" data-id="${circle.user_id}">
                     <ul class="item-content clearfix">
                         <li class="item item-describe">
                             <div class="item-describe-pic">
                                 <a href="/user/personalIndex?user=${circle.user_id}"><img src="${circle.user_photo}" width="71" height="73" alt="用户头像"></a>
                             </div>
                             <div class="item-describe-info">
                                 <div class="item-care-name f18 clearfix"><a href="/user/personalIndex?user=${circle.user_id}">${circle.user_name}</a></div>
                                 <div class="item-basic-info f12">
                                     <ul>
                                         <li class="care-person-school">${circle.user_campus_name}</li>
                                         <li class="split-bar">|</li>
                                         <li class="care-person-fans">粉丝<span>${circle.fansNum}</span></li>
                                         <li class="split-bar">|</li>
                                         <li class="care-person-care">关注<span>${circle.attentionNum}</span></li>
                                     </ul>
                                 </div>
                                 <div class="item-field item-good-field f12">
                                     <span>擅长领域：</span>
                                     <div class="item-label-info tag f12">
                                         {@each circle.skillArea as tag}
                                         <div class="item-tag tag-commerce">
                                             <span class="tag-rect">${tag.talking_type_name}</span>
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
                                     {@each circle.followmap as tag}
                                     <div class="item-tag tag-commerce" style="margin-bottom:8px">
                                         <span class="tag-rect">${tag.talking_type_name}</span>
                                     </div>
                                     {@/each}
                                 </div>
                             </div>
                         </li>
                         <li class="item item-operation">
                             {@if circle.eachOther == 0}
                                 <button class="operation careed">
                                     <i class="icon"></i><span>已关注</span>
                                 </button>
                             {@else if circle.eachOther == 1}
                                 <button class="operation care-each">
                                     <i class="icon"></i><span>相互关注</span>
                                 </button>
                             {@else}
                                 <button class="operation uncare">
                                     <i class="icon"></i><span>未关注</span>
                                 </button>
                             {@/if}
                         </li>
                     </ul>
                 </div>
                 {@/each}
                 */
            }
        };

        circle.init();
    }
);