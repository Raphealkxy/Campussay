/**
 * 个人设置-基本信息
 * Created by WXY on 2016/1/26.
 */
define(['lib/jquery', 'util/funcTpl', 'util/request', 'modules/topic-pop/commonPop', 'lib/juicer'],
    function ($, funcTpl, request, tip) {

    var basic = {
        _data: {},
        _addArea: {
            "skill": [],
            "follow": []
        },
        _deleArea: {
            "skill": [],
            "follow": []
        },
        _followInit: [],
        _skillInit: [],

        init:function () {
            // basic info
            request.post('/user/getUserSettingBasic', {
                "userId": 0
            }, function (res) {
                res = JSON.parse(res);
                if(res.code == 1){
                    var basicInfo = basic._data =  res.data.BasicInfo;
                    var _parent = $('.item-basic');
                    var institute = _parent.find($('.item-institute input'));
                    _parent.find('.item-real-name input').val(basicInfo.user_name);
                    _parent.find('.item-title-name input').val(basicInfo.user_title);
                    _parent.find('.item-school input').val(basicInfo.user_campus_name);
                    _parent.find('.item-desc-self textarea').val(basicInfo.user_description);
                    _parent.find('.item-sexual input').eq(basicInfo.user_sex).attr('checked', 'checked');
                    institute.eq(0).val(basicInfo.user_academe);
                    institute.eq(1).val(basicInfo.user_major);
                    $('.item-good-field .tag').html(juicer(funcTpl(basic.skill_tag_tpl), res.data));
                    $('.item-care-field .tag').html(juicer(funcTpl(basic.follow_tag_tpl), res.data));
                }
                basic.event();
            });
        },
        getArea:function () {
            var followArea = [], skillArea = [];
            // 初始化获得擅长、关注领域
            $('.item-good-field').find('.item-tag').map(function () {
                basic._skillInit.push($(this).data('id'));
            });
            $('.item-care-field').find('.item-tag').map(function () {
                basic._followInit.push($(this).data('id'));
            });
        },
        addArea:function () {
            // 添加领域 add tag
            $('.add-tag').on('click', function () {
                var _parent = $(this).parents('.item'),
                    $addBtn = $(this).parents('.select-sub-info'),
                    $secondTag = $addBtn.prev(),
                    $firstTag = $secondTag.prev(),
                    tagId = +$secondTag.find('input').attr('data-id'),
                    tagVal = $secondTag.find('input').val(),
                    firstTagVal = $firstTag.find('input').val();
                var tag = "<div class='item-tag' data-id='"+tagId+"'><span class='tag-rect'>"+tagVal+
                    "<a href='javascript:;' class='delete-tag'></a></span></div>";
                var msg = "";

                if (tagVal == "") {
                    if (firstTagVal == "") {
                        msg = "领域不能为空";
                    } else {
                        msg = "二级领域不能为空";
                    }
                    tip.info({
                        tip: msg, time: '2500'
                    });
                    return;
                }
                if (_parent.hasClass('item-good-field')) {
                    if (basic._skillInit.length+basic._addArea.skill.length > 10) {
                        tip.info({
                            tip: '最多选择10个擅长领域哦！',
                            time: '2500'
                        });
                        return;
                    }
                    if (basic.isUnique(tagId, basic._addArea.skill) && basic.isUnique(tagId, basic._skillInit)) {
                        basic._addArea.skill.push(tagId);
                    } else {
                        return;
                    }
                } else {
                    if (basic.isUnique(tagId, basic._addArea.follow) && basic.isUnique(tagId, basic._followInit)) {
                        basic._addArea.follow.push(tagId);
                    } else {
                        return;
                    }
                }
                $addBtn.next().find('.tag').append(tag);
            });
        },
        deleArea:function () {
            $('.tag').on('click', '.delete-tag', function () {
                var _parent = $(this).parents('.item');
                var tagId = +$(this).parents('.item-tag').attr('data-id');
                $(this).parents('.item-tag').fadeOut(300);
                if (_parent.hasClass('item-good-field')) {
                    if (basic.isUnique(tagId, basic._skillInit)) {
                        basic.remove(tagId, basic._addArea.skill);
                    } else {
                        basic._deleArea.skill.push(tagId);
                    }

                } else {
                    if (basic.isUnique(tagId, basic._followInit)) {
                        basic.remove(tagId, basic._addArea.follow);
                    } else {
                        basic._deleArea.follow.push(tagId);
                    }
                }
            });
        },
        event:function () {

            var _parent = $('.item-basic');
            basic.getArea();

            _parent.off('click');
            // 选择性别
            $('.radio-sexual').on('click', function () {
                var _radio = $(this).find('input');
                if (_radio.checked) { return; }
                _radio.attr('checked', 'checked').addClass('checked');
                $(this).siblings().find('input').removeAttr('checked').removeClass('checked');
            });
            // 选择学校
            _parent.on('focus', '.select-school input', function () {
                var _this = $(this);
                var select_content;
                var item = function () {
                    /*
                     {@each data as item}
                     <li ${item.campus_name}>${item.campus_name}</li>
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
            var lastVal1 = '';
            _parent.on('input propertychange', '.select-institute input', function () {
                var _this = $(this);
                var select_content;
                var item =  function () {
                    /*
                     {@each data as item}
                     <li title="${item.academy_name}">${item.academy_name}</li>
                     {@/each}
                     */
                };
                var nowVal = _this.val().trim();
                if (nowVal != "") {
                    if (nowVal != lastVal1) {
                        request.post('/education/getAllAcademy', {
                            "academy": _this.val()
                        }, function (res) {
                            res = JSON.parse(res);
                            if (res.code == 1) {
                                select_content = juicer(funcTpl(item), res);
                                _this.next().show(100).html(select_content);
                            }
                        });
                        lastVal1 = nowVal;
                    }
                } else {
                    select_content = '<li>请输入关键字</li>';
                    _this.next().show(100).html(select_content);
                    return;
                }
            });
            // 专业模糊查询结果
            var lastVal2 = '';
            _parent.on('input propertychange', '.select-major input', function () {
                var _this = $(this);
                var select_content;
                var item =  function () {
                    /*
                     {@each data as item}
                     <li title="${item}">${item}</li>
                     {@/each}
                     */
                };
                var nowVal = _this.val().trim();
                if (nowVal != "") {
                    if (nowVal != lastVal2) {
                        request.post('/education/getAllMajor', {
                            "major": _this.val()
                        }, function (res) {
                            res = JSON.parse(res);
                            if (res.code == 1) {
                                select_content = juicer(funcTpl(item), res);
                                _this.next().show(100).html(select_content);
                            }
                        });
                        lastVal2 = nowVal;
                    }
                } else {
                    select_content = '<li>请输入关键字</li>';
                    _this.next().show(100).html(select_content);
                    return;
                }
            });
            // 删除领域 delete tag
            basic.deleArea();
            // 添加领域 add tag
            basic.addArea();
            // 选择领域一级分类
            var firstField = function () {
                var _this = $(this);
                var select_content;
                var item =  function () {
                    /*
                     {@each data as item}
                     <li data-id="${item.talking_type_id}" title="${item.talking_type_name}">${item.talking_type_name}</li>
                     {@/each}
                     */
                };
                var $second = _this.parent().next();
                $second.find('input').val('');
                $second.find('ul').html('');
                request.post('/user/getAllFirstArea', {}, function (res) {
                    res = JSON.parse(res);
                    if (res.code == 1) {
                        select_content = juicer(funcTpl(item), res);
                        _this.next().html(select_content);
                    }
                });
            };
            // 选择领域二级分类
            var secondField = function () {
                var _this = $(this);
                var select_content;
                var item =  function () {
                    /*
                     {@each data as item}
                     <li data-id="${item.talking_type_id}" title="${item.talking_type_name}">${item.talking_type_name}</li>
                     {@/each}
                     */
                };
                var first_id = _this.parent().prev().find('input').attr('data-id');
                if (first_id) {
                    request.post('/user/getAllSecondArea', {
                        "id":first_id
                    }, function (res) {
                        res = JSON.parse(res);
                        if (res.code == 1) {
                            select_content = juicer(funcTpl(item), res);
                            _this.next().html(select_content);
                        }
                    });
                }
            };
            // 擅长领域 关注领域
            $('.select-good-field-main input, .select-care-field-main input').click(firstField);
            $('.select-good-field-sub input, .select-care-field-sub input').click(secondField);

            // 保存基本信息
            _parent.on('click', '.save', function () {
                var _this = $(this).parents('.item-body');
                var username = _this.find('.item-real-name input').val().trim(),
                    title = _this.find('.item-title-name input').val().trim(),
                    campus = _this.find('.item-school input').val().trim(),
                    academe = _this.find('.item-institute input:eq(0)').val().trim(),
                    major = _this.find('.item-institute input:eq(1)').val().trim(),
                    sex = _this.find('.item-sexual input[checked]').val();
                if (username.length > 10 || username == "") {
                    tip.info({ tip: '姓名长度为1-10个字符', time: '1000'});
                    return;
                }
                if (title.length > 15 || title == "") {
                    tip.info({ tip: '头衔长度为1-15个字符', time: '1000'});
                    return;
                }
                if (campus == '') {
                    tip.info({ tip: '学校不能为空', time: '1000'});
                    return;
                }
                if (academe == '') {
                    tip.info({ tip: '学院不能为空', time: '1000'});
                    return;
                }
                if (major == '') {
                    tip.info({ tip: '专业不能为空', time: '1000'});
                    return;
                }
                var reqData = {
                    "user_id": basic._data.user_id,
                    "user_title": title,
                    "user_name": username,
                    "user_campus_name": campus,
                    "user_academe": academe,
                    "user_major": major,
                    "user_sex": sex,
                    "user_description":_this.find('.item-desc-self textarea').val().trim()
                };
                request.post('/user/userUpdateBasic', {
                    "data":JSON.stringify({
                        "user": reqData,
                        "addSkill": basic._addArea.skill,
                        "addFollow": basic._addArea.follow,
                        "deleSkill": basic._deleArea.skill,
                        "deleFollow": basic._deleArea.follow
                    })
                }, function (res) {
                    res = JSON.parse(res);
                    if (res.code == 1) {
                        tip.info({
                            tip: '保存成功',
                            time: '2500',
                            reload: true
                        });
                    } else {
                        tip.info({
                            tip: res.msg,
                            time: '2500'
                        });
                    }
                });
            });
        },
        isUnique:function (val, arr) {
            var count = 0;
            for (var i = 0; i < arr.length; i++) {
                if (arr.indexOf(val) == -1) {
                    count++;
                }
            }
            if (arr.length == count) return true;
            return false;
        },
        remove:function (val, arr) {
            var index = arr.indexOf(val);
            if (index > -1) {
                arr.splice(index, 1);
            }
        },
        follow_tag_tpl:function () {
            /*
             {@each FollowArea as tag}
             <div class="item-tag" data-id="${tag.talking_type_id}">
             <span class="tag-rect">${tag.talking_type_name}<a href="javascript:;" class="delete-tag"></a> </span>
             </div>
             {@/each}
             */
        },
        skill_tag_tpl:function () {
            /*
             {@each SkillArea as tag}
             <div class="item-tag" data-id="${tag.talking_type_id}">
             <span class="tag-rect">${tag.talking_type_name}<a href="javascript:;" class="delete-tag"></a> </span>
             </div>
             {@/each}
             */
        }
    };

    return basic.init;
});