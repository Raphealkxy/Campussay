package com.campussay.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.campussay.service.BannerService;
import com.campussay.service.CampusService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/banner")
public class BannerController {
	
	@Autowired 
	private BannerService bannerservice;
	@Autowired
	private CampusService campusservice;
    /**
     * wzh
     * 获取用户的发布的talking
     * @throws Exception 
     */

	@RequestMapping(value = "Bannerindex")
	@ResponseBody
	public JSONObject Bannerindex(@RequestParam("campus_id") Integer campus_id ) throws Exception{
		if (campus_id == null || "".equals(campus_id)){
			return CommonUtil.constructResponse(0, "未选取学校",null);
		}
		List<Map<String, Object>> list=new ArrayList();
		 try{
		 	list=bannerservice.Bannerindex(campusservice.getCampusNameByid(campus_id));
		 }catch(Exception e){
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
			}
        return CommonUtil.constructResponse(1, null,list);
	}
}
