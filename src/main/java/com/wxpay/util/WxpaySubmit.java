package com.wxpay.util;

import com.campussay.util.CommonUtil;
import com.campussay.util.HttpRequestTool;
import com.campussay.util.XmlTool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @功能：微信支付取得签名
 * @作者：dinghongxing
 * @文件名：WxSign.java 
 * @包名：com.wxpay.util 
 * @项目名：zhuanquanquan
 * @部门：伏守科技项目开发部
 * @日期：2015年12月28日 下午1:38:43 
 * @版本：V1.0
 */
public class WxpaySubmit {
	/**
	 * 
	 * @描述：调用微信的接口（微信支付）
	 * @作者:丁洪星 
	 * @部门：伏守科技项目开发部
	 * @日期： 2015年12月4日 上午11:58:57 
	 * @版本： V1.0 
	 */
	public static Map<String,Object> callWeChatPay(String strOrderid,
												String strUserip,String strDesc,
													int totalfee,
														String payType) throws Exception {
		Map<String,Object> dataMap=new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date nowDate = new Date();
		Date afterDate = new Date(nowDate .getTime() + 900000);
		String str_afterDate = sdf.format(afterDate);
		String str_nowDate = sdf.format(nowDate);
		
		
		
			dataMap.put("appid", WxpayConfig.WX_APPID);
			dataMap.put("attach",strOrderid);
			dataMap.put("body",strDesc);
			dataMap.put("mch_id",WxpayConfig.WX_MERCHANTID);
			dataMap.put("nonce_str", CommonUtil.GUID().replace("_", ""));//随机字符串
			dataMap.put("notify_url",WxpayConfig.WX_CALLBACK_UTL);
			dataMap.put("time_expire",str_afterDate);
			dataMap.put("time_start",str_nowDate);
			
			dataMap.put("out_trade_no",strOrderid);
			dataMap.put("spbill_create_ip",strUserip);
			dataMap.put("total_fee", totalfee);
			dataMap.put("trade_type", payType);
			String sign = WxSign.getSign(dataMap,WxpayConfig.WX_APPSECRET);
			
			System.out.println("sign:"+sign);
			dataMap.put("sign", sign);
			String param = XmlTool.mapToXml(dataMap);
			//生成预支付的订单
			String path = WxpayConfig.WX_PREPAY_URL;
			//微信的返回结果
			String str = HttpRequestTool.sendPost(path, param);
			System.out.println("微信返回的字符串："+str);
			
			//处理返回的结果
			Map<String,Object> resultMap=XmlTool.xmlElements(str);
			return resultMap;
			
	}
}
