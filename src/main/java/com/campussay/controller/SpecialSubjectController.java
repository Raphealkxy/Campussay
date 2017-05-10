package com.campussay.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/SpecialSubject")
public class SpecialSubjectController {

	@Autowired
	//private SpecialSubjectService SpecialSubjectservice;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject takingtypedetail(){
		JSONObject jo=new JSONObject();
		//  TODO effine  代码出现错误暂时注释
		//List<SpecialSubject> list=SpecialSubjectservice.SpecialSubject_list();
		//jo.put("date", list);
		return null;
	}
}
