/**
 *  @description 发布talking入口文件
 *  @author 吴伦毅
 */

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
require(['lib/jquery', 'util/request', 'util/funcTpl', 'baidueditor', 'util/Headertip', 'modules/talkingBox/talkingBox','modules/textEditor/textEditor', 'modules/baseMoudle','modules/topic-pop/tipPop', 'modules/fileUpload/ajaxfileupload', 'bdlang', 'modules/Date/jquery.datetimepicker'],
    function ($, request, funcTpl, UE, headertip, talkingBox,editor,baseMoudle) {

    var ue = UE.getEditor('myEditor', function () {
    });

    var publish = {
        init: function () {//初始化
            this._publish();
            $('.J_nav-4').addClass('nav-active');//发布talking选项点亮
            var tk = this.getUrlParam('tk');//发布相似
            var etk = this.getUrlParam('etk');//修改

            publish._checks();//开启验证和提示
            publish._getInitData(tk);//获取初始下拉数据

            if(tk || etk){//如果有表示修改或者发布相似
                this._getTalking();//获取数据
            }

            publish.talkingBox();

            //富文本编辑器
            //editor.init();
        },
        _data: {//请求数据包
            talkingTitle: '',
            talkingType: '',
            talkingRootType: '',
            talkingMaxPersion: '',
            talkingPrice: '',
            talkingAddress: '',
            talkingMainPicture: '',
            talkingTarget: '',
            talkingInfo: '',
            talkingStartTime: '',
            talkingEndTime: '',
            talkingTool: 0,
            talkingToolNum: 0,
            talkingIsOnline: -1//-1线下1线上
        },
        clearData:function(){
            $.each(publish._data,function(key,value){
                if(publish._data[key] !== ''){
                    publish._data[key] = '';
                }
            })  
        },
        _Api: {
            getCategory: '/talking/getfirsttalking',
            getTalkingType: '/talking/getTalkingType',
            getPersonalInfo: '/talking/getuserContact',
            uploadImg: '/talking/talkingpicupload',
            publish: '/talking/relasetalking',//发布

            talkingDetail: '/talking/gettalkingdetails',//获取数据详情
            editPublish: '/talking/updatetalkingdetails'//修改
        },
        //验证用户的学生证信息和登录状态
        _publish:function(){
            //1.点击我想说
            request.get("/campus/isstudentresult",function(rsp){
                rsp = JSON.parse(rsp);
                var data = rsp.data;

                var jump = function(url){
                    setTimeout(function(){
                        window.location.href = url;
                    },2000);
                };
                switch (data){
                    case -1:baseMoudle.infoNotreload("请先登录");jump("/user/sign-in-web?url=/talking/publish");break;
                    //学生认证已通过
                    case 1:break;
                    //未提交学生证审核
                    case 3:baseMoudle.infoNotreload("未提交学生证");jump("/personalSetting/authentication");break;
                    //未通过学生证认证
                    case 2:baseMoudle.infoNotreload("学生证认证未通过，请重新提交");jump("/personalSetting/authentication");break;
                    //
                    case 0:baseMoudle.infoNotreload("学生证认证还在审核中");jump("/talking/ttIndex"); break;
                }


            });
        },
        _getTalking: function () {//获取课程信息
            var courseid = this.getUrlParam("tk")||this.getUrlParam('etk');
            var self = this;
            request.get(self._Api.talkingDetail,
                {
                    "talkingId": courseid
                }, function (rsp) {
                    rsp = JSON.parse(rsp);
                    if (rsp.code === 1) {
                        var data = rsp.data;
                        $('#class-title').val(data.talking_title);  //标题
                        $('#student-num').val(data.talking_max_persion);  //人数
                        $('#class-price').val(data.talking_price);   //价格
                        if (data.talking_is_online == -1) {
                            $('#class-address').val(data.talking_address); //地点
                        } else if(data.talking_is_online == 1) {
                            $('#online-style').val(data.talking_tool);
                            $('#online-num').val(data.talking_tool_num);
                            $('#online-num,#online-style').toggleClass('hide');
                            $('#class-address').toggleClass('hide');
                            $('#class-online').attr('checked','checked');
                        }
                        $('#view').attr("src", data.talking_main_picture).removeClass("hide");  //图片
                        $("#J_talking_type_first").html(data.talking_root_type_name); //一级分类
                        $('#J_talking_type_two').html(data.talking_type_name);//二级分类
                        $('#class-calssify').attr('data-id',data.talking_root_type);
                        $('#class-sub-classify').attr('data-id',data.talking_type);
                        $('body').trigger('get:subCategory', {talking_type_parent: data.talking_type});//获取子节点
                        $('#class-time').val(self._dateChange(data.talking_start_time));  //YY-MM-DD
                        $('.input-time-up').val(self._timeChange(data.talking_start_time)); //上课时间
                        $('.input-time-down').val(self._timeChange(data.talking_end_time));  //下课时间
                        $('.input-content').val(data.talking_target); //介绍
                        ue.setContent(data.talking_info); //交流详情
                    }
                });
        },
        getUrlParam: function (key) {
            var url = location.search;  //获取url中“？”后的字符串
            var theRequest = {};
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
            return year + "/" + mon + "/" + day;
        },
        _timeChange: function (time) {
            var date = new Date(time);
            var hour = date.getHours();
            var minutes = date.getMinutes() + 1 < 10 ? "0" + (date.getMinutes() + 1) : date.getMinutes() + 1;
            return hour + ":" + minutes;
        },
        _getInitData: function () {//获取初始数据
            function initCategory(url, data, dom) {//初始化分类列表
                request.post(url, data, function (res) {
                    res = JSON.parse(res);

                    var html = '';
                    if (1 == res.code) {

                        for (var i = 0; i < res.data.length; i++) {
                            html += '<li data-id="' + res.data[i].talking_type_id + '">' +
                                res.data[i].talking_type_name + '</li>';
                        }

                    } else {
                    }

                    $(dom).html(html);
                });
            }

            initCategory(publish._Api.getCategory, {talking_type_parent: 1}, '#J_category');//初始化以及类目

            $('body').on('get:subCategory', function (ev, data) {
                initCategory(publish._Api.getTalkingType, data, '#J_subCategory');//初始化子类目
            });

            request.post(publish._Api.getPersonalInfo, {}, function (res) {
                res = JSON.parse(res) || eval(res);
                if (1 == res.code) {
                    var htmlTpl = '';
                    for (var item in res.data) {
                        htmlTpl += '<li>' + res.data[item] + '</li>';
                    }
                    $('.class-info').html(htmlTpl);
                }
            });
        },
        _checks: function () {
            //验证标题
            $('#class-title').on('input change', function () {
                var $this = $(this);
                var titleValue = $.trim($this.val());
                var valueLength = titleValue.length;

                if (valueLength >= 0 && valueLength <= 20) {
                    $('.J_title_num').html(20 - valueLength);
                    publish._data.talkingTitle = titleValue;
                } else {
                    $this.val(publish._data.talkingTitle);
                }
            });

            //模拟下拉选择
            $('.mn-select').on('click mouseenter', function (event) {
                var $this = $(this);
                $this.find('ul').toggleClass('show-select');//打开下拉框

                if (event.target.localName == 'li') {
                    var $target = $(event.target);
                    var id = $target.data('id');

                    if (!!$target.parent('#J_category')[0]) {

                        $('body').trigger('get:subCategory', {talking_type_parent: id});//获取子节点
                        $('.J_sub').html('请选择');
                        $('#class-calssify').attr('data-id', id);
                    } else {
                        $('#class-sub-classify').attr('data-id', id);
                    }
                    $this.find('span').html($target.html());
                }
            }).mouseleave(function () {
                var $this = $(this);
                $this.find('ul').removeClass('show-select');
            });

            //验证人数
            $('#student-num').on('input', function () {
                var $this = $(this);
                var num = parseInt($.trim($this.val()));

                if (!!num) {
                    $this.val(num);
                } else {
                    $this.val('');
                }
            });

            //验证价格
            $('#class-price').on('change', function () {
                var $this = $(this);
                var num = parseFloat($.trim($this.val()));

                if (num == 0 || num) {
                    $this.val(num);
                } else {
                    $this.val('')
                }
            });

            //切换方式
            $('input[name="class-way"]').on('change', function () {
                $('#online-num,#online-style').toggleClass('hide');
                $('#class-address').toggleClass('hide');
            });

            //验证号码
           /* $('#online-num').on('input', function () {
                var $this = $(this);
                var regexp = /\d+/;
                var num = regexp.exec($this.val());

                if (num == 0 || !!num) {
                    $this.val(num);
                } else {
                    $this.val(publish._data.talkingToolNum);
                }
            });*/

            //上传图片
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
                            url: publish._Api.uploadImg, //需要链接到服务器地址
                            secureuri: false,
                            fileElementId: 'input-img',   //文件选择框的id属性
                            dataType: 'json',
                            success: function (res) {
                                if (res.code != 0) {
                                    var data = res.data;
                                    $('#view').attr("src", CP.imgServer + data.url).removeClass('hide');
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

            //交流时间
            $('#class-time').datetimepicker({
                yearOffset: 0,
                lang: 'ch',
                timepicker: false,
                format: 'Y/m/d',
                formatDate: 'Y/m/d',
                minDate: '-1970/01/02'
            });
            $('.input-time-up').datetimepicker({
                datepicker: false,
                format: 'H:i',
                step: 5
            });
            $('.input-time-down').datetimepicker({
                datepicker: false,
                format: 'H:i',
                step: 5
            });

            //获取时间
            function getTime() {
                var ymd = $('#class-time').val();
                var startTime = $('.input-time-up').val();
                var endTime = $('.input-time-down').val();

                if (startTime) {
                    publish._data.talkingStartTime = Date.parse(ymd + ' ' + startTime);//时间转换
                }

                if (endTime) {
                    publish._data.talkingEndTime = Date.parse(ymd + ' ' + endTime);//时间转换
                }
            }

            function getValue(dom) {
                return $('<div/>').text($.trim(dom.val())).html()
            }

            function getData() {
                publish._data.talkingTitle = getValue($('#class-title'));//标题
                publish._data.talkingTarget = getValue($('.input-content'));//获取交流内容
                getTime();//获取时间
                publish._data.talkingMainPicture = $('#view').attr('src');//获取图片
                publish._data.talkingAddress = getValue($('#class-address'));//获取地址
                publish._data.talkingTool = getValue($('#online-style'));//线上方式
                publish._data.talkingToolNum = getValue($('#online-num'));//获取线上号码
                publish._data.talkingIsOnline = getValue($('input[name="class-way"]:checked')); //获取模式
                publish._data.talkingPrice = getValue($('#class-price'));//获取价格
                publish._data.talkingMaxPersion = getValue($('#student-num'));//人数
                publish._data.talkingType = $('#class-sub-classify').attr('data-id');
                publish._data.talkingRootType = $('#class-calssify').attr('data-id');
            }

            //提交处理函数
            function putData() {
                getData();//获取所有数据

                var $btn = $('#J_submit');
                $btn.off('click', putData);//提交时关闭事件监听，避免重复提交

                //获取富文本编辑的内容
                ue.ready(function () {
                    publish._data.talkingInfo = ue.getContent(); //获取html内容
                });
    
                //获取富文本编辑的内容
                //publish._data.talkingInfo = $("#myeditor").html();
                
                var checkData = [
                    {name: 'talkingTitle', value: '亲！标题别忘了填哦！', d: ''},
                    {name: 'talkingType', value: '亲！类型别忘了填哦！', d: ''},
                    {name: 'talkingRootType', value: '亲！类型别忘了填哦！', d: ''},
                    {name: 'talkingMaxPersion', value: '亲！人数别忘了填哦！', d: ''},
                    {name: 'talkingPrice', value: '亲！价格别忘了填哦！', d: ''},
                    {name: 'talkingMainPicture', value: '亲！图片别忘了填哦！', d: ''},
                    {name: 'talkingTarget', value: '亲！内容别忘了填哦！', d: ''},
                    {name: 'talkingInfo', value: '亲！详情别忘了填哦！', d: ''}
                ];
                var message = {
                    tip:"",
                    push:function(msg){
                        if(!this.tip){
                            this.tip = msg
                        }
                    },
                    get:function(){
                        return this.tip;
                    }
                };
                var length = checkData.length, i = 0;
                
                //publish.clearData(); 

                for (i; i < length; i+=1) {
                    var name = checkData[i].name;
                    var d = checkData[i].d;
                    var value = checkData[i].value;
                    if (publish._data[name] == d || !publish._data[name]) {
                        message.push(value);
                        break;
                    }

                }
                //验证时间
                if (publish._data.talkingEndTime && publish._data.talkingStartTime) {
                    var time = publish._data.talkingEndTime - publish._data.talkingStartTime;

                    if (time < 30 * 60 * 1000) {
                        message.push('亲！课程不能小于30分钟哦！');
                    }
                } else {
                    message.push('亲！时间别忘了填哦！');
                }

                //验证地址
                if (publish._data.talkingIsOnline == -1) {
                    publish._data.talkingTool = 0;
                    publish._data.talkingToolNum = 0;

                    publish._data.talkingAddress || message.push('亲！地址别忘了填哦！');
                } else {
                    publish._data.talkingAddress = 0;
                    publish._data.talkingTool || message.push('亲！工具别忘了填哦！');
                    publish._data.talkingToolNum || message.push('亲！号码别忘了填哦！');
                }

                if (message.tip !== '') {
                    $('body').trigger('get:tip', [message.get()]);
                    $btn.on('click', putData);
                    return;
                }

                $btn.html('发布...');

                var api = publish._Api.publish;
                var etk = publish.getUrlParam('etk');

                if(etk){
                    api = publish._Api.editPublish;
                    publish._data.talkingId = etk;
                }

                request.post(api, publish._data, function (res) {
                    res = JSON.parse(res);

                    $('#J_submit').html('发布').on('click', putData);
                    if (1 == res.code) {
                        window.location.href = '/talking/classDetail?tk=' + (res.data || etk);
                    } else if (0 == res.code) {
                        $('body').trigger('get:tip', ['亲！您未认证哦！']);
                    } else if (10001 == res.code) {
                        $('body').trigger('get:tip', ['亲！检查下发布时间吧！']);
                    } else {
                        $('body').trigger('get:tip', ['亲！发布失败了！']);
                    }

                });
            }

            $('#J_submit').on('click', putData);
        },
        talkingBox: function () {
            $('.process1').on('click', function () {
                $('.talkingBox').css('display', 'none');
                $('#step1').fadeIn();
            });
            $('.process2').on('click', function () {
                $('.talkingBox').css('display', 'none');
                $('#step2').fadeIn();

            });
            $('.process3').on('click', function () {
                $('.talkingBox').css('display', 'none');
                $('#step3').fadeIn();
            });
            $('.stepBtn').on('click', function () {
                $('.talkingBox').fadeOut();
            })
        }
    };
    //初始化
    publish.init();
});



