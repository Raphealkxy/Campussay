package com.campussay.controller;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Answer;
import com.campussay.model.User;
import com.campussay.service.AnswerService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
 * Created by yangchun on 2016/1/28.
 */
@Controller
@RequestMapping("/answer")
public class AnswerController {

	@Autowired
	private AnswerService answerService;

	  /*
	   * 根据user_id得到回答
	   * 话题社部分的调用
    
	   */
	  @RequestMapping("getAllAnswerByUserId")
	  @ResponseBody
	  public JSONObject getAllAnswerByUserId(HttpSession session, @RequestParam(required = false)Integer userId, @RequestParam(required = false)Integer page){
		  HashMap<String,Object> list=null;
		  if(userId>0){
			  try{
				  list=answerService.selectAllAnswerByUserID(userId, page);
			  }catch(Exception e){
				  return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库错误",null);
			  }
			  if(list==null){
				  return CommonUtil.constructResponse(1,"数据库没有数据",list);
			  }
			  return CommonUtil.constructResponse(1,"获取数据成功",list);
		  }
		  if(userId==0){
			  User user=(User) session.getAttribute("user");
			  if(user==null){
				  return CommonUtil.constructHtmlResponse(-1, "请登录账户", null);
			  }
			  try{
				  list=answerService.selectAllAnswerByUserID(user.getUserId(), page);
			  }catch(Exception e){
				  return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库错误",null);
			  }
			  if(list==null){
				  return CommonUtil.constructResponse(1,"数据库没有数据",list);
			  }
			  return CommonUtil.constructResponse(1,"获取数据成功",list);
		  }
		 return CommonUtil.constructResponse(0,"获取用户信息失败",null);
	  }
    /*
     *得到answer数量通过topic_id
     */
    @RequestMapping("getAnswerNumByTopicId")
    @ResponseBody
    public JSONObject getAnswerNumByTopicId(int topic_id){
    	if(topic_id<0){
    		return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"传入参数非法",null);
    	}
    	HashMap<String,Object> answerNum=null;
    	try{
    		answerNum=answerService.getAnswerNumByTopicId(topic_id);
    	}catch(Exception e){
    		return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
    	}
    	if(answerNum==null){
    		return CommonUtil.constructResponse(1,"数据库没有数据",answerNum);
    	}
    	return CommonUtil.constructResponse(1,"获取数据成功",answerNum);
    	
    }
    /*
      * 根据topic_id得到所有回答
      * @param page 分页，为null时默认为1
      * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
      */
    @RequestMapping("getAnswersByTopicId")
    @ResponseBody
    public JSONObject getAnswersByTopicId(HttpSession session,Integer topic_id,@RequestParam(required = false)Integer page){
    	 User user=(User)session.getAttribute("user");
    	int userId=0;
    	if(page==null||page<0)
    		page=1;
    	HashMap list=null;
    	try{
    		if(user!=null){
    			userId=user.getUserId();
    		}
    		list=answerService.selectAnswersByTopicId(userId,topic_id, page);
    	}catch(Exception e){
    		return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
    	}
    	if(list==null){
    		return CommonUtil.constructResponse(1, "数据库没有数据", list);
    	}
    	return CommonUtil.constructResponse(1, "获取数据成功", list);
    }
    /*
    	对答案进行点赞
     */
    @RequestMapping("addIsLike")
    @ResponseBody
    public JSONObject addIsLike(HttpSession session, int answerId) throws Exception{
        User user=(User)session.getAttribute("user");
        if(user==null) {
            return CommonUtil.constructResponse(-1, "用户未登录", null);
        }
        int n=0;
        try{
        	n=answerService.addIsLike(user.getUserId(),user.getUserName(),answerId);
        }catch(Exception e){
    		return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
    	}
        if(n==0){
            return CommonUtil.constructResponse(0,"点赞失败！",null);
        }
        return CommonUtil.constructResponse(1,"点赞成功！",null);
    }
    /*
    对答案进行减赞
     */
    @RequestMapping("disIsLike")
    @ResponseBody
    public JSONObject disIsLike(HttpSession session,int answerId){
        User user=(User)session.getAttribute("user");int n=0;
    	if(user==null) {
        	return CommonUtil.constructResponse(-1, "用户未登录", null);
        }
    	try{
    		n=answerService.disIsLike(user.getUserId(),answerId);
    	}catch(Exception e){
     		return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
     	}
        if(n==0){
        	return CommonUtil.constructResponse(0,"减赞失败！",null);
        }
        return CommonUtil.constructResponse(1,"减赞成功！",null);
    }

    /*
     *添加回答topic
     */


    @RequestMapping("addAnswer")
    @ResponseBody
    public JSONObject addAnswer(HttpSession session,String context,int topic_id){
    	User user=(User) session.getAttribute("user");
        if(user==null){
            return CommonUtil.constructHtmlResponse(-1, "请登录账户", null);
        }
        Answer answer=new Answer();
        answer.setUid(user.getUserId());
        answer.setTopicId(topic_id);
        try {
			answer.setContext(context.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        answer.setTime(new Date());
        answer.setIslike(0);
        answer.setState(1);
        int n=0;
        try{
        	n=answerService.addAnswer(answer,user.getUserId(),user.getUserName());
        }catch(Exception e){
     		return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
     	}
        if(n==0){
            return CommonUtil.constructHtmlResponse(0, "添加失败", null);
        }else{
        	int lastPage=0;
        	try{
        		lastPage=answerService.getLastPageByTopicId(topic_id)/10;
        		if(answerService.getLastPageByTopicId(topic_id)%10!=0){
        			lastPage=lastPage+1;
        		}
        	}catch(Exception e){
         		return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
         	}
            HashMap<String,Integer> map=new HashMap<String,Integer>();
            map.put("lastPage",lastPage);
            map.put("topicId", topic_id);
            //发送消息，给关注用户
            return CommonUtil.constructHtmlResponse(1, "回答成功", map);
        }
    }
    

    @RequestMapping("getAnswerDetailById")
    @ResponseBody
    public JSONObject getAnswerDetailById(int answerId) {
		// TODO Auto-generated method stub
    	HashMap<String, Object> map=answerService.getAnswerDetailById(answerId);
    	return CommonUtil.constructHtmlResponse(1, "获取信息成功", map);
	}
    
}
