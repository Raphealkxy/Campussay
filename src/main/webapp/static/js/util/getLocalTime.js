/**
 * 时间戳转换时间
 * Created by WXY on 2016/1/24.
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

define(['lib/juicer'], function() {

    var getTime = function (unixTime, isFull, timeZone) {
        if (!unixTime) { return; }
        if (typeof (timeZone) == 'number') {
            unixTime = parseInt(unixTime) + parseInt(timeZone) * 60 * 60;
        }
        var time = new Date(unixTime);
        var ymdhis = "";
        ymdhis += time.getUTCFullYear() + "-";
        ymdhis += (time.getUTCMonth() + 1) + "-";
        ymdhis += time.getUTCDate();
        if (isFull === true) {
            ymdhis += " " + time.getUTCHours() + ":";
            ymdhis += time.getUTCMinutes() + ":";
            ymdhis += time.getUTCSeconds();
        }
        return ymdhis;
    };
    juicer.register("getTime",getTime);

    return getTime;
});