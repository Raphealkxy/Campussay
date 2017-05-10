/**
 * Created by liangbijie on 2016/1/30.
 * @modify: wangxinyu
 * @date: 2016/3/26
 *
 * config 参数的配置，请参考 modules/share/js/share.js
 */
define(['lib/jquery','modules/share/js/share'],function ($) {
    var index = {
        init:function() {
            $('head').append('<link rel="stylesheet" href="/static/js/modules/share/css/share.min.css" />');

            var $config = {
                source: 'www.campussay.com'
            };

            // Domready after initialization
            $(function () {
                $('.social-share').share($config);
            });
        }
    };
    return index.init;
});