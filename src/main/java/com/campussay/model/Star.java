package com.campussay.model;

/**
 * 大牛专题
 * Created by wangwenxiang on 15-12-3.
 */
public class Star {
    private int starId;
    private int starUser;//用户，外键
    private String starName;//姓名
    private String starCampusName;//学校名称
    private String starPicture;//牛人专区图片
    private String starInfo;//简介一，不能空
    private String starInfo1;//简介二
    private String starInfo2;//简介三
    private String starInfo3;//简介四
    private int starState;//状态

    public int getStarId() {
        return starId;
    }

    public void setStarId(int starId) {
        this.starId = starId;
    }

    public int getStarUser() {
        return starUser;
    }

    public void setStarUser(int starUser) {
        this.starUser = starUser;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public String getStarCampusName() {
        return starCampusName;
    }

    public void setStarCampusName(String starCampusName) {
        this.starCampusName = starCampusName;
    }

    public String getStarPicture() {
        return starPicture;
    }

    public void setStarPicture(String starPicture) {
        this.starPicture = starPicture;
    }

    public String getStarInfo() {
        return starInfo;
    }

    public void setStarInfo(String starInfo) {
        this.starInfo = starInfo;
    }

    public String getStarInfo1() {
        return starInfo1;
    }

    public void setStarInfo1(String starInfo1) {
        this.starInfo1 = starInfo1;
    }

    public String getStarInfo2() {
        return starInfo2;
    }

    public void setStarInfo2(String starInfo2) {
        this.starInfo2 = starInfo2;
    }

    public String getStarInfo3() {
        return starInfo3;
    }

    public void setStarInfo3(String starInfo3) {
        this.starInfo3 = starInfo3;
    }

    public int getStarState() {
        return starState;
    }

    public void setStarState(int starState) {
        this.starState = starState;
    }
}
