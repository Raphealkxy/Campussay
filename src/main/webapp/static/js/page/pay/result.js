require.config({
    baseUrl: CP.STATIC_ROOT
});

require(['lib/jquery','util/request', 'util/funcTpl', 'modules/baseMoudle', 'lib/juicer'],
	function($,request,funcTpl,baseMdle){
	
	var index = {
		init:function(){
			//获取课程信息
			this._getTalk();			

		},
		_getTalk:function(){
			var orderid = baseMdle.getUrlParam("out_trade_no");
			var self = this;

			if(orderid){
				request.get("/order/selectOrderTalkingInfo",{orderId:orderid},function(rsp){
					rsp = JSON.parse(rsp);

					if(rsp.code == 1){
						if(rsp.data.talking_start_time){
							 rsp.data.startTime = rsp.data.talking_start_time;
							 baseMdle._handleTalkData(rsp.data);
						}
						var tmpl = juicer(funcTpl(self._talkTpl),rsp);

						$("#J_talk").html(tmpl);
					}
				});
			}
		},
		_talkTpl:function(){
			/*
			<p>${data.talking_title}<span style="margin-left:26px">${data.user_name}</span> </p>
  			<p>交流时间&nbsp;&nbsp;:&nbsp;&nbsp;<span>${data.date}</span>&nbsp;&nbsp;<span>${talk.currentTime}</span></p>
	 	 	<div style="margin-top:12px"><a class="order-button" herf="/personalCenter/order">订单详情</a></div>
			*/
		}
	}

	index.init();
});