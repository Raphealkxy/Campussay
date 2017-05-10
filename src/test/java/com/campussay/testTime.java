package com.campussay;

import com.campussay.dao.TalkingDao;
import com.campussay.service.OrderQuartzService;

import cn.util.MD5;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pzy on 2016/3/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml", "classpath:spring-mvc.xml" })
public class testTime {
    @Autowired
    private TalkingDao talkingDao;
    @Autowired
    private OrderQuartzService orderQuartzService;
    @Test
    public void test1(){
        long currentTime = System.currentTimeMillis() + 120 * 60 * 1000;
        Date date = new Date(currentTime);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime="";
        nowTime= df.format(date);
        System.out.println(nowTime+"+10");
    }

    @Test
    public void test2(){
        orderQuartzService.CloseOrder();
    }
    
    //测试MD5加密
    @Test
    public void test3(){
    	String password="123456";
    	String passwd=MD5.MD5Encode(password, "utf-8");
    	System.out.println("加密之后的密码："+passwd);
    	if(MD5.MD5Encode("12345", "utf-8").equals(passwd)){
    		System.out.println("登陆成功");
    	}else{
    		System.out.println("密码错误");
    	}
    }

}
