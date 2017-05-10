package com.campussay.util;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.campussay.dao.OrderDao;
import com.campussay.dao.TalkingDao;
import com.campussay.service.OrderQuartzService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description 定时器任务,每隔一分钟执行一次,查找order_state=1的订单,order_creat_time
 * @author gr
 *
 */
@Transactional(readOnly = true)
public class SpringQtz {
	@Autowired
	private TalkingDao talkingDao;
	@Autowired
	private OrderDao orderDao;
	
	@Autowired 
	private OrderQuartzService orderQuartzService;

	//课程添加
	
	protected void execute() {
		// long ms = System.currentTimeMillis();
		System.out.println("45分钟关闭订单，修改talking人数");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String end_time = df.format(new Date());
		List<Map<String, Object>> notPayOrders = orderDao.getAllNotPayOrders();
		for (int i = 0; i < notPayOrders.size(); i++) {
			Map<String, Object> map = notPayOrders.get(i);
			String begin_time = map.get("order_creat_time").toString();
			// 传入订单创建时间和当前系统做比较,大于20则订单作废,并将课程剩余人数名额+1,需要做一个事物回滚
			if (CommonUtil.countTime(begin_time, end_time) > 45) {
				orderQuartzService.UpdateNotPayOrders(map.get("order_id").toString(),(Integer) map.get("order_talking"));
			}
		}
	}
	/*
    * 课程定时发送短信
    * */
	protected void executeSource(){
		System.out.println("课程定时发送短信");
		orderQuartzService.UpdateNotPayOrder();
	}
    /*
    * 修改订单
    * */
	protected void executeOrder(){
		System.out.println("修改订单");
		orderQuartzService.UpdateNotPayOrder();
		orderDao.updateOrderStateToFinish();
	}

	/*
	* 45分钟之后关闭未付款订单
	* */
//	protected void closeOrder(){
//		System.out.print("45分钟关闭订单----");
//		orderQuartzService.CloseOrder();
//	}
}
