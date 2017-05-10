package com.campussay.model;

import java.util.Date;

public class Information {
	
	
	public static final int SYSTEM_INFORMATION = -1;
	
	/**
	 * 信息已经读过
	 */
	public static final int INFORMATION_HAD_READ = 1;
	/**
	 * 信息未读过
	 */
	public static final int INFORMATION_DID_NOT_READ = 0;
	/**
	 * 信息被删除
	 */
	public static final int INFORMATION_STATUS_DELETE = -1;
	/**
	 * 信息可读
	 */
	public static final int INFORMATION_STATUS_CAN_USE = 0;
	
	private String informationId;//消息主键
	private String informationContent; //消息内容
	private int informationType;//消息类型 －1表示系统消息
	private int informationIsread;//消息是否已读 0未读 1已读
	private Date informationCrateTime;//消息生成时间
	private Date informationReadtime;//消息阅读时间
	
	private int infomationIsDelete;//消息是否已经被删除 －1表示删除 0表示未删除
	
	private String informationUser;//消息所属用户
	
	public int getInfomationIsDelete() {
		return infomationIsDelete;
	}
	public void setInfomationIsDelete(int infomationIsDelete) {
		this.infomationIsDelete = infomationIsDelete;
	}
	public String getInformationUser() {
		return informationUser;
	}
	public void setInformationUser(String informationUser) {
		this.informationUser = informationUser;
	}
	public String getInformationId() {
		return informationId;
	}
	public void setInformationId(String informationId) {
		this.informationId = informationId;
	}
	
	public String getInformationContent() {
		return informationContent;
	}
	public void setInformationContent(String informationContent) {
		this.informationContent = informationContent;
	}
	public int getInformationType() {
		return informationType;
	}
	public void setInformationType(int informationType) {
		this.informationType = informationType;
	}
	public int getInformationIsread() {
		return informationIsread;
	}
	public void setInformationIsread(int informationIsread) {
		this.informationIsread = informationIsread;
	}
	public Date getInformationCrateTime() {
		return informationCrateTime;
	}
	public void setInformationCrateTime(Date informationCrateTime) {
		this.informationCrateTime = informationCrateTime;
	}
	public Date getInformationReadtime() {
		return informationReadtime;
	}
	public void setInformationReadtime(Date informationReadtime) {
		this.informationReadtime = informationReadtime;
	}
	
	
}
