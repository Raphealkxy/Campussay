package com.campussay.controller;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Comments;
import com.campussay.model.User;
import com.campussay.service.CommentsService;
import com.campussay.service.UserService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

import org.apache.ibatis.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Comments")
public class CommentsController {
	@Autowired
	private CommentsService  commentsService;
	@Autowired 
    private UserService userService;
	
	private static final int pageSize=10;
	 /*
	   * 问答详细页，评论
	   * @param user_id(session)，通过自动参数绑定获取前端com中的内容
	   * 
	   * */
	  @RequestMapping("addAComments")
	  @ResponseBody
	  public JSONObject addOneAnswerByUserID(HttpServletRequest request, int answerId, String context){
		  User user=(User)request.getAttribute("user");
		  if(user==null){
			  return CommonUtil.constructResponse(-1, "请登录账户", null);
		  }
		  Comments comm=new Comments();
		  comm.setUid(user.getUserId());
		  comm.setAnswerId(answerId);
		  try {
			comm.setContext(context.getBytes("utf-8"));
		  } catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		  }
		  comm.setIslike(0);
		  Date date=new Date();
		  comm.setTime(date);
		  int n=0;
		  try{
			  n=commentsService.addAComments(comm,user.getUserId(),user.getUserName());
		  }catch(Exception e){
				return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
		  }
		  if(n==0){
				return CommonUtil.constructResponse(0, "添加失败", null);
		  }
		  Map<String,Object> userinfo=userService.getBasicInfoByUserId(user.getUserId());
		  Map<String,Object> mp=new HashMap<String,Object>();
		  mp.put("userName", user.getUserName());
		  mp.put("userPhoto", userinfo.get("user_photo"));
		  return CommonUtil.constructResponse(1, "添加成功", mp);
	  }

	/*
	 得到某一答案下面的所有评论
	 */
	@RequestMapping("getCommentsByAnswerId")
	@ResponseBody
	public JSONObject getCommentsByAnswerId(int answerId,@RequestParam(required = false)Integer page){
		if(page==null||page<0){
			page=1;
		}
		HashMap list=null;
		try{
			 list=commentsService.getCommentsByAnswerId(answerId,(page-1)*pageSize,pageSize);
		}catch(Exception e){
			 return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
		}
		if(list==null){
			return CommonUtil.constructResponse(1,"数据库没有数据",list);
		}
		return CommonUtil.constructResponse(1,"获取数据成功",list);
	}

}
