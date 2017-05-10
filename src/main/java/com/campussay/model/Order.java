package com.campussay.model;

import java.util.Date;

/**
 * 订单
 * Created by wangwenxiang on 15-12-3.
 */
public class Order {
    private String orderId;
    private int orderUser;//用户
    private int orderTalking;//说文
    private double orderPrice;//价格，因为用户购买后说文可能会改变，所以这里记录一下该说文的价格
    private Date orderCreatTime;//订单生成时间
    private Date orderPayTime;//用户付款时间，用户奖钱打入我们的账户中
    private Date orderConfirmTime;//订单确认付款时间，确认付款后将钱打入说者账户
    private int orderPayWays;//第三方支付方法，1-支付宝，2-微信支付
    private String orderPayCode;//第三方支付平台的订单编号
    private int orderState;//状态
    
    //刘鹏彦增加字段开始  
    //增加日期：2016年2月23日 17:01
    //增加了用户下单时留下的手机号 姓名 和 留言 信息
    private String orderUserTel;//用户留下的手机号
    private String orderUserRealname;//用户留下的真实姓名
    private String orderUserExtrInfo;//用户额外的留言信息
    
    private Date order_end_time;//订单必须完成时间（值就是课程结束时间）作用：在这个时间段后 状态为2的全部变为3
    //刘鹏彦增加字段结束

    

   
    public int getOrderUser() {
        return orderUser;
    }

    public Date getOrder_end_time() {
		return order_end_time;
	}

	public void setOrder_end_time(Date order_end_time) {
		this.order_end_time = order_end_time;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setOrderUser(int orderUser) {
        this.orderUser = orderUser;
    }

    public int getOrderTalking() {
        return orderTalking;
    }

    public void setOrderTalking(int orderTalking) {
        this.orderTalking = orderTalking;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Date getOrderCreatTime() {
        return orderCreatTime;
    }

    public void setOrderCreatTime(Date orderCreatTime) {
        this.orderCreatTime = orderCreatTime;
    }

    public Date getOrderPayTime() {
        return orderPayTime;
    }

    public void setOrderPayTime(Date orderPayTime) {
        this.orderPayTime = orderPayTime;
    }

    public Date getOrderConfirmTime() {
        return orderConfirmTime;
    }

    public void setOrderConfirmTime(Date orderConfirmTime) {
        this.orderConfirmTime = orderConfirmTime;
    }

    public int getOrderPayWays() {
        return orderPayWays;
    }

    public void setOrderPayWays(int orderPayWays) {
        this.orderPayWays = orderPayWays;
    }

    public String getOrderPayCode() {
        return orderPayCode;
    }

    public void setOrderPayCode(String orderPayCode) {
        this.orderPayCode = orderPayCode;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

	public String getOrderUserTel() {
		return orderUserTel;
	}

	public void setOrderUserTel(String orderUserTel) {
		this.orderUserTel = orderUserTel;
	}

	public String getOrderUserRealname() {
		return orderUserRealname;
	}

	public void setOrderUserRealname(String orderUserRealname) {
		this.orderUserRealname = orderUserRealname;
	}

	public String getOrderUserExtrInfo() {
		return orderUserExtrInfo;
	}

	public void setOrderUserExtrInfo(String orderUserExtrInfo) {
		this.orderUserExtrInfo = orderUserExtrInfo;
	}
    
    
}
