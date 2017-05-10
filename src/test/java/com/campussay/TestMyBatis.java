/**
 * @author effine
 * @Date 2016年1月11日  下午2:52:19
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay;

import com.campussay.dao.OrderDao;
import com.campussay.model.Area;
import com.campussay.service.AreaService;
import com.campussay.service.CampusService;
import com.campussay.service.OrderService;
import com.campussay.service.StudentCheckService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml", "classpath:spring-mvc.xml" })
public class TestMyBatis {
	@Resource
	private AreaService areaService;
	
	@Resource
	private OrderDao orderdao;

	@Resource
	private CampusService campusService;

	@Resource
	private StudentCheckService studentCheckService;


//	@Test
//	public void test1() {
//		Area area = areaService.getAreaById(1);
//		if (null != area) {
//			System.out.println(area.getAreaName());
//		} else {
//			System.out.println("无记录");
//		}
//	}

//	@Test
//	public void test2() {
//		studentCheckService.getStudentCheck(1);
//	}

//	@Test
//	public void test() {
//		System.out.print(orderService.getOrderByUserID("1","1",1));
//	}
//
	@Test
	public void testcloseOrder() {
		String orderId="0B53A115591A41C8BF56B1688974DF2C";
	}
}
