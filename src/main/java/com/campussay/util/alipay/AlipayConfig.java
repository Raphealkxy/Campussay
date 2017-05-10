package com.campussay.util.alipay;

import com.campussay.util.PropertiesUtils;

/* *
 *author : wlc
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = PropertiesUtils.getProp("ali_partner");
	
	// 收款支付宝账号
	public static String seller_email = PropertiesUtils.getProp("ali_seller_email");
	// 商户的私钥
	public static String key = PropertiesUtils.getProp("ali_key");

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = PropertiesUtils.getProp("ali_log_path");

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = PropertiesUtils.getProp("ali_input_charset");
	
	// 签名方式 不需修改
	public static String sign_type = PropertiesUtils.getProp("ali_sign_type");

}
