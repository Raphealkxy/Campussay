/**
 * 个人设置  tab
 *
 * author: 王欣瑜
 * date: 2016/1/21
 */

define(['lib/jquery', 'util/funcTpl', 'page/personal-setting/common/aside'], function($, funcTpl, aside) {

    var tab = {
        init:function (type) {
            tab.initActiveTab(type);
            tab.event();
        },

        // init current tab item
        initActiveTab:function (type) {
            var menuList1 = ['person', 'security', 'authentication'];

            var pos = menuList1.indexOf(type);
            var li = $('.personal-content-tab li').eq(pos);
            li.addClass('current-tab-item');
        },

        event:function () {
            // style: switch tab
            $('.personal-content-tab').on('click', '.tab-item', function(){
                if ($(this).hasClass('current-tab-item')) {
                    return;
                }
                $(this).addClass('current-tab-item');
                $(this).siblings().removeClass('current-tab-item');

                // aside($('.tab-item').index($(this)));

            });

            var arr = ['person', 'security', 'authentication'];
        }
    };

    return tab.init;
});