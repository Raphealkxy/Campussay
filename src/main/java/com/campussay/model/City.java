package com.campussay.model;

/**
 * 省份下城市信息
 * Created by wangwenxiang on 15-12-3.
 */
public class City {
    private int cityId;
    private int cityProvince; //所属省份
    private String cityName; //城市名称
    private int cityState; //状态

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityProvince() {
        return cityProvince;
    }

    public void setCityProvince(int cityProvince) {
        this.cityProvince = cityProvince;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityState() {
        return cityState;
    }

    public void setCityState(int cityState) {
        this.cityState = cityState;
    }
}
