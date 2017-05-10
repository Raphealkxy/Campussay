require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'util/request', 'util/funcTpl', 'util/Headertip',
        'modules/page/page', 'modules/topic-pop/commonPop','modules/textEditor/textEditor','modules/popup/signPop','modules/baseMoudle', 'lib/juicer'
    ],
    function($, request, funcTpl, Headertip, pager, commonpop,editor,pop) {
        var index = {
            init: function(pageNum) {
                var topicId = this.getUrlParam("topicid"),
                    self = this;
                this._getQuestion(topicId);
                this._getAnswer(topicId, pageNum);
                this._bindAnswer();
                //绑定点赞，评论，分享事件
                this._bind();
                //翻页 回调函数
                var initPage = pager.init('#page', function(num) {
                    var topicId = self.getUrlParam("topicid");
                    self._getAnswer(topicId, num);
                    $('body').animate({ scrollTop: "0px" }, 800);

                });

                pager.initPage = initPage;

                //初始化
                $('body').one('page:one', function(ev, size, totalList) {
                    initPage(size, totalList);
                });

                editor.init();
            },
            _bind: function() {
                var self = this;
                $("#J_answerlist").click(function(event) {
                    var e = $(event.target),
                        type = e.attr("data-type");; //点击事件类型 
                    if (!type) {
                        return }
                    //1.点赞
                    var voteHandle = function() {
                        var num = e.attr("data-num"); //点赞数量
                        var answerid = e.attr("data-id");
                        request.get('/answer/addIsLike', { answerId: answerid }, function(rsp) {
                            rsp = JSON.parse(rsp);
                            //未登录
                            if (rsp.code == -1) {
                                pop();
                            }
                            //点赞成功
                            if (rsp.code == 1) {
                                num++;
                                e.html("<i class='icon-vote'></i>" + num);
                                commonpop.tip({msg:"点赞成功"});
                            }else{
                                commonpop.tip({msg:rsp.msg,type:"error"});
                            }

                        });
                    }

                    //2.评论列表展开
                    var commentlistHandle = function() {
                        var isInit = e.parents(".J_answerHandle").attr("data-init"),
                            isInitDom = e.parents(".J_answerHandle"),
                            answerid = e.attr("data-id");
                        if (!answerid) return;
                        if (isInit == 1) {
                            var commentList = e.parents(".answer").next();//评论列表
                            if (commentList.hasClass("comment-list")) {
                                commentList.toggle();
                            }
                            return;
                        }

                        request.get("/Comments/getCommentsByAnswerId", { answerId: answerid }, function(rsp) {
                            rsp = JSON.parse(rsp);
                            if (rsp.code == 1) {
                                var commentDom = e.parents(".answer");
                                var tplData = {
                                    list: rsp.data.list,
                                    isInit: 1,
                                    answerid: answerid,
                                    isMore: 1,
                                    totalPage: Math.ceil(rsp.data.rows / 10),
                                };

                                //标记已经初始化评论，不用再次获取
                                isInitDom.attr("data-init", "1");

                                //没有查看更多
                                if (rsp.data.rows == rsp.data.list.length || rsp.data.list.length == 0) {
                                    tplData.isMore = 0
                                }

                                var tmpl = juicer(funcTpl(self._commentTpl), tplData);
                                tmpl = "<ul class='comment-list'>" + tmpl + "</ul>";
                                commentDom.after(tmpl);
                            }
                        });


                    }

                    //3.评论答案
                    var commentHandle = function(){
                        var answerid = e.attr("data-id"),
                        //content = $(".J_commentAnswer").parents(".comments-answer").find("textarea").val();
                            content = e.parents(".comments-answer").find("textarea").val();

                        if (!answerid || !content) {
                            commonpop.tip({msg:"请填写评论内容",type:error});
                            return;
                        }
                        var param = {
                            answerId: answerid,
                            context: content
                        };
                        request.get("/Comments/addAComments", param, function(rsp) {
                            rsp = JSON.parse(rsp);
                            if (rsp.code == 1) {
                                commonpop.tip({msg:"评论成功"});
                                var data = {
                                    context:content
                                };

                                if(rsp.data && rsp.data.userName){
                                    data.user_name = rsp.data.userName;
                                }
                                if(rsp.data && rsp.data.userPhoto){
                                    data.user_photo = rsp.data.userPhoto;
                                }

                                //添加进去
                                var tmpl = juicer(funcTpl(index._commentlistTpl),data);
                                var commentlastDom  = e.parents(".comment-list").children(".comments");
                                var commentDom;

                                if(commentlastDom[0]){
                                    commentDom  = $([].pop.call(commentlastDom)); 
                                    commentDom.after(tmpl);
                                }else{
                                    commentDom = $(e.parents(".comments-answer")[0]);
                                    commentDom.before(tmpl);
                                }
                            }
                            if (rsp.code == -1) {
                              pop();
                            }
                        });
                    };


                    //4.回复评论
                    var replycommentHandle = function(){

                    }

                    //5.分享功能
                    var shareHandle = function(){
                        var $config = {
                            url: window.location.href,
                            //url:"www.baidu.com",
                            source: 'www.campussay.com'
                        };
                        commonpop.share($config);
                    }

                    //6.查看更多评论
                    var getMoreComments = function(){
                        var page = e.attr("data-page"),//当前页码
                            answerid = e.attr("data-answerid");

                        if(!isNaN(page)){
                            page ++;
                        }else{
                            return
                        }
                        
                        request.get("/Comments/getCommentsByAnswerId", { answerId: answerid,page:page}, function(rsp){
                            rsp = JSON.parse(rsp);

                            if(rsp.code == 1){
                                var commentDom = e.parents(".comments-more");
                                var tplData = {
                                    list: rsp.data.list,
                                    isInit: 0,
                                    answerid: answerid,
                                    isMore: 0,
                                    totalPage: Math.ceil(rsp.data.rows / 10),
                                };

                                
                                var tmpl = juicer(funcTpl(self._commentTpl), tplData);
                                commentDom.before(tmpl);

                                //如果当前页时最后一页
                                if (tplData.totalPage == page) {
                                    e.parents(".comments-more").remove();
                                }
                            }

                        });
                    };

                    switch (type) {
                        case 'vote':
                            voteHandle();
                            break;
                        case 'commentlist':
                            commentlistHandle();
                            break;
                        case 'share':
                            shareHandle();
                            break;
                        case'comment': 
                            commentHandle();
                            break;
                        case 'reply-comment':
                            replycommentHandle();
                            break;
                        case 'get-moreComment':
                            getMoreComments();
                            break;            
                    }

                });
            },
            _bindAttention:function(){
                //关注问题 
                var follow = function(topicId,thisDom){

                    request.get("/followtopic/addAConcernTopic",{topicId:topicId},function(rsp){

                        rsp = JSON.parse(rsp);
                        if(rsp.code == 1){
                            commonpop.tip({msg:"成功关注"});
                            thisDom.html('已关注')
                                   .attr('data-type','cancle');
                        }else{
                            commonpop.tip({msg:rsp.msg});
                        }

                    });
                };

                //取消关注
                var cancle = function(topicId,thisDom){
                    request.get("/followtopic/cancelConcernTopic",{topicId:topicId},function(rsp){

                        rsp = JSON.parse(rsp);
                        if(rsp.code == 1){
                            commonpop.tip({msg:"取消关注"});
                            thisDom.html('<span style="margin-right:4px;">+</span>关注')
                                   .attr('data-type','follow');
                        }else{
                            commonpop.tip({msg:rsp.msg});
                        }

                    });
                }
                $.clickOnce("#J_attention",function(e){
                    var topicId = this.attr("data-id"),
                        type    = this.attr("data-type");

                    if(!topicId){
                        commonpop.tip({msg:"出错啦，请联系开发",type:"error"});
                        return
                    }
                    switch (type){
                        case 'follow':follow(topicId,$(this));break;
                        case 'cancle':cancle(topicId,$(this));break;
                    }

                });

            },
            //用户回答
            _bindAnswer: function() {

                var answerHandle = function(){
                    var param,
                        self = this,
                        answer = $("#editor").html(),
                        topicid = $("#J_topicid").attr("data-id");
                    if (answer == '' || !topicid) {
                        return }
                    param = {
                        context: answer,
                        topic_id: topicid
                    }
                    request.post('/answer/addAnswer', param, function(rsp) {
                        rsp = JSON.parse(rsp);
                        if (rsp.code == -1) {
                            pop();
                        }
                        if (rsp.code == 1) {
                            commonpop.tip({msg:"回答成功"});
                            $("#editor").html("");
                            //自动跳转到回答页面~ data lastPage
                            setTimeout(function() {
                                var currentPage = pager._getCurrent(); //当前页码
                                var lastPage = rsp.data.lastPage;
                                var pageCount = pager._getPageCount(); //总页数
                                var top = $(document).height() - $(window).height();

                                if (currentPage == lastPage) {
                                    //刷新当前页面
                                    pager._goPage({isJump:true,page:lastPage});
                                } else if (lastPage > pageCount) {
                                    var total = 10 * lastPage;
                                    //重新生成分页
                                    pager.initPage(10,total);
                                    pager._goPage(lastPage);
                                } else {
                                    pager._goPage(lastPage);
                                }
                                $('body').animate({ "scrollTop": top }, 800);

                            });
                        }
                    });
                }

                $.clickOnce("#J_buttonAnswer",answerHandle);
            },
            _getQuestion: function(topicId) {
                var self = this,
                    param = {
                        topic_id: topicId
                    };
                if (!topicId) {
                    Headertip.error("跳转有误", true, 3000);
                    return;
                }
                request.get("/topic/getDetailTopic", param, function(rsp) {
                    var tplData = {},
                        answerDom = $("#J_question"),
                        rsp = JSON.parse(rsp);
                    tplData.data = rsp.data[0];
                    if (rsp.code == 1) {
                        var tmpl = juicer(funcTpl(self._questionTpl), tplData);
                        answerDom.html(tmpl).hide().fadeIn();
                        self._bindAttention();
                    }
                });



            },
            _questionTpl: function() {
                /*
            <h1 id="J_topicid" data-id="${data.id}">${data.tile}</h1>
            <p class="tag-box">
                {@if data.parentName}
                    <span class="tag">${data.parentName}</span>
                {@/if}
                {@if data.talking_type_name}
                    <span class="tag">${data.talking_type_name}</span>
                {@/if}
            </p>
            <p class="detail-question">${data.intro}</p>

            {@if data.isFollow == 0}
                <div class="attention" id="J_attention" data-id=${data.id} data-type='follow'>
                        <span style="margin-right:4px;">+</span>关注
                </div>
            {@else}
                 <div class="attention" id="J_attention" data-id=${data.id} data-type='cancle'>
                        已关注
                </div>
            {@/if}    
            */
            },
            _getAnswer: function(topicId, pageNum) {
                var self = this,
                    param = {
                        topic_id: topicId,
                        page: pageNum
                    };

                if (!topicId) {
                    Headertip.error("跳转有误", true, 3000);
                    return;
                }
                request.get("/answer/getAnswersByTopicId", param, function(rsp) {
                    var tplData = {},
                        answerDom = $("#J_answerlist"),
                        rsp = JSON.parse(rsp);
                    if (rsp.code == 1) {
                        rsp.data.img = window.pub.userimg;
                        var tmpl = juicer(funcTpl(self._answerTpl), rsp.data);
                        answerDom.html(tmpl).hide().fadeIn();
                        //触发翻页初始化
                        $('body').trigger('page:one', [10, rsp.data.rows]);
                    }

                    if(rsp.code == -1){
                        pop();
                    }
                });
            },
            _commentlistTpl:function(){
                /*
                <li class="comments">
                        <div class="commets-user">
                            {@if user_photo}
                                <img src="${user_photo}"/>
                             {@else}
                                <img src="${user_photo|userPhoto}"/>    
                             {@/if}
                        </div>
                        <div class="comments-info">
                            <p><span class="user-name">${user_name}</span> ${context}</p>
                            <div class="comments-handle" style="display:none">
                                <span data-type="reply-comment">回复</span>
                            </div>
                        </div>  
                </li>
                */
            },
            _commentTpl: function() {
                /*
                {@each list as item,index}
                    <li class="comments">
                        <div class="commets-user">
                            {@if item.user_photo}
                                <img src="${item.user_photo}"/>
                             {@else}
                                <img src="${item.user_photo|userPhoto}"/>    
                             {@/if}
                        </div>
                        <div class="comments-info">
                            <p><span class="user-name">${item.user_name}</span> ${item.context}</p>
                            <div class="comments-handle" style="display:none">
                                <span data-type="reply-comment">回复</span>
                            </div>
                        </div>  
                    </li>
                {@/each}

                {@if isMore === 1}
                    <li class="comments-more">
                        <span class="J_commentMore" data-page="1" data-answerid="${answerid}" data-totalpage="${totalPage}" data-type="get-moreComment">查看更多</span>
                    </li>
                {@/if}

                {@if isInit===1}
                    <li class="comments-answer">
                        <textarea placeholder="和他交流一下你对问题的看法吧..."></textarea>
                        <div class="button-row">
                            <a class="botton-comments J_commentAnswer" data-id="${answerid}" data-type="comment">评论</a>
                        </div>
                    </li>
                </li>
                {@/if}
                */
            },
            _answerTpl: function() {
                /*
            {@each list as item,index}
            <li class="answer">
                <div class="answer-left">
                    <a>
                        {@if item.user_photo}
                            <img src="${item.user_photo}"  class="user-img">
                         {@else}
                            <img src="${img}"  class="user-img">
                         {@/if}

                    </a>
                    <div class="user-vote J_vote" data-id="${item.id}" data-type="vote" data-num=${item.islike}>
                        <i class="icon-vote"></i>${item.islike|handleIsLike}
                    </div>
                </div>
                <div class="answer-right">
                    <p class="user-name">${item.user_name}</p>
                    <p class="user-answer">$${item.context}
                        <span>

                        </span>
                    </p>
                    <p class="opera-panel J_answerHandle" data-init="0">
                        {@if !item.commentNum}
                        <span data-type="commentlist" data-id="${item.id}">
                            <i class="icon-comment" data-type="commentlist" data-id="${item.id}"></i>
                            0
                        </span>
                        {@else}
                         <span data-type="commentlist" data-id="${item.id}">
                            <i class="icon-comment" data-type="commentlist" data-id="${item.id}"></i>
                            <span data-type="commentlist" data-id="${item.id}">${item.commentNum}</span>
                        </span>
                        {@/if}
                        <span  data-type="share"><i class="icon-share" data-init="0"></i>分享</span>
                    </p>
                </div>
                <div class="time" data-id="${item.id}">
                    <span>${item.time|handleTime}</span>
                </div>
            </li>
            {@/each}   
            <!--
                {@helper handleTime}
                    function(startTime){
                        var currentTime = Date.parse(new Date()),
                            time = currentTime-startTime,
                            day  = parseInt(time/(1000*60*60*24)),
                            hour = parseInt(time/(1000*60*60)),
                            min  = parseInt(time/(1000*60)),
                            month = parseInt(day/30),
                            year = parseInt(month/12);
                         if(year){
                         return year+"年前"
                         }
                         if(month){
                         return month+"月前"
                         }
                         if(day){
                         return day+"天前"
                         }
                        if(hour){
                            return hour+"小时前"
                        }
                        if(min){
                            return min+"分钟前"
                        }           
                    }
                {@/helper}
                {@helper handleIsLike}
                    function(num){
                        if(typeof num == 'string'){
                            num = parseInt(num);
                        }
                        if(typeof num  !== 'number'){
                            return
                        }
                        var hundred  = parseInt(num/1000);
                        if(hundred == 0){return num};

                        var thousand = (num/1000);
                        if(!thousand.toString().indexOf(".")){
                            return thousand+"k"
                        }
                        else{
                            thousand = thousand.toFixed(1);
                            if(thousand.toString().length < 5){
                                return thousand+"k";
                            }
                        }
                    }
                {@/helper}    
            -->
            */
            },
            getUrlParam: function(name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
                var r = window.location.search.substr(1).match(reg); //匹配目标参数
                if (r != null) return unescape(r[2]);
                return null; //返回参数值
            },
            handleIsLike: function(num) {
                if (typeof num == 'string') {
                    num = parseInt(num);
                }
                if (typeof num !== 'number') {
                    return
                }
                var hundred = parseInt(num / 1000);
                if (hundred == 0) {
                    return num };

                var thousand = (num / 1000);
                if (!thousand.toString().indexOf(".")) {
                    return thousand + "k"
                } else {
                    thousand = thousand.toFixed(1);
                    if (thousand.toString().length < 5) {
                        return thousand + "k";
                    }
                }
            }
        }

        index.init();

    });