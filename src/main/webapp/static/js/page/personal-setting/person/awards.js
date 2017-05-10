/**
 * 个人设置-获奖经历
 * Created by WXY on 2016/1/26.
 */
define(['lib/jquery', 'util/funcTpl', 'util/request','modules/topic-pop/commonPop', 'lib/juicer'],
    function ($, funcTpl, request, tip) {

    var awards = {
        tpl:function () {
            /*
             {@each data as item}
             <div class="item-body-con" data-id="${item.prize_id}">
                 <!--<div class="item delete"><a href="javascript:;">删除这个获奖经历</a></div>-->
                 <div class="item item-time">
                     <label>时间：</label>
                     <div class="select select-start-year">
                         <input type="text" value="${item.prize_time}" readonly class="item-input item-select-input form_datetime">
                     </div>
                 </div>
                 <div class="item item-name">
                     <label>比赛名称：</label>
                     <input type="text" name="certification-name" class="item-input item-text-input" value="${item.prize_title}">
                 </div>
                 <div class="item item-desc">
                     <label>获奖描述：</label>
                     <input type="text" name="certification-name" class="item-input item-text-input" value="${item.prize_descript}">
                 </div>
                 <div class="item delete"><a href="javascript:;">删除这个获奖经历</a></div>
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
                    "prize_time": "",
                    "prize_title": "",
                    "prize_descript": ""
                }
            ]
        },
        _count: 0,

        init:function (datepicker) {
            if (awards._count == 1) { return; }
            awards._count = 1;

            var _parent = $('.item-awards');
            request.post('/prize/getUserSettingPrize', {}, function (res) {
                res = JSON.parse(res);
                if (res.code == 1) {
                    if (res.data.length > 0) {
                        awards._dataLen = res.data.length;
                        var tpl = juicer(funcTpl(awards.tpl), res);
                        _parent.find('.item-body').prepend(tpl);
                        // addBackup.remove();
                        // _parent.find('.delete').show();
                        // awards.watchModify($('.item-awards').find('.item-body-con'));
                    } else {
                        var tpl = juicer(funcTpl(awards.tpl), awards._req);
                        _parent.find('.item-body').prepend(tpl);
                    }
                }
                datepicker();
                awards.event();
            });
        },
        getValue:function (_this) {
            var id = _this.attr('data-id');
            var time = _this.find('.item-time input').val().trim(),
                title = _this.find('.item-name input').val().trim(),
                descript = _this.find('.item-desc input').val().trim();

            if (title == '') {
                awards._msg = '奖项名称未填写';
                return false;
            }
            if (descript == '') {
                awards._msg = '奖项描述未填写';
                return false;
            }
            if (time == '') {
                awards._msg = '时间未选择';
                return false;
            }
            var reqData = {
                "prize_time":_this.find('.item-time input').val().trim(),
                "prize_title":_this.find('.item-name input').val().trim(),
                "prize_descript":_this.find('.item-desc input').val().trim()
            };
            if (id) { reqData.prize_id = id; }
            return reqData;
        },
        getAddData:function() {
            // 拿到新增数据
            var len = awards._dataLen;
            var _this = $('.item-awards').find('.item-body-con').filter(':eq('+len+'), :gt('+len+')');
            //if (_this.length == 0) { awards._flag += 1; } else { console.log(1); }
            _this.each(function () {
                var reqData = awards.getValue($(this));
                if (reqData) {
                    awards._addList.push(reqData);
                    awards._flag = true;
                }
            });
        },
        getModifyData:function() {
            // 拿到修改数据
            var len = awards._dataLen;
            var _this = $('.item-awards').find('.item-body-con:lt(' + (len) + ')');
            //if (_this.length == 0) { awards._flag += 1; } else { console.log(1); }
            _this.each(function() {
                var reqData = awards.getValue($(this));
                if (reqData) {
                    awards._changeList.push(reqData);
                    awards._flag = true;
                }
            });
        },
        req:function () {
            request.post('/prize/userSettingPrize', {
                "data":JSON.stringify({
                    "change":awards._changeList,
                    "add":awards._addList,
                    "del":awards._deleList.join(',')
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
            var _parent = $('.item-awards');
            _parent.off('click');

            // save
            _parent.on('click', '.save', function () {
                awards.getAddData();
                awards.getModifyData();
                if (awards._flag == true) {
                    awards.req();
                } else {
                    awards._flag = 0;
                    tip.info({ tip: awards._msg, time: '1500' });
                }
            });
            // 删除
            _parent.on('click', '.delete', function () {
                if (confirm('确认删除？')) {
                    var _this = $(this).parent();
                    var id = _this.attr('data-id');
                    if (id) {
                        awards._deleList.push(id);
                        awards._dataLen -= 1;
                    }
                    _this.remove();
                } else {
                    return;
                }
            });
            // 添加
            _parent.on('click', '.add', function () {
                var tpl = juicer(funcTpl(awards.tpl), awards._req);
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

    return awards.init;
});