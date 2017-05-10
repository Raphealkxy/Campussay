require.config({
    baseUrl :  CP.STATIC_ROOT
});
require(['lib/jquery','modules/indexBanner','util/request','util/funcTpl','util/Headertip','modules/baseMoudle','modules/switchSchool/schoolIndex','modules/picFadeIn','lib/juicer'],
		function($,banner,request,funcTpl,Headertip,baseMdle,schoolindex,picFadeIn) {
	var index = {
		init:function () {
			//首页大banner
			schoolindex.initSchoolData();

			this._recommendMoudle();
			/*request.get('/talking/checkTalkingStatus',{talkingId:1},function(rsp){
				console.log(rsp);
			});*/ 
		},
		//类目推荐初始化
		_recommendMoudle:function(){
			var self = this;
			request.get('/talking/ShowHomePageNode',function(rsp){
				rsp = JSON.parse(rsp);
				if(rsp.code === 1 ){
					var tplData  		= {},
					    tmpl        		,
					    moudlelistDom 	= $("#J_moudleWrapper"),
					    i        		=  0,
					    len      		=  rsp.data.length;

					tplData.moudleList = rsp.data;
				    tmpl = juicer(funcTpl(self._courseTpl),tplData); 
				    moudlelistDom.append(tmpl);
                    picFadeIn();
                    //图片懒加载
                    index._lazyload();
			    }
			});    
		},
		//图片懒加载：第三栏及以下的图片，1s之后加载
		_lazyload:function(){
			setTimeout(function(){
				$(".lazyload").each(function(index){
					var src = $(this).attr("data-src");
					$(this).attr("src",src);
				});
			},1000);
		},
		//处理返回的函数
		_handleRsp:function(){
			/*var partPeple = function(item){
				if(item && item.talking_now_persion){
					item.talking_now_persion = item.talking_max_persion - item.talking_now_persion
				}
			}

			juicer.register('partPeple', function);*/
		},
		_courseTpl:function(){
			/*
			{@each moudleList as moudle,moudleIndex}
				<div class="class-head">
					<h2 class="class-title">
						${moudle.talking_type_name}
					</h2>
					<a class="moudle-more" target="_blank"  href="/talking/listenList?firstType=${moudle.talking_type_id}&filedName=${moudle.talking_type_name}" >更多>></a>
				</div>
				<div class="moudle clearfix">
					<a href="/talking/listenList?firstType=${moudle.talking_type_id}&&filedName=${moudle.talking_type_name}" class="moudle-img" target="_blank">
        				<img src="${moudle.talking_type_picture}" class="pic1">
        			</a>

					{@each moudle.talkings as item,index}
        					<a href=/talking/classDetail?tk=${item.talking_id} class="moudle-item" target="_blank">
				        		<div class="moudle-item-img">
				        		   	{@if moudleIndex > 2}
				        		   		<img data-src="${item.talking_main_picture}" alt="热门课程" class="pic1 lazyload">
				        		   	{@else}
				        		   		<img src="${item.talking_main_picture}" alt="热门课程" class="pic1">
				        		   	{@/if}
								</div>
								<div class="moudle-item-des">
									<p class="course-itemdes-title">${item.talking_title}</p>
									<p> <span>说友：</span>${item.user_name}</p>
									<div class="row-bottom">
										<div class="pirce">
											<span>¥</span>
											<strong>${item.talking_price}</strong>
									    </div>
									    <div class="part">
									    	 ${item|partPepole}/${item.talking_max_persion}参加
									    </div>
									</div>
								</div>
			        		</a>
	        		{@/each}	
				</div>
	        {@/each}
	       
	       <!--
{@helper partPepole}
    function(item){
    	        var partnum = item.talking_max_persion - item.talking_now_persion;
				return partnum
			}
{@/helper}
-->
			*/
		}
	}
	index.init();
});

