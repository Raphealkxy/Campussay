/**
 * 账户设置——认证设置
 * @author: wangxinyu
 * @date: 2016/1/21
 * @last-modified: 2016/1/21
 */
require.config({
    baseUrl: CP.STATIC_ROOT,
    shim:{
        fileupload:{
            deps:['lib/jquery'],
            exports: 'fileupload'
        }
    },
    paths:{
        fileupload:"modules/fileUpload/fileUpload"
    }
});

require(['lib/jquery', 'util/funcTpl', 'util/request', 'page/personal-setting/common/setting',
    'modules/topic-pop/commonPop', 'modules/baseMoudle', 'fileupload'],
    function($, funcTpl, request, setting, tip, baseMoudle) {

        var authenticationSetting = {
            init: function() {
                var opt = {
                    tab: 'authentication',
                    aside: funcTpl(authenticationSetting.authentication_aside_tpl),
                    callback: authenticationSetting.event
                };
                setting(opt);
            },

            _checkState: null,

            authentication_aside_tpl:function () {
                /*
                 <ul class="personal-menu authentication-setting-menu">
                     <li class="personal-menu-item current-menu-item">
                         <i class="icon-identity selected"></i>
                         <a href="#">学生证认证</a>
                         <ul class="sub-personal-menu">
                             <!--<li class="sub-personal-menu-item"><i> </i> <a href="#">身份证认证</a></li>
                             <li class="sub-personal-menu-item"><i> </i> <a href="#">学生证认证</a></li>-->
                         </ul>
                     </li>
                     <!--<li class="personal-menu-item">
                         <i class="icon-other-identity"></i>
                         <a href="#">其他认证</a>
                         <ul class="sub-personal-menu">
                             <li class="sub-personal-menu-item"><i> </i> <a href="#">学历认证</a></li>
                             <li class="sub-personal-menu-item"><i> </i> <a href="#">技能证书认证</a></li>
                         </ul>
                     </li>-->
                 </ul>
                 */
            },

            event: function() {
                request.post('/education/getStudentPicture', function (res) {
                    res = JSON.parse(res);
                    var $label2 = $('#label-2');
                    if (res.code == 1) {
                        var data = res.data;
                        if (res.data.length > 0) {
                            $('.preview').attr('src', data[0].student_check_picture);
                            var $span = $('.upload-info span');
                            authenticationSetting._checkState = data[0].student_check_state;
                            switch (authenticationSetting._checkState) {
                                case 0:
                                    // 已提交 待审核
                                    $span.text('待审核-点击图片可重传');
                                    break;
                                case 1:
                                    // 审核通过
                                    $label2.find('.item-operation').html('已通过学生证认证');
                                    break;
                                case 2:
                                    // 审核未通过
                                    $span.text('审核未通过-点击图片可重传');
                                    break;
                                case 3:
                                    // 未提交
                                    // $span.text('点击图片上传学生证');
                                    break;
                            }
                        }
                    }
                    // 图片上传
                    $('.student-card-upload').ajaxfileuploadPic({
                        url:"/education/educationupload"
                    });
                    // 保存图片上传
                    $label2.on('click', '.upload', function () {
                        var address = $(this).parents('.item-body').find('img').attr('src'),
                            state;

                        request.post('/education/insertStudent', {
                            "studentCheckPicture": address,
                            "studentCheckState": 0
                        }, function (res) {
                            res = JSON.parse(res);
                            if (res.code == 1) {
                                tip.info({
                                    tip: '保存成功', time: 1000
                                })
                            } else {
                                tip.info({
                                    tip: '保存失败', time: 1000
                                })
                            }
                        });
                    });
                });
            }
        };

        authenticationSetting.init();
    }
);
