package com.campussay.controller;

import com.alibaba.fastjson.JSONObject;
import com.campussay.util.AuthCodeUtil;
import com.campussay.util.CommonUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

@Controller
public class PageController {

	/**
	 * 前端页面路径:话题社问答详情页面
	 */
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	/**
	 * 前端页面路径:话题社问答详情页面
	 */
	@RequestMapping("topic/answerdetail")
	public String topic_answer() {
		return "topic/htanswer";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("user/personal-info")
	public String personal_info() {
		return "personal-page/personal-info";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("user/personal-comment")
	public String personal_comment() {
		return "personal-page/personal-comment";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("user/sign-in-web")
	public String sign_in_web() {
		System.out.println("asd");
		return "sign-in/sign-in-web";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("talking/listenList")
	public String listen_talking_liste() {
		return "listen-talking-list";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("talking/ttIndex")
	public String ttIndex() {
		return "ttIndex";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("user/pwdFound/{step}")
	public String pwdFound(@PathVariable("step") String step) {
		return "pwd-found/step-"+step;
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("talking/classDetail")
	public String talkingInfo() {
		return "class-detail";
	}


	/**
	 * 前端页面路径
	 */
	@RequestMapping("talking/publish")
	public String publish() {
		return "publish-talk";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("talking/friends")
	public String friends() {
		return "talking-friends";
	}

	/**
	 * talking 修改页面，前端页面路径
	 */
	@RequestMapping("talking/edit")
	public String edit() {
		return "edit-talking";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("topic/hot")
	public String hot() {
		return "hot/hot";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("personalCenter/order")
	public String order() {
		return "personalCenter/order";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("personalCenter/circle")
	public String circle() {
		return "personalCenter/circle";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("personalCenter/publish")
	public String personalCenterPublish() {
		return "personalCenter/publish";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("personalCenter/account")
	public String account() {
		return "personalCenter/account";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("personalSetting/person")
	public String person() {
		return "personalSetting/person";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("personalSetting/security")
	public String security() {
		return "personalSetting/security";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("personalSetting/authentication")
	public String authentication() {
		return "personalSetting/authentication";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("user/personal-index-fans")
	public String personalIndexIans() {
		return "personal-index-fans";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("topic/field")
	public String filed() {
		return "topic/field";
	}
	
	/**
	 * 前端页面路径
	 */
	@RequestMapping("topic/answer")
	public String answer() {
		return "personal-page/answer-list";
	}

	/**
     * 前端页面路径
   	 */
    	@RequestMapping("user/personalIndex")
    	public String personalIndex() {
    		return "personal-index";
    	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("topic/htIndex")
	public String htIndex() {
		return "topic/htIndex";
	}

	/**
	 * 支付页面 
	 */
	@RequestMapping("pay")
	public String pay() {
		return "pay/pay";
	}

	/**
	 * 支付成功页面
	 */
	@RequestMapping("payresult")
	public String payresult() {
		return "pay/payresult";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("message")
	public String message() {
		return "message/message";
	}

	/**
	 * 前端页面路径
	 */
	@RequestMapping("about")
	public String about() {
		return "about/about";
	}

	/**
	 * 获取图片验证码
	 * @return 图片流
	 */
	@RequestMapping(value="code/getAuthCode")
	public void getPictureVerificationCode(HttpServletResponse response, HttpSession session,String type)throws Exception{
		if (type == null){
			return;
		}
		AuthCodeUtil authCodeUtil = AuthCodeUtil.Instance();
		String pictureVerificationCode = authCodeUtil.getString();
		ByteArrayInputStream image = authCodeUtil.getImage();
		session.setAttribute(type, pictureVerificationCode);
		OutputStream stream = response.getOutputStream();
		byte[] data = new byte[image.available()];
		image.read(data);
		stream.write(data);
		stream.flush();
		stream.close();
	}

	/**
	 * 图片验证码验证接口
	 */
	@RequestMapping("code/checkAuthcode")
	@ResponseBody
	public JSONObject checkAuthcode(HttpSession session,String type,String code){
		if (type == null || "".equals(type) || code==null || "".equals(code))
			return CommonUtil.constructResponse(0,"参数错误",null);
		Object sessionCode = session.getAttribute(type);
		if (code.equals(sessionCode)){
			session.removeAttribute(type);
			return CommonUtil.constructResponse(1,null,null);
		}else {
			return CommonUtil.constructResponse(0,"验证码错误",null);
		}

	}
	
	
	
	/*
	 * 返回下载页面
	 */
	@RequestMapping("download")
	public String download(){
		return "download";
	}

}