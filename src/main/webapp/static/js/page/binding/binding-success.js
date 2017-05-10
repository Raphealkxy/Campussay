/**
 * ¼¤»î³É¹¦
 *
 * author: wangxinyu
 * date: 2015-12-24
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery'], function($) {

    var activate = {
        init: function() {
            activate.jumpCountdown();
        },

        jumpCountdown: function() {
            var num = 5, _this = $('.jump-countdown');

            var countdown = setInterval(function () {
                if (num === 0) {
                    clearInterval(countdown);
                    activate.returnIndex();

                    return;
                }
                num--;
                _this.html(num+" ");
            }, 1000);

        },

        returnIndex: function() {
            window.location = '/index.html';
        }
    };

    activate.init();
});