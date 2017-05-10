package com.campussay.model;


public class TalkingPublish {
	   private String talkingId;
	    public String getTalkingId() {
		return talkingId;
	}
	public void setTalkingId(String talkingId) {
		this.talkingId = talkingId;
	}
	public String getTalkingUser() {
		return talkingUser;
	}
	public void setTalkingUser(String talkingUser) {
		this.talkingUser = talkingUser;
	}
	public String getTalkingTitle() {
		return talkingTitle;
	}
	public void setTalkingTitle(String talkingTitle) {
		this.talkingTitle = talkingTitle;
	}
	public String getTalkingPublishTime() {
		return talkingPublishTime;
	}
	public void setTalkingPublishTime(String talkingPublishTime) {
		this.talkingPublishTime = talkingPublishTime;
	}
	public String getTalkingRootType() {
		return talkingRootType;
	}
	public void setTalkingRootType(String talkingRootType) {
		this.talkingRootType = talkingRootType;
	}
	public String getTalkingType() {
		return talkingType;
	}
	public void setTalkingType(String talkingType) {
		this.talkingType = talkingType;
	}
	public String getTalkingMaxPersion() {
		return talkingMaxPersion;
	}
	public void setTalkingMaxPersion(String talkingMaxPersion) {
		this.talkingMaxPersion = talkingMaxPersion;
	}
	public String getTalkingNowPersion() {
		return talkingNowPersion;
	}
	public void setTalkingNowPersion(String talkingNowPersion) {
		this.talkingNowPersion = talkingNowPersion;
	}
	public String getTalkingPrice() {
		return talkingPrice;
	}
	public void setTalkingPrice(String talkingPrice) {
		this.talkingPrice = talkingPrice;
	}
	public String getTalkingAddress() {
		return talkingAddress;
	}
	public void setTalkingAddress(String talkingAddress) {
		this.talkingAddress = talkingAddress;
	}
	public String getTalkingMainPicture() {
		return talkingMainPicture;
	}
	public void setTalkingMainPicture(String talkingMainPicture) {
		this.talkingMainPicture = talkingMainPicture;
	}
	public String getTalkingTarget() {
		return talkingTarget;
	}
	public void setTalkingTarget(String talkingTarget) {
		this.talkingTarget = talkingTarget;
	}
	public String getTalkingStartTime() {
		return talkingStartTime;
	}
	public void setTalkingStartTime(String talkingStartTime) {
		this.talkingStartTime = talkingStartTime;
	}
	public String getTalkingEndTime() {
		return talkingEndTime;
	}
	public void setTalkingEndTime(String talkingEndTime) {
		this.talkingEndTime = talkingEndTime;
	}
	public String getTalkingInfo() {
		return talkingInfo;
	}
	public void setTalkingInfo(String talkingInfo) {
		this.talkingInfo = talkingInfo;
	}
	public String getTalkingCampus() {
		return talkingCampus;
	}
	public void setTalkingCampus(String talkingCampus) {
		this.talkingCampus = talkingCampus;
	}
	public String getTalkingState() {
		return talkingState;
	}
	public void setTalkingState(String talkingState) {
		this.talkingState = talkingState;
	}
	
		public String getTalkingTool() {
		return talkingTool;
	}
	public void setTalkingTool(String talkingTool) {
		this.talkingTool = talkingTool;
	}
	public String getTalkingToolNum() {
		return talkingToolNum;
	}
	public void setTalkingToolNum(String talkingToolNum) {
		this.talkingToolNum = talkingToolNum;
	}

	public String getTalkingIsOnline() {
		return talkingIsOnline;
	}
	public void setTalkingIsOnline(String talkingIsOnline) {
		this.talkingIsOnline = talkingIsOnline;
	}
		private String talkingUser;//发布者
	    private String talkingTitle;//标题
	    private String talkingPublishTime; //发布时间
	    private String talkingRootType; //说文根节点类型id
	    private String talkingType;//类型，外键，可以是没有展示出来的类型
	    private String talkingMaxPersion;//人数上限
	    private String talkingNowPersion;//已经报名的人数
	    private String talkingPrice;//价格，每人
	    private String talkingAddress;//线下交流时预留地址
	    private String talkingMainPicture;//主页展示图片
	    private String talkingTarget;//交流目标
	    private String talkingStartTime; //课程开始时间
	    private String talkingEndTime; //课程结束时间
	    private String talkingInfo;//详细说明，图文并茂
	    private String talkingCampus;//学校编号，从发布用户获取，方便后面查询
	    private String talkingState;//状态
	    private String talkingTool; //交流工具
	    private String talkingToolNum; //交流工具号码
	    
	    private String talkingIsOnline;//线上或者线下  －1 线下 1 线上
}
