package com.campussay.util;
/**
 * 
 * @ClassName: EnumUtil 
 * @Description: 主要定义一些常用的常量 
 * @author: liupengyan
 * @date: 2016年2月23日 下午5:11:19
 */
public class EnumUtil {
	
	/**
	 * 支付宝公钥
	 */
   public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	public enum TalkingStatus{
		
		CANUSER,//可以下单
		HADOVERDUE,//已经过期
		HADFULL,//经爆满
		NOTFOUND,//没有找到该课程
		HADPARTICIPATE,//已经参加
		UNKOWNERRRO,//未知错误
		SYSTEMERROR//系统错误
	}
	
	//状态,0-订单作废,1-下单未付款，2-已经付款，3-交流完成，未确认，4-交流完成，已确认，5-已经评价 6－申请退款 7-退款成功 8-退款失败（被拒） 
	/**
	 * 0-订单作废
	 */
	public static final int ORDER_STATE_DELETE = 0;
	/**
	 * 1-下单未付款
	 */
	public static final int ORDER_STATE_NOT_PAY = 1;
	/**
	 * 2-已经付款
	 */
	public static final int ORDER_STATE_HAD_PAY = 2;
	/**
	 *3-交流完成，未确认
	 */
	public static final int ORDER_STATE_NOT_CONFIRM = 3;
	/**
	 *4-交流完成，已确认
	 */
	public static final int ORDER_STATE_HAD_CONFIRM = 4;
	/**
	 *5-已经评价 
	 */
	public static final int ORDER_STATE_HAD_EVALUATE = 5;
	
	/**
	 *6－申请退款 
	 */
	public static final int ORDER_STATE_HAD_REFUND_ING = 6;
	
	/**
	 *7-退款成功
	 */
	public static final int ORDER_STATE_HAD_REFUND_SUCCESS = 7;
	
	/**
	 *8-退款失败（被拒）
	 */
	public static final int ORDER_STATE_HAD_REFUND_FAIL = 8;
	
	
	
	
	/**
	 * 分页查询 每页条数
	 */
	public static final int PAGE_SIZE = 10;
	
	/**
	 * 系统错误
	 */
   public static final int SYSTEM_ERROR = -5;
   /**
    * 未知错误
    */
   public static final int UNKOWN_ERROR = -4;
   
   /**
    * 正确
    */
   public static final int OK = 1;
   /**
    * 未登录
    */
   public static final int NOT_LOGIN = -1;
    
   /**
    * 不能为空
    */
   public static final int CAN_NOT_NULL = 100001;
   
   /**
    * 课程过期
    */
   public static final int TALKING_OVERDUE = 100002;
   
   /**
    * 课程爆满
    */
   public static final int TALKING_FULL = 100003;
   
   /**
    * 课程没有找到
    */
   public static final int TALKING_NOT_FOUND = 100004;
   /**
    * 订单不存在
    */
   public static final int ORDER_NOT_FOUND = 100005;
  /**
   * 订单不正常
   */
   public static final int ORDER_NOT_NORMAL = 100006;
   /**
    * 订单号重复
    */
   public static final int ORDER_NUM_REPEAT = 100007;
   /**
    * 该订单已经支付过
    */
   public static final int ORDER_HAD_PAY = 100008;
   /**
    * 已经拥有该课程
    */
   public static final int HAD_OWN_TALKING = 100009;
   /**
    * 同一用户不能同时创建同一产品订单
    */
   public static final int CAN_NOT_CREATE_ORDER_MEANWHILE = 100010;
   
   /**
    * 查无数据
    */
   public static final int NO_DATA = 100011;
   /**
    * 订单状态不正确
    */
   public static final int ORDER_STATE_NOT_RIGHTL =100012;
   /**
    * 订单用户不是当前操作用户
    */
   public static final int ORDER_USER_NOT_NOW_USER =100013;
   
   /**
    * 超长
    */
   public static final int TOO_LANG =100014;
   /**
    * 提现金额大于账户余额
    */
   public static final int TOO_MUCH_CASH =100015;
   /**
    * 已经申请提现了 不能再提
    */
   public static final int HAD_APPLY_TO_CASH =100016;
   /**
    * 申请提现金额必须大于0
    */
   public static final int APPLY_TO_CASH_MUST_LARGER_ZERO =100017;
   /**
    * 用户密码错误
    */
   public static final int PASSWORD_ERROR = 100018;
   
   
}
