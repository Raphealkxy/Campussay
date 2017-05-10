/**
 * 个人设置  aside
 *
 * author: 王欣瑜
 * date: 2016/1/21
 */

define(['lib/jquery', 'util/funcTpl', 'util/request','page/personal-center/common/cookie',
        'page/personal-setting/common/uploadPhoto', 'lib/juicer'],
    function($, funcTpl, request, cookie, uploadUserPic) {

    var aside = {
        init:function (opts) {
            aside.getUser();
            aside.initMenu(opts);
        },

        user_tpl:function () {
            /*
             <div class="person person-pic">
                 <img src="${user_photo}" width="71" height="73">
             </div>
             <div class="person person-info">
                <span class="person-name">${user_name}</span>
             </div>
             <div class="person person-rank"></div>
            */
        },
        // get user's information
        getUser:function () {
            request.post('/user/getUserSettingBasic', null, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    cookie.set("user", JSON.stringify(res.data.BasicInfo));
                    var user_tpl = juicer(funcTpl(aside.user_tpl), res.data.BasicInfo);
                    $('.personal-aside-item').empty().append(user_tpl);

                    uploadUserPic();
                }
            });
        },
        // init menu
        initMenu:function (opts) {
            $('.personal-aside-item+div').empty().append(opts.aside);
            var menuList = ['basic', 'skill', 'education', 'awards', 'practice', 'job'];
            var idx = menuList.indexOf(opts.menu);
            if (idx != -1) {
                var _this = $('.personal-menu-item:eq('+idx+')');
                _this.addClass('current-menu-item');
                _this.find('i').addClass('selected');
            }
            aside.event();
            if (opts.callback) {
                opts.callback();
            }
        },
        event:function () {
            // menu style
            var menuHoverIn = function () {
                var i = $(this).find('i');
                if (i.hasClass('selected')) {
                    return;
                }
                i.addClass('selected');
            };
            var menuHoverOut = function () {
                var i = $(this).find('i');
                if (!$(this).siblings().find('i').hasClass('selected')) {
                    return;
                }
                i.removeClass('selected');
            };
            // style: hover img
            $('.personal-menu-item').hover(menuHoverIn, menuHoverOut);
            // style: clicked menu
            $('.personal-menu-item').on('click', function () {
                menuHoverIn();
                $(this).addClass('current-menu-item');
                $(this).siblings().removeClass('current-menu-item');
                $(this).siblings().find('i').removeClass('selected')
            });
        }
    };

    return aside.init;

});
