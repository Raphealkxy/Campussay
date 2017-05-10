/**
 * @author 吴伦毅
 * @description 提示弹窗
 * @description 暂时没做扩展，只做了发布talking的
 * @description 只需要引入即可用$('body').trigger('get:type',[msg])
 * @description type表示调用的类型
 * @description msg穿入提示的信息
 * @description type tip(提示)
 */
define(['lib/jquery', "util/funcTpl", 'lib/juicer'], function ($, funcTpl) {
    var tipPop = {
        init: function () {
            $('head').append('<link rel="stylesheet" href="/static/js/modules/topic-pop/tipPop.css" />');
            tipPop._watch();
        },
        _tipPanel: null,
        _tpl: function () {
            /*
             <i class="pop-icon"></i>
             <i class="pop-close"></i>
             <p class="pop-text"><i class="error"></i> 发布失败! <span class='pop-content'>${msg}</span></p>
             */
        },
        _watch: function () {
            $('body').on('get:tip', function (ev, msg) {
                tipPop._paint(msg);
            });
        },
        _paint: function (msg) {
            if (!tipPop._tipPanel) {
                tipPop._tipPanel = $('<div class="pop-wrap">').html(juicer(funcTpl(tipPop._tpl), {msg: msg}));//初始化提示面板结构

                $('body').append(tipPop._tipPanel);
            } else {
                $('.pop-content').html(msg);//植入提示信息

                tipPop._tipPanel.show();//展示提示面板
            }

            var timer = null;//定时器

            $('.pop-close').on('click', function () {
                clearTimeout(timer);
                tipPop._tipPanel.hide();
            });

            timer = setTimeout(function () {
                tipPop._tipPanel.hide();
            }, 2000);

        }
    };

    tipPop.init();
});
