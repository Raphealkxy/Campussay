package com.campussay.model;

/**
 * 城市下的大学信息
 * Created by wangwenxiang on 15-12-3.
 */
public class Campus {
    private int campusId;
    private int campusArea; //大学所属地区
    private int campusCity; //所属城市
    private int campusProvince; //所属省份
    private String campusName; //学校名称
    private int campusState; //状态

    public int getCampusId() {
        return campusId;
    }

    public void setCampusId(int campusId) {
        this.campusId = campusId;
    }

    public int getCampusArea() {
        return campusArea;
    }

    public void setCampusArea(int campusArea) {
        this.campusArea = campusArea;
    }

    public int getCampusCity() {
        return campusCity;
    }

    public void setCampusCity(int campusCity) {
        this.campusCity = campusCity;
    }

    public int getCampusProvince() {
        return campusProvince;
    }

    public void setCampusProvince(int campusProvince) {
        this.campusProvince = campusProvince;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public int getCampusState() {
        return campusState;
    }

    public void setCampusState(int campusState) {
        this.campusState = campusState;
    }
}
