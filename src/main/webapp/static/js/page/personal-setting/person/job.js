/**
 * 个人设置-工作经验
 * Created by WXY on 2016/1/26.
 */
define(['lib/jquery', 'util/funcTpl', 'util/request','modules/topic-pop/commonPop', 'lib/juicer'],
    function ($, funcTpl, request, tip) {

    var job = {
        tpl:function () {
            /*
            {@each data as item}
             <div class="item-body-con" data-id="${item.work_experience_id}">
                 <!--<div class="item delete"><a href="javascript:;">删除这个工作经历</a></div>-->
                 <div class="item item-time">
                     <label>时间：</label>
                     <div class="select select-start-year">
                         <input type="text" value="${item.work_experience_time|splitTime, 0}" readonly class="item-input item-select-input form_datetime">
                     </div><span>至</span>
                     <div class="select select-end-year">
                         <input type="text" value="${item.work_experience_time|splitTime, 1}" readonly class="item-input item-select-input form_datetime">
                     </div>
                 </div>
                 <div class="item item-job-name">
                     <label>在&nbsp;&nbsp;</label>
                     <input type="text" name="job-site" class="item-input item-text-input" value="${item.work_experience_place}">
                     <span>担任</span>
                     <input type="text" name="job-name" class="item-input item-text-input" value="${item.work_experience_role}">
                 </div>
                 <div class="item item-job-desc">
                     <label>工作描述&nbsp;&nbsp;</label>
                     <textarea class="select-result">${item.work_experience_descript}</textarea>
                 </div>
                 <div class="item delete"><a href="javascript:;">删除这个工作经历</a></div>
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
                    "work_experience_time": "",
                    "work_experience_place": "",
                    "work_experience_role": "",
                    "work_experience_descript": ""
                }
            ]
        },
        _count: 0,

        init:function (datepicker) {
            if (job._count == 1) { return; }
            job._count = 1;
            var splitTime = function (str, i) {
                return str.split('-')[i];
            };
            juicer.register("splitTime", splitTime);

            var _parent = $('.item-job');
            request.post('/work/getUserSettingWorkExperience', {}, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    if (res.data.length > 0) {
                        job._dataLen = res.data.length;
                        var tpl = juicer(funcTpl(job.tpl), res);
                        _parent.find('.item-body').prepend(tpl);
                        // job.watchModify($('.item-job').find('.item-body-con'));
                    } else {
                        var tpl = juicer(funcTpl(job.tpl), job._req);
                        _parent.find('.item-body').prepend(tpl);
                    }
                }
                datepicker();
                job.event();
            });
        },
        getValue:function (_this) {
            var id = _this.attr('data-id');
            var timeb = _this.find('.item-time input:eq(0)').val(),
                timel = _this.find('.item-time input:eq(1)').val(),
                time = timeb + "-" + timel,
                place = _this.find('.item-job-name input:eq(0)').val().trim(),
                role = _this.find('.item-job-name input:eq(1)').val().trim(),
                descript = _this.find('.item-job-desc textarea').val();

            if (place == '') {
                job._msg = '工作地点未填写';
                return false;
            }
            if (role == '') {
                job._msg = '担任角色未填写';
                return false;
            }
            if (descript == '') {
                job._msg = '工作描述未填写';
                return false;
            }
            if (time == '-') {
                job._msg = '时间未选择';
                return false;
            }
            // 选择时间 后一个时间大于前一个时间
            if (timel < timeb) {
                job._msg = '时间起止选择错误';
                return false;
            }
            var reqData = {
                "work_experience_time":time,
                "work_experience_place":place,
                "work_experience_role":role,
                "work_experience_descript":descript
            };
            if (id) { reqData.work_experience_id = id; }
            return reqData;
        },
        getAddData:function() {
            // 拿到新增数据
            var len = job._dataLen;
            var _this = $('.item-job').find('.item-body-con').filter(':eq('+len+'), :gt('+len+')');
            //if (_this.length == 0) { job._flag += 1; } else { console.log(1); }
            _this.each(function () {
                var reqData = job.getValue($(this));
                if (reqData) {
                    job._addList.push(reqData);
                    job._flag = true;
                }
            });
        },
        getModifyData:function() {
            // 拿到修改数据
            var len = job._dataLen;
            var _this = $('.item-job').find('.item-body-con:lt(' + (len) + ')');
            //if (_this.length == 0) { job._flag += 1; } else { console.log(1); }
            _this.each(function() {
                var reqData = job.getValue($(this));
                if (reqData) {
                    job._changeList.push(reqData);
                    job._flag = true;
                }
            });
        },
        req:function () {
            request.post('/work/userSettingWorkExperience', {
                "data":JSON.stringify({
                    "change":job._changeList,
                    "add":job._addList,
                    "del":job._deleList.join(',')
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
            var _parent = $('.item-job');
            _parent.off('click');

            // 保存
            _parent.on('click', '.save', function () {
                job.getAddData();
                job.getModifyData();
                if (job._flag == true) {
                    job.req();
                } else {
                    job._flag = 0;
                    tip.info({ tip: job._msg, time: '1500' });
                }
            });
            // 删除
            _parent.on('click', '.delete', function () {
                if (confirm('确认删除？')) {
                    var _this = $(this).parent();
                    var id = _this.attr('data-id');
                    if (id) {
                        job._deleList.push(id);
                        job._dataLen -= 1;
                    }
                    _this.remove();
                } else {
                    return;
                }
            });
            // 添加
            _parent.on('click', '.add', function () {
                var tpl = juicer(funcTpl(job.tpl), job._req);
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

    return job.init;
});