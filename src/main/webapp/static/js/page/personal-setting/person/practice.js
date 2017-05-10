/**
 * 个人设置-实践经历
 * Created by WXY on 2016/1/26.
 */
define(['lib/jquery', 'util/funcTpl', 'util/request','modules/topic-pop/commonPop', 'lib/juicer'],
    function ($, funcTpl, request, tip) {

    var practice = {
        tpl:function () {
            /*
            {@each data as item}
             <div class="item-body-con" data-id="${item.campus_experience_id}">
             <!--<div class="item delete"><a href="javascript:;">删除这个实践经历</a></div>-->
                 <div class="item item-time">
                     <label>时间：</label>
                     <div class="select select-start-year">
                         <input type="text" value="${item.campus_experience_time|splitTime, 0}" readonly class="item-input item-select-input form_datetime">
                     </div><span>至</span>
                     <div class="select select-end-year">
                         <input type="text" value="${item.campus_experience_time|splitTime, 1}" readonly class="item-input item-select-input form_datetime">
                     </div>
                 </div>
                 <div class="item item-job-name">
                     <label>在&nbsp;&nbsp;</label>
                     <input type="text" name="job-site" class="item-input item-text-input" value="${item.campus_experience_title}">
                     <span>担任</span>
                     <input type="text" name="job-name" class="item-input item-text-input" value="${item.campus_experience_role}">
                 </div>
                 <div class="item item-job-desc">
                     <label>工作描述&nbsp;&nbsp;</label>
                     <textarea class="select-result">${item.campus_experience_descript}</textarea>
                 </div>
                 <div class="item delete"><a href="javascript:;">删除这个实践经历</a></div>
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
                    "campus_experience_time": "",
                    "campus_experience_title": "",
                    "campus_experience_role": "",
                    "campus_experience_descript": ""
                }
            ]
        },
        _count: 0,

        init:function (datepicker) {
            if (practice._count == 1) { return; }
            practice._count = 1;
            var splitTime = function (str, i) {
                return str.split('-')[i];
            };
            juicer.register("splitTime", splitTime);
            var _parent = $('.item-practice');
            // var addBackup = $('.item-practice').find('.item-body-con');
            request.post('/experience/getUserSettingCampusExperience', {}, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    if (res.data.length > 0) {
                        practice._dataLen = res.data.length;
                        var tpl = juicer(funcTpl(practice.tpl), res);
                        _parent.find('.item-body').prepend(tpl);
                        // addBackup.remove();
                        // _parent.find('.delete').show();
                        // practice.watchModify($('.item-practice').find('.item-body-con'));
                    } else {
                        var tpl = juicer(funcTpl(practice.tpl), practice._req);
                        _parent.find('.item-body').prepend(tpl);
                    }
                }
                datepicker();
                practice.event();
            });
        },
        getValue:function (_this) {
            var id = _this.attr('data-id');
            var timeb = _this.find('.item-time input:eq(0)').val(),
                timel = _this.find('.item-time input:eq(1)').val(),
                time = timeb + "-" + timel,
                title = _this.find('.item-job-name input:eq(0)').val().trim(),
                role = _this.find('.item-job-name input:eq(1)').val().trim(),
                descript = _this.find('.item-job-desc textarea').val();

            if (title == '') {
                practice._msg = '实践地点未填写';
                return false;
            }
            if (role == '') {
                practice._msg = '担任角色未填写';
                return false;
            }
            if (descript == '') {
                practice._msg = '经历描述未填写';
                return false;
            }
            if (time == '-') {
                practice._msg = '时间未选择';
                return false;
            }
            // 选择时间 后一个时间大于前一个时间
            if (timel < timeb) {
                practice._msg = '时间起止选择错误';
                return false;
            }
            var reqData = {
                "campus_experience_time":time,
                "campus_experience_title":title,
                "campus_experience_role":role,
                "campus_experience_descript":descript
            };
            if (id) { reqData.campus_experience_id = id; }
            return reqData;
        },
        getAddData:function() {
            // 拿到新增数据
            var len = practice._dataLen;
            var _this = $('.item-practice').find('.item-body-con').filter(':eq('+len+'), :gt('+len+')');
            //if (_this.length == 0) { practice._flag += 1; } else { console.log(1); }
            _this.each(function () {
                var reqData = practice.getValue($(this));
                if (reqData) {
                    practice._addList.push(reqData);
                    practice._flag = true;
                }
            });
        },
        getModifyData:function() {
            // 拿到修改数据
            var len = practice._dataLen;
            var _this = $('.item-practice').find('.item-body-con:lt(' + (len) + ')');
            //if (_this.length == 0) { practice._flag += 1; } else { console.log(1); }
            _this.each(function() {
                var reqData = practice.getValue($(this));
                if (reqData) {
                    practice._changeList.push(reqData);
                    practice._flag = true;
                }
            });
        },
        req:function () {
            request.post('/experience/userSettingCampusExperience', {
                "data":JSON.stringify({
                    "change":practice._changeList,
                    "add":practice._addList,
                    "del":practice._deleList.join(',')
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
            var _parent = $('.item-practice');
            _parent.off('click');

            // 保存
            _parent.on('click', '.save', function () {
                practice.getAddData();
                practice.getModifyData();
                if (practice._flag == true) {
                    practice.req();
                } else {
                    practice._flag = 0;
                    tip.info({ tip: practice._msg, time: '1500' });
                }
            });
            // 删除
            _parent.on('click', '.delete', function () {
                if (confirm('确认删除？')) {
                    var _this = $(this).parent();
                    var id = _this.attr('data-id');
                    if (id) {
                        practice._deleList.push(id);
                        practice._dataLen -= 1;
                    }
                    _this.remove();
                } else {
                    return;
                }
            });
            // 添加
            _parent.on('click', '.add', function () {
                var tpl = juicer(funcTpl(practice.tpl), practice._req);
                $(tpl).insertBefore($(this));
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
        }
    };
    return practice.init;
});