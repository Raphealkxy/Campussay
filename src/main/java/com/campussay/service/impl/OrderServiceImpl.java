
/**
 * @author effine
 * @Date 2016年1月10日  下午8:35:22
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.campussay.dao.ApplayRefundOrderDao;
import com.campussay.dao.ApplayToCashRecodeDao;
import com.campussay.dao.OrderDao;
import com.campussay.dao.TalkingDao;
import com.campussay.dao.UserDao;
import com.campussay.exception.MeanwhileCreateTalkingOrderException;
import com.campussay.exception.TalkingFullException;
import com.campussay.model.ApplayRefundOrder;
import com.campussay.model.Order;
import com.campussay.model.Talking;
import com.campussay.service.OrderService;
import com.campussay.util.CommonUtil;
import com.campussay.util.ComparatorRecord;
import com.campussay.util.EnumUtil;
import com.campussay.util.EnumUtil.TalkingStatus;

@Transactional
@Service
public class OrderServiceImpl implements OrderService{

	/**
	 * 申请退款申请中
	 */
	private static final int APPLAY_REFUND_ORDER_STATE_REQUEST = 0;
	
	@Resource
	private TalkingDao talkingDao;
	@Autowired
	private OrderDao orderDao;
	@Resource
	private UserDao userDao;
	@Resource
	private ApplayRefundOrderDao applayRefundOrderDao;
	@Resource
	private ApplayToCashRecodeDao applayToCashRecodeDao;
	
	public static int INFORECORDSIZE=0;

	/**
	 * 根据用户名获得所有订单
	 *
	 * @param userID 用户id
	 * @param page   当前页码
	 * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
	 */
	@Override
	public JSONObject getOrderByUserID(String userID, String state,int page) {
		int code;
		int start = (page-1)*10;
		int end = page * 10;
		List lists = orderDao.getOrderByUserID(userID, state,start, end);
		int count = orderDao.getOrderByUserIDPageTotal(userID, state);
		HashMap map = new HashMap();
		map.put("count",count);
		map.put("rows",lists);
		code = lists.size();
		if (code > 0) {
			code = 1;
		} else {
			code = 0;
		}
		JSONObject jsonObject = CommonUtil.constructResponse(code, lists.size() + "", map);
		return jsonObject;
	}

	/**
	 * 根据用户及订单状态获得不同的订单
	 *
	 * @param userID 用户id
	 * @param state  订单状态
	 * @param page   当前页码
	 * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
	 */
	@Override
	public JSONObject getOrderByUserIDState(String userID, String state,
			int page) {
		int code;
		int start = (page-1)*10;
		int end = page * 10;
		List lists = orderDao.getOrderByUserIDState(userID, state, start, end);
		code = lists.size();
		if (code > 0) {
			code = 1;
		} else {
			code = 0;
		}
		JSONObject jsonObject = CommonUtil.constructResponse(code, lists.size() + "", lists);
		return jsonObject;
	}

	@Override
	public int getMyTalkingSaleInfoCount(int userId,Integer state) {
		return orderDao.getMyTalkingSaleInfoCount(userId,state);
	}

	@Override
	public List<HashMap> getMyTalkingSaleInfo(int userId, Integer page, Integer state) {
		if (page == null || page < 1)
			page = 1;
		String userID=String.valueOf(userId);
		List<HashMap> result=new ArrayList<HashMap>();
		List<HashMap> list=new ArrayList<HashMap>();
		List<HashMap> list1=orderDao.getMyOrderRecord(userId);
		for(HashMap mp:list1){
			mp.put("stand", "1");
		}
		List<HashMap> list2=orderDao.getMyTalkingSaleRecord(userId);
		for(HashMap mp:list2){
			mp.put("stand", "2");
		}
		List<HashMap> list3=applayRefundOrderDao.getOneApplyRefundRecord(userId);
		for(HashMap mp:list3){
			mp.put("stand", "3");
		}
		List<HashMap> list4=applayToCashRecodeDao.getOneApplyToCashRecord(userID);
		for(HashMap mp:list4){
			mp.put("stand", "4");
		}
		list.addAll(list1);
		list.addAll(list2);
		list.addAll(list3);
		list.addAll(list4);
		INFORECORDSIZE=list.size();
	    ComparatorRecord<HashMap> comparator=new ComparatorRecord();
		Collections.sort(list,comparator);
		int end=0;
		if(page*10>list.size()){
			end=list.size();
		}else{
			end=page*10;
		}
		result=list.subList((page-1)*10, end);
		return result;
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public TalkingStatus createOrder(Order createOrder) throws Exception {
		// TODO Auto-generated method stub
		if (createOrder == null) {
			throw new Exception("传入的订单信息对象为null");
		} else {
			
			List<HashMap> order = orderDao.checkUserIsCreateOrder(createOrder);
			//检测用户是否已经下过订单
			if (order != null && order.size() > 0){
				return TalkingStatus.HADPARTICIPATE;
			}
			int orderTalking = createOrder.getOrderTalking();
			HashMap<String, Object> talkingInfo = (HashMap<String, Object>) talkingDao.getTakingInfo(orderTalking);
			if (talkingInfo == null) {
				return TalkingStatus.NOTFOUND;
			} else {
				double talking_parive = 0;
				try {
					
					talking_parive = (double) talkingInfo.get("talking_price");
					createOrder.setOrderPrice(talking_parive);
					
				} catch (Exception e) {
					e.printStackTrace();
					//抛出异常 事物回滚
					throw new Exception("课程价格转换为double失败或数据库插入失败");
				}
				
				//状态,0-订单作废,1-下单未付款，2-已经付款，3-交流完成，未确认，4-交流完成，未确认，5-已经评价
				// 10表示正在开课,100表示课程结束,0表示课程被删除 -1 未知
				int talking_state = -1;
				talking_state = (int) talkingInfo.get("talking_state");
				if (talking_state == 0) {
					// 没找到该课程
					return TalkingStatus.NOTFOUND;
				} else if (talking_state == 100) {
					return TalkingStatus.HADOVERDUE;
				} else if (talking_state == 10||talking_state==50||talking_state==55) {
					Date talking_start_time =(Date)	talkingInfo.get("talking_start_time");
					Date talking_end_time =(Date)	talkingInfo.get("talking_end_time");
					if (talking_end_time == null) {
						return TalkingStatus.NOTFOUND;
					}
				    Date nowDate =	CommonUtil.getSystemTime();
						
						
				     if(!talking_start_time.before(nowDate)){
					
					
				//	try {
						//修改影响行数
					     int updateLineNum = 	talkingDao.updateTalkingRemainNum(orderTalking);
					     
					     if (updateLineNum !=1){
					    	//抛出异常 事物回滚
					    	 throw new TalkingFullException();
					     }else{
					    	 
					    	// 课程最多人数
							//	int talking_max_persion = (int) talkingInfo.get("talking_max_persion");
								// 现在已经参加课程人数
								//int talking_now_persion = (int) talkingInfo.get("talking_now_persion");
								//if (talking_max_persion - talking_now_persion <= 0) {
									//return TalkingStatus.HADFULL;
								///} else {
					    	    createOrder.setOrder_end_time(talking_end_time);
									orderDao.createOrder(createOrder);
									//防止同一个用户同时操作创建订单
									List<HashMap> hadCreateOrder = orderDao.checkUserIsCreateOrder(createOrder);
									if (hadCreateOrder != null && hadCreateOrder.size() > 1){
										//抛出异常 事物回滚
										throw new MeanwhileCreateTalkingOrderException();
									}
									return TalkingStatus.CANUSER;
								//}
					     }
					//} catch (Exception e) {
					//	throw new Exception("talking_max_persion或者talking_now_persion转换数字出错");
					//}
				} else {
					return TalkingStatus.UNKOWNERRRO;
				}
				
			 }else{
			    	
		    	    talkingDao.updateTalkingStatus(Talking.TALKING_HAD_PASS, orderTalking);
		    	    return TalkingStatus.HADOVERDUE;
		    	
		    	
		    }
			}
		}
	}

	@Override
	public HashMap<String, Object> selectOrderByOrderId(String orderId) {
//		return  orderDao.getTalkingInfo(orderId);
		return orderDao.selectOrderByOrderId(orderId);
	}

	@Override
	public JSONObject getOrderByUserIDPageTotal(String userID, String state) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 支付成功后回调
	 */
	@Override
	public void paySuccess(String strOrderid, String strTradeNo, int payWay) throws Exception {
	
		try {
			orderDao.paySuccess(strOrderid, strTradeNo, payWay, CommonUtil.getSystemTime());
		} catch (Exception e) {
			// TODO: handle exceptio
			throw e;
		}
	}

	@Override
	public List<Map> OrderTalking(int orderTalking) {
		return orderDao.OrderTalking(orderTalking);
	}

	@Override
	public Map OrderReason(int orderTalking) {
		Map<String, Object> map = orderDao.OrderReason(orderTalking);
		if (map == null) {
			Map maps = new HashMap();
			maps.put("sum", 0);
			return maps;
		}
		return map;
	}
	public HashMap selectUserOrderByStatus(String userID, Integer state, int start, int pageSize) {
		
		try {
			HashMap restult = new HashMap();
			List<HashMap> resultList = orderDao.selectOrderByStatus(userID, state, start, pageSize);
			if  (resultList == null||resultList.size()<=0){
				return null;
			}else{
		        int dataCount = orderDao.selectOrderByStatusPage(userID, state);
				restult.put("resultList", resultList);
				restult.put("dataCount", dataCount);
				restult.put("pageSize", pageSize);
				restult.put("system_time", CommonUtil.getStrSystemTime());
				return restult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		
		
	}
	/**
	 * 
	 * @Title: confirmTakingFinish 
	 * @Description: 确认订单已经完成
	 * @param orderId
	 * @return: int 确认结果
	 */
	@Transactional(rollbackFor=Exception.class)
	@Override
	public int confirmTakingFinish(String orderId,String userId) throws ParseException,Exception {
	     Date orderConfirmTime;
		try {
			orderConfirmTime = CommonUtil.getSystemTime();
			HashMap orderInfo = orderDao.selectOrderByOrderId(orderId);
			if(orderInfo==null){
				return EnumUtil.ORDER_NOT_FOUND;
			}
			
			Integer order_user = (Integer)orderInfo.get("order_user");
			//订单异常
			if (order_user==null){
				return EnumUtil.ORDER_NOT_NORMAL;
			}
			
			  if(!(userId+"").equals(order_user.toString())){
				  return EnumUtil.ORDER_USER_NOT_NOW_USER;
			  }
			
			
			Integer order_state =(Integer) orderInfo.get("order_state"); 
			//订单状态当前不是课程完成等待确认状态，说着异常
			if(order_state.intValue()!= EnumUtil.ORDER_STATE_NOT_CONFIRM){
				return EnumUtil.ORDER_STATE_NOT_RIGHTL;
			}
			
			
			Integer order_talking =(Integer) orderInfo.get("order_talking"); 
			//订单异常
			if (order_talking==null){
				return EnumUtil.ORDER_NOT_NORMAL;
			}
			
		Map talking_info = 	talkingDao.getTaking(order_talking);
			
		if (talking_info == null){
			return EnumUtil.ORDER_NOT_NORMAL;
		}
		
		  Integer i_talking_user =(Integer)talking_info.get("talking_user");
			
			if(i_talking_user == null){
				return EnumUtil.ORDER_NOT_NORMAL;
			}
			
			Double o_order_price =(Double) orderInfo.get("order_price");
			if(o_order_price == null){
				return EnumUtil.ORDER_NOT_NORMAL;
			}else{
				
				double   d_order_price   =   o_order_price.doubleValue();
				BigDecimal   big_order_price   =   new   BigDecimal(d_order_price);  
				double   cash   =   big_order_price.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
				userDao.updateUserBalance(cash, i_talking_user.toString()); 
				orderDao.lpyUpdateOrderState(orderId, EnumUtil.ORDER_STATE_HAD_CONFIRM, orderConfirmTime,userId);
				return EnumUtil.OK;
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
	}

	
	
	
	/**
	 * 
	 * @Title: requestRefund 
	 * @Description: 申请退款
	 * @param orderId
	 * @param reason 退款原因
	 * @return: int 确认结果
	 */
	@Transactional(rollbackFor=Exception.class)
	@Override
	public int requestRefund(String orderId,String userId,String reason) throws ParseException,Exception {
	     Date orderConfirmTime;
		try {
			orderConfirmTime = CommonUtil.getSystemTime();
			HashMap orderInfo = orderDao.selectOrderByOrderId(orderId);
			if(orderInfo==null){
				return EnumUtil.ORDER_NOT_FOUND;
			}
			
			Integer order_user = (Integer)orderInfo.get("order_user");
			//订单异常
			if (order_user==null){
				return EnumUtil.ORDER_NOT_NORMAL;
			}
			
			  if(!(userId+"").equals(order_user.toString())){
				  return EnumUtil.ORDER_USER_NOT_NOW_USER;
			  }
			
			
			Integer order_state =(Integer) orderInfo.get("order_state"); 
			//订单状态当前不是课程等待确认状态，修改申请退款，是在确认课程的同一时间 说明异常
			if(order_state.intValue()!= EnumUtil.ORDER_STATE_NOT_CONFIRM ){
				return EnumUtil.ORDER_STATE_NOT_RIGHTL;
			}
			//
			
			Double o_order_price =(Double) orderInfo.get("order_price");
			if(o_order_price == null){
				return EnumUtil.ORDER_NOT_NORMAL;
			}else{
				
				
				orderDao.requestRefundOrder(orderId, EnumUtil.ORDER_STATE_HAD_REFUND_ING, reason, userId);	
				
				ApplayRefundOrder applayRefundOrder = new ApplayRefundOrder();
				applayRefundOrder.setApplay_refund_id(CommonUtil.GUID().replace("_", ""));
				applayRefundOrder.setApplay_refund_order_reason(reason);
				applayRefundOrder.setApplay_refund_order_state(APPLAY_REFUND_ORDER_STATE_REQUEST);
				applayRefundOrder.setApplay_refund_order_time(CommonUtil.getSystemTime());
				applayRefundOrder.setApplay_refund_order_user(userId);
				applayRefundOrder.setApply_refund_order_id(orderId);
				applayRefundOrderDao.addRecode(applayRefundOrder);;
				
				return EnumUtil.OK;
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
	}

	/**
	 * 
	 * @Title: changeOrderState 
	 * @Description: 修改订单状态
	 * @param orderId
	 * @param orderState
	 * @throws Exception
	 * @return: void
	 */
	@Override
	public void changeOrderState(String orderId,int orderState,String userId) throws Exception {
		orderDao.lpyUpdateOrderState(orderId, orderState, null,userId);
	}

	@Override
	public HashMap selectOrderTitleAndUserName(String orderId) throws Exception {
		try {
			return orderDao.selectOrderTitleAndUserName(orderId);
		} catch (Exception e) {
			throw e;
		}
		
	}

	@Override
	public List<Map> mapTalkingLists(int talkingId) {
		List<Map> mapList;
		mapList=orderDao.mapTalkingLists(talkingId);
		if(mapList.size()<=0){
			mapList=new ArrayList<>();
			Map map=new HashMap();
			map.put("sum",0);
			mapList.add(map);
		}
		return mapList;
	}

//	public HashMap<String,Object>getTalkingInfo(String orderId){
//		return orderDao.getTalkingInfo(orderId);
//	}

	@Override
	public List<HashMap<String, Object>> BuyOrderSpend(int order_user) {
		List<HashMap<String, Object>> list=new ArrayList();
		try{
			list=orderDao.BuyOrderSpend(order_user);
		}catch (Exception e) {
			throw e;
		}
		return list;
	}

	@Override
	public List<HashMap<String, Object>> cashrecord(int user_id) {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> list=new ArrayList();
		try{
			list=orderDao.cashrecord(user_id);
		}catch (Exception e) {
			throw e;
		}
		return list;
	}

	@Override
	public HashMap<String, Object> isorderstate(int order_user, int order_talking) {
		HashMap map=new HashMap();
		try{
			map=orderDao.isorderstate(order_user, order_talking);
		}catch (Exception e) {
			throw e;
		}
		return map;
	}

	@Override
	public int updateCloseOrder(String orderId, int state, String closeReason) {
		// TODO Auto-generated method stub
		return orderDao.updateCloseOrder(orderId,state,closeReason);
	}


}


