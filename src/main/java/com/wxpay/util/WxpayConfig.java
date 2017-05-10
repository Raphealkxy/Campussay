package com.wxpay.util;


/* *
 *类名：WxpayConfig
 *功能：微信支付基础配置类
 */

public class WxpayConfig {
	
	/**
	 * 微信生成预支付订单访问路径
	 */
	public static final String WX_PREPAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 微信支付成功回调地址
	 */
	public static final String WX_CALLBACK_UTL = "" + "page/wxPayCallback.do";
	/**
	 * 微信openid
	 */
	public static final String WX_APPID = "wx05f641a4f65851a0";
	/**
	 * 微信商户id
	 */
	public static final String WX_MERCHANTID = "1302271701";
	/**
	 * 微信APPSECRET
	 */
	public static final String WX_APPSECRET = "ba446a9c5be90c17077363f84debb1c4";
	
}
