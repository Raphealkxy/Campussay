/**
 * 提醒消息 的弹窗
 * author: 王欣瑜
 * date: 2016/1/28
 */
define(['lib/jquery', 'modules/tip/popup', 'util/funcTpl', 'lib/juicer'], function($, popup, funcTpl) {

    var header_tpl = function () {
        /*
         <a href="javascript:;" class="popup-close"></a>
         */
    };

    var footer_tpl = function () {
        /*
        <div class="btn-group">
            <div class="btn btn-dark">确定</div>
            <div class="btn btn-light">取消</div>
        </div>
        */
    }

    var tip = {
        warn: function (content, timeout, width, height) {
            var optdef = {
                popup_width: width || '356px',
                popup_height: height || '183px',
                popup_header: funcTpl(header_tpl),
                popup_body: content,
                click_mask_fire: true,
                callback: function () {
                    var firetip = timeout || 1200;
                    setTimeout(function () {
                        // $(".popup-close").click();
                    }, firetip);
                }
            };
            popup(optdef);
            $('head').append('<link rel="stylesheet" href="/static/js/modules/tip/tip.css" />');
        },

        info: function (content, width, height) {
            var optdef = {
                popup_width: width || '356px',
                popup_height: height || '183px',
                popup_header: funcTpl(header_tpl),
                popup_body: content,
                popup_footer: funcTpl(footer_tpl),
                click_mask_fire: true,
                callback: function () {
                }
            };
            popup(optdef);
            $('head').append('<link rel="stylesheet" href="/static/js/modules/tip/tip.css" />');
        }
    };

    return tip;
});

// tip.warn('sorry', 1000);
// tip.info('ok');