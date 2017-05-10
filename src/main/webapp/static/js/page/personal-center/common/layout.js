/**
 * 个人设置  初始化布局
 *
 * author: 王欣瑜
 * date: 2016/1/21
 */

define(['lib/jquery', 'util/funcTpl', 'page/personal-center/common/header', 'page/personal-center/common/aside','page/personal-center/common/tab','modules/baseMoudle'],
    function($, funcTpl, header, aside,tab) {

    var layout = {
        init: function(opts) {
            var optdef = {
                    tab: '',
                    menu: '',
                    callback: null
                },
                opt = $.extend(optdef, opts);
            aside(opt.menu);
            tab(opt);
            header();
        }
    };

    return layout.init;
});