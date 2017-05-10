package com.campussay.model;

public class Follow {
	
    private Integer id;

    private Integer uid;

    private Integer takingTypeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getTakingTypeId() {
        return takingTypeId;
    }

    public void setFieldId(Integer takingTypeId) {
        this.takingTypeId = takingTypeId;
    }
}