/**
 * @author effine
 * @Date 2016年1月10日  下午9:50:50
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import com.campussay.dao.BannerDao;
import com.campussay.model.Banner;
import com.campussay.service.BannerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor=Exception.class)
public class BannerServiceImpl implements BannerService {
	@Autowired
	private BannerDao bannerdao;
	
	/*
	 * Author：汪治宏
	 * 首页轮滑
	 */
	public List<Map<String,Object>> Bannerindex(String campus_name) throws Exception{
		List<Map<String,Object>> ba=new ArrayList();
		try{
			ba=bannerdao.Bannerindex(campus_name);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}
		return ba;
	}
	
	/*
	 * 增加图片
	 */
	public int addBanner(List<Banner> list) throws Exception{
		int i;
		try{
		i=bannerdao.addBanner(list);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}
		return i;
	}
	
}
