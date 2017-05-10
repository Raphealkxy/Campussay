//展示弹框，并且有一个关闭按钮
define(['lib/jquery', "util/funcTpl", 'lib/juicer'], function($, funcTpl) {
    var pop = {
        //初始化 pop
        init: function(options) {

            //如果当前有弹框并且不重新生成弹框
            var popDOm = $("#J_pop")[0];

            if(options && options.isRepaind && $("#J_pop")[0]){
                $(popDOm).fadeIn();
                return;
            }else if(popDOm){
                $(popDOm).remove();
            }

            var css = '<link rel="stylesheet" href="/static/js/modules/tip/tip.css"/>',
                defaultOpt = {
                    width: "200px",
                    height: "200px",
                    content: "xx",
                    type: "ask", //表示不同的弹框类型,主要用来做样式覆盖,默认是ask,order表示订单的弹框,comment-pop表示评价课程弹框
                    bind: {
                        datatype: null //添加点击事件
                    },
                    addevent: null, //fn添加任意dom的任意事件 $(dom).click();
                    reload: false, //是否关闭之后自动刷新,默认false 不刷新 没想好
                    time: null ,//自动销毁时间设置，默认null
                    closeIcon:true//是否有关闭图标，默认有
                },
                tmpl;

            //用户配置，替换默认配置。    
            if (options) {
                $.each(options, function(key, value) {
                    if (defaultOpt[key] || defaultOpt[key] === null || defaultOpt[key] === false) {
                        defaultOpt[key] = value;
                    }
                });
            }

            switch (defaultOpt.type) {
                case 'order':
                    defaultOpt.classHead = 'orderpop-head';
                    break;
                case 'ask':
                    defaultOpt.classHead = 'askpop-head';
                    break;
                case 'switchschool-pop':
                    defaultOpt.title = '立即选择，开启你的校园说';
                    break;
            }

            tmpl = juicer(funcTpl(this.tpl), defaultOpt);
            tmpl = tmpl.replace("##content", defaultOpt.content);

            $('body').append(tmpl).css('display','none').fadeIn();
            $('head').append(css);
            //添加弹窗的绑定事件
            this._bind(defaultOpt);

            //一定时间自动销毁弹窗
            if (defaultOpt.time) {

                setTimeout(function() {

                    $("#J_pop").fadeOut(300,function(){
                        $("#J_pop").remove();
                    });
                    if (defaultOpt.reload) {
                        window.location.reload();
                    }

                }, defaultOpt.time);
            }
        },
        _bind: function(options) {
            var self = this;

            //添加用户自定义事件
            if (options.addevent) {
                    options.addevent();
                };

            $("#J_pop").on("click", function(event) {
               
                //查看的是，target元素
                var type = $(event.target).attr("data-type");
                if (!type) {return }

                switch (type) {
                    case 'close':
                        self.destory();
                        if (options.reload) {
                            window.location.reload();
                        }
                        break;
                }

                //自定义事件
                if (options.bind) {
                    if (options.bind[type]) {
                        options.bind[type]($(event.target));
                    }
                }


            });
        },
        //注销dom
        destory: function() {
            $("#J_pop").fadeOut();
        },
        //3s之后自动销毁弹框
        autoDestory: function(time) {
            var time = time || 3000;

            setTimeout(function() {
                $("#J_pop").fadeOut();

                if (pop.afterDestory) {
                    pop.afterDestory();
                }
            }, time);
        },
        //销毁之后的动作
        afterDestory: null,
        //弹窗模板
        tpl: function() {
            /*
             <div id="J_pop" class=${type}>   
                <div class="popup" style="width:${width};height:${height}">
                    <div class="popup-head ${classHead}">
                        <span>${title}</span>
                        <a class="pop-icon"></a>
                        {@if closeIcon} 
                            <a class="popup-close" data-type="close" href="javascript:;"></a>
                        {@/if}
                    </div>
                    ##content
                </div>
                <div class="popup-mask-layer"></div>
            </div>      
            */
        }
    }

    return pop;

});
