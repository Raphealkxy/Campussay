package com.campussay.exception;
/**
 * 
 * @ClassName: MeanwhileCreateTalkingOrderException 
 * @Description: 同时创建一个课程订单异常
 * @author: liupengyan
 * @date: 2016年2月26日 下午12:50:55
 */
public class MeanwhileCreateTalkingOrderException extends Exception {
	
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	
	public MeanwhileCreateTalkingOrderException(){
		
		super("不能同时创建同一课程订单");
		
	}
}
