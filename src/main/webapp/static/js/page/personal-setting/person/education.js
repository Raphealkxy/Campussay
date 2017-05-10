/**
 * 个人设置-教育经历
 * Created by WXY on 2016/1/26.
 */
define(['lib/jquery', 'util/funcTpl', 'util/request', 'page/personal-setting/common/unique','modules/topic-pop/commonPop', 'lib/juicer'],
    function ($, funcTpl, request, unique, tip) {

    var education = {
        tpl: function () {
            /*
            <!--{@helper addPercent}
                 function(data) {
                    if (data) return data+'%';
                 }
             {@/helper}-->
            {@each data as item}
             <div class="item-body-con" data-id="${item.education_id}">
             <!--<div class="item delete"><a href="javascript:;">删除这个教育经历</a></div>-->
                 <div class="item item-time">
                     <label>时间：</label>
                     <div class="select select-start-year">
                         <input type="text" value="${item.education_time|splitTime, 0}" readonly class="item-input item-select-input form_datetime">
                     </div><span>至</span>
                     <div class="select select-end-year">
                         <input type="text" value="${item.education_time|splitTime, 1}" readonly class="item-input item-select-input form_datetime">
                     </div>
                 </div>
                 <div class="item item-school">
                     <label>大学：</label>
                     <div class="select select-school">
                         <input type="text" name="school" class="item-input item-select-input" value="${item.education_campus_name}" readonly>
                         <ul class="select-content">
                         </ul>
                     </div>
                 </div>
                 <div class="item item-institute">
                     <label>学院及专业：</label>
                     <div class="select select-institute">
                         <input type="text" name="institute" class="item-input item-select-input" value="${item.education_academe}" style="background:none">
                         <ul class="select-content"></ul>
                     </div>
                     <div class="select select-major">
                         <input type="text" name="major" class="item-input item-select-input" value="${item.education_major}" style="background:none">
                         <ul class="select-content"></ul>
                     </div>
                 </div>
                 <div class="item item-degree">
                     <label>学位：</label>
                     <div class="select select-degree">
                         <input type="text" name="degree" class="item-input item-select-input" value="${item.education_degree}" readonly>
                         <ul class="select-content">
                             <li>高中</li>
                             <li>专科</li>
                             <li>本科</li>
                             <li>硕士</li>
                             <li>博士</li>
                         </ul>
                     </div>
                 </div>
                 <div class="item item-major-rank">
                     <label>专业排名：</label>
                     <div class="select select-major-rank">
                         <input type="text" name="degree" class="item-input item-select-input" value="${item.education_ranking|addPercent}" readonly>
                         <ul class="select-content">
                             <li>10%</li>
                             <li>20%</li>
                             <li>30%</li>
                             <li>40%</li>
                             <li>50%</li>
                             <li>60%</li>
                         </ul>
                     </div>
                 </div>
                 <div class="item delete"><a href="javascript:;">删除这个教育经历</a></div>
             </div>
             {@/each}
            */
        },

        // 初始获得数据长度
        _dataLen: 0,
        // flag
        _flag: 0,
        // 用户填写错误信息提示
        _msg: '',
        // 监听数据集
        _watchList:{},
        // 修改数据集
        _changeList:[],
        // 新增数据集
        _addList:[],
        // 删除数据集
        _deleList: [],
        _req: {
            "data": [
                {
                    "education_campus_name": "",
                    "education_academe": "",
                    "education_major": "",
                    "education_time": "",
                    "education_degree": "",
                    "education_ranking": ""
                }
            ]
        },
        _count: 0,
        init:function (datepicker) {
            if (education._count == 1) { return; }
            education._count = 1;
            var splitTime = function (str, i) {
                return str.split('-')[i];
            };
            juicer.register("splitTime", splitTime);

            var _parent = $('.item-education');
            request.post('/education/getUserSettingEducation', {}, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    if (res.data.length > 0) {
                        education._dataLen = res.data.length;
                        var tpl = juicer(funcTpl(education.tpl), res);
                        _parent.find('.item-body').prepend(tpl);
                    } else {
                        var tpl = juicer(funcTpl(education.tpl), education._req);
                        _parent.find('.item-body').prepend(tpl);
                    }
                }
                datepicker();
                education.event();
            });
        },
        /*watchModify:function (target) {
            var val = '';
            target.on('change', 'input', function () {
                var _parent = $(this).parents('.item-body-con');
                var id = _parent.attr('data-id');
                education._watchList[id] = education.getValue(_parent);
            });
            target.on('click', 'input[name="degree"]>ul', function () {
                $(this).prev().trigger('change');
            });
        },*/
        getValue:function (_this) {
            var id = _this.attr('data-id');
            var timeb = _this.find('.item-time input:eq(0)').val(),
                timel = _this.find('.item-time input:eq(1)').val(),
                time = timeb + "-" + timel,
                campus_name = _this.find('.item-school input').val().trim(),
                academe = _this.find('.item-institute input:eq(0)').val().trim(),
                major = _this.find('.item-institute input:eq(1)').val().trim(),
                degree = _this.find('.item-degree input').val(),
                ranking = _this.find('.item-major-rank input').val().replace(/\%/, '');

            if (campus_name == '') {
                education._msg = '学校名称未填写';
                return false;
            }
            if (academe == '') {
                education._msg = '学院信息未填写';
                return false;
            }
            if (major == '') {
                education._msg = '专业信息未填写';
                return false;
            }
            if (degree == '') {
                education._msg = '学历信息未填写';
                return false;
            }
            if (ranking == '') {
                education._msg = '排名未填写';
                return false;
            }
            if (time == '-') {
                education._msg = '时间未选择';
                return false;
            }
            // 选择时间 后一个时间大于前一个时间
            if (timel < timeb) {
                education._msg = '时间起止选择错误';
                return false;
            }
            var reqData ={ 
                "education_campus_name":campus_name,
                "education_academe":academe,
                "education_major":major,
                "education_time": time,
                "education_degree":degree,
                "education_ranking":ranking
            };
            if (id) { reqData.education_id = id; }
            return reqData;
        },
        getAddData:function() {
            // 拿到新增数据
            var len = education._dataLen;//获取用户原来的教育信息数量
            var _this = $('.item-education').find('.item-body-con').filter(':eq('+len+'), :gt('+len+')');
            //if (_this.length == 0) { education._flag += 1; } else { console.log(1); }
            _this.each(function () {
                var reqData = education.getValue($(this));
                /*if (!$.isEmptyObject(reqData)) {
                    education._addList.push(reqData);
                    education._flag += 1;
                }*/
                if (reqData) {
                    education._addList.push(reqData);
                    education._flag = true;
                }
            });
        },
        getModifyData:function() {
            // 拿到修改数据
            var len = education._dataLen;
            var _this = $('.item-education').find('.item-body-con:lt(' + (len) + ')');
            //if (_this.length == 0) { education._flag += 1; } else { console.log(1); }
            _this.each(function() {
                var reqData = education.getValue($(this));
                /*if (!$.isEmptyObject(reqData)) {
                    education._changeList.push(reqData);
                    education._flag += 1;
                }*/
                if (reqData) {
                    education._changeList.push(reqData);
                    education._flag  = true;
                }
            });
        },
        req:function () {
            request.post('/education/userSettingEducation', {
                "data":JSON.stringify({
                    "change":education._changeList,
                    "add":education._addList,
                    "del":education._deleList.join(',')
                })
            }, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    tip.info({
                        tip: '保存成功',
                        time: '1500',
                        reload: true
                    });
                } else {
                    tip.info({
                        tip: '请填写完整',
                        time: '1500'
                    });
                }
            });
        },
        event:function () {
            var _parent = $('.item-education');
            _parent.off('click');
            // 保存
            _parent.on('click', '.save', function () {
                //得到新增的数据
                education.getAddData();
                education.getModifyData();
                if (education._flag === true ) {
                    education.req();
                } else {
                    education._flag = 0;
                    tip.info({ tip: education._msg, time: '1500' });
                }
            });
            // 删除
            _parent.on('click', '.delete', function () {
                if (confirm('确认删除？')) {

                    var _this = $(this).parent();
                    var id = _this.attr('data-id');
                    if (id) {
                        education._deleList.push(id);
                        education._dataLen -= 1;
                    }
                    _this.remove();
                } else {
                    return;
                }
            });
            // 添加
            _parent.on('click', '.add', function () {
                var $add = $(this);
                var tpl = juicer(funcTpl(education.tpl), education._req);
                $(tpl).insertBefore($add);
                var top = _parent.find('.item-body-con:last').offset().top;
                $('body').animate({
                    scrollTop: top
                }, {
                    duration: 1000
                });
                $('.form_datetime').datetimepicker({
                    timepicker: false,
                    format: 'Y/m'
                });
            });
            // 选择学校
            _parent.on('focus', '.select-school input', function () {
                var _this = $(this);
                var select_content;
                var item = function () {
                     /*
                     {@each data as item}
                         <li title="${item.campus_name}">${item.campus_name}</li>
                     {@/each}
                     */
                };
                request.post('/campus/getALLcampusname', {}, function (res) {
                    res = JSON.parse(res);
                    if (res.code == 1) {
                        select_content = juicer(funcTpl(item), res);
                        _this.next().empty().append(select_content);
                    }
                });
            });
            // 学院模糊查询结果
            var timer1 = null;
            _parent.on('focus', '.select-institute input', function () {
                var _this = $(this);
                var select_content;
                var item =  function () {
                    /*
                     {@each data as item}
                     <li title="${item.academy_name}">${item.academy_name}</li>
                     {@/each}
                     */
                };
                timer1 = setInterval(function () {
                    if (_this.val().trim() != "") {
                        request.post('/education/getAllAcademy', {
                            "academy": _this.val()
                        }, function (res) {
                            res = JSON.parse(res);
                            if (res.code == 1) {
                                select_content = juicer(funcTpl(item), res);
                                _this.next().empty().append(select_content);
                            }
                        });
                    } else {
                        select_content = '<li>请输入关键字</li>';
                        _this.next().empty().append(select_content);
                        return;
                    }
                }, 1200);
                _this.blur(function () {
                    clearInterval(timer1);
                });
            });
            // 专业模糊查询结果
            var timer2 = null;
            _parent.on('focus', '.select-major input', function () {
                var _this = $(this);
                var select_content;
                var item =  function () {
                    /*
                     {@each data as item}
                     <li title="${item}">${item}</li>
                     {@/each}
                     */
                };
                timer2 = setInterval(function () {
                    if (_this.val().trim() != "") {
                        request.post('/education/getAllMajor', {
                            "major": _this.val()
                        }, function (res) {
                            res = JSON.parse(res);
                            if (res.code == 1) {
                                select_content = juicer(funcTpl(item), res);
                                _this.next().empty().append(select_content);
                            }
                        });
                    } else {
                        select_content = '<li>请输入关键字</li>';
                        _this.next().empty().append(select_content);
                        return;
                    }
                }, 1200);
                _this.blur(function () {
                    clearInterval(timer2);
                });
            });
        }
    };
    return education.init;
});