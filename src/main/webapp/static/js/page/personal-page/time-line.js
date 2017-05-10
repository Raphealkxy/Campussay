/**
 * Created by zhujing on 2016/1/20.
 *
 */

//����ʱ����
define(["lib/jquery"],function($){
    return {
        //设置时间轴
        setTimeLine : function(){
            //获奖成果
            var awardHeight = $(".wrap-award").height();//获奖成果的高度
            var firstAward = $(".wrap-award li:first-child").height()/2;
            var lastAward = $(".wrap-award li:last-child").height()/2 ;
            $(".line-award").css({"height":(awardHeight-firstAward-lastAward)+'px',"top":firstAward+'px'});
            //社团经验
            var socialheight = $(".social-exp-wrap").height();  //获取容器的高度
            var socialfirst = $(".social-exp-wrap div:first-child ul").height()/2; //获取容器第一个子元素的高度
            var sociallast = $(".social-exp-wrap div:last-child ul").height()/2;
            var paraheight = $(".social-exp-wrap div:last-child p").height();
            var marginBottom = parseInt($(".social-exp-wrap-list").css("marginBottom"));
            var setHeight = {"height":(socialheight-socialfirst-sociallast-paraheight-marginBottom)+'px',"top":socialfirst+'px'};
            $(".line").css(setHeight);
            //工作经验
            var workheight = $(".work-exp-wrap").height();  //获取容器的高度
            var workfirst = $(".work-exp-wrap div:first-child ul").height()/2; //获取容器第一个子元素的高度
            var worklast = $(".work-exp-wrap div:last-child ul").height()/2;
            var paraheightl = $(".work-exp-wrap div:last-child p").height();
            var marginBottoml = parseInt($(".work-exp-wrap-list").css("marginBottom"));

            var setHei = {"height":(workheight-workfirst-worklast-paraheightl-marginBottoml)+'px',"top":workfirst+'px'};
            $(".line-work").css(setHei);
        }
    };
        //var  test= function contentHeight($ele){
    //    var workheight = $ele.height();
    //    var workfirst = $($ele div:first-child ul").height()/2;
    //}


});

