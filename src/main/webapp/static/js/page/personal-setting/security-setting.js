/**
 * 账户设置——安全设置
 * @author: wangxinyu
 * @date: 2016/1/18
 * @last-modified: 2016/1/21
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'util/funcTpl', 'page/personal-setting/common/setting','modules/baseMoudle'],
    function($, funcTpl, setting, baseMoudle) {

        var securitySetting = {
            init: function() {
                var opt = {
                    tab: 'security',
                    aside: funcTpl(securitySetting.security_aside_tpl),
                    callback: securitySetting.event
                };
                setting(opt);

            },

            security_aside_tpl:function () {
                /*
                 <ul class="personal-menu security-setting-menu">
                 <li class="personal-menu-item current-menu-item">
                 <i class="icon-account selected"></i>
                 <a href="#">账号绑定</a>
                 <!--<ul class="sub-personal-menu">
                 <li class="sub-personal-menu-item"><i> </i> <a href="#">邮箱绑定</a></li>
                 <li class="sub-personal-menu-item"><i> </i> <a href="#">QQ账号绑定</a></li>
                 </ul>-->
                 </li>
                 <li class="personal-menu-item">
                 <i class="icon-lock"></i>
                 <a href="/user/pwdFound/1">找回或修改密码</a>
                 </li>
                 </ul>
                 */
            },

            event: function() {}
        };

        securitySetting.init();
    }
);
