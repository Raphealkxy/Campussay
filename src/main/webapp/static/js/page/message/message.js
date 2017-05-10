/**
 * Created by wulunyi on 2016/3/5.
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery','modules/message/message'], function ($,message) {
    message.init();
});
