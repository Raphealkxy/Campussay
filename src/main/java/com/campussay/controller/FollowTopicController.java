package com.campussay.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.User;
import com.campussay.service.FollowTopicService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

@Controller
@RequestMapping("followtopic")
public class FollowTopicController {
	@Autowired
	private  FollowTopicService followTopicService;
	
	
	/*
	 * 添加topic关注
	 */
	@RequestMapping("addAConcernTopic")
    @ResponseBody
    public JSONObject addAConcernTopic(HttpSession session, Integer topicId){
		User user=(User)session.getAttribute("user");
        int n=0;
        if(user==null){
            return CommonUtil.constructHtmlResponse(-1,"用户未登录！",null);
        }
        try{
        	n=followTopicService.addAConcernTopic(user.getUserId(),topicId);
        }catch(Exception e){
        	  return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
        }
        if(n==0){
            return CommonUtil.constructHtmlResponse(0,"关注失败！",null);
        }
        return CommonUtil.constructHtmlResponse(1,"关注成功！",null);
    }
	
	
	/*
     *用户取消关注话题
     */
    @RequestMapping("cancelConcernTopic")
    @ResponseBody
    public JSONObject cancelConcernTopic(HttpSession session,Integer topicId){
        User user=(User)session.getAttribute("user");
        if(user==null){
            return CommonUtil.constructHtmlResponse(-1,"用户未登录！",null);
        }
        int n=0;
        try{
        	n=followTopicService.cancelConcernTopic(user.getUserId(),topicId);
        }catch(Exception e){
      	  	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
        }
        if(n==0){
            return CommonUtil.constructHtmlResponse(0,"取消关注失败！",null);
        }
      return CommonUtil.constructHtmlResponse(1,"取消关注成功！",null);
    }

}
