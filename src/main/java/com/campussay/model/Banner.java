package com.campussay.model;

/**
 * Created by wangwenxiang on 15-12-24.
 */
public class Banner {
	private int bannerId;
    private String bannerPicture;
    private String bannerUrl;
    private String bannerDesc;
    private int bannerOrder;
    private int bannerState;
    private int campusId;

    public int getCampusId() {
		return campusId;
	}

	public void setCampusId(int campusId) {
		this.campusId = campusId;
	}

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerPicture() {
        return bannerPicture;
    }

    public void setBannerPicture(String bannerPicture) {
        this.bannerPicture = bannerPicture;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getBannerDesc() {
        return bannerDesc;
    }

    public void setBannerDesc(String bannerDesc) {
        this.bannerDesc = bannerDesc;
    }

    public int getBannerOrder() {
        return bannerOrder;
    }

    public void setBannerOrder(int bannerOrder) {
        this.bannerOrder = bannerOrder;
    }

    public int getBannerState() {
        return bannerState;
    }

    public void setBannerState(int bannerState) {
        this.bannerState = bannerState;
    }
}
