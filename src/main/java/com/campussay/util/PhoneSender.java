package com.campussay.util;

import com.campussay.model.PhoneMessage;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangwenxiang on 15-12-25.
 */
public class PhoneSender {
	// 注册短信
	public static int send(PhoneMessage message) {
		TaobaoClient client = new DefaultTaobaoClient(
				"http://gw.api.taobao.com/router/rest", "23335486",
				"5f28a82bedd8956f4038aedd462d295f");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName(message.getSmsFreeSignName());
		req.setSmsParamString("{\"code\":\"" + message.getCode()
				+ "\",\"product\":\"校园说\"}");
		req.setRecNum(message.getPhoneNumber());
		req.setSmsTemplateCode(message.getSmsTemplateCode());
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
		} catch (ApiException ae) {
			return -1;
		}
		return 1;
	}


    //开课前两小时通知(线上)
    public static int sendOnline(Map maps){
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest",
                "23335486", "5f28a82bedd8956f4038aedd462d295f");
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType("normal");
        req.setSmsFreeSignName("活动验证");
        req.setSmsParamString("{\"name\":\""+maps.get("order_name").toString()+"\",\"cource\":\""+"《"+maps.get("talkingTitle").toString()+"》"+"\",\"time\":\""+maps.get("time").toString()+"\",\"way\":\""+"QQ/微信："+maps.get("talking_tool_num").toString()+"\",\"phone\":\""+maps.get("speak_userPhone").toString()+"\"}");
        //用户的电话
        req.setRecNum(maps.get("orderUserTel").toString());
        req.setSmsTemplateCode("SMS_7280583 ");
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try{
            rsp = client.execute(req);
        } catch (ApiException ae){
            return -1;
        }
        return 1;
    }

    //开课前两小时通知(线下)
    public static int sendOutLine(Map maps){
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest",
                "23335486", "5f28a82bedd8956f4038aedd462d295f");
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType("normal");
        req.setSmsFreeSignName("活动验证");
        req.setSmsParamString("{\"name\":\""+maps.get("order_name").toString()+"\",\"cource\":\""+"《"+maps.get("talkingTitle").toString()+"》"+"\",\"time\":\""+maps.get("time").toString()+"\",\"address\":\""+maps.get("talking_address").toString()+"\",\"phone\":\""+maps.get("speak_userPhone").toString()+"\"}");
        req.setRecNum(maps.get("orderUserTel").toString());
        req.setSmsTemplateCode("SMS_7240619");
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try{
            rsp = client.execute(req);
        } catch (ApiException ae){
            return -1;
        }
        return 1;
    }

	// 购买talking
	// 告诉发布课程的人某某参加了他的某某课程
	public static int sends(Map maps) {
		TaobaoClient client = new DefaultTaobaoClient(
				"http://gw.api.taobao.com/router/rest", "23335486",
				"5f28a82bedd8956f4038aedd462d295f");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName("活动验证");
		req.setSmsParamString("{\"name\":\""
				+ maps.get("user_name").toString()
				+ "\",\"cource\":\"" + maps.get("talking_title").toString()
				+ "\"}");
		req.setRecNum(maps.get("user_phone").toString());
		req.setSmsTemplateCode("SMS_7125157");
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
		} catch (ApiException ae) {
			return -1;
		}
		return 1;
	}


	// 退款成功
	public static int sendResund(Map map) {
		TaobaoClient client = new DefaultTaobaoClient(
				"http://gw.api.taobao.com/router/rest", "23335486",
				"5f28a82bedd8956f4038aedd462d295f");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName("活动验证");
		req.setSmsParamString("{\"course\":\""
				+ map.get("talking_title").toString() + "\"}");
		req.setRecNum(map.get("order_user_tel").toString());
		req.setSmsTemplateCode("SMS_7155116");
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
		} catch (ApiException ae) {
			return -1;
		}
		return 1;
	}

	// 退款失败
	public static int ResundFail(Map map) {
		TaobaoClient client = new DefaultTaobaoClient(
				"http://gw.api.taobao.com/router/rest", "23335486",
				"5f28a82bedd8956f4038aedd462d295f");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName("活动验证");
		req.setSmsParamString("{\"course\":\""
				+ map.get("talking_title").toString() + "\"}");
		req.setRecNum(map.get("order_user_tel").toString());
		req.setSmsTemplateCode("SMS_6075078");
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
		} catch (ApiException ae) {
			return -1;
		}
		return 1;
	}

	public static void main(String[] arg0) {
		
//		Map<String, Object> mp = new HashMap<String, Object>();
//		mp.put("order_user_realname", "test1");
//		mp.put("order_user_tel", "18723503620");
//		mp.put("talking_title", "asdasd");
//		sends(mp);
	}
}
