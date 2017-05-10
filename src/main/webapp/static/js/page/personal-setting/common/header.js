/**
 * 个人设置  header
 *
 * author: 王欣瑜
 * date: 2016/1/21
 */
 define(['lib/jquery', 'util/funcTpl', 'page/personal-center/common/cookie'],
     function ($, funcTpl, cookie) {

     var header = {
         _userId: '',
         init: function () {

             // header init
             var header_tpl = funcTpl(header.header_tpl);
             $(header_tpl).appendTo('.personal-main-nav');
             header.event();
         },

         header_tpl: function () {
             /*
             <div class="nav-bar">
                 <div class="global-center clearfix">
                     <a href="/" class="link-index">
                         <img src="/static/img/page/personal-center/logo.png" alt="logo" title="校园说网"/>
                     </a>
                     <ul class="nav-list">
                          <li><a href="/personalCenter/order">首页</a></li>
                          <li><a href="/personalSetting/person#basic" class="nav-active">账户设置</a></li>
                          <li><a href="/user/personalIndex?user=0">我的主页</a></li>
                     </ul>
                 </div>
             </div>
             <div class="breadcrumb global-center clearfix">
                 <span>首页</span>
                 <span>个人中心</span>
             </div>
             */
         },

         event:function () {
             // 设置我的主页的连接地址
             $('.nav-list li:last-child').on('click', function () {
                 var user = JSON.parse(cookie.get("user"));
                 header._userId = user.user_id;
                 window.location.href = "/user/personalIndex?user="+header._userId;
             });
         }
     };

     return header.init;
     }
 );