/**
 * Created by liangbijie on 2016/1/25.
 */
define(['lib/jquery', 'util/funcTpl', 'modules/page/page', 'modules/popup/signPop', 'util/request', 'modules/topic-pop/commonPop', 'lib/juicer'], function($, funcTpl, page, pop, request,commonPop) {
    $('head').append('<link rel="stylesheet" href="/static/js/modules/page/page.css" />');
    var hot_list = {
        init: function($hot_ul, num, prosecute) {
            hot_list.add($hot_ul, num, prosecute);

            page.init()
            var initPage = page.init('#page', function(num) {
                hot_list.add($hot_ul, num, prosecute);
            });
            $('body').one('page:set', function(ev, size, totalList) {
                initPage(size, totalList);
            });
            initPage(hot_list.listinfo.size, hot_list.listinfo.rows);
        },
        listinfo: {
            rows: 0,
            size: 10
        },
        //将获取的列表用juicer编译并且插入指定的节点
        add: function($hot_ul, num, prosecute) {
            hot_list.post(num, callback);

            function callback(data1) {
                juicer.register('words', hot_list._getWords);
                var add_list = juicer(funcTpl(hot_list.list), data1);
                $hot_ul.css('display', 'none').html(add_list).fadeIn();
                hot_list.share(); //分享
                hot_list.dot_like(); //添加点赞的功能
                hot_list.changePage($hot_ul); //添加翻页功能
                prosecute && prosecute();

            }
        },
        //获取第num页的列表数据
        post: function(num, fn) {
            var date = new Date();
            var url = 'getNewTopics?date=' + date;
            request.post(url, {
                page: num
            }, function(date) {
                var date1 = JSON.parse(date);
                date1.data.headIco = window.pub.userimg;
                console.log(date1)
                if (date1.code) {
                    hot_list.listinfo.rows = date1.data.rows;
                    fn && fn(date1.data);
                }
            });
        },
        _getWords:function(words){
            var words = words.replace(/<\/?.+?>/g,""); 
            return words;
        },
        //列表模版
        list: function() {
            /*{@each list as item,index}
             <li class="hot-li clearfix ">
                 <div class="like
             {@if item.userIsLike==1}
             dot-like
             {@else}{@/if}
                 ">
                     <a href="/topic/answerdetail?topicid=${item.id}" class="head-ico"><img class="head-ico" src="
                     {@if item.cover_img}
                         ${item.cover_img}
                     {@else}
                         ${headIco}
                     {@/if}
                     " alt="头像"/></a>
                     <a class="islike" href="/topic/hot?answerId=${item.answerId}" data-id="${item.answerId}">
                         <span>{@if item.isLike}
                         ${item.isLike}
                {@else}0{@/if}</span></a>
                 </div>
                 <div class="li-info">
                     <h3 style="font-weight:bold"><a href="/topic/answerdetail?topicid=${item.id}" target="_blank">${item.tile}</a></h3>

                     <div class="li-tag">
                         <a href="/topic/answer?id=${item.taking_type_id}">${item.parentName}/${item.talking_type_name}</a>
                         <a href="/topic/htIndex?user=${item.id}">${item.user_name}</a>
                     </div>
                     <p>
                     $${item.context|words}
                     </p>

                     <div class="li-num">
                         <div><span>${item.answerNum}</span>条回答</div>
                         <div class="prosecute-share">
                             <a href="javascript:;" class="prosecute">举报</a>
                             <a href="#" class="answer-share" data-url="/topic/answerdetail?topicid=${item.id}">分享</a>
                         </div>
                     </div>
                 </div>
             </li>
             {@/each}*/
        },

        //点赞（这里后台还需要给我一个数据：列表的每一项是否被点赞）
        dot_like: function() {
            $('.islike ').on('click', function(event) {
                event.preventDefault();
                var _this = this;
                var addurl = '/answer/addIsLike?answerId=' + $(this).data('id'); //加赞地址
                var disurl = '/answer/disIsLike?answerId=' + $(this).data('id'); //减赞地址
                var dot_like = $(_this).find('span');
                if ($(_this).parent().hasClass('dot-like')) { //如果已经点了赞，再点击则请求减赞地址
                    request.get(disurl, function(data) {
                        var data1 = JSON.parse(data);
                        if (data1.code == 1) {
                            $(_this).parent().removeClass('dot-like');
                            var num = parseInt(dot_like.html());
                            num--;
                            dot_like.html(num);
                        } else if (data1.code == -1) {} else {
                            console.log(data1.msg); //输出减赞失败原因
                        }

                    })
                } else {
                    request.get(addurl, function(data) { //如果未点赞，点击则请求加赞地址
                        var data1 = JSON.parse(data);
                        if (data1.code == 1) {
                            $(_this).parent().addClass('dot-like');
                            var num = parseInt(dot_like.html());
                            num++;
                            dot_like.html(num);
                        } else if (data1.code == 0) {
                            console.log(data1.msg); //输出减赞失败原因
                        } else if(data1.code == -1){
                            pop();
                        } else{
                            commonPop.tip({msg:"点赞失败"});
                        }

                    })
                }
            })
        },
        // 分享
        share: function() {
            $('.answer-share').on('click', function() {
                var $config = {
                    url: $(this).attr('data-url'),
                    source: 'www.campussay.com'
                };
                commonPop.share($config);
            });
        },
        //翻页
        changePage: function($hot_ul) {
            //初始化


            $('body').trigger('page:set', [hot_list.listinfo.size, hot_list.listinfo.rows]);

        }
    };
    return hot_list;
});