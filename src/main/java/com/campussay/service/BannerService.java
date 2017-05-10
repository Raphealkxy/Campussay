package com.campussay.service;

import com.campussay.model.Banner;

import java.util.List;
import java.util.Map;


public interface BannerService {
	
	/*
	 * Author：汪治宏
	 * 首页轮滑
	 */
	public abstract List<Map<String,Object>> Bannerindex(String campus_name) throws Exception;
	
	/*
	 * 增加图片
	 */
	public int addBanner(List<Banner> list) throws Exception;
}
