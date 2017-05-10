package com.campussay.controller;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.TalkingComment;
import com.campussay.model.User;
import com.campussay.service.TalkingCommentService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

@Controller
@RequestMapping("/comment")
public class TalkingCommentController {

    @Autowired
    private TalkingCommentService talkingCommentService;

    /**
     * 获取用户所有获得的评价,分页
     * @param userId 查看的用户id
     * @param page 分页，为null时默认为1
     * @return 结果列表
     */
    @RequestMapping("getAllCommentByUser")
    @ResponseBody
    public JSONObject getAllCommentByUser(Integer userId, @RequestParam(required = false)Integer page){
        if (userId == null)
            return CommonUtil.constructResponse(0,"用户id错误",null);
        JSONObject jo = new JSONObject();
        int count = talkingCommentService.getAllCommentCountByUser(userId);
        jo.put("count",count);
        if (count > 0){
            jo.put("rows",talkingCommentService.getAllCommentByUser(userId,page));
        }else {
            jo.put("rows",null);
        }
        return CommonUtil.constructResponse(1,null,jo);
    }
    
    /**
     * 添加talking评论
     * localhost:8080/comment/saveTalkingComment?talkingCommenttalking=1&talkingCommentContent=test&talkingCommentGrade=3&orderId=1
     *
     * @param talkingComment   当前页码
     * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
     */
//    @RequestMapping("/saveTalkingComment")
//    @ResponseBody
//    @Deprecated
//    public JSONObject saveTalkingComment(HttpServletRequest request, TalkingComment talkingComment, String orderId) {
////    	 talkingComment.setTalkingCommentUser(1);
////         return talkingCommentService.saveTalkingComment(talkingComment,1);
//    	
//        User user = (User)request.getAttribute("user");
//        if(user != null) {
//            int userID = user.getUserId();
//            talkingComment.setTalkingCommentUser(userID);
//            return talkingCommentService.saveTalkingComment(talkingComment, orderId);
//        } else {
//            return CommonUtil.constructResponse(0, "未登陆", null);
//        }
//    }
    
    
  
    /**
     * 
     * @Title: lpySaveTalkingComment 
     * @Description: 添加评论
     * @param session
     * @param request
     * @param strTalkingComment
     * @param orderId
     * @return
     * @return: JSONObject
     */
    @RequestMapping("/lpySaveTalkingComment")
    @ResponseBody
    public JSONObject lpySaveTalkingComment(HttpSession session,HttpServletRequest request, String strTalkingComment, String orderId) {

    	User user= (User) session.getAttribute("user");
		if(user==null){
			return  CommonUtil.constructResponse(EnumUtil.NOT_LOGIN,"请登录",null);
		}
        
		
		
		if(strTalkingComment==null ||"".equals(strTalkingComment.trim())){
			return  CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL,"评价内容必须填写",null);
		}
		if(orderId==null ||"".equals(orderId.trim())){
			return  CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL,"订单id必须填写",null);
		}
		System.out.println("strTalkingComment.length==>"+strTalkingComment.length());
		
		if(strTalkingComment.length()>200){
			return  CommonUtil.constructResponse(EnumUtil.TOO_LANG,"评价内容必须少于200个字",null);
		}
		TalkingComment  talkingComment = new TalkingComment();
            int userID = user.getUserId();
            talkingComment.setTalkingCommentUser(userID);
            talkingComment.setTalkingCommentContent(strTalkingComment);
            try {
				Date time = CommonUtil.getSystemTime();
				talkingComment.setTalkingCommentTime(time);
				talkingComment.setTalkingUserName(user.getUserName());
				 return talkingCommentService.saveTalkingComment(talkingComment, orderId);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"系统错误",null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"系统错误",null);
			}
           
       
        
        
    }
}
