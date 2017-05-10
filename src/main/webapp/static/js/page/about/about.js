/**
 * Created by liangbijie on 2016/4/6.
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'modules/baseMoudle'], function($) {
    var about = {
        init: function() {
            about.geturl();
            about.sideNavClick();
            about.info.navId = $.getUrlVar('navId');
            if(about.info.navId){
                about.initSkip(about.info.navId);
            }
        },
        info:{
            navId:''
        },
        sideNavClick: function() {
            var _this = $('#sideNav');
            _this.find('h3').click(function() {
                var id = $(this).attr('data-id');
                skip(id);
                //移除所有的hover样式
                _this.find('ul span').removeClass('hoverColor');
                _this.find('h3').removeClass('hover hoverSlide');
                //当前点击的对象加hover样式
                $(this).addClass('hover').parent().parent().find('ul').slideUp();

                //滑入滑出操作
                if (!$(this).next().is(":visible") && $(this).siblings('ul').length == 1) {
                    $(this).next().slideDown();
                    $(this).addClass('hoverSlide')
                } else {
                    $(this).removeClass('hoverSlide')
                }

            });
            //点二级导航的操作
            _this.find('ul span').on('click', function() {

                var id = $(this).attr('data-id');
                skip(id);
                _this.find('ul span').removeClass('hoverColor');
                $(this).addClass('hoverColor');
            });

            function skip(id) {
                //页面跳转操作
                if (!id) {
                    return;

                }
                $('#content').children().hide();
                $('#' + id).fadeIn();
            }
        },
        initSkip:function(idName){
            var id = "span[data-id='"+idName+"']";
            console.log($(id).length)
            $(id).trigger('click');
        },
        geturl: function () {
            $.extend({
                //url参数约定search表示问号，$表示等于号
                getUrlVars: function () {
                    var vars = [], hash;
                    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
                    for (var i = 0; i < hashes.length; i++) {
                        hash = hashes[i].split('=');
                        vars.push(hash[0]);
                        vars[hash[0]] = hash[1];
                    }
                    return vars;
                },
                getUrlVar: function (name) {
                    var url = $.getUrlVars()[name];
                    if(name == 'url' && url){
                       url = url.replace('search',"?").replace('$','=');
                    }
                    return url;
                }
            });
        }
    };
    about.init();
});