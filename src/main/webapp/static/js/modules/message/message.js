/**
 *  Created by wuLunYi on 2016/3/5.
 */

define(['lib/jquery', 'util/request', 'util/funcTpl', 'modules/page/page','modules/topic-pop/commonPop', 'lib/juicer','modules/baseMoudle'], function ($, request, funcTpl, page,tip) {

    var message = {
        init: function () {
            this._getMessage('', '', ''); //step1 -> 获取消息数据

            this._watchOption();//step2 -> 监听按钮操作
            this.delMessage();
            var pageSet = page.init('#page', function (num) {
                message._getMessage('', '', num);
            });
            $('body').on('page:set', function (ev, limit, totalSize) {
                pageSet(limit, totalSize)
            })
        },
        _API: {
            getMessage: '/information/getuserInformationList',//获取用户消息
            setMessageRead: '/information/setInformationRead',//设置为已读
            setAllMessageRead:'/user/updateInfo_systemReaded',//设置全部的消息为已读
            delMessage: '/information/deleteInformation',//删除消息
            delAllMessage: '/user/deleteInfo_systemReaded'//删除所有的消息

        },
        _DATA: {
            dataCount: 0,
            ids: []
        },
        delMessage:function(){
            $('#message-list').on('click','.delMessage',function(){
                var id = $(this).attr('id');
                var str = "'"+id+"'";
                message._messageOption(message._API.delMessage,str,delOne);
                function delOne(){
                    message._getMessage('', '', '');
                }
            })
                .on('click','.passMessage',function (){
                $(this).attr('disabled','disabled');
                var id = $(this).attr('id');
                var str = "'"+id+"'";
                message._messageOption(message._API.setMessageRead,str,passOne(this));
                function passOne(dom){
                    var _this = $(dom).parent();
                    console.log(_this.length);
                    var classStr;
                    if(_this.hasClass('odd')){
                        classStr = _this.attr('class').replace('odd','').trim() +'D'+' odd';
                    }else{
                        classStr = _this.attr('class').trim() +'D';
                    }
                        _this.removeClass().addClass(classStr);
                        $(dom).fadeOut();
                }
            })
        },
        _getMessage: function (informationType, informationStatus, pageNum) {
            var data = {};//请求参数

            informationType && (data.informationType = informationType);
            informationStatus && (data.informationStatus = informationStatus);
            pageNum && (data.pageNum = pageNum);

            request.get(
                this._API.getMessage,
                data,
                function (res) {
                    res = JSON.parse(res);

                    if (1 == res.code) {
                        if (message._DATA.dataCount != res.data.dataCount) {
                            message._DATA.dataCount = res.data.dataCount;

                            $('.dataCount').html(message._DATA.dataCount);

                            $('body').trigger('page:set', [res.data.pageSize, res.data.dataCount]);
                        }
                        //缓存未读id数组
                        var data = $.grep(res.data.resultList, function (item) {
                            return item.information_isread == 0;
                        });
                        message._DATA.ids = $.map(data, function (item) {
                            return "'" + item.information_id + "'";
                        });

                        var tpl = '{@each resultList as item,index}' +
                            '<li class="${item.information_type,item.information_isread|messageIco} {@if index%2==1}odd{@/if}" id="${item.information_id}">' +
                            '$${item.information_content}' +
                            '<span>${item.information_crate_time|handleTime}</span>'+
                            '<button id="${item.information_id}" class="delMessage">删除</button>'+
                            '{@if item.information_isread==0}<button id="${item.information_id}" class="passMessage">已读</button>{@/if}'+
                            '</li>' +
                            '{@/each}';
                        juicer.register('handleTime', message._handleTime);
                        juicer.register('messageIco',message._messageIco);
                        var html = juicer(tpl, res.data);
                        $('#message-list').html(html);

                    } else if (100011 == res.code) {
                        $('#message-list').html($.BlackImgsm());
                        $('.dataCount').html(0);
                        $('body').trigger('page:set', [1,1]);
                    } else if (-1 == res.code) {
                        window.location.href='/user/sign-in-web?url=/message';
                    } else if (-5 == res.code) {
                        console.log(res.msg);
                    }
                }
            );
        },

        _messageOption: function (url, informationIds, fun) {
            request.post(
                url,
                {
                    informationIds: "[" + informationIds + "]"
                },
                function (res) {
                    res = JSON.parse(res);

                    switch (res.code) {
                        case 1:
                            fun && fun();
                            break;
                        case 100001:
                            console.log(res.msg);
                            break;
                        case -1:
                            console.log(res.msg);
                            break;
                        case -4:
                            console.log(res.msg);
                            break;
                        case -5:
                            console.log(res.msg);
                            break;
                        default :
                            break;
                    }
                }
            );
        },
        _watchOption: function () {
            //监听类型变化
            $('#filter-wrap').on('click', 'button', function (ev) {
                var type = $(ev.target).data('type');

                message._getMessage(type, '', '');
            });

            //监听选择
            $('.del,.read').on('click', function (ev) {
                var $this = $(this), url;

                if ($this.hasClass('del')) {
                    if($('.dataCount').html()==0){
                        return ;
                    }
                    url = message._API.delAllMessage;
                    request.get(url,function(data){
                        data = JSON.parse(data);
                        if(data.code ){
                            message._getMessage('', '', '');
                            tip.info({
                                tip: '全部消息已经删除',
                                time: '1500',
                                reload: false
                            });
                        }else{
                            alert(data.msg);
                        }
                    })
                } else {
                    if($('.dataCount').html()==0){
                        tip.info({
                            tip: '没有可读的消息',
                            time: '1500',
                            reload: false
                        });
                        return;
                    }
                    url = message._API.setAllMessageRead;
                    request.get(url,function(data){
                        data = JSON.parse(data);
                        if(data.code ){
                            message._getMessage('', '', '');
                            tip.info({
                                tip: '系统消息全部置为已读',
                                time: '1500',
                                reload: false
                            });
                        }else{
                            alert(data.msg);
                        }
                    })
                }
            })
        },
        //注册的图标显示函数
        _messageIco:function(state,read){
            if(state == 1&& read==0){
                return 'love'
            }
            if(state == 1&& read==1){
                return 'loveD'
            }
            if(state == 2&& read==0){
                return 'message'
            }
            if(state == 2&& read==1){
                return 'messageD'
            }
            if(state == 3&& read==0){
                return 'checked'
            }
            if(state == 3&& read==1){
                return 'checkedD'
            }
            if(state == 4&& read==0){
                return 'ht'
            }
            if(state == 4&& read==1){
                return 'htD'
            }
        },
        //注册的时间函数
        _handleTime:function(time){
            var currentTime = new Date();
            var startTime = new Date(time);
            var year = currentTime.getFullYear()-startTime.getFullYear();
            var month = currentTime.getMonth()-startTime.getMonth();
            var day = currentTime.getDate()-startTime.getDate();
            if(year){
                return year+"年前"
            }
            if(month>1){
                return month+"月前"
            }
            if(day>0){
                if(day ==1 ){
                    if(startTime.getMinutes()<10){
                        return '昨天'+startTime.getHours()+':0'+startTime.getMinutes()
                    }else{
                        return '昨天'+startTime.getHours()+':'+startTime.getMinutes()
                    }
                }else{
                    return day+"天前"
                }
            }else{
                if(startTime.getMinutes()<10){
                    return startTime.getHours()+':0'+startTime.getMinutes()
                }else{
                    return startTime.getHours()+':'+startTime.getMinutes()
                }
            }
        }
    };

    return message;
});