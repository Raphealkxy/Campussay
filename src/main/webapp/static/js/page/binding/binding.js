/**
 * �����䡢�ֻ�
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

            // ������֤��
            $('.send-verification-code').on('click', function() {
                mailObj.mail = $(this).prev().val().trim();

                // ���������Ч��
                var mail_pattern = /^\w+([\-\.]\w+)*@\w+([\-\.]\w+)*\.\w+([\-\.]\w+)*$/;
                if (!mail_pattern.test(mailObj.mail)) return;

                // TODO: ��������Ÿ���̨
                request.post('', mailObj, function() {});
            });

            // ����
            $('.activate').on('click', function() {
                mailObj.code = $(this).prev().val().trim();

                // ��������
                request.post('', mailObj, function() {});
            });
        }

    };

    binding.init();
});
