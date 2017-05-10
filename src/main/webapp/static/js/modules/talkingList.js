/**
 * @description 说友列表渲染核心js
 *  @author wulunyi
 */
define(['lib/jquery', 'util/request', 'util/funcTpl', 'modules/page/page','modules/picFadeIn', 'lib/juicer', 'modules/baseMoudle'], function ($, request, funcTpl, page,picFadeIn) {
    'use strict';
    var talking = {
        init: function () {
            //获取参数
            var param = talking._param();
            var hasFriends = (/^friends/).test(window.location.hash.slice(1));//获取hash值判断是显示说友还是列表
            var firstFiled = talking.getUrlParam("firstType");//从url获取一级领域
            var filedName  = talking.getUrlParam("filedName");//从url获取一级领域名字
            
            //如果传递了参数tap 1表示说友0表示talking
            //如果hash 有friends 则表明选择了说友否则默认为talking列表
            if (!hasFriends) {
                //若当前url跳转参数
                if(firstFiled !== undefined){
                    talking._data.RootNode_id = firstFiled;//
                    $('.address-bar span').eq(1).html("");
                }
                talking._currentTpl = funcTpl(talking._talkingTpl);

                document.title = 'talking 列表';//设置标题
            } else {
                $('.J_switch_tap').toggleClass('tab-active');

                $('.J_sex').removeClass('hide'); //打开性别选项

                document.title = '说友列表';//设置标题
                talking._currentTpl = funcTpl(talking._friendsTpl);
                talking._data.type = 1;
            }

            $('.J_nav-1').addClass('nav-active');  //顶级导航tab标签的变化
            //获取翻页钩子函数->回调为翻页函数
            var initPage = page.init('#page', function (num) {
                talking._data.pageCount = num;
                talking._talkingList(talking._currentTpl);
            });
            //初始化
            function setPage() {
                talking._data.pageCount = 1;
                $('body').one('page:set', function (ev, size, totalList) {
                    initPage(size, totalList);
                });
            }

            setPage();
            talking._switch(setPage);//初始化点击切换事件 （关闭原因，切换后点击到新页面返回时跳转页面不确定）
            //talking._talkingList(talking._currentTpl); //拉取列表
            talking._getChoose(setPage); //获取选项
        },
        _Api: {
            getList: '/talking/ShowTalkingByCfOrCyOrCs',
            getChoose: '/talking/ShowTalkingCf'
        },
        _data: {LeafNode_id: 0, RootNode_id: 0, city_id: 0, campus_id: 0, pageCount: 1, type: 0, sex: -1, style: -1},//默认请求参数
        _currentDom: {classification: '', subClassification: '', city: '', campus: ''},//缓存选择面板选择
        _chooseData: {},//缓存服务器选择选项数据
        _currentTpl: '',//渲染是talking还是说友的模板
        _param: function () {
            var paramSearch = window.location.search.slice(1),
                paramArr = [],
                oParam = {};
            paramArr = paramSearch.split('&');
            for (var i = 0; i < paramArr.length; i++) {
                var inArr = paramArr[i].split('=');
                oParam[inArr[0]] = parseInt(inArr[1]);
            }
            return oParam;
        },
        _talkingTpl: function () {
            /*
             <!--
             {@helper getDate}
             function(time) {
             var oTime = new Date(time);
             return [oTime.getMonth()+1+"月",oTime.getDate()+"日"].join('');
             }
             {@/helper}
             {@helper getHour}
             function(time) {
             var oTime = new Date(time);
             function toNormal(num){
             return num>9?num:'0'+num;
             }
             return [toNormal(oTime.getHours()),toNormal(oTime.getMinutes())].join(':');
             }
             {@/helper}
             -->
             {@each talking as item}
             <li class="listen-talking-item" >
             <a href="/talking/classDetail?tk=${item.talking_id}" class="inline-block" target="_blank"><img class="pic1" src="${item.talking_main_picture}" alt="pic"  onerror="this.src = window.pub.classImg" /></a>
             <div class="clearfix">
             <h2><a href="/talking/classDetail?tk=${item.talking_id}" target="_blank">${item.talking_title}<a></h2>
             <ul class="course-info">
             <li class="course-info-item">
             {@if item.talking_user_sex==0}
             <i class="sex-man inline-block course-info-item-pic"></i><span>${item.user_name}</span>
             {@else}
             <i class="sex-gir inline-block course-info-item-pic"></i><a href=/user/personalIndex?user=${item.user_id} target="_blank"><span>${item.user_name}</span></a>
             {@/if}
             <!--<span class="star-container">
             <i class="inline-block star stary"></i>
             <i class="inline-block star stary"></i>
             <i class="inline-block star stary"></i>
             <i class="inline-block star starn"></i>
             <i class="inline-block star starn"></i>
             </span>-->
             </li>
             <li class="course-info-item">
             <i class="campus inline-block course-info-item-pic"></i><span>${item.campus_name}</span>
             </li>
             <li class="course-info-item">
             <i class="time inline-block course-info-item-pic"></i><span>${item.talking_start_time|getDate} ${item.talking_start_time|getHour}-${item.talking_end_time|getHour}</span>
             </li>
             </ul>
             <ul class="course-deal">
             <li class="course-deal-item">&yen;<span class="course-deal-price">${item.talking_price}</span></li>
             <li>
             <a class="course-att-btn inline-block" href="/talking/classDetail?tk=${item.talking_id}">
             {@if item.talking_now_persion!=0}
             <span class="current-att">${item.talking_max_persion-item.talking_now_persion}</span>/${item.talking_max_persion}参加
             {@else}
             已满员
             {@/if}
             </a>
             </li>
             </ul>
             </div>
             </li>
             {@/each}
             */
        },
        _friendsTpl: function () {
            /*
             {@each talking as item}
             <li class="talk-friends-item clearfix">
             <dl class="talk-friends-msg">
             <dt>
             <a href="/user/personalIndex?user=${item.user_id}" class="inline-block" target="_blank">
             <img src="${item.user_photo}" alt="pic" onerror='this.src = window.pub.userimg'/>
             </a>
             {@if item.user_sex==1}
             <i class="inline-block sex-boy"></i>
             {@else}
             <i class="inline-block sex-girl"></i>
             {@/if}
             </dt>
             <dd><a href="/user/personalIndex?user=${item.user_id}" target="_blank">${item.user_name}</a></dd>
             </dl>
             <ul class="talk-friends-info">
             <li>
             <span>认证 :</span>
             <span class="ids-container">
             {@if item.user_student_check_result==0}
             <i title="学生认证" class="inline-block no-ids-xs ids"></i>
             {@else}
             `                  <i title="学生认证" class="inline-block ids-xl ids"></i>
             {@/if}

             <!--<i title="身份认证" class="inline-block ids-sf ids"></i>
             <i title="学生认证" class="inline-block ids-xs ids"></i>
             <i title="学历认证" class="inline-block ids-xl ids"></i>-->
             </span>
             </li>
             <!--<li>
             <span>评价 : </span>
             <span class="star-container">
             <i class="inline-block star stary"></i>
             <i class="inline-block star stary"></i>
             <i class="inline-block star stary"></i>
             <i class="inline-block star starn"></i>
             <i class="inline-block star starn"></i>
             </span>
             </li>-->
             <li>
             <span>学校 : </span><span>${item.campus_name}</span>
             </li>
             <li class="talk-friends-info-item">
             <span>Talking数量 : ${item.user_talking_count}</span>
             </li>
             <li class="talk-friends-info-item">
             <span>擅长领域 : </span>

             {@each item.user_SkillArea as subitem,index}
             {@if index<3}
             <span class="good-at inline-block">${subitem.talking_type_name}</span>
             {@/if}
             {@/each}

             </li>
             </ul>
             </li>
             {@/each}
             */
        },
        _getChoose: function (setPage) {//获取面板选择项数据
            request.get(talking._Api.getChoose, {}, function (res) {
                    res = JSON.parse(res) || eval(res);
                    if (1 == res.code) {
                        //缓存数据
                        talking._chooseData = res.data;

                        var initHtml = ' <li class="filter-checked all" data-id="0">全部</li>';
                        var classificationHtml = initHtml,
                            subClassificationHtml = initHtml,
                            cityHtml = initHtml,
                            campusHtml = initHtml;
                        //渲染一级分类和二级分类
                        $(res.data.classification).each(function (index) {
                            var item = this;
                            classificationHtml += '<li data-id="' + item.taking_type_id + '" data-index="' + index + '">' + item.taking_type_name + '</li>';
                            $(item.LeafNodes).each(function (index) {
                                var item = this;
                                subClassificationHtml += '<li data-id="' + item.talking_type_id + '">' + item.talking_type_name + '</li>';
                            });
                        });
                        //渲染城市和学校
                        $(res.data.city_campus).each(function (index) {
                            cityHtml += '<li data-id="' + this.city_id + '" data-index="' + index + '">' + this.city_name + '</li>';
                            $(this.campus).each(function (index) {
                                campusHtml += '<li data-id="' + this.campus_id + '">' + this.campus_name + '</li>';
                            });
                        });
                        //添加并获取选中节点
                        talking._currentDom.classification = $('#J_classification').html(classificationHtml).find('.all');
                        talking._currentDom.subClassification = $('#J_subClassification').html(subClassificationHtml).find('.all');
                        talking._currentDom.city = $('#J_city').html(cityHtml).find('.all');
                        talking._currentDom.campus = $('#J_campus').html(campusHtml).find('.all');
                        //添加监听 监听选项的变化
                        talking._filterSwitch(setPage);
                        if(talking._data.RootNode_id){
                            $('#J_classification').find("li[data-id="+talking._data.RootNode_id+"]").click();
                            console.log('123');
                        }else{
                            $('#J_classification li').eq(0).click();
                        }
                    }
                }
            );
        },
        _filterSwitch: function (setPage) {
            /*
             * @description 给各个选项添加监听
             * @param prentDom_id {String} 一级节点选项容器id
             * @param _$parentDom {String} 缓存jquery一级选项名(缓存已选中)
             * @param _parentQDataName {String} 缓存请求父级数据名
             * @param _childQDataName {String} 缓存请求子级数据名
             * @param _parentDataName {String} 缓存父级数据名
             * @param _nodeDataName {String} 缓存子节点名
             * @param _nodeDataName_id {String} 缓存子节点下数据id
             * @param _nodeDataName_name {String} 缓存子节点下数据name
             * @param _$childName {String} 缓存jquery子节点对象
             * @param childDom_id {String} 二级节点选项容器id
             * */
            function addWatch(prentDom_id, _$parentName, _parentQDataName, _parentDataName, _nodeDataName, _nodeDataName_id, _nodeDataName_name, _$childName, childDom_id, _childQDataName) {
                $(prentDom_id).on('click','li', function (ev) {
                    if($(this).parent().attr('id')=='J_classification'){
                        var str = $(this).html();
                        $('.address-bar span').eq(0).html(str);
                        $('.address-bar span').eq(1).html('全部');
                    }
                    // //重复点击不执行（暂时没有找到好的解决方案，解决从首页跳到牛人列表显示问题，所以只能把防止重复点击给去了）
                    // if (talking._currentDom[_$parentName][0] == this) {
                    //     return;
                    // }
                    //获取绑定数据
                    var index = $(this).data('index');
                    var id = $(this).data('id');
                    //状态变化切换
                    talking._currentDom[_$parentName].removeClass('filter-checked');
                    $(this).addClass('filter-checked');
                    talking._currentDom[_$parentName] = $(this);
                    // 请求数据变化
                    talking._data[_parentQDataName] = id;
                    talking._data[_childQDataName] = 0;
                    //子节点变化
                    var childNodeHtml = ' <li class="filter-checked all" data-id="0">全部</li>';
                    if (id != 0) {
                        var dataArr = talking._chooseData[_parentDataName][index][_nodeDataName];
                        $(dataArr).each(function (index) {
                            childNodeHtml += '<li data-id="' + this[_nodeDataName_id] + '">' + this[_nodeDataName_name] + '</li>';
                        });
                    } else {
                        $(talking._chooseData[_parentDataName]).each(function (index) {
                            $(this[_nodeDataName]).each(function (index) {
                                childNodeHtml += '<li data-id="' + this[_nodeDataName_id] + '">' + this[_nodeDataName_name] + '</li>';
                            });
                        });
                    }
                    talking._currentDom[_$childName] = $(childDom_id).html(childNodeHtml).find('.all');
                    //请求数据
                    setPage();//重开翻页组件监听
                    talking._talkingList(talking._currentTpl);
                });

                $(childDom_id).on('click','li', function (ev) {

                    var str = $(this).html();
                    $('.address-bar span').eq(1).html(str);

                    if (talking._currentDom[_$childName][0] == this) {
                        return;
                    }
                    talking._data[_childQDataName] = $(this).data('id');
                    //状态变化
                    talking._currentDom[_$childName].removeClass('filter-checked');
                    $(this).addClass('filter-checked');
                    talking._currentDom[_$childName] = $(this);
                    //请求数据
                    setPage();//重开翻页组件监听
                    talking._talkingList(talking._currentTpl);
                });

            }

            addWatch('#J_classification', 'classification', 'RootNode_id', 'classification', 'LeafNodes', 'talking_type_id', 'talking_type_name', 'subClassification', '#J_subClassification', 'LeafNode_id');
            //城市监听
            addWatch('#J_city', 'city', 'city_id', 'city_campus', 'campus', 'campus_id', 'campus_name', 'campus', '#J_campus', 'campus_id');

            //性别选择
            $('input[name="sex"],input[name="style"]').on('change', function () {
                var Arr = [];
                var name = this.name;
                $('input[name=' + name + ']:checked').each(function () {
                    Arr.push($(this).val())
                });
                switch (Arr.length) {
                    case 1:
                        talking._data[this.name] = Arr[0];
                        break;
                    case 0:
                    case 2:
                        talking._data[this.name] = -1;
                        break;
                    default :
                        break;
                }
                //请求数据
                setPage();//重开翻页组件监听
                talking._talkingList(talking._currentTpl);
            });
        },
        _talkingList: function (tpl) {
            request.get(
                talking._Api.getList,
                talking._data,
                function (res) {
                    res = JSON.parse(res) || eval(res);
                    if (1 == res.code) {
                        var tmp = juicer(tpl, res.data);
                        $(".talking-list").html(tmp);
                        //触发翻页初始化
                        $('body').trigger('page:set', [12, res.data.size]);

                        picFadeIn();
                    }
                });
        },
        getUrlParam: function(name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
                var r = window.location.search.substr(1).match(reg); //匹配目标参数
                if (r != null) return unescape(r[2]);
                return null; //返回参数值
            },
        _switch: function (setPage) {
            $('.J_switch_tap').on('click', function () {
                if (!$(this).hasClass('tab-active')) {
                    talking._data.type = talking._data.type == 0 ? 1 : 0;

                    $('.J_switch_tap').toggleClass('tab-active');
                    $('.sex-input-wrap').toggleClass('hide');
                    $('.publish-time').toggleClass('hide');

                    if (talking._data.type == 0) {
                        talking._data.sex = -1;
                        talking._currentTpl = funcTpl(talking._talkingTpl);

                        $('.sex-input').attr({'checked':'checked'});
                        document.title = 'talking 列表';
                    } else {
                        var Arr = [];
                        var name = this.name;

                        $('input[name="sex"]:checked').each(function () {
                            Arr.push($(this).val())
                        });

                        switch (Arr.length) {
                            case 1:
                                talking._data.sex = Arr[0];
                                break;
                            case 0:
                            case 2:
                                talking._data.sex = -1;
                                break;
                            default :
                                break;
                        }
                        document.title = '说友列表';
                        talking._currentTpl = funcTpl(talking._friendsTpl);
                    }
                    setPage();
                    talking._talkingList(talking._currentTpl);
                }
            });
        }
    };
    return talking;
});
