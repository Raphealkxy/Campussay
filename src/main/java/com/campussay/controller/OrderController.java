package com.campussay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.exceptions.IbatisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.campussay.exception.MeanwhileCreateTalkingOrderException;
import com.campussay.exception.TalkingFullException;
import com.campussay.model.Order;
import com.campussay.model.PhoneMessage;
import com.campussay.model.User;
import com.campussay.service.OrderService;
import com.campussay.service.TalkingService;
import com.campussay.service.UserService;
import com.campussay.service.impl.OrderServiceImpl;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;
import com.campussay.util.EnumUtil.TalkingStatus;
import com.campussay.util.PhoneSender;
import com.campussay.util.QRCodeUtil;
import com.campussay.util.StringUtil;
import com.campussay.util.XmlTool;
import com.campussay.util.alipay.AlipayConfig;
import com.wxpay.util.WxSign;
import com.wxpay.util.WxpayConfig;
import com.wxpay.util.WxpaySubmit;

import freemarker.core.ParseException;

@Controller
@RequestMapping("/order")
public class OrderController {
	private static Logger log = LoggerFactory.getLogger(OrderController.class);
	/**
	 * 订单已经支付
	 */
	private static final int ORDER_HAD_PAY = 2;
	/**
	 * 订单暂未支付
	 */
	private static final int ORDER_NOT_PAY = 1;
	/**
	 * 支付宝支付方式
	 */
	private static final int ALIPAY_PAY_TYPE = 1;
	/**
	 * 微信支付方式
	 */
	private static final int WXPAY_PAY_TYPE = 2;
	@Autowired
	private OrderService orderService;
	@Autowired
	private TalkingService talkingService;
	@Autowired
	private UserService userService;

	@RequestMapping("test")
	public void test() {
		System.out.print("hello");
	}

	/**
	 * 订单未付款
	 */
	private static final int ORDER_NUT_PAY = 1;// 状态,1-下单未付款

	/**
	 * 根据用户名或者状态获得订单 localhost:8080/order/getOrderByUserID?page=1
	 * @param page 当前页码
	 * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
	 */
	@RequestMapping("/getOrderByUserID")
	@ResponseBody
	public JSONObject getOrderByUserID(HttpServletRequest request, String state, int page) {
		User user = (User) request.getAttribute("user");
		if (user != null) {
			String userID = user.getUserId() + "";
			return orderService.getOrderByUserID(userID, state, page);
		} else {
			return CommonUtil.constructResponse(0, "未登陆", null);
		}
	}
	/**
	 * 根据用户名或者状态获得订单的分页数目 localhost:8080/order/getOrderByUserIDPageTotal&state=1
	 * @param request
	 * @param state
	 * @return
	 */
	@RequestMapping("/getOrderByUserIDPageTotal")
	@ResponseBody
	public JSONObject getOrderByUserIDPageTotal(HttpServletRequest request, String state) {
		User user = (User) request.getAttribute("user");
		if (user != null) {
			String userID = user.getUserId() + "";
			return orderService.getOrderByUserIDPageTotal(userID, state);
		} else {
			return CommonUtil.constructResponse(0, "未登陆", null);
		}
	}
	/**
	 * 根据用户及订单状态获得不同的订单 localhost:8080/order/getOrderByUserIDState?page=1&state=1
	 * @param state 订单状态
	 * @param page 当前页码
	 * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
	 */
	@RequestMapping("/getOrderByUserIDState")
	@ResponseBody
	public JSONObject getOrderByUserIDState(HttpServletRequest request, String state, int page) {
		User user = (User) request.getAttribute("user");
		if (user != null) {
			String userID = user.getUserId() + "";
			return orderService.getOrderByUserIDState(userID, state, page);
		} else {
			return CommonUtil.constructResponse(0, "未登陆", null);
		}
	}
	/**
	 * 获取自己发布的talking售卖详情
	 */
	@RequestMapping("getMyTalkingSaleInfo")
	@ResponseBody
	public JSONObject getMyTalkingSaleInfo(HttpServletRequest request, @RequestParam(required = false) Integer page) {
		User user = (User) request.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(0, "未登陆", null);
		}
		JSONObject jo = new JSONObject();
	    jo.put("rows", orderService.getMyTalkingSaleInfo(user.getUserId(), page, null));
	    int count = OrderServiceImpl.INFORECORDSIZE;
		jo.put("count", count);
	    return CommonUtil.constructResponse(1, "获取信息成功", jo);
	}
	/**
	 * @author liupengyan
	 * @Title: createOrderInfo
	 * @Description: 创建用户订单
	 * @param request
	 * @return: JSONObject
	 */
	@RequestMapping("createOrderInfo")
	@ResponseBody
	public JSONObject createOrderInfo(HttpServletRequest request, HttpSession session) {
		log.debug("进入创建订单");
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(EnumUtil.NOT_LOGIN, "未登陆", null);
		}
		JSONObject jo = new JSONObject();
		// 得到用户填入的电话
		String order_user_tel = (String) request.getParameter("order_user_tel");
		// 得到用户填入的真实姓名
		String order_user_realname = (String) request.getParameter("order_user_realname");
		// 得到用户填入的额外信息
		String order_user_extr_info = (String) request.getParameter("order_user_extr_info");
		// 得到课程id
		String talkingId = (String) request.getParameter("talkingId");
		log.debug("收到前端传入参数===>order_user_tel:" + order_user_tel + "   order_user_realname:" + order_user_realname
				+ "  order_user_extr_info:" + order_user_extr_info + "  talkingId:" + talkingId);
		if (StringUtil.isEmpty(order_user_tel)) {
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "手机号必须填写", null);
		}
		if (StringUtil.isEmpty(order_user_realname)) {
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "姓名必须填写", null);
		}
		if (order_user_extr_info == null) {
			order_user_extr_info = "";
		}
		if (StringUtil.isEmpty(talkingId)) {
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "课程id不能为空", null);
		}
		int i_talkingId = -1;
		try {
			i_talkingId = Integer.parseInt(talkingId);
			// 订单号 去掉“－”
			String orderId = CommonUtil.GUID().replace("_", "");
			// 创建订单对象
			Order createOrder = new Order();
			createOrder.setOrderUserTel(order_user_tel);
			createOrder.setOrderUserRealname(order_user_realname);
			createOrder.setOrderUserExtrInfo(order_user_extr_info);
			createOrder.setOrderUser(user.getUserId());
			createOrder.setOrderTalking(i_talkingId);
			createOrder.setOrderCreatTime(CommonUtil.getSystemTime());
			createOrder.setOrderId(orderId);
			createOrder.setOrderState(ORDER_NUT_PAY);
			log.debug("开始调用服务层创建订单");
			TalkingStatus talkingStatus = orderService.createOrder(createOrder);
			HashMap<String, String> orderInfo = new HashMap<String, String>();
			orderInfo.put("orderId", orderId);
			JSONObject result = null;
			switch (talkingStatus) {
				case CANUSER:
					result = CommonUtil.constructResponse(EnumUtil.OK, "该课程可以下单学习", orderInfo);
//					HashMap<String,Object> phoneMessage=orderService.getTalkingInfo(createOrder.getOrderId());
//					PhoneSender.sends(phoneMessage);  //发送短信给说友，提醒他有人参加他的课程
					break;
				case HADOVERDUE:
					result = CommonUtil.constructResponse(EnumUtil.TALKING_OVERDUE, "该课程已经过期", null);
					break;
				case HADPARTICIPATE:
					result = CommonUtil.constructResponse(EnumUtil.HAD_OWN_TALKING, "已经拥有该课程", null);
					break;
				case HADFULL:
					result = CommonUtil.constructResponse(EnumUtil.TALKING_FULL, "该课程已经爆满", null);
					break;
				case NOTFOUND:
					result = CommonUtil.constructResponse(EnumUtil.TALKING_NOT_FOUND, "没找到该课程", null);
					break;
				case UNKOWNERRRO:
					result = CommonUtil.constructResponse(EnumUtil.UNKOWN_ERROR, "未知错误", null);
					break;
				case SYSTEMERROR:
					result = CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
					break;
				default:
					result = CommonUtil.constructResponse(EnumUtil.UNKOWN_ERROR, "未知错误", null);
					break;
			}
			return result;
		} catch (TalkingFullException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return CommonUtil.constructResponse(EnumUtil.TALKING_FULL, "该课程已经爆满", null);
		} catch (MeanwhileCreateTalkingOrderException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_CREATE_ORDER_MEANWHILE, "同一用户同一产品不能同时创建订单", null);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	/**
	 * @Title: gotoPay
	 * @Description: 支付宝支付接口
	 * @param request
	 * @param orderid
	 * @return
	 * @return: String
	 */
	@ResponseBody
	@RequestMapping("gotoAliPay")
	public JSONObject gotoPay(HttpServletRequest request, String orderid) {
		
		if (orderid == null) {
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "订单号不能为空", null);
		}
		try {
			// 从数据库得到订单信息
			HashMap<String, Object> order = orderService.selectOrderByOrderId(orderid);
			Properties prop = new Properties();
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("alipay.properties");
			String ali_returnURL = null;
			String ali_notifyURL = null;
			String ali_partner = null;
			String ali_input_charset = null;
			String ali_seller_email = null;
			try {
				prop.load(in);
				ali_returnURL = prop.getProperty("ali_returnURL").trim();
				ali_notifyURL = prop.getProperty("ali_notifyURL").trim();
				ali_partner = prop.getProperty("ali_partner").trim();
				ali_input_charset = prop.getProperty("ali_input_charset").trim();
				ali_seller_email = prop.getProperty("ali_seller_email").trim();
			} catch (IOException e) {
				e.printStackTrace();
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "配置文件不存在", null);
			}
			if (order == null) {
				// 订单信息为空
				return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_FOUND, "该订单不存在", null);
			} else {
				double order_price;
				try {
					order_price = (double) order.get("order_price");
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
					return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL, "该订单异常", null);
				}
				Integer integer_order_talking = (Integer) order.get("order_talking");
				if (integer_order_talking == null) {
					return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL, "该订单异常", null);
				}
				Map<String, Object> talkingInfo = talkingService.gettalkingdetails(integer_order_talking.intValue());
				if (talkingInfo == null) {
					return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL, "该订单异常", null);
				}
				String talking_title = (String) talkingInfo.get("talking_title");
				if (talking_title == null) {
					return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL, "该订单异常", null);
				}
				// 支付类型
				String payment_type = "1";
				// 把请求参数打包成数组
				Map<String, String> sParaTemp = new HashMap<String, String>();
				sParaTemp.put("service", "create_direct_pay_by_user");
				sParaTemp.put("partner", ali_partner);
				sParaTemp.put("seller_email", ali_seller_email);
				sParaTemp.put("_input_charset", ali_input_charset);
				sParaTemp.put("payment_type", payment_type);
				sParaTemp.put("notify_url", ali_notifyURL);
				sParaTemp.put("return_url", ali_returnURL);
				sParaTemp.put("out_trade_no", orderid);
				sParaTemp.put("subject", talking_title);
				sParaTemp.put("total_fee", order_price + "");
				sParaTemp.put("body", "body");
				sParaTemp.put("it_b_pay", "15m");
				sParaTemp.put("show_url", "show_url");
				sParaTemp.put("anti_phishing_key", "");
				sParaTemp.put("exter_invoke_ip", CommonUtil.getIpAddr(request));
				// 建立请求
				String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
				HashMap<String, String> dataJson = new HashMap<String, String>();
				dataJson.put("sHtmlText", sHtmlText);
				return CommonUtil.constructResponse(EnumUtil.OK, "成功调用支付宝", dataJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	/**
	 * @Title: callWeChatPay
	 * @Description: 微信支付接口
	 * @param request
	 * @param orderid
	 * @return
	 * @throws Exception
	 * @return: JSONObject
	 */
	@ResponseBody
	@RequestMapping("gotoWxPay")
	public JSONObject callWeChatPay(HttpServletRequest request, String orderid) throws Exception {
		if (orderid == null) {
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "订单号不能为空", null);
		}
		// 从数据库得到订单信息
		HashMap<String, Object> order = orderService.selectOrderByOrderId(orderid);
		if (order == null) {
			// 订单信息为空
			return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_FOUND, "该订单不存在", null);
		} else {
			double order_price;
			try {
				order_price = (double) order.get("order_price");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL, "该订单异常", null);
			}
			Integer integer_order_talking = (Integer) order.get("order_talking");
			if (integer_order_talking == null) {
				return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL, "该订单异常", null);
			}
			Map<String, Object> talkingInfo = talkingService.gettalkingdetails(integer_order_talking.intValue());
			if (talkingInfo == null) {
				return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL, "该订单异常", null);
			}
			String talking_title = (String) talkingInfo.get("talking_title");
			if (talking_title == null) {
				return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL, "该订单异常", null);
			}
			int amount = (int) (order_price * 100);
			Map<String, Object> map = WxpaySubmit.callWeChatPay(orderid, CommonUtil.getIpAddr(request), talking_title,
					amount, "NATIVE");
			String return_code = (String) map.get("return_code");
			if ("SUCCESS".equals(return_code)) {
				// 生成预支付订单成功 的到二维码地址
				String code_url = (String) map.get("code_url");
				// 成功
				Map<String, Object> returnMap = new HashMap<String, Object>();
				returnMap.put("code_url", "data:image/png;base64," + QRCodeUtil.getPayQRCode(code_url));
				return CommonUtil.constructResponse(EnumUtil.OK, "微信支付准备就绪", returnMap);
			} else {
				// 生成预支付订单失败
				if ("OUT_TRADE_NO_USED".equals(return_code)) {
					// 商户订单号重复
					return CommonUtil.constructResponse(EnumUtil.ORDER_NUM_REPEAT, "商户订单号重复", null);
				} else if ("ORDERPAID".equals(return_code)) {
					// 该订单已经支付过
					return CommonUtil.constructResponse(EnumUtil.ORDER_HAD_PAY, "该订单已经支付过", null);
				} else if ("SYSTEMERROR".equals(return_code)) {
					// 系统错误
					return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_FOUND, "订单不存在，请联系平台", null);
				} else {
					return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
				}
			}
		}
	}
	/**
	 * 支付宝支付后回调
	 * @Title: aliPayCallback
	 * @Description: TODO
	 * @param request
	 * @return
	 * @return: String
	 */
	@RequestMapping("aliPayCallback")
	public String aliPayCallback(HttpServletRequest request) {
		System.out.println("支付宝支付回调了1");
		Map<String, String> params = new HashMap<String, String>();
		System.out.println("支付宝支付回调了2");
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		System.out.println("支付宝支付回调了3");
		// 商户订单号
		String strOrderid = request.getParameter("out_trade_no");
		// 支付宝交易号
		String strTradeNo = request.getParameter("trade_no");
		// 交易状态
		String strTradeStatus = request.getParameter("trade_status");
		// 商户号
		String seller_id = request.getParameter("seller_id");
		// 商户号
		String total_fee = request.getParameter("total_fee");
		
		String sigin_type = request.getParameter("sigin_type");
		
		System.out.println("支付宝支付回调了4");
		System.out.println("out_trade_no||strOrderid:"+strOrderid);
		//暂时不验证
		//if (AlipayNotify.verify(params,sigin_type)) {
			// 验证成功
			if ("TRADE_SUCCESS".equals(strTradeStatus) || "TRADE_FINISHED".equals(strTradeStatus)) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				// 如果有做过处理，不执行商户的业务程序
				// 注意：
				// 退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				// 付款成功，订单状态进行修改
				try {
					Map<String, Object> queryOrder = orderService.selectOrderByOrderId(strOrderid);
					if (queryOrder == null) {
						// 订单不存在
						System.out.println("订单不存在=====>>>>" );
						return "fail";
					} else {
						// 只要状态不是未付款 我们认为就是已经付款
						Integer interger_order_stat=(Integer) queryOrder.get("order_state");
						if(interger_order_stat==null){
							
							System.out.println("订单状态值为null 表示未支付过");

							if (!AlipayConfig.partner.equals(seller_id)) {
								// 请求时的商户号和通知时的商户号不一致
								System.out.println("请求时的商户号和通知时的商户号不一致=====>>>>" );
								return "fail";
							} else {
								// 判断金额
								int iTotalfee = (int) (Double.parseDouble(total_fee) * 100);
								System.out.println("传回来的额金额值乘以100=====>>>>" + iTotalfee);
								int iAmount = (int) ((double) queryOrder.get("order_price") * 100);
								System.out.println("订单的金额值乘以100=====>>>>" + iAmount);
								if (iTotalfee == iAmount) {
									// 两次的金额相等
									orderService.paySuccess(strOrderid, strTradeNo, ALIPAY_PAY_TYPE);
									System.out.println("支付宝要调用短信接口了");
									sendSMS(queryOrder);
									return "success";
								} else {
									// 两次金额不相等
									System.out.println("两次金额不相等=====>>>>" );
									return "fail";
								}
							}
						}else{
							if (ORDER_NOT_PAY != (int)interger_order_stat ) {
								// 支付成功，说明已经回调过
								System.out.println("已经支付过了=====>>>>" );
								return "success";
							} else {
								if (!AlipayConfig.partner.equals(seller_id)) {
									// 请求时的商户号和通知时的商户号不一致
									System.out.println("请求时的商户号和通知时的商户号不一致=====>>>>" );
									return "fail";
								} else {
									// 判断金额
									int iTotalfee = (int) (Double.parseDouble(total_fee) * 100);
									System.out.println("传回来的额金额值乘以100=====>>>>" + iTotalfee);
									int iAmount = (int) ((double) queryOrder.get("order_price") * 100);
									System.out.println("订单的金额值乘以100=====>>>>" + iAmount);
									if (iTotalfee == iAmount) {
										// 两次的金额相等
										orderService.paySuccess(strOrderid, strTradeNo, ALIPAY_PAY_TYPE);
										System.out.println(queryOrder);
										sendSMS(queryOrder);
										return "success";
									} else {
										// 两次金额不相等
										System.out.println("两次金额不相等=====>>>>" );
										return "fail";
									}
								}
							}
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "fail";
				}
			} else {
				System.out.println("支付结果问题=====>>>>"+strTradeStatus );
				return "success";
			}
			//
//		} else {
//			System.out.println("验证失败=====>>>>" );
//			// 验证失败
//			return "fail";
//		}
	}
	/**
	 * @Title: wxPayCallback
	 * @Description: 微信支付后回调
	 * @param request
	 * @param response
	 * @return: void
	 */
	@RequestMapping("wxPayCallback")
	public void wxPayCallback(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("微信支付回调了");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			String str = sb.toString();
			Map<String, Object> map = XmlTool.xmlElements(str);
			String sign = (String) map.get("sign");
			map.remove("sign");
			String ownSign = WxSign.getSign(map, WxpayConfig.WX_APPSECRET);
			if (sign.equals(ownSign)) {
				// 商户订单号
				String out_trade_no = (String) map.get("out_trade_no");
				// 微信支付订单号
				String transaction_id = (String) map.get("transaction_id");
				// 支付结果
				String return_code = (String) map.get("return_code");
				// 支付金额
				String total_fee = (String) map.get("total_fee");
				// 判断签名及结果
				if ("SUCCESS".equals(return_code)) {
					// 付款成功，订单状态进行修改
					try {
						Map<String, Object> queryOrder = orderService.selectOrderByOrderId(out_trade_no);
						if (queryOrder == null) {
							// 订单不存在
							returnMap.put("return_code", "FAIL");
						} else {
							// 只要状态不是未付款 我们认为就是已经付款
							if (ORDER_NOT_PAY != (int) queryOrder.get("order_state")) {
								// 支付成功，说明已经回调过
								returnMap.put("return_code", "SUCCESS");
							} else {
								// 判断金额
								int iTotalfee = Integer.parseInt(total_fee);
								System.out.println("传回来的额金额值=====>>>>" + iTotalfee);
								int iAmount = (int) ((double) queryOrder.get("order_price") * 100);
								System.out.println("订单的金额值=====>>>>" + iAmount);
								if (iAmount == iTotalfee) {
									// 两次金额相等
									orderService.paySuccess(out_trade_no, transaction_id, WXPAY_PAY_TYPE);
									System.out.println("要调用短信借口了");
									//xx参加了你的xx课程
									sendSMS(queryOrder);
									returnMap.put("return_code", "SUCCESS");
								} else {
									// 两次金额不相等
									returnMap.put("return_code", "FAIL");
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						returnMap.put("return_code", "FAIL");
					}
				} else {
					// 支付失败
					returnMap.put("return_code", "SUCCESS");
				}
			} else {
				returnMap.put("return_code", "FAIL");
			}
		} catch (Exception e) {
			returnMap.put("return_code", "FAIL");
		}
		String xmlResult = XmlTool.mapToXml(returnMap);
		System.out.println("给微信的通知====》》》》" + xmlResult);
		try {
			response.getWriter().print(xmlResult);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @Title: sendSMS 
	 * @Description: xx参加了你的xx课程
	 * @param queryOrder 订单信息
	 * @return: void
	 */
	private void sendSMS(Map<String, Object> queryOrder){
		//xx参加了你的xx课程
		try {
			
			if(queryOrder != null){
			Integer order_user=	(Integer)queryOrder.get("order_user");
			Integer order_talking =	(Integer)queryOrder.get("order_talking");
			   if(order_user!=null&&order_talking!=null){
					
				    Map talkingDetail =    talkingService.getTalkingInfoAndPhoneNUM(order_talking);
				   if(talkingDetail!=null){
					   
					String talking_title =  (String) talkingDetail.get("talking_title");
					if(talking_title==null){
						talking_title="";
					}
					String user_phone =  (String) talkingDetail.get("user_phone");
					if(user_phone==null){
						 throw new Exception("短信发送失败，课程说客手机号空");
					}else{
						Map userInfoDetail = userService.getBasicInfoByUserId(order_user);
						  if(userInfoDetail!=null){
								String user_name =  (String) userInfoDetail.get("user_name");
								if(user_name==null){
									user_name="";
								}
								//TODO 发送短消息告诉用户 xx参加了你的xx课程
								
								Map<String,Object> userinfo=getSendMsg((int)queryOrder.get("order_talking"),(int)queryOrder.get("order_user"));
							  	PhoneSender.sends(userinfo);
						  }
					}
				   }else{
					   throw new Exception("短信发送失败，订单课程信息异常");
				   }
			    }else{
			    	 throw new Exception("短信发送失败，订单用户或者课程id异常");
			    }
			}else{
				 throw new Exception("短信发送失败，订单不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//此处及时发送短信失败 也应该让程序继续执行
		}
	}
	
	/*
	*Order中订单回复界面
	*@param orderTalking  话题id
	*@param Json   order_id  主字段
	* order_user_tel 回复电话
	* order_user_realname   回复名字
	* order_user_extr_info   留言
	* order_talking     话题id
	* pzy
	* */
	@RequestMapping("OrderTalking")
	@ResponseBody
	public JSONObject OrderTalking(HttpSession session, int orderTalking) {
		/*
		 * User user= (User) session.getAttribute("user"); if(user!=null){ return
		 * CommonUtil.constructResponse(0,"请登录",null); }
		 */
		return CommonUtil.constructResponse(1, null, orderService.OrderTalking(orderTalking));
	}
	/**
	 * @Title: selectUserOrderByStatus
	 * @Description: 分页查询用户的各种状态下的订单
	 * @param session
	 * @param state 查询订单状态
	 * @param pageNum 查询第几页
	 * @return
	 * @return: JSONObject
	 */
	@RequestMapping("selectUserOrderByStatus")
	@ResponseBody
	public JSONObject selectUserOrderByStatus(HttpSession session, Integer state, Integer pageNum) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(EnumUtil.NOT_LOGIN, "请登录", null);
		}
		int userId = user.getUserId();
		if (pageNum == null) {
			pageNum = new Integer(1);
		}
		int i_pageNum = pageNum;
		i_pageNum -= 1;
		if (i_pageNum < 0) {
			i_pageNum = 0;
		}
		int start = i_pageNum * EnumUtil.PAGE_SIZE;
		try {
			Map result = orderService.selectUserOrderByStatus(userId + "", state, start, EnumUtil.PAGE_SIZE);
			
			if (result == null) {
				return CommonUtil.constructResponse(EnumUtil.NO_DATA, "暂无数据", null);
			} else {
				return CommonUtil.constructResponse(EnumUtil.OK, "查询成功", result);
			}
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	/**
	 * @Title: confirmTakingFinish
	 * @Description: 用户确认课程完成
	 * @param session
	 * @param orderId
	 * @return
	 * @return: JSONObject
	 */
	@RequestMapping("confirmTakingFinish")
	@ResponseBody
	public JSONObject confirmTakingFinish(HttpSession session, String orderId) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(EnumUtil.NOT_LOGIN, "请登录", null);
		}
		if (orderId == null || "".equals(orderId.trim())) {
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "orderId必须填写", null);
		}
		int userId = user.getUserId();
		try {
			int confirmResult = orderService.confirmTakingFinish(orderId, userId + "");
			if (confirmResult == EnumUtil.OK) {
				return CommonUtil.constructResponse(confirmResult, "确认成功", null);
			} else if (confirmResult == EnumUtil.ORDER_NOT_FOUND) {
				return CommonUtil.constructResponse(confirmResult, "订单不存在", null);
			} else if (confirmResult == EnumUtil.ORDER_NOT_NORMAL) {
				return CommonUtil.constructResponse(confirmResult, "订单金额异常", null);
			} else if (confirmResult == EnumUtil.ORDER_STATE_NOT_RIGHTL) {
				return CommonUtil.constructResponse(confirmResult, "订单状态错误", null);
			} else if (confirmResult == EnumUtil.ORDER_USER_NOT_NOW_USER) {
				return CommonUtil.constructResponse(confirmResult, "订单用户不是当前操作用户", null);
			} else {
				return CommonUtil.constructResponse(EnumUtil.UNKOWN_ERROR, "未知错误", null);
			}
		} catch (ClassCastException e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		} catch (ParseException e) {
			return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL, "订单金额异常", null);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	/**
	 * @Title: requestRefund
	 * @Description: 用户申请退款
	 * @param session
	 * @param orderId
	 * @return
	 * @return: JSONObject
	 */
	@RequestMapping("requestRefund")
	@ResponseBody
	public JSONObject requestRefund(HttpSession session, String orderId, String reason) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(EnumUtil.NOT_LOGIN, "请登录", null);
		}
		if (orderId == null || "".equals(orderId.trim())) {
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "orderId必须填写", null);
		}
		if (reason == null || "".equals(reason.trim())) {
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "reason必须填写", null);
		}
		if (reason.length() > 255) {
			return CommonUtil.constructResponse(EnumUtil.TOO_LANG, "原因字数不能大于255", null);
		}
		int userId = user.getUserId();
		try {
			int confirmResult = orderService.requestRefund(orderId, userId + "", reason);
			if (confirmResult == EnumUtil.OK) {
				return CommonUtil.constructResponse(confirmResult, "退款成功", null);
			} else if (confirmResult == EnumUtil.ORDER_NOT_FOUND) {
				return CommonUtil.constructResponse(confirmResult, "订单不存在", null);
			} else if (confirmResult == EnumUtil.ORDER_NOT_NORMAL) {
				return CommonUtil.constructResponse(confirmResult, "订单金额异常", null);
			} else if (confirmResult == EnumUtil.ORDER_STATE_NOT_RIGHTL) {
				return CommonUtil.constructResponse(confirmResult, "订单状态错误", null);
			} else if (confirmResult == EnumUtil.ORDER_USER_NOT_NOW_USER) {
				return CommonUtil.constructResponse(confirmResult, "订单用户不是当前操作用户", null);
			} else {
				return CommonUtil.constructResponse(EnumUtil.UNKOWN_ERROR, "未知错误", null);
			}
		} catch (ClassCastException e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		} catch (ParseException e) {
			return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL, "订单金额异常", null);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}


	/*
	* 查看付款失败原因
	* @param orderTalking 话题id
	* @result order_close_reason 失败原因
	* */
	@RequestMapping("OrderReson")
	@ResponseBody
    public JSONObject OrderReson(HttpSession session,int orderTalking){
		/*	User user= (User) session.getAttribute("user");
		if(user!=null){
			return  CommonUtil.constructResponse(0,"请登录",null);
		}*/
		return CommonUtil.constructResponse(1,null,orderService.OrderReason(orderTalking));

	}
	/**
	 * 
	 * @Title: selectOrderTitleAndUserName 
	 * @Description: 根据订单id查询课程信息
	 * @param session
	 * @param orderId
	 * @return
	 * @return: JSONObject
	 */
	@RequestMapping("selectOrderTalkingInfo")
	@ResponseBody
	public JSONObject selectOrderTitleAndUserName(HttpSession session,String orderId){
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(EnumUtil.NOT_LOGIN, "请登录", null);
		}
		if (orderId == null || "".equals(orderId.trim())) {
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "orderId必须填写", null);
		}
		try {
			
			Integer userid = user.getUserId() ;
			if(userid==null){
				return CommonUtil.constructResponse(EnumUtil.NOT_LOGIN, "请登录", null);
			}
			
			HashMap  ordertalkdinginfo =  orderService.selectOrderTitleAndUserName(orderId);
			
			
			if(ordertalkdinginfo == null){
				return CommonUtil.constructResponse(EnumUtil.ORDER_NOT_FOUND, "订单不存在", null);
			}else{
				
			Integer user_ID = (Integer)	ordertalkdinginfo.get("user_id");
				if(user_ID == null){
					return CommonUtil.constructResponse( EnumUtil.ORDER_NOT_NORMAL, "订单异常", null);
				}
				
				
				if(user_ID.toString().equals(userid.toString())){
					return CommonUtil.constructResponse(EnumUtil.OK, "查询成功", ordertalkdinginfo);
				}else{
					return CommonUtil.constructResponse( EnumUtil.ORDER_USER_NOT_NOW_USER, "订单用户不是当前操作用户", null);
				}
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
		
	}

	/*
	* 根据课程号查询报名用户id
	*@param int orderTalking   talkingId
	*@result
	* */
	@RequestMapping("getUserLine")
	@ResponseBody
	public JSONObject getUserLine(HttpSession session,Integer orderTalking){
		/*User user= (User) session.getAttribute("user");
		if(user==null){
			return CommonUtil.constructResponse(0,"请登录",null);
		}*/
		return CommonUtil.constructResponse(1,null,orderService.mapTalkingLists(orderTalking));
	}
	
	/*
	* 获取用户已买课程
	* */
	@RequestMapping("BuyOrderSpend")
	@ResponseBody
	public JSONObject BuyOrderSpend(HttpSession session){
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(EnumUtil.NOT_LOGIN, "请登录", null);
		}
		int order_user=user.getUserId();
		List<HashMap<String, Object>> list=new ArrayList();
		try{
			list=orderService.BuyOrderSpend(order_user);
		}catch (Exception e) {
			throw e;
		}
		return CommonUtil.constructResponse(1,null,list);
	}
	
	/*
	* 获取用户已买课程
	* */
	@RequestMapping("cashrecord")
	@ResponseBody
	public JSONObject cashrecord(HttpSession session){
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(EnumUtil.NOT_LOGIN, "请登录", null);
		}
		int order_user=user.getUserId();
		List<HashMap<String, Object>> list=new ArrayList();
		try{
			list=orderService.cashrecord(order_user);
		}catch (Exception e) {
			throw e;
		}
		return CommonUtil.constructResponse(1,null,list);
	}
	
	
	/*
	 * 有人购买订单成功之后，想说友发送短信，发送短信的内容
	 */
	public Map<String,Object> getSendMsg(int order_talking,int order_user){
		Map mp=new HashMap<String,Object>();
	    mp=talkingService.getUserTalkingInfo(order_talking);
		String user_name=(String) userService.getBasicInfoByUserId(order_user).get("user_name");
		mp.put("user_name", user_name);
		return mp;
	}
	
}
