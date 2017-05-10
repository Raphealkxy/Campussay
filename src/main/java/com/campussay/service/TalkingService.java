/**
 * @author effine
 * @Date 2016年1月10日  下午9:07:14
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Talking;
import com.campussay.util.EnumUtil.TalkingStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TalkingService {

	/**
	 * 获取talking首页banner图片
	 */
	public abstract Map<String,Object> getBannerPicture() ;
	/*
	 * @author hdn Talking详情页 基础数据获取
	 * Talking和Talking课程为null返回null,Talking图片允许没有,因为有张主图片
	 */
	public abstract Map<String, Object> TalkingShow(int Talking_id) throws Exception;
	
	/**
	 * wzh
	 * 返回用户id
	 * @throws Exception 
	 */
	public abstract int userisbuy(int taking_id,int user_id) throws Exception;
	/*
	 * @author hdn Talking详情页 详情板块
	 */
	public abstract Map<String, Object> TalkingDetails(int Talking_id);

	/*
	 * @author hdn Talking详情页 安排板块
	 */
	public abstract List<Map<String, Object>> TalkingClass(int Talking_id);

	/*
	 * @author hdn Talking详情页 评价板块
	 */
	public abstract List<Map<String, Object>> TalkingComment(int Talking_id,
			int page) throws Exception;

	/*
	 * @author hdn Talking详情页 评价板块 评价数
	 */
	public abstract int countComment(int Talking_id);

	/*
	 * @author hdn Talking详情页 立刻参与
	 */
	public abstract int joinTalking(int Talking_id, int user_id) throws Exception;
	/*
	 * @author hdn 说友其他课程
	 */
	public abstract List<Map<String,Object>> getothertalking(int talking_user) throws Exception;
	/*
	 * @author hdn Talking发布页 获取用户通信信息
	 */
	public abstract Map<String, Object> userContact(int user_id) throws Exception;
	
	/*
	 * @author hdn Talking发布页 获取Talking类别
	 */
	public abstract List<Map<String, Object>> TalkingType(
			int Talking_type_parent) throws Exception;
	/*
	 * @author wzh
	 * 获取user_id
	 * @param TalkingId talkingid
	 */
	public abstract int getUserID(int TalkingId) throws Exception;
	/*
	 * @author wzh taking详情页 说有介绍  @param user_id 用户id
	 */
	public Map<String,Object> getUserDetails(int user_id) throws Exception;
	/*
	 * @author hdn Talking详情页 提交Talking
	 */
	public abstract int newTalking(Talking Talking);
	
	/*
	 * @author wzh taking发布页  获取获取一级taking
	 */
	public  abstract List<Map<String,Object>> firsttakingType() throws Exception;
	/*
	 * @author wzh 获得学校名称
	 */
	public abstract String getUserCampusName(Integer user_id) throws Exception;
	public abstract int getUserCampusId(String campus_name) throws Exception;
	
	/*
	 * @author wzh 发布课程
	 */
	public abstract int CreateNewTalking(Talking talking) throws Exception;
	/*
	 * @author wzh 编辑课程
	 */
	public abstract Map<String,Object> gettalkingdetails(int talkingid) throws Exception;
	/*
	 * @author wzh taking发布页 修改已有talking
	 */
	public abstract int updatetalkingdetails(Talking t) throws Exception;
	
	/**
	 * @author wzh
	 * taking发布页
	 * 精品talking
	 * @throws Exception 
	 */
	public abstract List<Map<String,Object>> Specialtalking(int talking_campus) throws Exception;
	/**
	 * 返回听听堂首页根节点
	 * @param null
	 * @return 
	 * @author JavaGR_ais
	 */
	public abstract JSONArray ShowHomePageNode();


	/**
	 * 听听堂分类列表,一次性封装好并返回分类,城市,学校。 
	 * @param null
	 * @return 
	 * @author JavaGR_ais
	 */
	public abstract JSONObject ShowTalkingCf();

	/**
	 * 听听堂分类,组合查询课程或者说友。
	 * @param LeafNode_id
	 * @param RootNode_id
	 * @param Node_id
	 * @param city_id
	 * @param campus_id
	 * @param pageCount
	 * @return
	 * @author JavaGR_ais
	 */
	public abstract JSONObject ShowTalkingByCfOrCyOrCs(int LeafNode_id,
			int RootNode_id,  int city_id, int campus_id,
			int pageCount,int sex,int type);
	
	/**
	 * 听听堂分类,组合查询说友。
	 * @param LeafNode_id
	 * @param RootNode_id
	 * @param Node_id
	 * @param city_id
	 * @param campus_id
	 * @param pageCount
	 * @param type
	 * @return
	 * @author JavaGR_ais
	 */
	public abstract JSONObject ShowFriendsSaidByCfOrCyOrCs(int LeafNode_id,
			int RootNode_id, int Node_id, int city_id, int campus_id,
			int pageCount);
	
	//
	public abstract List<Map<String,Object>> SelectT_taking_leafNodesByNode_id(int taking_type_id);

	/**
	 * wangwenxiang
	 * 获取用户的发布的talking
	 * @param userId 用户id
	 * @param page 分页
	 */
	public abstract List<HashMap<String, Object>> getUserTalking(int userId,
			Integer page,Integer state);

	/**
	 * wangwenxiang
	 * 获取用户的发布的talking
	 * @param userId 用户id
	 */
	abstract int getUserTalkingCount(int userId,Integer state);

	/**
	 * wangwenxiang
	 * 获取他所参与（购买）的talking信息列表
	 * @param userId 用户id
	 * @param page 分页
	 */
	public abstract List<HashMap<String, Object>> getUserBuyTalking(int userId,
			Integer page,Integer state);


	int getUserBuyTalkingCount(int userId,Integer state);


	boolean delTalking(int userId,int talkingId);
	
	
	/**
	 * @author liupengyan
	 * @Title: checkTalkingStatus 
	 * @Description: 检查talking当前的状态，状态可能为过期或者已经爆满</br>
	 * @param talkingId
	 * @return
	 * @return: int talking当前是否可以下单状态</br>
	 *  10表示正在开课,100表示课程结束,0表示课程被删除  －1 表示课程已经爆满 1 表示可以下单
	 *                                      
	 */
	public TalkingStatus checkTalkingStatus(Integer talkingId) throws Exception;
	
	/**
	 * 
	 * @Title: getTalkingInfoForCreateOrder 
	 * @Description: 得到课程信息 主要用于下单时 确认产品信息
	 * @param talkingId
	 * @return
	 * @return: HashMap<String,Object>
	 */
	public HashMap<String, Object> getTalkingInfoForCreateOrder(int talkingId);
	
	public String SelectCityNameByCity_id(int city_id);
	
	/**
	 * 
	 * @Title: getTalkingInfoAndPhoneNUM 
	 * @Description: 根据课程 的到课程名称和用户手机号
	 * @param talkingId
	 * @return
	 * @return: HashMap<String,Object>
	 */
	public HashMap<String, Object> getTalkingInfoAndPhoneNUM(int talkingId);
	public int judgeIsDelTalking(int talking_id);
	
	public Map<String,Object> getUserTalkingInfo(int talking_id);
	
}
