/**
 * 个人设置  上传头像
 *
 * author: 王欣瑜
 * date: 2016/3/21
 */
define(['lib/jquery', 'util/funcTpl', 'util/request', 'modules/topic-pop/commonPop', 'modules/navigator',
        'modules/fileUpload/ajaxfileupload', 'lib/juicer'],
    function($, funcTpl, request, tip, navi) {

    var uploadUserPhoto = {
        init:function () {
            uploadUserPhoto.event();
        },
        _url: {
            uploadPic: '/user/userpicupload',
            insertPic: '/user/insertuserphoto',
            modifyPic: '/user/updateuserphoto'
        },
        user_tpl:function () {
            /*
             <div class="item-body" id="uploadUserPhoto">
                 <div class="item item-upload-user-photo">
                     <a class="select-img-btn user-photo-upload" data-file="userPhotoUpload">
                         <img src="" class="preview" width="83" height="73" />
                         <i class="icon-upload"></i>
                     </a>
                     <input type="file" name="morePicMainFile" id="userPhotoUpload" style="display:none" value="" />
                     <div class="upload-info">
                         <i class="icon-warn"></i>
                         <span>请上传你的美美的照片吧~</span>
                     </div>
                 </div><br/>
                 <div class="item item-operation">
                     <a href="javascript:;" class="operation upload">确定</a>
                 </div>
             </div>
            */
        },
        event:function () {
            var self = this;
            var $userPic = $('.personal-aside').find('.person-pic>img');
            var picUrl = '';
            $userPic.on('click', function() {
                self.$img = $(this);
                // 出现弹窗
                var tmp = funcTpl(uploadUserPhoto.user_tpl);
                tip.uploadPhoto({tip: tmp});
                // 上传 || 修改头像 url
                var srcVal = $(this).attr('src');
                //var saveUrl = (srcVal=="")? uploadUserPhoto._url.insertPic : uploadUserPhoto._url.modifyPic;
                var saveUrl = uploadUserPhoto._url.modifyPic;
                // 图片上传
                upload();
                $('#uploadUserPhoto').on('click', '.upload', function () {
                    savePic(saveUrl);
                });
            });
            // upload user pic
            var upload = function () {
                var preview = null;
                $('.user-photo-upload').click(function(){
                    var file = $(this).attr("data-file")? "#"+$(this).attr("data-file") : "",
                        preview = $(this).attr("data-preview") ? "#"+$(this).attr("data-preview") : $(this).find(".preview");

                    $(file).click();
                    // 兼容IE
                    if (navi.ie) {
                        setTimeout(function() {
                            changeFunc();
                        }, 0);
                    }
                    $(file).on('change', function () {
                        changeFunc();
                    });
                    var changeFunc = function () {
                        var $info = $('.upload-info>span');

                        if( $(file).val() != ""){
                            if(!/\.jpg$|\.gif$|\.png$/i.test($(file).val())){
                                $info.html('上传图片格式不正确,请确保是gif,png,jpg图片');
                                $(file).val("");
                                return;
                            }

                            $info.html('正在上传中，请稍等....');
                            $.ajaxFileUpload({
                                url:uploadUserPhoto._url.uploadPic,            //需要链接到服务器地址
                                secureuri:false,
                                fileElementId:$(file).attr("id"),   //文件选择框的id属性
                                dataType: 'json',
                                success: function(res){
                                    var data = res.data;
                                    picUrl = CP.imgServer+data.url;
                                    $(preview).attr("src",picUrl);
                                    $(preview).show();
                                    $info.html('图片上传成功 O(∩_∩)O~');
                                },
                                error: function (data, status, e)
                                {
                                    $info.html("上传失败:"+e);
                                }
                            });
                        }
                    };
                    // $(file).replaceWith('<input type="file" name="morePicMainFile" id="userPhotoUpload" style="display:none" value="" />');
                });
            };
            // 保存图片上传
            var savePic = function (saveUrl) {
                var address = $('#uploadUserPhoto').find('img').attr('src');
                request.post(saveUrl, {
                    "user_photo": address
                }, function (res) {
                    res = JSON.parse(res);
                    if (res.code == 1) {
                        $('.upload-info>span').html('设置头像成功');
                        $userPic.attr('src', picUrl);
                        $('#J_pop').remove();
                    } else {
                        $('.upload-info>span').html('设置头像失败');
                    }
                });
            };
        }
    };

    return uploadUserPhoto.init;

});
