package com.campussay.controller;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.User;
import com.campussay.service.FollowService;
import com.campussay.service.TalkingTypeService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;
    @Autowired
    private TalkingTypeService talkingTypeService;
    private int pageSize=10;

    /*
     *用户添加关注领域
     */
    @RequestMapping("addAConcernField")
    @ResponseBody
    public JSONObject addAConcernField(HttpSession session, Integer field_id){
        User user=(User)session.getAttribute("user");
        int n=0;
        if(user==null){
            return CommonUtil.constructHtmlResponse(-1,"用户未登录！",null);
        }
        try{
        	n=followService.addAConcernField(user.getUserId(),field_id);
        }catch(Exception e){
        	  return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
        }
        if(n==0){
            return CommonUtil.constructHtmlResponse(0,"关注失败！",null);
        }
        return CommonUtil.constructHtmlResponse(1,"关注成功！",null);
    }
    /*
     *用户取消关注领域
     */
    @RequestMapping("cancelConcernField")
    @ResponseBody
    public JSONObject cancelConcernField(HttpSession session,Integer field_id){
        User user=(User)session.getAttribute("user");
        if(user==null){
            return CommonUtil.constructHtmlResponse(-1,"用户未登录！",null);
        }
        int n=0;
        try{
        	n=followService.cancelConcernField(user.getUserId(),field_id);
        }catch(Exception e){
      	  	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
        }
        if(n==0){
            return CommonUtil.constructHtmlResponse(0,"取消关注失败！",null);
        }
      return CommonUtil.constructHtmlResponse(1,"取消关注成功！",null);
    }
    /*
     *得到用户的关注領域名稱
     */
    @RequestMapping("getFollowsByUserId")
    @ResponseBody
    public JSONObject getFollowsByUserId(HttpServletRequest request){
        User user=(User)request.getAttribute("user");
        if(user==null){
            return CommonUtil.constructHtmlResponse(-1,"用户未登录！",null);
        }
        List<HashMap> list=null;
        try{
         list=followService.getFollowsByUserId(user.getUserId());
        }catch(Exception e){
      	  	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
        }
        if(list==null||list.size()==0){
            return CommonUtil.constructHtmlResponse(0,"没有关注领域",null);
        }
        return CommonUtil.constructHtmlResponse(1,"成功",list);
    }


    /*
     *获取用户未关注的领域
     */
    @RequestMapping("getUnFollowsByUserId")
    @ResponseBody
    public JSONObject getUnFollowsByUserId(HttpServletRequest request,@RequestParam(required = false)Integer page){
        User user=(User)request.getAttribute("user");
        HashMap list=null;
        if(user==null){
            return CommonUtil.constructHtmlResponse(-1,"用户未登录！",null);
        }

        if(page==null||page<0)
            page=1;
        try{
        	list=followService.getUnFollowsByUserId(user.getUserId(),(page-1)*pageSize,pageSize);
        }catch(Exception e){
      	  	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
        }
        if(list==null){
        	 return CommonUtil.constructHtmlResponse(1,"数据库没有数据",list);
        }
       return CommonUtil.constructHtmlResponse(1,"获取数据成功",list);
    }

    /*
    获取用户擅长领域的topic
     */
    @RequestMapping("getSkillByUserId")
    @ResponseBody
    public JSONObject getSkillByUserId(HttpServletRequest request,@RequestParam(required = false)Integer page){
        User user=(User)request.getAttribute("user");
        if(user==null){
            return CommonUtil.constructHtmlResponse(-1,"用户未登录！",null);
        }
        if(page==null||page<0){
            page=1;
        }
        HashMap list=null;
        try{	
        	list=followService.getSkillByUserId(user.getUserId(),(page-1)*pageSize,pageSize);
        }catch(Exception e){
      	  	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
        }
        if(list==null){
       	 	return CommonUtil.constructHtmlResponse(1,"数据库没有数据",list);
        }
        return CommonUtil.constructHtmlResponse(1,"获取数据成功",list);
    }
    //用户擅长领域topic数量
    @RequestMapping("getSkillTopicNum")
    @ResponseBody
    public JSONObject getSkillTopicNum(HttpServletRequest request){
        User user=(User)request.getAttribute("user");
        if(user==null){
            return CommonUtil.constructHtmlResponse(-1,"用户未登录！",null);
        }
        HashMap<String,Object> hashMap=null;
        try{
        	hashMap=followService.getSkillTopicNum(user.getUserId());
        }catch(Exception e){
      	  	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
        }
        if(hashMap==null){
        	return CommonUtil.constructHtmlResponse(1,"数据库没有数据",hashMap);
        }
        return CommonUtil.constructHtmlResponse(1,"获取数据成功",hashMap);
    }
    //判断用户是否关注领域社
    @RequestMapping("isConcernField")
    @ResponseBody
    public JSONObject isConcernField(HttpSession session, int taking_type_id){
    	Map<String,Object> mp=null;
    	try{
        	mp=talkingTypeService.getTalkingTypeDetailById(taking_type_id);
    	}catch(Exception e){
       	  	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
        }
        User user=(User)session.getAttribute("user");
        if(user==null){
            return CommonUtil.constructHtmlResponse(-1,"用户未登录！",mp);
        }
        int n=0;
        try{
        	n=followService.isConcernField(user.getUserId(),taking_type_id);
        }catch(Exception e){
       	  	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
        }
        if(n==0){
            return CommonUtil.constructHtmlResponse(0,"用户未关注！",mp);
        }
        return CommonUtil.constructHtmlResponse(1,"用户已关注！",mp);
    }

}
