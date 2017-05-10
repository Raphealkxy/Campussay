package com.campussay.model;

import java.util.Date;

/**
 * 用户消息
 * Created by wangwenxiang on 15-12-3.
 */
public class Message {
    private int messageId;
    private int messageFromUser;//消息来源，可以是用户也可以是系统，为0时表示用户
    private int messageToUser;//消息接收用户
    private String messageTitle;//消息标题，消息类型，like系统消息，用户消息
    private String messageContent;//消息内容
    private Date messageTime;//创建时间
    private int messageState;//状态

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageFromUser() {
        return messageFromUser;
    }

    public void setMessageFromUser(int messageFromUser) {
        this.messageFromUser = messageFromUser;
    }

    public int getMessageToUser() {
        return messageToUser;
    }

    public void setMessageToUser(int messageToUser) {
        this.messageToUser = messageToUser;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public int getMessageState() {
        return messageState;
    }

    public void setMessageState(int messageState) {
        this.messageState = messageState;
    }
}
