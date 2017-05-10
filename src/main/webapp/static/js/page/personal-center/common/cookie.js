/**
 * 个人中心  aside
 *
 * author: 王欣瑜
 * date: 2015-12-30
 */

define(['lib/jquery', 'util/funcTpl', 'util/request', 'lib/juicer'], function($, funcTpl, request) {

    var cookie = {

        set:function (name, value) {
            var Days = 30;
            var exp = new Date();
            exp.setTime(exp.getTime() + Days*24*60*60*1000);

            document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
        },

        get:function (name) {
            var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
            if(arr=document.cookie.match(reg))
                return unescape(arr[2]);
            else
                return null;
        },

        delete:function (name) {
            var exp = new Date();
            exp.setTime(exp.getTime() - 1);
            var cval = getCookie(name);
            if(cval != null)
                document.cookie= name + "="+cval+";expires="+exp.toGMTString();
        }
    };

    return cookie;

});
