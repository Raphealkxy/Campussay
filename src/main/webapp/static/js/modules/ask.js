/*
FZQ 我要提问弹窗
*/
define(['lib/jquery','modules/topic-pop/popPanel',"util/funcTpl",'util/request','modules/popup/signPop','modules/tip/tip','lib/juicer'],
	function($,popPanel,funcTpl,request,loginpop,tip){
	var index = {
		init:function(){

			//检查用户是否登录
			var isLogin = this._checkLogin();
			var opt = {
			    	width:"760px",
			    	height:"482px",
			    	content:""
			    }
			isLogin.then(this._initContent(opt));    
		},
		_checkLogin:function(){
			var ajax = request.get('/user/isLogin',{},function(rsp){
				rsp = JSON.parse(rsp);

				if(rsp.code != 1){
					window.location.href = '/user/sign-in-web';
				}
			});

			return ajax;
		},
		_initContent:function(opt){
			//获取一级领域
			var filedUrl = "/talkingType/getFirstTalkingType",
				self     = this;

			request.get(filedUrl,function(rsp){
				var tmpl,
					tplData = {};
				rsp = JSON.parse(rsp);

				if(rsp.code == 1){
     	            tmpl = juicer(funcTpl(self._contentTpl),rsp);
     	            opt.content = tmpl;
					popPanel.init(opt);    
					self._bind();
				}else if(rsp.code == -1){
					window.location.href = '/user/sign-in-web?/topic/hot';
				}

			});

		},	
		_bind:function(){
			var self = this;

			var initFiled = function(firstFiled){
				if(!firstFiled)return

					request.get("/talkingType/getSecondTalkingType",{talkingTypeId:firstFiled},function(rsp){
						rsp =JSON.parse(rsp);

						if(rsp.code == 1){
							var tmpl;
							if(rsp.data.length == 0){
								tmpl = "<ul class='filed-select' style='display:none'><li data-type='J_tag'>无</li></ul>";
							}else{
								tmpl = juicer(funcTpl(self._filedTpl),rsp);
							}

     	            		var secondFile = $("#J_sfiledWraper").html(tmpl);
     	            		secondFile.show('slow');
						}
					});
			}

			//1.一级领域，展示
			$("#J_firstfiled").hover(function(){
				$("#J_firstfiledWraper").show();
			},function(){
				$("#J_firstfiledWraper").hide();
			});


			//2.生成二级领域
			$.clickOnce("#J_firstfiledWraper",function(e){
				var firstFiled  = e.attr("data-id");
				var firstFiledName = e.html();
				if(!firstFiled){
					return
				}

				$("#J_firstfiledName").html(firstFiledName);
				initFiled(firstFiled);
			},500);

			var filedId;//二级领域id
			
			//3.生成标签
			$.clickOnce("#J_sfiledWraper",function(e){
				var secondfiledId  = e.attr("data-id"),
					filedName      = e.html();

				filedId = secondfiledId;//存下二级领域
				if(!filedId){return}

				$("#J_sfiledName").html(filedName);	
				$("#J_sfiledWraper").hide('slow');
				this.find("ul").html("");

				$("#J_filedIcon").removeClass("icon-warn-ask").addClass("icon-green-success-ask");
			});

			var time = 0;
			
			//5.检验标题超出字数
			var checkFlag = false
			$("#J_title").on('input',function(){
				setTimeout(function(){
					checkFlag = true
				},1000);

				if(!checkFlag){
					return
				}	
				var title = $(this).val(),
					total = 25,
					tipDom = $("#J_titleTip .row-right"),
					overSize = $("#J_overlenSize"),
					len;

				if(title){
					len = title.length;
				}else{
					return
				}
				if(len > total){
					var over = len - total;
					tipDom.html("<div data-type='input'>已超出<span id='J_overlen'>"+over+"</span>字</div>");
				}else{
					var inputSize = total - len;
					tipDom.html("<div data-type='input'>还可以输入<span id='J_overlen'>"+inputSize+"</span>字</div>");
				}
			});

			//5.发布问题
			var pubFlag  = true;//当前是否可发布
			$("#J_pubquestion").on('click',function(){
				//若上一个提问没有返回，就不发送下一个
				if(!pubFlag){
					return
				}
				var titleDom = $("#J_title"),
					title    = titleDom.val(),
				    filedDom = $("#J_sfiledWraper"),
				    filedid  = filedId,
				    desDom   = $("#J_des"),
				    self     = this,
				    des      = desDom.val();
				if(!title){
					titleDom.attr("placeholder","标题不能为空");
					return;
				}  

				if(title.length > 25){
					$("#J_tip").html("<span style='color:red'>标题超出字数</span>");
					return;
				} 
				if(!des){
					desDom.attr("placeholder","答案不能为空");
					return;
				}
				if(!filedid){
					$("#J_tip").html('若未找到分类请选择‘其他’，我们后续会创建对应的课程分类');
					return;
				}

				var param 	 = {
					title:title,
					intro:des,
					takingTypeId:filedid
				}

				pubFlag = false;//设置当前不可以，发布
				request.get("/topic/AddATopic",param,function(rsp){
					pubFlag = true
					rsp = JSON.parse(rsp);
					if(rsp.code == 1){
						$(self).html("成功发布");
						setTimeout(function(){
							window.location.href="/topic/answerdetail?topicid="+rsp.data.topicId;
						},500);

					}else{
						$("#J_tip").html('<span style="color:red">网络异常，提问失败请重试</span>');
					}
					
				});


			});
		},
        _contentTpl:function(){
        	/*
        	<div id="icon-bird"><img src='/static/img/common/bird-big.png'></div>
        	<div class="ask-pop">
        		<div class="row row-title">
					<div class="row-left"><span class="icon-required">*</span>提问<span class="colon">:</span> </div>
					<div class="row-right">
						<input class="ask-title" id="J_title"/>
					</div>
        		</div>

        		<div class="row row-oversize" id="J_titleTip">
	        		<div class="row-right">
							标题可以输入25字
					</div>
        		</div>

        		<div class="row">
					<div class="row-left">问题描述 <span class="colon">:</span> </div>
					<div class="row-right">
						<textarea  class="ask-detail" id="J_des"></textarea>
					</div>
        		</div>

        		<div class="row">
					<div class="row-left"><span class="icon-required">*</span>提问领域<span class="colon">:</span></div>
					<div class="row-right clearfix">
						<div class="ask-filed" data-type="J_firstfiled" id="J_firstfiled"> 
							<span id="J_firstfiledName"></span>
							<ul class="filed-select" style="display:none" id="J_firstfiledWraper">
								{@each data as item}
									<li data-id=${item.talking_type_id} data-type="J_secondfiled"> ${item.talking_type_name}</li>
								{@/each}  
							</ul>
						</div>
						<div class="ask-filed J_filed ask-second-filed" id="J_sfiled">
							<span id="J_sfiledName"></span>
							<ul class="filed-select" style="display:none;height:auto" id="J_sfiledWraper"></ul>	
						</div>
						
						<div class="ask-tip">
						<span class="icon-warn-ask icon-ask" id="J_filedIcon"></span>
						提问领域越精准，越容易让同学们发现
						</div>
					</div>
        		</div>
				
				<div class="row row-tag">
					<div class="row-right" id="J_tip">
					</div>
				</div>
        		
				<div class="row">
					<div class="row-right row-button">
						<a class="ask-button ask-button-pub"  id="J_pubquestion">发布</a>
						<a class="ask-button ask-button-cancle" data-type="close">取消</a>
					</div>
        		</div>

        	</div>
        	*/
        },
        _filedTpl:function(){
        	/*
					{@each data as item}
						<li data-type="J_tag" data-id=${item.talking_type_id}>${item.talking_type_name}</li>
					{@/each}
        	*/
        }

	}

    return index;
});