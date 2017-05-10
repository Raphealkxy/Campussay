package com.campussay.model;

import java.util.Date;

/**
 * 说文评论，只有购买的用户可以评论
 * Created by wangwenxiang on 15-12-3.
 */
public class TalkingComment {
    private int talkingCommentId;
    private int talkingCommenttalking;//所属说文
    private int talkingCommentUser;//评价者用户id
    private String talkingCommentContent;//评论内容
    private int talkingCommentGrade;//最综评分评分
    private Date talkingCommentTime;//评论时间
    private int talkingCommentState;//评论状态
    private String talkingUserName;//评价者用户名
    
    
    public String getTalkingUserName() {
		return talkingUserName;
	}

	public void setTalkingUserName(String talkingUserName) {
		this.talkingUserName = talkingUserName;
	}

	public int getTalkingCommentId() {
        return talkingCommentId;
    }

    public void setTalkingCommentId(int talkingCommentId) {
        this.talkingCommentId = talkingCommentId;
    }

    public int getTalkingCommenttalking() {
        return talkingCommenttalking;
    }

    public void setTalkingCommenttalking(int talkingCommenttalking) {
        this.talkingCommenttalking = talkingCommenttalking;
    }

    public int getTalkingCommentUser() {
        return talkingCommentUser;
    }

    public void setTalkingCommentUser(int talkingCommentUser) {
        this.talkingCommentUser = talkingCommentUser;
    }

    public String getTalkingCommentContent() {
        return talkingCommentContent;
    }

    public void setTalkingCommentContent(String talkingCommentContent) {
        this.talkingCommentContent = talkingCommentContent;
    }

    public int getTalkingCommentGrade() {
        return talkingCommentGrade;
    }

    public void setTalkingCommentGrade(int talkingCommentGrade) {
        this.talkingCommentGrade = talkingCommentGrade;
    }

    public Date getTalkingCommentTime() {
        return talkingCommentTime;
    }

    public void setTalkingCommentTime(Date talkingCommentTime) {
        this.talkingCommentTime = talkingCommentTime;
    }

    public int getTalkingCommentState() {
        return talkingCommentState;
    }

    public void setTalkingCommentState(int talkingCommentState) {
        this.talkingCommentState = talkingCommentState;
    }
}
