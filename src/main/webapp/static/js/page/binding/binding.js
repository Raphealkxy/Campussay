/**
 * 绑定邮箱、手机
 *
 * author: wangxinyu
 * date: 2015-12-24
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'util/request'], function($, request) {
    var binding = {
        init: function() {
            binding.validate();
            binding.tryNow();
        },

        tryNow: function() {
            $('.try-now').on('click', function() {
                window.location = '/index.html';
            });
        },

        validate: function() {
            var mailObj = {};

            // 发送验证码
            $('.send-verification-code').on('click', function() {
                mailObj.mail = $(this).prev().val().trim();

                // 检测邮箱有效性
                var mail_pattern = /^\w+([\-\.]\w+)*@\w+([\-\.]\w+)*\.\w+([\-\.]\w+)*$/;
                if (!mail_pattern.test(mailObj.mail)) return;

                // TODO: 发送邮箱号给后台
                request.post('', mailObj, function() {});
            });

            // 激活
            $('.activate').on('click', function() {
                mailObj.code = $(this).prev().val().trim();

                // 立即激活
                request.post('', mailObj, function() {});
            });
        }

    };

    binding.init();
});
