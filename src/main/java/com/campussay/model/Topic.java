package com.campussay.model;

import java.util.Date;

public class Topic {
    private Integer id;

    private Integer takingTypeId;

    private Integer userId;

    private String tile;

    private String coverImg;

    private Date createTime;

    private Date updateTime;

    private byte[] intro;

    private int state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTakingTypeId() {
        return takingTypeId;
    }

    public void setTakingTypeId(Integer takingTypeId) {
        this.takingTypeId = takingTypeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile == null ? null : tile.trim();
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg == null ? null : coverImg.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public byte[] getIntro() {
        return intro;
    }

    public void setIntro(byte[] intro) {
        this.intro = intro;
    }

    public void setState(int state){this.state=state;}

    public int getState(){return state;}


}