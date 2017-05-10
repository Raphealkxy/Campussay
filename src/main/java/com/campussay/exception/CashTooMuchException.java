package com.campussay.exception;

public class CashTooMuchException extends Exception {
	
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	
	public CashTooMuchException(){
		
		super("申请提现金额大于余额");
		
	}
}
