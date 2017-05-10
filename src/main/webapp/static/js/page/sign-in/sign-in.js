/**
 * Created by liangbijie on 2016/3/12.
 */

require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'page/sign-in/sign'], function ($, sign) {
    var sign_in = {
        init: function () {
            sign();
        }
    };
    sign_in.init();
});
