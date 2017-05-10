/**
 * 弹窗插件
 * author: 王欣瑜
 * date: 2015/12/13
 */

define(["lib/jquery", "util/funcTpl", "lib/juicer"],function($, funcTpl){

    var popup = {

        // 全局模板
        "popup_tpl": function() {
            /*
             <div id="popup">
                 <div class="popup-header">
                     $${popup_header}
                     <a href="javascript:;" class="popup-close"></a>
                 </div>
                 <div class="popup-body global-center clearfix">$${popup_body}</div>
                 <div class="popup-footer clearfix">$${popup_footer}</div>
             </div>
             <div class="popup-mask-layer"></div>
             */
        },

        // 初始化方法
        "init": function(opt) {
            // 初始化弹框
            popup.initDialog(opt);

            // 销毁弹框
            popup.fireDialog(opt);
        },

        // 初始化弹框
        "initDialog": function(opts) {

            var optdef = {
                    popup_width: '456px',
                    popup_height: '353px',
                    popup_header: '',
                    popup_body: '', // html string
                    click_mask_fire: false,
                    callback: null
                },
                opt = $.extend(optdef, opts);

            var popup_html = juicer(funcTpl(this.popup_tpl), opt),
                _popup, _mark;

            if (opt.popup_body) {
                /*
                 * 重复的popup body
                 */
                _popup = $("#popup");
                _mark = $(".popup-mask-layer");

                if (_popup.length > 0) {
                    _popup.remove();
                    _mark.remove();
                    /*_popup.find('.popup-body').remove();
                    _popup.find('.popup-footer').remove();*/
                }

                $(popup_html).appendTo('body');

                /*
                js 设置位置居中出现问题，可能是$('.popup')没获取到导致
                改为css调整位置
                */
                popup.setPosition(opt.popup_width, opt.popup_height);

                if (opt.callback) opt.callback();

            }
        },

        // 销毁弹框
        "fireDialog": function(opt) {
            var popup_close = $(".popup-close"),
                popup = $("#popup"),
                popup_mask = $(".popup-mask-layer");

            popup_close.on("click", function() {
                popup.remove();
                popup_mask.remove();

                // if (opt.cancleCallback) opt.cancleCallback();
            });

            //遮罩层单击关闭
            if (opt.click_mask_fire) {
                popup_mask.on("click", function(){
                    console.log(popup);
                    popup.remove();
                    popup_mask.remove();
                });
            }
        },

        //调整位置
        "setPosition": function(w, h){
            /*$(".popup").css({
                marginTop: -($(".popup").outerHeight() / 2) + 'px',
                marginLeft: -($(".popup").outerWidth() / 2) + 'px'
            });*/
            $("#popup").css({
                width: w,
                height: h
            });
        }

    };

    return popup;
});

