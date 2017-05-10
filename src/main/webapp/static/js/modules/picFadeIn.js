/**
 * Created by liangbijie on 2016/4/2.
 */
define(['lib/jquery'], function($) {
     var picFadeIn = {
         init:function(){
             $('.pic1').each(function() {
                 $(this).css('display','none');
                 $(this).load(function () {
                     $(this).fadeIn();
                 })
             });
         }
     };
     return picFadeIn.init;

});