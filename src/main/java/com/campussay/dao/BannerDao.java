package com.campussay.dao;


import com.campussay.model.Banner;


import com.campussay.model.Banner;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * Created by wangwenxiang on 15-12-4.
 */
@Repository
public interface BannerDao {
	
	public List<Map<String,Object>> Bannerindex(@Param("campus_name") String campus_name);
	
	/*
	 * 增加图片
	 */
	public int addBanner(List<Banner> list);
}
