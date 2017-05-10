package com.campussay.model;

import java.util.Date;

/**
 * 说友发表的说文产品
 * Created by wangwenxiang on 15-12-3.
 */
public class Talking {
	/**
	 * 课程状态  已经结束
	 */
	public static final Integer  TALKING_HAD_PASS=100;
	
    private int talkingId;
    private int talkingUser;//发布者
    private String talkingTitle;//标题
    private Date talkingPublishTime; //发布时间
    private int talkingRootType; //说文根节点类型id
    private int talkingType;//类型，外键，可以是没有展示出来的类型
    private int talkingMaxPersion;//人数上限
    private int talkingNowPersion;//已经报名的人数
    private double talkingPrice;//价格，每人
    private String talkingAddress;//线下交流时预留地址
    private String talkingMainPicture;//主页展示图片
    private String talkingTarget;//交流目标
    private Date talkingStartTime; //课程开始时间
    private Date talkingEndTime; //课程结束时间
    private String talkingInfo;//详细说明，图文并茂
    private int talkingCampus;//学校编号，从发布用户获取，方便后面查询
    private int talkingState;//状态
    private int talking_check_state;//审核状态
    

    private String talking_is_online;//线上或者线下 －1 线下 1 线上
    
    public String getTalking_is_online() {
		return talking_is_online;
	}

	public void setTalking_is_online(String talking_is_online) {
		this.talking_is_online = talking_is_online;
	}

	private String talking_tool; //交流工具
    private String talking_tool_num; //交流工具号码

    public String getTalking_tool() {
		return talking_tool;
	}

	public void setTalking_tool(String talking_tool) {
		this.talking_tool = talking_tool;
	}

	public String getTalking_tool_num() {
		return talking_tool_num;
	}

	public void setTalking_tool_num(String talking_tool_num) {
		this.talking_tool_num = talking_tool_num;
	}

	public int getTalking_check_state() {
		return talking_check_state;
	}

	public void setTalking_check_state(int talking_check_state) {
		this.talking_check_state = talking_check_state;
	}

	public int getTalkingId() {
        return talkingId;
    }

    public void setTalkingId(int talkingId) {
        this.talkingId = talkingId;
    }

    public int getTalkingUser() {
        return talkingUser;
    }

    public void setTalkingUser(int talkingUser) {
        this.talkingUser = talkingUser;
    }

    public String getTalkingTitle() {
        return talkingTitle;
    }

    public void setTalkingTitle(String talkingTitle) {
        this.talkingTitle = talkingTitle;
    }

    public int getTalkingRootType() {
        return talkingRootType;
    }

    public void setTalkingRootType(int talkingRootType) {
        this.talkingRootType = talkingRootType;
    }

    public int getTalkingType() {
        return talkingType;
    }

    public void setTalkingType(int talkingType) {
        this.talkingType = talkingType;
    }

    public int getTalkingMaxPersion() {
        return talkingMaxPersion;
    }

    public void setTalkingMaxPersion(int talkingMaxPersion) {
        this.talkingMaxPersion = talkingMaxPersion;
    }

    public int getTalkingNowPersion() {
        return talkingNowPersion;
    }

    public void setTalkingNowPersion(int talkingNowPersion) {
        this.talkingNowPersion = talkingNowPersion;
    }

    public double getTalkingPrice() {
        return talkingPrice;
    }

    public void setTalkingPrice(double talkingPrice) {
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

    public Date getTalkingStartTime() {
        return talkingStartTime;
    }

    public void setTalkingStartTime(Date talkingStartTime) {
        this.talkingStartTime = talkingStartTime;
    }

    public Date getTalkingEndTime() {
        return talkingEndTime;
    }

    public void setTalkingEndTime(Date talkingEndTime) {
        this.talkingEndTime = talkingEndTime;
    }

    public String getTalkingInfo() {
        return talkingInfo;
    }

    public void setTalkingInfo(String talkingInfo) {
        this.talkingInfo = talkingInfo;
    }

    public int getTalkingCampus() {
        return talkingCampus;
    }

    public void setTalkingCampus(int talkingCampus) {
        this.talkingCampus = talkingCampus;
    }

    public int getTalkingState() {
        return talkingState;
    }

    public void setTalkingState(int talkingState) {
        this.talkingState = talkingState;
    }

    public Date getTalkingPublishTime() {
        return talkingPublishTime;
    }

    public void setTalkingPublishTime(Date talkingPublishTime) {
        this.talkingPublishTime = talkingPublishTime;
    }
}
