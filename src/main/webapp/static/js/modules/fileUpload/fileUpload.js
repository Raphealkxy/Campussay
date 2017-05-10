/**
 * 文件上传
 * Created by WXY on 2016/1/25.
 */
require.config({
    baseUrl :  CP.STATIC_ROOT
});
define(['lib/jquery', 'modules/topic-pop/commonPop', 'modules/navigator', 'modules/fileUpload/ajaxfileupload'], function ($, tip, navi) {

    $.fn.extend({
        "ajaxfileuploadPic": function (opt) {
            $(this).click(function(){
                var url = opt.url || "",
                //data= opt.data || {data:""},
                    file = $(this).attr("data-file")? "#"+$(this).attr("data-file") : "",
                    preview = $(this).attr("data-preview") ? "#"+$(this).attr("data-preview") : $(this).find(".preview");

                $(file).click();
                // 兼容 IE
                if (navi.ie) {
                    debugger
                    setTimeout(function() {
                        changeFunc();
                    }, 0);
                }
                $(file).on('change', function () {
                    changeFunc();
                });
                var changeFunc = function () {
                    if( $(file).val() != ""){
                        console.log($(file).val());
                        if(!/\.jpg$|\.gif$|\.png$/i.test($(file).val())){
                            tip.info({
                                tip: "上传图片格式不正确,请确保是gif,png,jpg图片",
                                time: 1000
                            });
                            $(file).val("");
                            return;
                        }
                        tip.info({
                            tip: "正在上传中，请稍等....",
                            time: 2000
                        });
                        
                        $.ajaxFileUpload({
                            url:url,            //需要链接到服务器地址
                            secureuri:false,
                            fileElementId:$(file).attr("id"),   //文件选择框的id属性
                            dataType: 'json',
                            success: function(res){
                                var data = res.data;
                                $(preview).attr("src",CP.imgServer+data.url);
                                $(preview).show();
                                if(opt.callback){
                                    opt.callback(data,preview);
                                }
                                tip.info({
                                    tip: "图片上传成功",
                                    time: 2000
                                });
                            },
                            error: function (data, status, e)
                            {
                                tip.info({
                                    tip: "上传失败:"+e,
                                    time: 2000
                                });
                            }
                        });
                    }
                };
            });
        }
    });

});

