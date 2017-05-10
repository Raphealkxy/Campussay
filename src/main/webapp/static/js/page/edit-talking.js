require.config({
    baseUrl: CP.STATIC_ROOT,
    paths: {
        'baidueditor': 'modules/umeditor/umeditor',
        'bdlang': 'modules/umeditor/lang/zh-cn/zh-cn'
    },
    shim: {
        'baidueditor': {
            deps: ['modules/umeditor/umeditor.config']
        },
        'bdlang': {
            deps: ['baidueditor']
        },
        'modules/Date/jquery.datetimepicker': {
            deps: ['lib/jquery']
        }
    }
});
require(['lib/jquery', 'util/request', 'util/funcTpl', 'util/Headertip', 'baidueditor', 'modules/fileUpload/ajaxfileupload'
        , 'bdlang', 'modules/Date/jquery.datetimepicker', 'lib/juicer'],
    function ($, request, funcTpl, headertip, UE) {
        var ue = UE.getEditor('myEditor', function () {
        });
        var index = {
            init: function () {
                this._getTalking(); //获取课程信息
                this._getTalkingClassify(); //获取分类信息
                this._uploadPic(); //上传图片
                this._timePicker(); //时间
                this._select(); //下拉筛选
                this._save(); //保存
            },
            _getTalking: function () {//获取课程信息
                var courseid = this.getUrlParam("tk");
                var self = this;
                request.get('/talking/gettalkingdetails',
                    {
                        "talkingId": courseid
                    }, function (rsp) {
                        rsp = JSON.parse(rsp);
                        if (rsp.code === 1) {
                            var data = rsp.data;
                            $('#class-title').val(data.talking_title);  //标题
                            $('#student-num').val(data.talking_max_persion);  //人数
                            $('#class-price').val(data.talking_price);   //价格
                            $('#class-address').val(data.talking_address); //地点
                            $('#view').attr("src", data.talking_main_picture).removeClass("hide");  //图片
                            $("#J_talking_type_first").html(data.talking_root_type_name); //一级分类
                            $('#J_talking_type_two').html(data.talking_type_name);//二级分类
                            $('#class-time').val(self._dateChange(data.talking_start_time));  //YY-MM-DD
                            $('.input-time-up').val(self._timeChange(data.talking_start_time)); //上课时间
                            $('.input-time-down').val(self._timeChange(data.talking_end_time));  //下课时间
                            $('.input-content').val(data.talking_target); //介绍
                            ue.setContent(data.talking_info); //交流详情
                        }
                    });
            },
            //上传图片
            _uploadPic: function () {
                function upImg() {
                    $('#input-img').on('change', function () {
                        if ($(this).val() != "") {
                            if (!/\.jpg$|\.jpeg$|\.gif$|\.png$/i.test($(this).val())) {
                                headertip.error("上传图片格式不正确,请确保是gif,png,jpeg,jpg图片");
                                $(this).val("");
                                return;
                            }
                            headertip.info("图片上传中,请稍等...");
                            $.ajaxFileUpload({
                                url: "/talking/talkingpicupload", //需要链接到服务器地址
                                secureuri: false,
                                fileElementId: 'input-img',   //文件选择框的id属性
                                dataType: 'json',
                                success: function (res) {
                                    if (res.code != 0) {
                                        var data = res.data;
                                        $('#view').attr("src", 'http://103.227.76.194:7000/' + data.url).removeClass('hide');
                                        headertip.success("图片上传成功", 2000, true);
                                    } else {
                                        headertip.error(res.msg);
                                    }

                                },
                                error: function (data, status, e) {
                                    headertip.error("上传失败:" + e);
                                }
                            });
                        }
                        $('#input-img').remove();
                        $('#input-wrap').append('<input type="file" id="input-img" accept="image/gif,image/jpeg,image/png" name="input-img"/>');
                        upImg();
                    });
                }

                upImg();
            },
            _timePicker: function () {
                //交流时间
                var YMD = 0,
                    beginTime = 0,
                    endTime = 0;
                $('#class-time').datetimepicker({
                    yearOffset: 0,
                    lang: 'ch',
                    timepicker: false,
                    format: 'Y/m/d',
                    formatDate: 'Y/m/d',
                    minDate: '-1970/01/02', // yesterday is minimum date
                    onChangeDateTime: function (date) {
                        YMD = new Date([date.getFullYear(), date.getMonth() + 1, date.getDate()].join('/')).getTime();
                        // publish._data.talkingStartTime = YMD+beginTime;
                        // publish._data.talkingEndTime = YMD+endTime;
                    }
                });
                $('.input-time-up').datetimepicker({
                    datepicker: false,
                    format: 'H:i',
                    step: 5,
                    onChangeDateTime: function (date) {
                        beginTime = (date.getHours() * 60 * 60 + date.getMinutes() * 60) * 1000;
                    }
                });

                $('.input-time-down').datetimepicker({
                    datepicker: false,
                    format: 'H:i',
                    step: 5,
                    onChangeDateTime: function (date) {
                        endTime = (date.getHours() * 60 * 60 + date.getMinutes() * 60) * 1000;
                        // publish._data.talkingEndTime = YMD+endTime;
                    }
                });
            },
            _select: function () {

                function initCategory(id, dom) {//初始化分类列表
                    request.post('/talking/getTalkingType', {
                        "talking_type_parent": id
                    }, function (res) {
                        res = JSON.parse(res) || eval(res);
                        if (1 == res.code) {
                            var html = '';
                            for (var i = 0; i < res.data.length; i++) {
                                html += '<li data-id="' + res.data[i].talking_type_id + '">' +
                                    res.data[i].talking_type_name + '</li>';
                            }
                            html += '<li data-id="-1">其它</li>';
                            $(dom).html(html);
                        } else {
                            console.log(res.msg);
                        }
                    });
                }


                $('.mn-select').on('click mouseenter', function (event) {//模拟下拉选择
                    event = event || window.event;
                    var $this = $(this),
                        liL = $this.find('li').length;
                    $this.find('ul').css({'bottom': -30 * liL + "px"}).toggleClass('show-select');
                    if (event.target.localName == 'li') {
                        var $target = $(event.target);
                        if (!!$target.parent('#J_category')[0]) {
                            var id = $target.data('id');
                            initCategory(id, '#J_subCategory');//初始化子类目
                            $('.J_sub').html('请选择');
                            publish._data.talkingType = $target.data('id');
                        } else {
                            publish._data.talkingRootType = $target.data('id');
                        }
                        $this.find('span').html($target.html());
                    }
                }).mouseleave(function (ev) {
                    var $this = $(this);
                    $this.find('ul').removeClass('show-select');
                });
            },
            //保存接口
            _save: function () {
                $("#J_submit").click(function () {
                    var talkingTitle = $('#class-title').val().trim();
                    if (talkingTitle == "") {
                        headertip.error("信息不完整");
                        return;
                    }
                    var talkingMaxPersion = $('#student-num').val().trim();
                    if (talkingMaxPersion == 0 || "") {
                        headertip.error("信息不完整");
                        return;
                    }
                    var talkingPrice = $('#class-price').val().trim();
                    if (talkingPrice == 0 || "") {
                        headertip.error("信息不完整");
                        return;
                    }
                    var talkingAddress = $('#class-address').val().trim();
                    if (talkingAddress == "") {
                        headertip.error("信息不完整");
                        return;
                    }
                    var talkingMainPicture = $('#input-wrap').val().trim();
                    if (talkingMainPicture == "") {
                        headertip.error("信息不完整");
                        return;
                    }
                    var talkingTarget = $('.input-content').val().trim();
                    if (talkingTarget == "") {
                        headertip.error("信息不完整");
                        return;
                    }
                    var talkingInfo = $('#myEditor').val().trim();
                    if (talkingInfo == "") {
                        headertip.error("信息不完整");
                        return;
                    }
                    var talkingStartTime = $('#class-on').val().trim();
                    if (talkingStartTime == 0 || "") {
                        headertip.error("信息不完整");
                        return;
                    }
                    var talkingEndTime = $('#class-off').val().trim();
                    if (talkingEndTime == 0 || "") {
                        headertip.error("信息不完整");
                        return;
                    }

                    request.post("/talking/relasetalking", {
                        talkingTitle: talkingTitle,
                        talkingType: '',
                        talkingRootType: '',
                        talkingMaxPersion: talkingMaxPersion,
                        talkingPrice: talkingPrice,
                        talkingAddress: talkingAddress,
                        talkingMainPicture: talkingMainPicture,
                        talkingTarget: talkingTarget,
                        talkingInfo: talkingInfo,
                        talkingStartTime: talkingStartTime,
                        talkingEndTime: talkingEndTime
                    }, function (res) {
                        res = JSON.parse(res);
                        if (1 == res.code) {
                            window.location.href = '/talking/classDetail?tk=' + res.data;
                        } else {
                            $('body').trigger('get:tip', ['发布失败']);
                        }
                    });
                })
            },
            _getTalkingClassify: function () { // 获取分类信息
                var self = this;
                request.post('/talking/getTalkingType', {"talking_type_parent": 1}, function (rsp) {
                    rsp = JSON.parse(rsp);
                    if (rsp.code === 1) {
                        var tpl;
                        tplData = {},
                            tplData.Typelist = rsp.data;
                        var tmpl = juicer(funcTpl(self._classifytpl), tplData);
                        var x = $('#J_talking_type_first');
                        $('#J_category').append(tmpl);
                    }

                });

            },
            getUrlParam: function (key) {
                var url = location.search;  //获取url中“？”后的字符串
                var theRequest = new Object();
                if (url.indexOf("?") != -1) {
                    var str = url.substr(1);
                    strs = str.split("&");
                    for (var i = 0; i < strs.length; i++) {
                        theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
                    }
                }
                return theRequest[key];
            },
            _dateChange: function (time) {
                var date = new Date(time);
                var year = date.getFullYear();
                var mon = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1 ) : date.getMonth() + 1;
                var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
                return year + "-" + mon + "-" + day;
            },
            _timeChange: function (time) {
                var date = new Date(time);
                var hour = date.getHours();
                var minutes = date.getMinutes() + 1 < 10 ? "0" + (date.getMinutes() + 1) : date.getMinutes() + 1;
                return hour + ":" + minutes;
            },
            _classifytpl: function () {
                /*
                 {@each Typelist as Item}
                 <li>${Item.talking_type_name}</li>
                 {@/each}
                 */
            }
        };

        index.init();
    });