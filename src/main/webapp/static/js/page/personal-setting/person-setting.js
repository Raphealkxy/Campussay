/**
 * 账户设置——个人设置
 * @author: wangxinyu
 * @date: 2016/1/21
 * @last-modified: 2016/1/25
 */
require.config({
    baseUrl: CP.STATIC_ROOT,
    shim:{
        datepicker:{
            deps:['lib/jquery'],
            exports:'datepicker'
        },
        fileupload:{
            deps:['lib/jquery'],
            exports: 'fileupload'
        }
    },
    paths:{
        datepicker:"modules/Date/jquery.datetimepicker",
        fileupload:"modules/fileUpload/fileUpload",
        person:"page/personal-setting/person"
    }
});
require(['lib/jquery', 'util/funcTpl', 'util/request', 'page/personal-setting/common/setting',
        'person/basic', 'person/education','person/awards','person/practice','person/job',
        'modules/baseMoudle', 'lib/juicer', 'datepicker', 'fileupload'],
    function ($, funcTpl, request, setting, basic, education, awards, practice, job, baseMoudle) {

        var personSetting = {
            // _addBackup: null,
            _anchor: 'basic',

            init: function () {
                personSetting._anchor = window.location.hash.replace("#", "");
                var opt = {
                    tab: 'person',
                    aside: funcTpl(personSetting.person_aside_tpl),
                    menu: personSetting._anchor,
                    callback: personSetting.event
                };
                setting(opt);
            },
            person_aside_tpl: function () {
                /*
                 <ul class="personal-menu person-setting-menu">
                     <li class="personal-menu-item" data-label="basic">
                         <i class="icon-basic"></i>
                         <a href="#basic">基本信息</a>
                     </li>
                     <li class="personal-menu-item" data-label="skill">
                         <i class="icon-star"></i>
                         <a href="#skill" data-label="basic-info">技能证书</a>
                     </li>
                     <li class="personal-menu-item" data-label="education">
                         <i class="icon-book"></i>
                         <a href="#education" data-label="basic-info">教育经历</a>
                     </li>
                     <li class="personal-menu-item" data-label="awards">
                         <i class="icon-cup"></i>
                         <a href="#awards" data-label="basic-info">获奖成果</a>
                     </li>
                     <li class="personal-menu-item" data-label="practice">
                         <i class="icon-flag"></i>
                         <a href="#practice" data-label="basic-info">实践经历</a>
                     </li>
                     <li class="personal-menu-item" data-label="job">
                         <i class="icon-box"></i>
                         <a href="#job" data-label="basic-info">工作经验</a>
                     </li>
                 </ul>
                 */
            },
            event: function () {
                var initContent = function (anchor) {
                    if (anchor == '') anchor = 'basic';
                    var label = '.item-'+anchor;
                    var $main = $(label).find('.item-body');
                    var isvisible = $main.is(':visible');

                    // style
                    if (isvisible) return;
                    $('.item-content > .item-body').slideUp();
                    $main.delay(400).slideDown(1000);
                    
                    // js
                    if (anchor == 'skill') return;
                    eval(anchor)(personSetting.datepicker);
                };
                initContent(personSetting._anchor);

                // 点击标题时候 高亮对应菜单
                var highlightMenu = function (hash) {
                    var list = $('.personal-menu-item');
                    var $tar = list.find('a[href="'+hash+'"]');
                    list.removeClass('current-menu-item');
                    list.find('i').removeClass('selected');
                    $tar.parent().addClass('current-menu-item');
                    $tar.prev().addClass('selected');
                };

                // 点击左侧菜单展开
                $('.personal-menu').on('click', '.personal-menu-item', function () {
                    var anchor = $(this).attr('data-label');
                    initContent(anchor);
                });
                // 点击标题展开
                $('.item-title').on('click', function() {
                    var _main = $(this).next(),
                        anchor = _main.attr('data-label');
                    window.location.hash = '#'+anchor;
                    initContent(anchor);
                    highlightMenu('#'+anchor);
                });

                // 模拟选择框选择
                $('.item-body').on('click', '.select', function (e) {
                    $('.item-body .select').not($(this)).find('ul').hide();
                    var e = e || window.event,
                        tar = e.target || e.srcElement;
                    var _content = $(this).find('ul');

                    _content.toggle(100, function () {
                        if (tar.nodeName.toLowerCase() == 'li') {
                            _content.prev('input').val(tar.innerHTML);
                            _content.prev('input').attr('data-id', $(tar).data('id'));
                        } else {
                            return;
                        }
                    });

                }).on('mouseleave', '.select', function (e) {
                    $(this).find('ul').hide();
                });

                // 取消
                $('.cancel-binding').click(function () {
                    window.location.reload();
                });
            },
            datepicker:function () {
                $('.form_datetime').datetimepicker({
                    timepicker: false,
                    format: 'Y/m'
                });
            }
        };
        personSetting.init();
    }
);
   