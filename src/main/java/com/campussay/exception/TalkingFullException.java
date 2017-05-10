package com.campussay.exception;
/**
 * 
 * @ClassName: TalkingFullException 
 * @Description: 课程被抢完异常
 * @author: liupengyan
 * @date: 2016年2月25日 下午1:52:45
 */
public class TalkingFullException extends Exception {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	
	public TalkingFullException(){
		
		super("该课程已经爆满");
		
	}
	
}
