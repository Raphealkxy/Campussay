/**
 * 个人设置  初始化布局
 *
 * author: 王欣瑜
 * date: 2016/1/21
 */

define(['lib/jquery', 'util/funcTpl', 'page/personal-setting/common/header', 'page/personal-setting/common/aside','page/personal-setting/common/tab'],
    function($, funcTpl, header, aside,tab) {

    var layout = {
        init: function(opts) {
            var optdef = {
                    tab: '',
                    aside: '',
                    menu: '',
                    callback: null
                },
                opt = $.extend(optdef, opts);

            tab(opt.tab);
            aside(opt);
            header();
        }
    };

    return layout.init;
});