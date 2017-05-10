/**
 * 个人中心  aside
 *
 * author: 王欣瑜
 * date: 2015-12-30
 */

define(['lib/jquery', 'util/funcTpl', 'util/request', 'page/personal-center/common/cookie',
        'page/personal-setting/common/uploadPhoto', 'lib/juicer'],
    function($, funcTpl, request, cookie, uploadUserPic) {

    var aside = {
        init:function (type) {
            aside.getUser(type);
        },

        user_tpl:function () {
            /*
             <div class="personal-aside-item personal-info">
                 <div class="person person-pic">
                     <img src="${user_photo}" width="71" height="73">
                 </div>
                 <div class="person person-info">
                     <span class="person-name">${user_name}</span>
                 </div>
                 <div class="person person-rank"></div>
             </div>
             <ul class="personal-aside-item personal-menu">
                 <li class="personal-menu-item">
                     <i class="my-order"></i>
                     <a href="/personalCenter/order">我的订单</a>
                 </li>
                 <li class="personal-menu-item">
                     <i class="my-publish"></i>
                     <a href="/personalCenter/publish">我的发布</a>
                 </li>
                 <li class="personal-menu-item">
                     <i class="my-circle"></i>
                     <a href="/personalCenter/circle">我的圈子</a>
                 </li>
                 <li class="personal-menu-item">
                     <i class="receive-money-account"></i>
                     <a href="/personalCenter/account">收款账户</a>
                 </li>
             </ul>
            */
        },

        // get user's information
        getUser:function (type) {
            request.post('/user/getUserSettingBasic', null, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    cookie.set("user", JSON.stringify(res.data.BasicInfo));
                    var user_tpl = juicer(funcTpl(aside.user_tpl), res.data.BasicInfo);
                    $('.personal-aside').append(user_tpl);

                    aside.initActiveMenu(type);
                    aside.event();
                    uploadUserPic();
                }
            });
        },

        // init current menu item
        initActiveMenu:function (type) {
            var menuList1 = ['order', 'publish', 'circle', 'account'];

            var pos = menuList1.indexOf(type);
            var li = $('.personal-menu li').eq(pos);
            li.addClass('current-menu-item');
            li.find('i').addClass('selected');
        },

        event:function () {
            // style: hover img
            $('.personal-menu-item').hover(function() {
                var i = $(this).find('i');
                if (i.hasClass('selected')) {
                    return;
                } else {
                    i.addClass('selected');
                }
            }, function () {
                var i = $(this).find('i');
                if ($(this).siblings().find('i').hasClass('selected')) {
                    i.removeClass('selected');
                } else {
                    return;
                }
            });
        }
    };

    return aside.init;

});
