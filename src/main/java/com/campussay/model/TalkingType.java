package com.campussay.model;

/**
 * 说文分类表，树形结构
 * Created by wangwenxiang on 15-12-3.
 */
public class TalkingType {
    private int talkingTypeId;
    private String talkingTypeName;//类型名称
    private String talkingTypePicture;//图片
    private String talkingTypeFloor;//所属层级
    private String talkingTypeDescription;
    private int talkingTypeParent;//父节点
    private boolean talkingTypeIsLeaf;//是否是叶子节点，只有叶子节点才能成为说文类型
    private int talkingTypeState;//状态

    public int getTalkingTypeId() {
        return talkingTypeId;
    }

    public void setTalkingTypeId(int talkingTypeId) {
        this.talkingTypeId = talkingTypeId;
    }

    public String getTalkingTypeName() {
        return talkingTypeName;
    }

    public void setTalkingTypeName(String talkingTypeName) {
        this.talkingTypeName = talkingTypeName;
    }

    public String getTalkingTypePicture() {
        return talkingTypePicture;
    }

    public void setTalkingTypePicture(String talkingTypePicture) {
        this.talkingTypePicture = talkingTypePicture;
    }

    public String getTalkingTypeFloor() {
        return talkingTypeFloor;
    }

    public void setTalkingTypeFloor(String talkingTypeFloor) {
        this.talkingTypeFloor = talkingTypeFloor;
    }

    public int getTalkingTypeParent() {
        return talkingTypeParent;
    }

    public void setTalkingTypeParent(int talkingTypeParent) {
        this.talkingTypeParent = talkingTypeParent;
    }

    public boolean isTalkingTypeIsLeaf() {
        return talkingTypeIsLeaf;
    }

    public void setTalkingTypeIsLeaf(boolean talkingTypeIsLeaf) {
        this.talkingTypeIsLeaf = talkingTypeIsLeaf;
    }

    public int getTalkingTypeState() {
        return talkingTypeState;
    }

    public void setTalkingTypeState(int talkingTypeState) {
        this.talkingTypeState = talkingTypeState;
    }

    public void setTalkingTypeDescription(String talkingTypeDescription){
        this.talkingTypeDescription=talkingTypeDescription;
    }
    public String getTalkingTypeDescription(){
        return this.talkingTypeDescription;
    }
}
