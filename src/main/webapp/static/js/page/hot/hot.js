/**
 * Created by liangbijie on 2016/1/24.
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery', 'util/request','page/hot/hot-list','util/funcTpl','lib/juicer','modules/baseMoudle'], function ($, request,hot_list,funcTpl) {
  var hot = {
      init:function(){
        hot.InitList();
        hot.prosecute_btn();
        hot.nav();
        hot._getPersonal(0);
          hot.navHover();
      },
      //初始化列表
      InitList:function(){
          var num =1;
          var $hot_ul = $('.hot-ul');
          hot_list.init($hot_ul,num,hot.prosecute);
      },
      hotInfo:{
        _this_prosecute:null
      },
      navHover:function(){
          $('.ht-common-switch li').on('click',function(){
              $(this).addClass('switch-on').siblings().removeClass('switch-on');
          })
      },
      //举报弹窗按钮
      prosecute:function(){
          $('.prosecute').on('click',function(){
              hot.hotInfo._this_prosecute = this;
              if($(this).attr('class')=='prosecute'){
                  $('.MsgBack').fadeIn('fast');
                  $('.alert').fadeIn('fast');
              }
          })
      },
      //举报框 举报和取消按钮
      prosecute_btn:function(){
          $('.report').on('click',function(){
              $(hot.hotInfo._this_prosecute).attr('class','prosecuted').html('已举报');
              close();
          });
          $('.close').on('click',function(){
              close();
          });
        var close = function(){
                $('.MsgBack').fadeOut();
                $('.alert').fadeOut();
        }
      },
      //个人信息
      //一级导航栏（领域动态和等你来答）
      nav:function(){
         var nav = $('.content-nav').find('li');
        $(nav).on('click',function(){
            //让选中的导航栏处于被激活状态
            $(this).children().addClass('nav-active')
                .parent().siblings().find('a').removeClass('nav-active');
            //判断导航栏第一个是否为激活状态，激活状态则显示响应的内容
            //反之隐藏
            if($($(nav)[0].firstChild).attr('class')=='nav-active'){
                $('.hot-ul').fadeIn('fast');
                $('.answer').css('display','none');
                $('#page').fadeIn('fast');//显示页码跳转条
            }else{
                $('.hot-ul').css('display','none');
                $('.answer').fadeIn('fast');
                $('#page').css('display','none');//隐藏页码跳转条
            }
        })
      },
      //等你来答（开发中）
      //个人页面
      _templatePersonal : function () {
          /*
           <div class="person-info-content clearfix">
           <div class="person-name">
           <a href="/topic/htIndex?user=0"><img src="${user_photo}" alt=""/></a>
           <div >
           <h3><a href="/topic/htIndex?user=0">${user_name}</a></h3>
           <ul>
           <li>${user_campus_name}</li>
           <li>${academy}</li>
           </ul>
           </div>
           </div>
           <dl class="good-skill">
           <dt>擅长领域 : </dt>
           {@each skillsName as item}
           <dd><span><strong>${item.talking_type_name}</strong></span></dd>
           {@/each}
           </dl>
           <dl class="attention">
           <dt>关注领域 : </dt>
           {@each concernName as item}
           <dd><span><strong>${item.talking_type_name}</strong></span></dd>
           {@/each}
           </dl>
           </div>
           <ul class="person-num clearfix">
           <li><a href="/topic/htIndex?user=0&type=1"><span>${answerNum}</span>回答</a></li>
           <li><a href="/personalCenter/publish" ><span>${talkingNum}</span>Talking</a></li>
           <li><a href="/personalCenter/circle?type=1" ><span>${attentionNum}</span>粉丝</a></li>
           </ul>
           */
      },
      //获取个人面板的信息
      _getPersonal : function (userId){
          hot._commonRender(hot._API.getDetailMsg,{userId:userId},".person-info",funcTpl(hot._templatePersonal));
          //request.get(index._API.getDetailMsg,{userId:userId}, function (res) {
          //
          //});
          //$('body').trigger('page:set',[10,count]);
      },
      _API :{
          getDetailMsg: "/topic/getUserDetailMsgById"
      },
      _commonRender: function (url,param,dom,tpl,fn) {
          request.post(url,param, function (res) {
              res = JSON.parse(res)||eval(res);
              if(1 == res.code){
                  var htmlT = juicer(tpl,res.data);
                  $(dom).html(htmlT);
                  fn&&fn(res.data);
                  return res.data.size;
              }
          });
      }
  };
    hot.init();
    return hot;
});
