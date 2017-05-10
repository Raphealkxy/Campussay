package com.campussay.model;

import java.util.Date;

public class ApplayToCashRecode {
	
	private String applayToCashRecodeId; //记录主键
	private String applayToCashRecodeContent;//记录内容
	private Date applayToCashRecodeTime;//记录时间
	private String applayToCashRecodeUser;//记录用户
	
	private int applayToCashRecodeStatus;//审核状态
	
	public int getApplayToCashRecodeStatus() {
		return applayToCashRecodeStatus;
	}
	public void setApplayToCashRecodeStatus(int applayToCashRecodeStatus) {
		this.applayToCashRecodeStatus = applayToCashRecodeStatus;
	}
	public String getApplayToCashRecodeId() {
		return applayToCashRecodeId;
	}
	public void setApplayToCashRecodeId(String applayToCashRecodeId) {
		this.applayToCashRecodeId = applayToCashRecodeId;
	}
	public String getApplayToCashRecodeContent() {
		return applayToCashRecodeContent;
	}
	public void setApplayToCashRecodeContent(String applayToCashRecodeContent) {
		this.applayToCashRecodeContent = applayToCashRecodeContent;
	}
	public Date getApplayToCashRecodeTime() {
		return applayToCashRecodeTime;
	}
	public void setApplayToCashRecodeTime(Date applayToCashRecodeTime) {
		this.applayToCashRecodeTime = applayToCashRecodeTime;
	}
	public String getApplayToCashRecodeUser() {
		return applayToCashRecodeUser;
	}
	public void setApplayToCashRecodeUser(String applayToCashRecodeUser) {
		this.applayToCashRecodeUser = applayToCashRecodeUser;
	}
	
	
	
	
}
