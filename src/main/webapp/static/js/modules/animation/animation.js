/**
 * 过渡动画
 * Created by WXY on 2016/3/25.
 */
define(['lib/jquery'], function($) {
    var animation = {
        init:function() {
            var animation = funcTpl(animation.animationTpl);
            $(animation).appendTo('body');
            $('head').append('<link rel="stylesheet" href="./animation.css" />');

            this.fire();
        },
        // 注销
        fire:function(cb, time) {
            var duration = time || 2000;
            setTimeout(function() {
                $('.animation-container').remove();
            }, duration);
        },
        animationTpl:function() {
            /*
             <div class="animation-container">
                 <div class="animation1">
                     <div class="bar1"></div>
                     <div class="bar2"></div>
                     <div class="bar3"></div>
                     <div class="bar4"></div>
                 </div>
             </div>
             <div class="popup-mask-layer"></div>
            */
        }
    };

    return animation.init;
});