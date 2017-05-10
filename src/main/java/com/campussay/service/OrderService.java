/**
 * @author effine
 * @Date 2016年1月10日  下午8:35:33
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Order;
import com.campussay.util.EnumUtil.TalkingStatus;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OrderService {

	/**
	 * 根据用户名获得所有订单
	 *
	 * @param userID
	 *            用户id
	 * @param page
	 *            当前页码
	 * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
	 */
	public JSONObject getOrderByUserID(String userID, String state,int page);

	/**
	 * 根据用户名或者状态获得订单的分页数目
	 * @param userID
	 * @param state
     * @return
     */
	JSONObject getOrderByUserIDPageTotal(String userID, String state);

	/**
	 * 根据用户及订单状态获得不同的订单
	 *
	 * @param userID
	 *            用户id
	 * @param state
	 *            订单状态
	 * @param page
	 *            当前页码
	 * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
	 */
	public JSONObject getOrderByUserIDState(String userID,
			String state, int page);

	/**
	 *
	 */
	int getMyTalkingSaleInfoCount(int userId,Integer page);

	/**
	 *
	 */
	List<HashMap> getMyTalkingSaleInfo(int userId,Integer page,Integer state);
	
	
	/**
	 * 
	 * @Title: createOrder 
	 * @Description: 创建订单
	 * @param createOrder
	 */
	public TalkingStatus createOrder(Order createOrder) throws Exception;

	//根据order的id获取参与者姓名
//	public HashMap<String, Object> getTalkingInfo(String orderId);

	public HashMap<String, Object> selectOrderByOrderId(String orderId);

	/**
	 * 支付成功 修改订单状态
	 * @Title: paySuccess 
	 * @Description: TODO
	 * @param strOrderid
	 * @param strTradeNo
	 * @param payWay
	 * @throws Exception
	 * @return: void
	 */
	public void paySuccess(String strOrderid,String strTradeNo,int payWay) throws Exception;


	/*
	* 订单回复
	* */
	List<Map> OrderTalking(int orderTalking);
	
	 /**
     * 
     * @Title: selectUserOrderByStatus 
     * @Description: 根据不同条件查询用户订单信息
     * @param userID 用户id
     * @param state 订单状态
     * @param start 查询起始第几条
     * @param pageSize 分页 每页长度
     * @return
     * @return: List<HashMap>
     */
	public HashMap selectUserOrderByStatus(String userID,Integer state,int start, int pageSize);

	/*
	* 错误原因
	* */
	Map OrderReason(int orderTalking);
	/**
	 * 
	 * @Title: confirmTakingFinish 
	 * @Description: 确认订单已经完成
	 * @param orderId 订单id
	 * @param userId 用户ID
	 * @return: int 确认结果
	 */
	public int confirmTakingFinish(String orderId,String userId) throws ParseException,Exception;
	/**
	 * 
	 * @Title: changeOrderState 
	 * @Description: 修改订单状态
	 * @param orderId  订单id
	 * @param orderState 订单将要修改为的状态
	 * @param userId 用户id
	 * @throws Exception
	 * @return: void
	 */
	public void changeOrderState(String orderId,int orderState,String userId) throws Exception;
	/**
	 * 
	 * @Title: requestRefund 
	 * @Description: 用户申请退款
	 * @param orderId  订单id
	 * @param orderState 订单将要修改为的状态
	 * @param userId 用户id
	 * @param reason 退款原因
	 * @throws Exception
	 * @return: void
	 */
	int requestRefund(String orderId, String userId,String reason) throws ParseException, Exception;
	
	/**
	 * 
	 * @Title: selectOrderTitleAndUserName 
	 * @Description: 支付成功后 在最后页面根据订单ID的到 课程信息
	 * @param orderId
	 * @throws Exception
	 * @return: void
	 */
	public HashMap selectOrderTitleAndUserName(String orderId) throws 	Exception;

	/*
    * 根据talkingId查询用户信息
    *
    * */
	List<Map> mapTalkingLists(int talkingId);
	
	 //显示购买课程
    public abstract List<HashMap<String,Object>> BuyOrderSpend(int order_user);
    
    //提现
    public abstract List<HashMap<String,Object>>  cashrecord(int user_id);
    
    //是否存在订单
    public abstract HashMap<String, Object> isorderstate(int order_user,int order_talking);

    public int updateCloseOrder(String orderId,int state,String closeReason);
}
