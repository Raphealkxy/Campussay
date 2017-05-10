/**
 * Created by liangbijie on 2016/1/28.
 */
define(['lib/jquery'],function($){
    $('head').append('<link rel="stylesheet" href="/static/js/modules/alert/alert.css" />');
var alertBox = {
    init:function($dom,question,trueText,falseText){
        alertBox._alertInfo($dom,question,trueText,falseText);
        return function($dom,question,trueText,falseText){
            alert(falseText);
        }
    },
    _alertInfo:function($dom,question,trueText,falseText){
        var dom =
        '<div class="MsgBack">'+
        '</div>'+
        '<div class="alert">'+
        '<img src="/static/img/page/hot/alert.png">'+
        '<p>'+'</p>'+
        '<a href="javascript:;"><span class="report">'+'</span></a>'+
        '<a href="javascript:;"><span class="close">'+'</span></a>'+
        '</div>';
        $("body").append(dom);
        $('.alert p').html(question);
        $('.report').html(trueText);
        $('.close').html(falseText);
    }
};
    return alertBox
});