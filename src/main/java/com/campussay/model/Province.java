package com.campussay.model;

/**
 * 省份信息
 * Created by wangwenxiang on 15-12-3.
 */
public class Province {
    private int provinceId;
    private String provinceName;//省份名字
    private int provinceState;//状态


    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceState() {
        return provinceState;
    }

    public void setProvinceState(int provinceState) {
        this.provinceState = provinceState;
    }
}
