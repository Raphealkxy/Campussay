package com.campussay.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.User;
import com.campussay.service.CampusService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;
import com.campussay.util.alipay.httpClient.HttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/campus")
public class CampusController {
	
	@Autowired
	private CampusService campusservice;

	/**
	 * 获取所有状态为10的学校id和名称
	 * @return
	 * @throws Exception 
     */
	@RequestMapping("/getALLcampusname")
	@ResponseBody
	public JSONObject getallcampusname() throws Exception{
		List<Map<String, Object>> list=new ArrayList();
		try{
			 list=campusservice.getAllCampusName();
		 }catch(Exception e){
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
		}
//		return CommonUtil.constructResponse(1, null,list);
		return CommonUtil.constructResponse(1, "33",list);
	}
	
	/**
	 * 学生认证
     */
	@RequestMapping("/isstudentresult")
	@ResponseBody
	public JSONObject isstudentresult(HttpServletRequest request) throws Exception{
			int result;
			int user_id;
			HttpSession session=request.getSession();
			User user=(User) session.getAttribute("user");
			if(user!=null||"".equals(user)){
				user_id=user.getUserId();
			}else{
				return CommonUtil.constructResponse(EnumUtil.NOT_LOGIN, "未登陆",null);
			}
			result=campusservice.getStudentCheckCampus(user_id);
			return CommonUtil.constructResponse(1, "success",result);
	}
	
	


}
