/**
 * 个人中心  tab
 *
 * @author: 王欣瑜
 * @date: 2015/12/30
 * @last-modified: 2016/1/21
 */

define(['lib/jquery', 'util/funcTpl'], function($, funcTpl) {

    var tab = {
        init:function (opt) {
            if(opt.callback){
                opt.callback();
            }
            tab.event();
            tab.skipSwitch();
        },

        event:function () {
            // style: switch tab
            $('.personal-content-tab').on('click', '.tab-item', function(){
                if ($(this).hasClass('current-tab-item')) {
                    return;
                } else {
                    $(this).addClass('current-tab-item');
                    $(this).siblings().removeClass('current-tab-item');
                }
            });
        },
        skipSwitch:function(){
            function GetQueryString(name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
                var r = window.location.search.substr(1).match(reg);
                if (r!=null) return (r[2]); return null;
            }
            var sname = GetQueryString("type");
            if(sname!=null)
            {
                $('.tab-item').eq(1).trigger('click');
            }
        }
    };

    return tab.init;
});
