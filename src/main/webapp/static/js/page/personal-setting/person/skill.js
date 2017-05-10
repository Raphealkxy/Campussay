/**
 * 个人设置-技能证书
 * Created by WXY on 2016/1/26.
 */
define(['lib/jquery', 'util/funcTpl', 'util/request', 'lib/juicer'],
    function ($, funcTpl, request) {

    var skill = {
        init:function () {
            // skill.event();
        },

        event:function () {
            // 上传图片
            $(".btn-upload").ajaxfileuploadPic({
                url:"/honeycomb/upload/w/uploadImgFile"
            });
        }
    };

    return skill.init;
});