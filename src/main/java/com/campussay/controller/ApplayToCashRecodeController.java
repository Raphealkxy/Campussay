package com.campussay.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.User;
import com.campussay.service.ApplayToCashRecodeService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

@Controller
@RequestMapping("/applayToCashRecode")
public class ApplayToCashRecodeController {
	
	@Resource
	private ApplayToCashRecodeService applayToCashRecodeService;
	
	
	@RequestMapping("selectUserCashRecode")
	@ResponseBody
	public JSONObject selectUserOrderByStatus(HttpSession session,Integer state,Integer pageNum){
		User user= (User) session.getAttribute("user");
		if(user==null){
			return  CommonUtil.constructResponse(EnumUtil.NOT_LOGIN,"请登录",null);
		}
		int userId = user.getUserId();
		if (pageNum == null){
			pageNum = new Integer(1);
		}
	
		int i_pageNum = pageNum;
		i_pageNum -=1;
		
		if (i_pageNum<0){
			i_pageNum=0;
		}
	    int start=	i_pageNum*EnumUtil.PAGE_SIZE;
		
	    
		try {
			Map result = 	applayToCashRecodeService.selectUserApplayToCashRecode(userId+"",  start, EnumUtil.PAGE_SIZE);
			
			if(result == null ){
				return  CommonUtil.constructResponse(EnumUtil.NO_DATA, "暂无数据",null);
				
			}else{
				return  CommonUtil.constructResponse(EnumUtil.OK, "查询成功",result);
			}
		} catch (Exception e) {
			return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"系统错误",null);
		}

		
	}
	
	
}
