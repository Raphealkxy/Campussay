package com.campussay.service;

public interface OrderQuartzService {

	public void UpdateNotPayOrders(String order_id,Integer order_talking_id);

	public void UpdateNotPayOrder();

    //大牛收到短信，客户支付成功   请求参数：orderId 对应数据库order_id
	public void SuccessPayOrder(String orderId);

	//退款成功  请求参数：orderId 对应数据库order_id
	public void RefundSuccess(String orderId);

	/*
	* 退款失败
	* 请求参数：orderId 对应数据库order_id
	* */
	public void RefundFail(String orderId);

	/*
	* 关闭订单
	* */
	public void CloseOrder();
}
