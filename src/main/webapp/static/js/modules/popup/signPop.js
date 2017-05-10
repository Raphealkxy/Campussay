/**
 * "登录/注册" 的弹窗
 * 核心样式和登录注册逻辑，引自弼杰sign-in模块
 * author: 王欣瑜
 * date: 2016/4/9
 */
define(["lib/jquery", "modules/popup", "modules/popup/popTpl", "page/sign-in/sign"], function($, popup, popTpl, sign) {
	var signPop = {
		init: function() {
			signPop.renderStyle();

			var optdef = {
				popup_width: '',
				popup_height: '', 
				popup_header: '',
				popup_body: popTpl(), // 弹窗内容
				click_mask_fire: true,
				callback: sign
			};
			popup.init(optdef);
		},
		// 样式
		renderStyle: function() {
			var style = $('#popStyle').length;
			if (!style) {
				$('head').append('<link rel="stylesheet" href="/static/css/modules/popup.css" id="popStyle" />');
			}
		}
	};

	return signPop.init;
});