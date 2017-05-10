package com.campussay.service.impl;

import com.campussay.dao.OrderDao;
import com.campussay.dao.TalkingDao;
import com.campussay.service.OrderQuartzService;
import com.campussay.util.PhoneSender;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service("OrderQuartzService")
public class OrderQuartzServiceImpl implements OrderQuartzService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private TalkingDao talkingDao;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED,rollbackFor=Exception.class,isolation=Isolation.REPEATABLE_READ)
	@Override
	public void UpdateNotPayOrders(String order_id,Integer order_talking_id) {
		System.out.println(order_talking_id);
		try{
			Map<String,Object> talking=talkingDao.getTaking(order_talking_id);
			int talking_now_persion=(int) talking.get("talking_now_persion");
			int talking_max_persion=(int) talking.get("talking_max_persion");
			if(talking_now_persion<talking_max_persion){
				orderDao.updateTalkingPeopleByOrder_id(order_talking_id);
			}
			orderDao.updateNotPayOrdersState(order_id);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public void UpdateNotPayOrder() {
		int count = 0;
		long millionSeconds = 0, millionSecond = 0;
		// 查询出所有没有发送短信的订单
		List<HashMap> mapList = orderDao.sendPhoneMessage();
		// System.out.println(mapList.size());
		for (HashMap map : mapList) {
			// 处理时间
			String date = map.get("talking_start_time").toString();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.m");
			try {
				millionSeconds = df.parse(date).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
				System.out.println("定时器时间异常");
			}
			// 判断开课前两小时发送短信
			millionSecond = System.currentTimeMillis() + 30 * 60 * 1000;
			String orderUserTel = map.get("order_user_tel").toString();
			String userName = map.get("user_name").toString();
			String userPhone = map.get("user_phone").toString();
			String talkingTitle = map.get("talking_title").toString();
			String orderId = (String) map.get("order_id");
			if (millionSecond > millionSeconds) {
				Map<String, Object> maps = new HashMap<>();
				maps.put("order_name", map.get("user_name"));
				maps.put("time", date);
				maps.put("talkingTitle", talkingTitle);
				maps.put("speak_userPhone", userPhone);
				maps.put("orderUserTel", orderUserTel);
				if (map.get("talking_is_online").toString().equals("-1")) { // 表示线下交流
					maps.put("talking_address", map.get("talking_address"));
					PhoneSender.sendOutLine(maps);
					// System.out.println("线下发送短信一次");
				} else { // 线上交流
					maps.put("talking_tool_num", map.get("talking_tool_num"));
					PhoneSender.sendOnline(maps);
					// System.out.println("线上发送短信一次");
				}
				orderDao.setPhoneMessage(orderId);
			}
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void SuccessPayOrder(String orderId) {
		try {
			Map map = orderDao.SuccessPayOrder(orderId);
			if (map == null) {
				throw new Exception("短信发送失败，订单异常");
			}
			if (map.get("order_user_tel") == null) {
				throw new Exception("短信发送失败，用户异常");
			}
			PhoneSender.sends(map);
			System.out.println("发送短信了");
		} catch (Exception e) {
			System.out.println("发送失败");
		}

	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void RefundSuccess(String orderId) {
		try {
			Map map = orderDao.RefundSuccess(orderId);
			if (map == null) {
				throw new Exception("短信发送失败，订单异常");
			}
			if (map.get("order_user_tel") == null) {
				throw new Exception("短信发送失败，用户异常");
			}
			PhoneSender.sendResund(map);
		} catch (Exception e) {
			System.out.println("发送失败");
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void RefundFail(String orderId) {
		try {
			Map map = orderDao.RefundFail(orderId);
			if (map == null) {
				throw new Exception("短信发送失败，订单异常");
			}
			if (map.get("order_user_tel") == null) {
				throw new Exception("短信发送失败，用户异常");
			}
			PhoneSender.ResundFail(map);
		} catch (Exception e) {
			System.out.println("发送失败");
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void CloseOrder() {
		
	}
}
