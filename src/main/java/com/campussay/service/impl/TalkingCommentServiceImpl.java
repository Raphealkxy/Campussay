
/**
 * @author effine
 * @Date 2016年1月10日  下午9:05:40
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.campussay.dao.OrderDao;
import com.campussay.dao.TalkingCommentDao;
import com.campussay.dao.TalkingDao;
import com.campussay.model.TalkingComment;
import com.campussay.service.TalkingCommentService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;
import com.campussay.util.InfomationUtil;

@Service
public class TalkingCommentServiceImpl implements TalkingCommentService{

	@Autowired
	private TalkingCommentDao talkingCommentDao;
	@Autowired
	private InfomationUtil infomationUtil;

	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private TalkingDao talkingDao;

	@Override
	public List<Map<String, Object>> getAllCommentByUser(int userId,
			Integer page) {
		// TODO  unimplements method stub
		if (page == null || page < 1)
			page = 1;
		return talkingCommentDao.getAllCommentByUser(userId,(page-1)*10,10);
	}

	@Override
	public int getAllCommentCountByUser(int userId) {
		return talkingCommentDao.getAllCommentCountByUser(userId);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public JSONObject saveTalkingComment(TalkingComment talkingComment, String orderId) throws Exception {

		
		
		HashMap order = orderDao.selectOrderByOrderId(orderId);
		if(order == null){
			return  CommonUtil.constructResponse(EnumUtil.ORDER_NOT_FOUND,"订单未找到",null) ;
		}
		try {
			 Integer order_talking=(Integer)order.get("order_talking");
		   
				
		     Integer order_user=(Integer)order.get("order_user");
		     Integer order_state=(Integer)order.get("order_state");
		     if(order_state == null){
		    	 return  CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL,"订单异常",null) ;
		     }
		     if(order_talking == null){
		    	 return  CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL,"订单异常",null) ;
		     }
		     if(order_user == null){
		    	 return  CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL,"订单异常",null) ;
		     }
		   
		     
		     if (order_state!=EnumUtil.ORDER_STATE_HAD_CONFIRM){
		    	 return  CommonUtil.constructResponse(EnumUtil.ORDER_STATE_NOT_RIGHTL,"订单状态错误",null) ;
		     }
		     //评价者用户id
		  int talkingUserId =    talkingComment.getTalkingCommentUser();
		     
		  if(!(talkingUserId+"").equals(order_user+"")){
		    	 return  CommonUtil.constructResponse(EnumUtil.ORDER_USER_NOT_NOW_USER,"订单用户不是当前操作用户",null) ;
		   }
		     
		  talkingComment.setTalkingCommenttalking(order_talking);
		  talkingCommentDao.saveTalkingComment(talkingComment);
		  orderDao.lpyUpdateOrderState(orderId, EnumUtil.ORDER_STATE_HAD_EVALUATE, null,talkingComment.getTalkingCommentUser()+"");
		  
		  
		Map talking =   talkingDao.getTaking(order_talking);
		
		
		
		
		if(talking == null){
			 return  CommonUtil.constructResponse(EnumUtil.ORDER_STATE_NOT_RIGHTL,"订单状态错误",null) ;
		}
		String talking_title =(String) talking.get("talking_title");
		  if (talking_title==null){
			  talking_title="未知";
		  }
		  
		  Integer talking_user =(Integer) talking.get("talking_user");
		  if (talking_user==null){
			  return  CommonUtil.constructResponse(EnumUtil.ORDER_NOT_NORMAL,"订单异常",null) ;
		  }
		  
		  
		  
		  
		  
		  //评价内容  被评价着的userid  消息类型
		  String talkingUserName=talkingComment.getTalkingUserName();//评价者的姓名
		  if(talkingUserName==null){
			talkingUserName="未知用户";
		  }
		
		 
		  String messageContent = "‘"+talkingUserName+"’评价了你的<a href=\"/talking/classDetail?tk="+order_talking+"\">"+talking_title+"</a>课程";
		  infomationUtil.createInformation(messageContent, talking_user.toString(), InfomationUtil.MESSAGE_TYPE_DYNAMIC_REMINDER);
		
		  return  CommonUtil.constructResponse(EnumUtil.OK,"评价成功",null) ;
		} catch (ClassCastException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	    
		

	}

}


