/**
 *  首页大banner轮播图
 *
 *  @author jipeng
 */

define(['lib/jquery','util/request','util/funcTpl', 'modules/picFadeIn','lib/juicer'], function ($,request,funcTpl,picFadeIn) {
	'use strict';
	
	var banner = {
		init:function (schoolid) {
			banner._decorators(schoolid);

		},
		_lazyLoad:function(){
			setTimeout(function(){
				var imglist = $(".common-blist img");

				$.each(imglist,function(index,value){
					var imgSrc = $(imglist[index]).attr("data-src");

					if(imgSrc){
						imglist[index].src = imgSrc;
					}
				});

			},1000);			
		},
		data:null,
		//banner的模版 01
		_tpl :function () {
			/*
			<div class="common-banner">
				<div class="common-blist">
				   {@each imgList as item,index}
				   		{@if index < 1}
							<a href="${item.banner_url}" target="_blank"><img src="${item.banner_picture}" class="pic1"></a>
						{@else}
							<a href="${item.banner_url}" target="_blank"><img data-src="${item.banner_picture}" class="pic1"></a>
						{@/if}
				   {@/each}
				</div>
				<div class="common-slist">
					 {@each imgList as item,index}
						<a {@if index == 0}class="active"{@/if} ></a>
					 {@/each}
				</div>
			</div>
			*/
		},
		_decorators:function (schoolid) {

			request.get('/banner/Bannerindex',{campus_id:schoolid},function (res) {
				var tplData = {};
				res = JSON.parse(res);
				
			    if(1 == res.code){
			    	tplData.imgList = res.data;
			    	banner.data = res.data;
					var tmp = juicer(funcTpl(banner._tpl),tplData);
					$(".main-banner").html(tmp);
                    picFadeIn() ;//等待图片完全加载完再淡出

                    setTimeout(function(){
                    	banner._annimation();
						banner._lazyLoad();
                    },3000);
					//banner._annimation();
					//banner._lazyLoad();
				}
			})
		},
		//开始动画
		_annimation:function () {
		  var commonBanner = $(".common-banner"),
		  	  commonBlist = $(".common-blist"),
		      bImgs = $(".common-blist  img"),//图像集合
			  commonSlist = $(".common-slist"),
		      btns = $(".common-slist > a"),//分页按钮
			  imgWidth = $(bImgs[0]).width(),
			  btnWidth = $(btns[0]).outerWidth()+10,//加10是因为有10px margin
			  size = bImgs.length,//图像个数
			  index = 0;//切换索引
		   if(size == 0){
			   return;
		   }  
	       commonBlist.css({"width":size * imgWidth});
	       commonSlist.css({"width":size * btnWidth},{"marginleft":-(size * btnWidth/2)});
		   commonSlist.css({"marginLeft":-(size * btnWidth/2)});
		   
		   //动画函数
		   var change = function (index) {
			   commonBlist.stop();
			   var left = -(imgWidth * index);
			   $(commonSlist).find(".active").removeClass('active');
			   $(btns[index]).addClass('active');
			   commonBlist.animate({"left":left});

			   //切换颜色
			   var bgcolor = banner.data[index].banner_background;
			   if(bgcolor){
			   	$(".banner").css('background',bgcolor);
			   }
		   }
		   var autoShow = function () {
			   index = (index+1)%size;
			   change(index);
		   }
		   
		    //分页按钮点击
	        btns.click(function(){
	            index = $(this).index();
	            change(index)
	        });
		    //鼠标移入移出
			commonBlist.hover(function(){
				clearInterval(init);
			},function(){
				clearInterval(init);
				init = setInterval(autoShow,3000);
			});
		   
		    var init = setInterval(autoShow,3000);
		   
		},
		
	}
	return banner.init;
})