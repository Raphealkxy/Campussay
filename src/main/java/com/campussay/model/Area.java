package com.campussay.model;

/**
 * 城市下的地区
 * 
 * @author wangwenxiang
 */
public class Area {

	private int areaId;
	private int areaCity; // 所属城市
	private String areaName; // 名字
	private int areaState; // 状态

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

	public int getAreaCity() {
		return areaCity;
	}

	public void setAreaCity(int areaCity) {
		this.areaCity = areaCity;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public int getAreaState() {
		return areaState;
	}

	public void setAreaState(int areaState) {
		this.areaState = areaState;
	}
}
