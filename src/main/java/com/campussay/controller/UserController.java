package com.campussay.controller;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.campussay.exception.CashTooMuchException;
import com.campussay.model.Mail;
import com.campussay.model.PhoneMessage;
import com.campussay.model.User;
import com.campussay.service.CampusExperienceService;
import com.campussay.service.EducationService;
import com.campussay.service.PrizeService;
import com.campussay.service.TalkingService;
import com.campussay.service.TalkingTypeService;
import com.campussay.service.UserService;
import com.campussay.service.WorkExprienceService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;
import com.campussay.util.InfomationUtil;
import com.campussay.util.MailSender;
import com.campussay.util.PhoneSender;
import com.campussay.util.PropertiesUtils;
import com.campussay.util.SftpUtils;
import com.campussay.util.StringUtil;

import cn.util.MD5;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private WorkExprienceService workExprienceService;

	@Autowired
	private EducationService educationService;

	@Autowired
	private PrizeService prizeService;

	@Autowired
	private CampusExperienceService campusExperienceService;

	@Autowired
	private TalkingService talkingService;

	@Autowired
	private TalkingTypeService talkingTypeService;
	
	@Autowired
	private InfomationUtil infomationUtil;

	int pageNum=10;



	@RequestMapping("isLogin")
	@ResponseBody
	public JSONObject isLogin(HttpSession session){
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(-1, "你还未登录", null);
		}
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userName",user.getUserName());
			return CommonUtil.constructResponse(1, null, jsonObject);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}

	/**
	 * 用户登陆
	 * 验证用户是否存在
	 * 验证用户是否正常
	 * 验证用户密码
	 *
	 * @param account  用户账号
	 * @param password 用户密码
	 * @return json
	 */
	@RequestMapping(value = "/userLogin")
	@ResponseBody
	public JSONObject userLogin(@RequestParam("account") String account,
								String password, HttpSession session)  {
		//String password1=CommonUtil.KL(password);
		//System.out.println(password1);
		int loginType = 0;
		if (!StringUtil.checkEmail(account)) {
			if (!StringUtil.checkPhoneNumber(account)) {
				return CommonUtil.constructResponse(0, "请输入正确的手机号码", null);
			}
			loginType = 1;
		}
		try {
			User user = userService.getUserByAccount(loginType, account);
			if (user == null) {
                return CommonUtil.constructResponse(0, "该账号没有注册", null);
            }
			if (user.getUserState() == 0) {
                return CommonUtil.constructResponse(0, "该账号已经冻结", null);
            }
			if (password != null && MD5.MD5Encode(password, "utf-8").equals(user.getUserPassword())) {
				/*System.out.println("mima:"+user.getUserPassword());
				System.out.println("huanyuan:"+CommonUtil.KL(user.getUserPassword()));*/
                user.setUserPassword(null);
                session.setAttribute("user", user);
                return CommonUtil.constructResponse(1,"登陆成功", null);
            } else {
                return CommonUtil.constructResponse(0, "密码错误", null);
            }
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("this is a error"+e);
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}

	@RequestMapping("signout")
	@ResponseBody
	public JSONObject signout(HttpSession session){
		try {
			session.removeAttribute("user");
			return CommonUtil.constructResponse(1,"成功下线",null);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}

	/**
	 * 用户注册第一步，获取用户邮箱或者手机号码
	 * 验证用户邮箱、手机格式
	 * 验证数据库是否已经注册
	 * 产生数字验证码，存入session和application
	 *
	 * @param request 通过request获取全局变量application和session
	 * @param account 获取用户邮箱
	 * @return 返回结果
	 */
	@RequestMapping("register1")
	@ResponseBody
	public JSONObject register1(HttpServletRequest request, String account) {
		int registerType = 0;
		if (!StringUtil.checkEmail(account)) {
			if (!StringUtil.checkPhoneNumber(account)) {
				return CommonUtil.constructResponse(0, "请输入正确的手机号码", null);
			}
			registerType = 1;
		}
		int haveAccount = userService.checkMailorPhone(registerType, account);
		if (haveAccount != 0) {
			if (haveAccount == -1)
				return CommonUtil.constructResponse(0, "注册类型错误", null);
			return CommonUtil.constructResponse(0, "该" + (registerType == 0 ? "邮箱" : "手机号码") + "已经被注册", null);
		}
		HttpSession session = request.getSession();
		String code = StringUtil.getRandomCode(4);
		if (registerType == 0) {
			String content = "你好，验证码为：" + code;
			Mail mail = new Mail("注册验证", account, content);
			try {
				MailSender.send(mail);
			} catch (MessagingException me) {
				return CommonUtil.constructResponse(0, "服务器异常", null);
			}
		} else if (registerType == 1) {
			PhoneMessage phoneMessage = new PhoneMessage(account, code, "注册验证", "SMS_7125144");
			PhoneSender.send(phoneMessage);
		}
		session.setAttribute("registerAccount", account);
		session.setAttribute("register" + account, code);
		return CommonUtil.constructResponse(1, null, null);
	}

	/**
	 * 用户注册第二步，验证邮箱、手机验证码
	 * 从session中获取第一步中的邮箱、手机
	 * 通过邮箱获取application中的code并验证
	 *
	 * @param code 用户填写的验证码
	 * @return json
	 */
	@RequestMapping("register2")
	@ResponseBody
	public JSONObject register2(HttpSession session, String code, String userName, String userPassword) {
		if (code == null || "".equals(code)) {
			return CommonUtil.constructResponse(0, "验证码未知错误", null);
		}
		Object account = session.getAttribute("registerAccount");
		if (account == null || "".equals(account)) {
			return CommonUtil.constructResponse(0, "验证码超时，请重新发送", null);
		}
		String session_code = session.getAttribute("register" + account).toString();
		if (code.equals(session_code)) {  //如果验证码正确
			if (userName == null || "".equals(userName) || userPassword == null || "".equals(userPassword)) {
				return CommonUtil.constructResponse(0, "用户信息错误", null);
			}
			session.removeAttribute("register" + account);
			int registerType = 0;
			User user = new User();
			if (!StringUtil.checkEmail(account.toString())) {
				if (!StringUtil.checkPhoneNumber(account.toString())) {
					return CommonUtil.constructResponse(0, "请输入正确的手机号码", null);
				}
				registerType = 1;
			}
			if (registerType == 0) {
				user.setUserMail(account.toString());
			} else if (registerType == 1) {
				user.setUserPhone(account.toString());
			}
			user.setUserName(userName);
			
			//将密码进行MD5加密，然后放入数据库中
			String passwd=MD5.MD5Encode(userPassword, "utf-8");
			user.setUserPassword(passwd);
			user.setUserRegisterTime(new Date());
			if (userService.addUser(registerType, user) == 1) {
				JSONObject jb = new JSONObject();
				jb.put("account", account);
				return CommonUtil.constructResponse(1, "success", jb);
			} else {
				return CommonUtil.constructResponse(0, "系统错误", null);
			}
		}
		return CommonUtil.constructResponse(0, "验证码错误", null);
	}

	/**
	 * 注册第三步，填写真实姓名和密码，存入数据库
	 * 验证前两步是否合法
	 * @param request request
	 * @param user_name 用户真实姓名、昵称
	 * @param user_password 用户密码
	 * @return json
	 *//*
	@RequestMapping("register3")
	@ResponseBody
	public JSONObject register3(HttpServletRequest request,String user_name,String user_password){
		if(user_name == null || "".equals(user_name) || user_password == null || "".equals(user_password)){
			return CommonUtil.constructResponse(0,"用户信息错误",null);
		}
		HttpSession session = request.getSession();
		Object account= session.getAttribute("registerAccount");
		if(account == null || "".equals(account)){
			return CommonUtil.constructResponse(0,"非法用户",null);
		}
		session.removeAttribute("registerAccount");
		Object isTrue =session.getAttribute("registered"+account.toString());
		if(isTrue == null || !(Boolean) isTrue){
			return CommonUtil.constructResponse(0,"用户验证失败",null);
		}
		session.removeAttribute("registered"+account.toString());
		User user = new User();
		int registerType = 0;
		if (!StringUtil.checkEmail(account.toString())){
			if (!StringUtil.checkPhoneNumber(account.toString())){
				return CommonUtil.constructResponse(0,"格式错误",null);
			}
			registerType = 1;
		}
		if (registerType == 0){
			user.setUserMail(account.toString());
		}else if (registerType == 1){
			user.setUserPhone(account.toString());
		}
		user.setUserName(user_name);
		user.setUserPassword(user_password);
		user.setUserRegisterTime(new Date());
		if(userService.addUser(registerType,user) == 1){
			JSONObject jb = new JSONObject();
			jb.put("account",account);
			return CommonUtil.constructResponse(1,"success",jb);
		}else{
			return CommonUtil.constructResponse(0,"系统错误",null);
		}
	}*/

	/**
	 * 找回密码第一步，获取用户邮箱或者手机号码
	 * 验证用户邮箱、手机格式
	 * 验证数据库是否已经注册
	 * 产生数字验证码，存入session
	 *
	 * @param request 通过request获取session
	 * @param account 获取用户邮箱或者手机
	 * @return 返回结果
	 */
	@RequestMapping("findPassword1")
	@ResponseBody
	public JSONObject findPassword1(HttpServletRequest request, String account) {
		int accountType = 0;
		if (!StringUtil.checkEmail(account)) {
			if (!StringUtil.checkPhoneNumber(account)) {
				return CommonUtil.constructResponse(0, "请输入正确的手机号码", null);
			}
			accountType = 1;
		}
		int haveAccount = userService.checkMailorPhone(accountType, account);
		if (haveAccount != 1) {
			if (haveAccount == -1)
				return CommonUtil.constructResponse(0, "账号类型错误", null);
			return CommonUtil.constructResponse(0, "该" + (accountType == 0 ? "邮箱" : "手机号码") + "没有被注册", null);
		}
		HttpSession session = request.getSession();
		String code = StringUtil.getRandomCode(4);
		if (accountType == 0) {
			String content = "你好，验证码为：" + code;
			Mail mail = new Mail("找回密码", account, content);
			try {
				MailSender.send(mail);
			} catch (MessagingException me) {
				return CommonUtil.constructResponse(0, "服务器异常", null);
			}

		} else if (accountType == 1) {
			PhoneMessage phoneMessage = new PhoneMessage(account, code, "变更验证", "SMS_7125141");
			PhoneSender.send(phoneMessage);
		}
		session.setAttribute("findPassAccount", account);
		session.setAttribute("findPass" + account, code);
		return CommonUtil.constructResponse(1, null, null);
	}

	/**
	 * 找回密码第二步，验证邮箱、手机验证码
	 * 从session中获取第一步中的邮箱、手机
	 * 通过邮箱获取application中的code并验证
	 *
	 * @param code 用户填写的验证码
	 * @return json
	 */
	@RequestMapping("findPassword2")
	@ResponseBody
	public JSONObject findPassword2(HttpSession session, String code, String userPassword) {
		if (code == null || "".equals(code)) {
			return CommonUtil.constructResponse(0, "验证码未知错误", null);
		}
		Object account = session.getAttribute("findPassAccount");
		if (account == null || "".equals(account)) {
			return CommonUtil.constructResponse(0, "验证码超时，请重新发送", null);
		}
		Object session_code = session.getAttribute("findPass" + account);
		if (code.equals(session_code)) {
			session.removeAttribute("findPass" + account);
			if (userPassword == null || "".equals(userPassword)) {
				return CommonUtil.constructResponse(0, "用户密码错误", null);
			}
			int accountType = 0;
			if (!StringUtil.checkEmail(account.toString())) {
				if (!StringUtil.checkPhoneNumber(account.toString())) {
					return CommonUtil.constructResponse(0, "请输入正确的手机号码", null);
				}
				accountType = 1;
			}
			if (userService.updatePasswordByAccount(accountType, account.toString(), userPassword)) {
				JSONObject jb = new JSONObject();
				jb.put("account", account);
				return CommonUtil.constructResponse(1, "success", jb);
			} else {
				return CommonUtil.constructResponse(0, "系统错误,修改失败", null);
			}
		}
		return CommonUtil.constructResponse(0, "验证码错误", null);
	}

	/**
	 * 找回密码第三步，修改密码
	 * 验证前两步是否合法
	 * @param request request
	 * @param user_password 用户密码
	 * @return json
	 */
	/*@RequestMapping("findPassword3")
	@ResponseBody
	public JSONObject findPassword3(HttpServletRequest request,String user_password){
		if(user_password == null || "".equals(user_password)){
			return CommonUtil.constructResponse(0,"用户密码错误",null);
		}
		HttpSession session = request.getSession();
		Object account= session.getAttribute("findPassAccount");
		if(account == null || "".equals(account)){
			return CommonUtil.constructResponse(0,"非法用户",null);
		}
		session.removeAttribute("findPassAccount");
		Object isTrue =session.getAttribute("findPassed"+account.toString());
		if(isTrue == null || !(Boolean) isTrue){
			return CommonUtil.constructResponse(0,"用户验证失败",null);
		}
		session.removeAttribute("findPassed"+account.toString());
		int accountType = 0;
		if (!StringUtil.checkEmail(account.toString())){
			if (!StringUtil.checkPhoneNumber(account.toString())){
				return CommonUtil.constructResponse(0,"格式错误",null);
			}
			accountType = 1;
		}
		if(userService.updatePasswordByAccount(accountType,account.toString(),user_password)){
			JSONObject jb = new JSONObject();
			jb.put("account",account);
			return CommonUtil.constructResponse(1,"success",jb);
		}else{
			return CommonUtil.constructResponse(0,"系统错误,修改失败",null);
		}
	}*/

	/**
	 * 修改密码
	 * 用户已经登陆
	 *
	 * @param oldPassword 用户填写的旧密码
	 * @param newPassword 用户填写的新密码
	 * @return json
	 */
	@RequestMapping("changePassword")
	@ResponseBody
	public JSONObject changePassword(HttpServletRequest request, String oldPassword, String newPassword) {
		Object o = request.getAttribute("user");
		if (o == null) {
			return CommonUtil.constructResponse(0, "未登陆", null);
		}
		if (newPassword == null || "".equals(newPassword)) {
			return CommonUtil.constructResponse(0, "新密码错误", null);
		}
		int userId = ((User) o).getUserId();  //获取用户id
		if (userService.updatePasswordByUserId(userId, oldPassword, newPassword)) {
			return CommonUtil.constructResponse(1, null, null);
		} else {
			return CommonUtil.constructResponse(0, "原密码错误", null);
		}
	}

	/**
	 * 查看用户的个人基本信息
	 *
	 * @param userId 被查看资料的用户id
	 * @return json
	 */
	@RequestMapping("getBasicInfo")
	@ResponseBody
	public JSONObject getBasicInfo(HttpSession session, Integer userId) {
		if (userId == null) {
			return CommonUtil.constructResponse(0, "用户不存在", null);
		}
		User u = (User) session.getAttribute("user");
		int user_id = userId;
		if (user_id == 0) {
			if (u == null) {
				return CommonUtil.constructResponse(-1, null, null);
			}
			userId = u.getUserId();
		}
		Map<String, Object> user = null;
		try {
			user = userService.getBasicInfoByUserId(userId);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
		if (user == null) {
			return CommonUtil.constructResponse(0, "用户不存在", null);
		}
		if (u == null){
			if(user_id != 0){
				user.put("attention",0); //如果未登录,查看其它用户资料
			}
		}else{
			if (user_id != 0){
				if (user_id == u.getUserId()){
					user.put("attention",-1); //如果已经登录,并且查看的用户id和已经登录的id是一样的
				}else {
					user.put("attention",userService.userRelation(u.getUserId(),userId)); //如果已经登录,查看其它用户的资料
				}
			}else {
				user.put("attention",-1); //如果已经登录,通过userId=0查看自己的资料
			}
		}
		user.put("skill",talkingTypeService.getUserSkill(userId));
		return CommonUtil.constructResponse(1, null, user);
	}

	
	
	/**
	 * 查看用户的粉丝数量和关注数量
	 * 只查数量
	 *
	 * @param userId 被查看资料的用户id
	 * @return json
	 */
	@RequestMapping("getAttentionCount")
	@ResponseBody
	public JSONObject getAttentionCount(Integer userId,HttpSession session) {
		User user=(User) session.getAttribute("user");
		if(user==null){
			return CommonUtil.constructResponse(-1, "用户未登录", null);
		}
		if (userId == null) {
			return CommonUtil.constructResponse(0, "用户不存在", null);
		}
		try {
			if(userId==0)
				userId=user.getUserId();
			Map<String, Integer> counts = userService.getAttentionCount(userId);
			counts.put("talkingCount", talkingService.getUserTalkingCount(userId, 10));
			return CommonUtil.constructResponse(1, null, counts);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}

	/**
	 * 获取用户的粉丝详情列或者关注详情列
	 * 分页，一页10条
	 *
	 * @param page     分页，页码，可以为null
	 * @param listType 0-粉丝，1-关注
	 * @param user_id  被查看资料的用户id
	 * @return json
	 */
	@RequestMapping("getAttentionList")
	@ResponseBody
	public JSONObject getAttentionList(Integer user_id, Integer listType, @RequestParam(required = false) Integer page) {
		if (user_id == null || listType == null) {
			return CommonUtil.constructResponse(-1, "参数错误", null);
		}
		try {
			List<HashMap<String, Object>> list = userService.getAttentionList(user_id, listType, page);
			return CommonUtil.constructResponse(1, null, list);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}

	/**
	 * 用户关注其他用户
	 *
	 * @param userId 被关注用户id
	 * @return json
	 */
	@RequestMapping("attentionUser")
	@ResponseBody
	public JSONObject attentionUser(Integer userId,int attention, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(-1, "未登陆", null);
		}
		if (userId == null || "".equals(userId)) {
			return CommonUtil.constructResponse(0, "参数错误", null);
		}
		if (userId == user.getUserId())
			return CommonUtil.constructResponse(0, "不能关注自己", null);
		int count = 0;
		StringBuffer strBuffer=new StringBuffer();
		try {
			count = userService.attentionUser(user.getUserId(), userId,attention);
		} catch (Exception e) {
			return CommonUtil.constructResponse(0, "不能重复关注", null);
		}
		if (count > 0) {
			strBuffer.append("<a href='user/getuserId?userId="+userId+"'>"+user.getUserName()+"</a>"+"关注了你");
			try {
				infomationUtil.createInformation(strBuffer.toString(),String.valueOf(userId),4);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return CommonUtil.constructResponse(1, null, null);
		}
		return CommonUtil.constructResponse(1, "失败", null);
	}


	/**
	 * 个人设置中，基本信息填写 查询
	 * 只要用户登陆，就可以直接查询到信息
	 * @return json
	 */
	@RequestMapping("getUserSettingBasic")
	@ResponseBody
	public JSONObject getUserSettingBasic(HttpServletRequest request) {
		// TODO 填写用户基本信息
		User user = (User) request.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(-1, "您还未登陆", null);
		}
		try{
			int userId = user.getUserId();
			JSONObject jo = new JSONObject();

			jo.put("BasicInfo",userService.getAllUserInfoByUserId(userId));
			jo.put("SkillArea",userService.getUserSkillArea(userId));
			jo.put("FollowArea",userService.getUserFollowArea(userId));

			return CommonUtil.constructResponse(1,null,jo);
		}catch (Exception e){
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误", null);
		}
	}

	/**
	 * 获取用户是否已经认证
	 */
	@RequestMapping("getUserCheckResult")
	@ResponseBody
	public JSONObject getUserCheckResult(HttpSession session){
		// TODO 填写用户基本信息
		User user = null;
		try {
			user = (User) session.getAttribute("user");
			if (user == null) {
                return CommonUtil.constructResponse(-1, "您还未登陆", null);
            }
		} catch (Exception e) {
			return CommonUtil.constructResponse(-5,"系统错误",null);
		}
		int userId=user.getUserId();
		Map<String,Object> result= null;
		try {
			result = userService.getUserCheckResult(userId);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误", null);
		}
		if(result==null){
			JSONObject jo=new JSONObject();
			jo.put("user_student_check_result",0);
			jo.put("student_check_picture",null);
			return CommonUtil.constructResponse(0,"该学生没有认证",jo);
		}
		return CommonUtil.constructResponse(1,"查询成功",result);

	}
	/**
	 * 个人设置中，基本信息填写 修改
	 * @param data 包含删除领域的所有ID、添加领域的所有ID
	 * @return json
	 */
	@RequestMapping("userUpdateBasic")
	@ResponseBody
	public JSONObject userUpdateBasic(HttpServletRequest request,@RequestParam("data")String data){
			User user=(User)request.getAttribute("user");
		if(user==null)
			return CommonUtil.constructResponse(0,"你还未登录",null);
		int userId=user.getUserId();
		try {
			JSONObject jo=JSONObject.parseObject(data);
			JSONObject u=jo.getJSONObject("user");
			user.setUserName(u.getString("user_name"));
			return userService.userUpdateBasic(userId,data);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误", null);
		}

	}
	@RequestMapping("getUserInfo")
	@ResponseBody
	public JSONObject getDetailUser(HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(0, "用户未登录！", null);
		}
		try {
			HashMap<String, Object> userinfo = userService.getBasicInfoByUserId(user.getUserId());
			return CommonUtil.constructResponse(1, null, userinfo);
		}catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}

	/**
	 * 获取所有的一级关注领域
	 */
	@RequestMapping("getAllFirstArea")
	@ResponseBody
	public JSONObject getAllFirstArea() {
		try {
			return userService.getAllFirstArea();
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}

	/**
	 * 根据所选的一级领域id获取二级关注领域
	 * @param id 表示一级领域id
	 * @return 返回二级领域id
	 */
	@RequestMapping("getAllSecondArea")
	@ResponseBody
	public JSONObject getAllSecondArea(int id)
	{
		try {
			return userService.getAllSecondArea(id);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}


	/**
	 * 获取用户的简历信息
	 * 如果user=0,表示获取自己的简历信息
	 */
	@RequestMapping("getUserResume")
	@ResponseBody
	public JSONObject getUserResume(HttpSession session,Integer userId){
		if (userId == null){
			return CommonUtil.constructResponse(0,"参数错误",null);
		}
		if (userId == 0){
			User user = (User)session.getAttribute("user");
			if (user == null){
				return CommonUtil.constructResponse(-1,null,null);
			}
			userId = user.getUserId();
		}
		JSONObject jo = new JSONObject();
		//获取教育经历
		jo.put("education",educationService.getUserEducation(userId,10));
		//获取获奖成果
		jo.put("prize",prizeService.getUserPrize(userId,10));
		//获取社团经历
		jo.put("campusExperience",campusExperienceService.getCampusExperience(userId,10));
		//获取工作经历
		jo.put("workExperience",workExprienceService.getUserWorkExperience(userId,10));
		return CommonUtil.constructResponse(1,null,jo);
	}

	/**
	 * 获取用户的第三方收款账户信息
	 */
	@RequestMapping("getUserPayAccountInfo")
	@ResponseBody
	public JSONObject getUserPayAccountInfo(HttpSession session){
		User user = (User)session.getAttribute("user");
		if (user == null){
			return CommonUtil.constructResponse(0,"未登录",null);
		}
		JSONObject jo = new JSONObject();
		jo.put("userPayAccount",user.getUserPayAccount());
		jo.put("userPayName",user.getUserPayName());
		
		
		
	  	
		Map userBalanceInfo =null;
		try {
			userBalanceInfo=userService.getUserBalance(user.getUserId()+"");
			Double D_user_balance = (Double) userBalanceInfo.get("user_balance");
			
			if (D_user_balance==null){
				D_user_balance=new Double(0);
			}
			
			BigDecimal   b_d_user_balance   =   new   BigDecimal(D_user_balance); 
			
			jo.put("userBalance",b_d_user_balance.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
			return CommonUtil.constructResponse(1,null,jo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"系统错误",null);
		}
		
		
		
		
	}

	/**
	 * 更新用户的第三方收款账户信息
	 */
	@RequestMapping("setUserPayAccountInfo")
	@ResponseBody
	public JSONObject setUserPayAccountInfo(HttpServletRequest request,String userPayAccount,String userPayName){
		User user = (User)request.getAttribute("user");
		if (user == null){
			return CommonUtil.constructResponse(0,"未登录",null);
		}
		if (userPayAccount == null || "".equals(userPayAccount) || userPayName == null || "".equals(userPayName)){
			return CommonUtil.constructResponse(0,"参数不能为空",null);
		}
		if (userService.setUserPayAccountInfo(user.getUserId(),userPayAccount,userPayName)){
			user.setUserPayAccount(userPayAccount);
			user.setUserPayName(userPayName);
			return CommonUtil.constructResponse(1,null,null);
		}
		return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
	}


	/*
		获取我关注人的信息
	 */
	@RequestMapping("getMyConcernMsg")
	@ResponseBody
	public JSONObject getMyConcernMsg(HttpServletRequest request,@RequestParam(required = false)Integer page){
		User user = (User)request.getAttribute("user");
		if (user == null){
			return CommonUtil.constructResponse(-1,"未登录",null);
		}
		if(page==null){
			page=1;
		}
		try {
			Map<String,Object> userMap=userService.getMyConcernMsg(user.getUserId(),(page-1)*pageNum,page*pageNum);
			return CommonUtil.constructResponse(1,"成功",userMap);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}

	@RequestMapping("getMyFansMsg")
	@ResponseBody
	public JSONObject getMyFansMsg(HttpServletRequest request,@RequestParam(required = false)Integer page) {
		User user = (User)request.getAttribute("user");
		if (user == null){
			return CommonUtil.constructResponse(-1,"未登录",null);
		}
		if(page==null){
			page=1;
		}
		try {
			Map<String,Object> userMap=userService.getMyFansMsg(user.getUserId(),(page-1)*pageNum,page*pageNum);
			return CommonUtil.constructResponse(1,"成功",userMap);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	
	/**
	 * 
	 * @Title: applyToCash 
	 * @Description: 申请提现
	 * @param session
	 * @param cash 提现金额
	 * @return
	 * @return: JSONObject
	 */
	@RequestMapping("applyToCash")
	@ResponseBody
	public JSONObject applyToCash(HttpSession session,Double cash,String password) {
		User user= (User) session.getAttribute("user");
		if(user==null){
			return  CommonUtil.constructResponse(EnumUtil.NOT_LOGIN,"请登录",null);
		}
		if(cash==null){
			return  CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "提现cash必须填写",null);
		}
		if(cash.doubleValue()<=0){
			return  CommonUtil.constructResponse(EnumUtil.APPLY_TO_CASH_MUST_LARGER_ZERO, "提现cash必须大于0",null);
		}
		int userId = user.getUserId();
		String name=null;
		try{
			name=userService.istruepassword(userId, password);
			if(name==null||"".equals(name)){
				return CommonUtil.constructResponse(0,"password is error",null);
			}
		}catch(Exception e){
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"系统错误",null);
		}
		try {
			return userService.applyToCash(userId+"", cash,session);
		} catch (CashTooMuchException e) {
			return CommonUtil.constructResponse(EnumUtil.TOO_MUCH_CASH,"提现金额大于账户余额",null);
		}catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"系统错误",null);
		}
	}
	
	
	@RequestMapping("getMyFansAndConcernNum")
	@ResponseBody
	public JSONObject getMyFansAndConcernNum(HttpSession session){
		User user=(User) session.getAttribute("user");
		HashMap mp=null;
		int userId=0;
		if(user!=null){
			userId=user.getUserId();
		}
		try {
			 mp=userService.getMyFansAndConcernNum(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误", null);
		}
		if(mp==null){
			return CommonUtil.constructResponse(1, "数据库没有数据", mp);
		}
		return CommonUtil.constructResponse(1, "获取数据成功", mp);
	}


	/*
    * 移动端版本更新接口
    * @param 无
    * @result
    *
    * */
	@RequestMapping("apkVersion")
	@ResponseBody
	public JSONObject apkVersion(){
		return CommonUtil.constructResponse(1,null,userService.apkVersion());
	}
	
	@RequestMapping("userAcountCheck")
	@ResponseBody
	public JSONObject userAcountCheck(HttpSession session,String userPassword){
		User user= (User) session.getAttribute("user");
		if(user==null){
			return  CommonUtil.constructResponse(EnumUtil.NOT_LOGIN,"请登录",null);
		}
		if(userPassword==null||"".equals(userPassword.trim())){
			return  CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "密码必须填写",null);
		}
		
	  
	try {
		Map userPasswordMap = userService.selectUserPasswordById(user.getUserId()+"");
		
		
		String user_password =(String)userPasswordMap.get("user_password");
		if (userPassword.equals(user_password)){
			return  CommonUtil.constructResponse(EnumUtil.OK, "验证通过",null);
		}else{
			return  CommonUtil.constructResponse(EnumUtil.PASSWORD_ERROR, "密码错误",null);
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误错误", null);
	}
		
		
	}
	
	/**
	 * 
	 * @Title: showNotReadInfoCount
	 * @Description: 根据用户ID查询用户密码信息
	 * @throws Exception
	 * @return: int
	 * @author JavaGR_ais
	 */
	@RequestMapping("showNotReadInfoCount")
	@ResponseBody
	public JSONObject showNotReadInfoCount(HttpSession session){
		
		User user = (User) session.getAttribute("user");
		
		if(user==null){
			return CommonUtil.constructResponse(-1,"用户未登录",null);
		}
		else{
			int result;
			try {
				result = userService.showNotReadInfoCount(user.getUserId());
			} catch (Exception e) {
				
				return CommonUtil.constructResponse(0,"数据库sql异常",null);		
			}
			return CommonUtil.constructResponse(1,"用户未读条数",result);
		}
		
		
	}
	
	
	
	/**
	 * author:wzh
	 * 用户头像
	 * 上传图片
	 */
	@RequestMapping(value="/userpicupload")
	@ResponseBody
	public JSONObject userpicupload(HttpServletRequest request,
			HttpServletResponse response){
		SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyyMMdd");// 等价于now.toLocaleString()
	    Calendar now = Calendar.getInstance();  
        int year=now.get(Calendar.YEAR);
        int mouth=(now.get(Calendar.MONTH) + 1);
        String date=year+"."+mouth;
		//时间                             
		String Releasetalking_user="user_photo";
		String result = null;// 上传后返回情况说明
		String path = null;// 上传图片路径
		// //创建一个通用的多部分解析器
		CommonsMultipartResolver cmr = new CommonsMultipartResolver(request
				.getSession().getServletContext());
		// //判断 request 是否有文件上传,即多部分请求
		if (cmr.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest mhsr = (MultipartHttpServletRequest) request;
			// //取得request中的所有文件名
			Iterator<String> iter = mhsr.getFileNames();
			while (iter.hasNext()) {
				// //取得上传文件
				MultipartFile file = mhsr.getFile((String) iter.next());

				// //取得当前上传文件的文件名称
				String filename = file.getOriginalFilename();
				// 获得文件后缀
				String fileSuffixName = filename.substring(filename.indexOf("."));
				/**
				 * 上传文件大小,类型判断
				 */
				if (file.getSize() > 1048576) {
					result = "上传失败：上传文件大小大于1M"; 
					return CommonUtil.constructResponse(0, result, null);

				} else if (!fileSuffixName.equals(".jpg")
						&& !fileSuffixName.equals(".png")
						&& !fileSuffixName.equals(".gif")) {
					result = "上传失败：上传文件类型不正确";
				
					return CommonUtil.constructResponse(0, result, null);

				}

				// 生成的GUID为一串32位字符组成的128位数据上传文件重命名filename1
				CommonUtil cu = new CommonUtil();
				String UUID = cu.GUID();

				String filename1 = UUID + fileSuffixName;
				// 验证当前操作系统，构建上传路径
				String os = System.getProperty("os.name").toLowerCase();
				if (os.indexOf("win") >= 0)
					path = PropertiesUtils.getProp("path.win") + "/"+Releasetalking_user+"/"+date.toString();
				else
					path = PropertiesUtils.getProp("path.linux") + "/"+Releasetalking_user+"/"+date.toString();
				 
				// 定义上传路径
				// path =
				// "http://103.249.252.139:9001/usr/sendfoodpic/"+filename1;
				// String path2 =
				// path = "/static/image/icon/"+filename1;

				/*
				 * File file2 = new File(path2);
				 * 
				 * String snacks_pic1 = path2;
				 */
				
				File file2 = new File(path); 
				if(!file2.exists()){
					file2.mkdirs();
				}
				
				File file3 = new File(path+"/"+filename1);
				// String snacks_pic1 = filename1;
				try {
					// transfer方法是MultipartFile包中提供的方法，直接可以写入文件到指定目录
					file.transferTo(file3);

					// 复制Web服务器文件到文件服务器
					boolean status = SftpUtils.uploadFile(path+"/"+filename1);
					if (status) {
						result = "上传成功";
						System.out.println("上传成功***");
						// 删除WEB服务器文件
							file3.delete();
						// TODO effine 文件上传成功返回URL需要讨论确 定
							JSONObject jo=new JSONObject();
							jo.put("url", Releasetalking_user+"/"+date.toString()+"/"+filename1);
						return CommonUtil.constructResponse(1, result,
								jo);
					}

				} catch (Exception e) {
					result = e.getMessage();

					e.printStackTrace();
					return CommonUtil.constructResponse(0, result, null);
				}

			}
		}
		return  CommonUtil.constructResponse(0, null, null);
	}
	
	
//	@RequestMapping("insertuserphoto")
//	@ResponseBody
//	public JSONObject insertuserphoto(HttpServletRequest request,@RequestParam String user_photo) {
//		User user = (User)request.getAttribute("user");
//		if (user == null){
//			return CommonUtil.constructResponse(-1,"未登录",null);
//		}
//		int user_id=user.getUserId();
//		int i=userService.insertuserphoto(user_photo, user_id);
//		if(i==1){
//			return  CommonUtil.constructResponse(1, "success", null);
//		}else{
//			return  CommonUtil.constructResponse(0, null, null);
//		}
//	}
	
	
	@RequestMapping("updateuserphoto")
	@ResponseBody
	public JSONObject updateuserphoto(HttpServletRequest request,@RequestParam String user_photo) {
		User user = (User)request.getAttribute("user");
		if (user == null){
			return CommonUtil.constructResponse(-1,"未登录",null);
		}
		int user_id=user.getUserId();
		int i= 0;
		try {
			i = userService.updateuserphoto(user_photo, user_id);
		} catch (Exception e) {
			return CommonUtil.constructResponse(-5,"数据库错误",null);
		}
		if(i==1){
			return  CommonUtil.constructResponse(1, "success", null);
		}else{
			return  CommonUtil.constructResponse(0, null, null);
		}
	}
	
	/**
	 * 将系统消息全部置为已读,
	 * @param null
	 * @return
	 * @author JavaGR_ais
	 */
	@RequestMapping("updateInfo_systemReaded")
	@ResponseBody
	public JSONObject updateInfo_systemReaded(HttpSession session){
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(-1, "你还未登录", null);
		}
	
		try {
			int user_id = user.getUserId();
			int result = userService.updateInfo_systemReaded(user_id);
			
			if(result>0){
				return CommonUtil.constructResponse(1, "系统消息全部置为已读", null);

			}
			
			return CommonUtil.constructResponse(1, "该用户没有系统消息", null);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	
	/**
	 * 将系统消息全部置为已读,
	 * @param null
	 * @return
	 * @author JavaGR_ais
	 */
	@RequestMapping("deleteInfo_systemReaded")
	@ResponseBody
	public JSONObject deleteInfo_systemReaded(HttpSession session){
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(-1, "你还未登录", null);
		}
	
		try {
			int user_id = user.getUserId();
			int result = userService.deleteInfo_systemReaded(user_id);
			
			if(result>0){
				return CommonUtil.constructResponse(1, "系统消息删除成功", null);

			}
			
			return CommonUtil.constructResponse(1, "该用户没有系统消息", null);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
		}
	}
	
	
	/**
	 * 找回密码第一步，获取用户邮箱或者手机号码
	 * 验证用户邮箱、手机格式
	 * 验证数据库是否已经注册
	 * 产生数字验证码，存入session
	 *
	 * @param request 通过request获取session
	 * @param account 获取用户邮箱或者手机
	 * @return 返回结果
	 */
	@RequestMapping("findPasswordApp1")
	@ResponseBody
	public JSONObject findPasswordApp1(HttpServletRequest request, String account) {
		int accountType = 0;
			if (!StringUtil.checkPhoneNumber(account)) {
				return CommonUtil.constructResponse(0, "请输入正确的手机号码", null);
			}
			accountType = 1;
		int haveAccount = userService.checkMailorPhone(accountType, account);
		if (haveAccount != 1) {
			if (haveAccount == -1)
				return CommonUtil.constructResponse(0, "账号类型错误", null);
			return CommonUtil.constructResponse(0, "该" + (accountType == 0 ? "邮箱" : "手机号码") + "没有被注册", null);
		}
		HttpSession session = request.getSession();
		String code = StringUtil.getRandomCode(4);
		if (accountType == 1) {
			PhoneMessage phoneMessage = new PhoneMessage(account, code, "变更验证", "SMS_7125141");
			PhoneSender.send(phoneMessage);
		}
		session.setAttribute("findPassAccount", account);
		session.setAttribute("findPass" + account, code);
		return CommonUtil.constructResponse(1, null, null);
	}
	
	/**
	 * 找回密码第二步，验证邮箱、手机验证码
	 * 从session中获取第一步中的邮箱、手机
	 * 通过邮箱获取application中的code并验证
	 *
	 * @param code 用户填写的验证码
	 * @return json
	 */
	@RequestMapping("findPasswordApp2")
	@ResponseBody
	public JSONObject findPasswordApp2(HttpSession session, String code, String userPassword) {
		if (code == null || "".equals(code)) {
			return CommonUtil.constructResponse(0, "验证码未知错误", null);
		}
		Object account = session.getAttribute("findPassAccount");
		if (account == null || "".equals(account)) {
			return CommonUtil.constructResponse(0, "验证码超时，请重新发送", null);
		}
		Object session_code = session.getAttribute("findPass" + account);
		if (code.equals(session_code)) {
			session.removeAttribute("findPass" + account);
			if (userPassword == null || "".equals(userPassword)) {
				return CommonUtil.constructResponse(0, "用户密码错误", null);
			}
			int accountType = 0;
				if (!StringUtil.checkPhoneNumber(account.toString())) {
					return CommonUtil.constructResponse(0, "请输入正确的手机号码", null);
				}
				accountType = 1;
			if (userService.updatePasswordByAccount(accountType, account.toString(), userPassword)) {
				JSONObject jb = new JSONObject();
				jb.put("account", account);
				return CommonUtil.constructResponse(1, "success", jb);
			} else {
				return CommonUtil.constructResponse(0, "系统错误,修改失败", null);
			}
		}
		return CommonUtil.constructResponse(0, "验证码错误", null);
	}
	/**
	 * 安卓用户注册第一步，获取用户邮箱或者手机号码
	 * 验证用户手机格式
	 * 验证数据库是否已经注册
	 * 产生数字验证码，存入session和application
	 *
	 * @param request 通过request获取全局变量application和session
	 * @param account 获取用户邮箱
	 * @return 返回结果
	 */
	@RequestMapping("registerApp1")
	@ResponseBody
	public JSONObject registerApp1(HttpServletRequest request, String account) {
		int registerType = 0;
			if (!StringUtil.checkPhoneNumber(account)) {
				return CommonUtil.constructResponse(0, "请输入正确的手机号码", null);
			}
			registerType = 1;
		int haveAccount = userService.checkMailorPhone(registerType, account);
		if (haveAccount != 0) {
			if (haveAccount == -1)
				return CommonUtil.constructResponse(0, "注册类型错误", null);
			return CommonUtil.constructResponse(0, "该" + (registerType == 0 ? "邮箱" : "手机号码") + "已经被注册", null);
		}
		HttpSession session = request.getSession();
		String code = StringUtil.getRandomCode(4);
		if (registerType == 1) {
			PhoneMessage phoneMessage = new PhoneMessage(account, code, "注册验证", "SMS_7125144");
			PhoneSender.send(phoneMessage);
		}
		session.setAttribute("registerAccount", account);
		session.setAttribute("register" + account, code);
		return CommonUtil.constructResponse(1, null, null);
	}

	/**
	 * 安卓用户注册第二步，验证手机验证码
	 * 从session中获取第一步中的邮箱、手机
	 * 通过邮箱获取application中的code并验证
	 *
	 * @param code 用户填写的验证码
	 * @return json
	 */
	@RequestMapping("registerApp2")
	@ResponseBody
	public JSONObject registerApp2(HttpSession session, String code) {
		if (code == null || "".equals(code)) {
			return CommonUtil.constructResponse(0, "验证码未知错误", null);
		}
		Object account = session.getAttribute("registerAccount");
		if (account == null || "".equals(account)) {
			return CommonUtil.constructResponse(0, "验证码超时，请重新发送", null);
		}
		String session_code = session.getAttribute("register" + account).toString();
		if (code.equals(session_code)) {  //如果验证码正确
				session.removeAttribute("register" + account);
				return CommonUtil.constructResponse(1, "success", null);
		}
		return CommonUtil.constructResponse(0, "验证码错误", null);
	}
	
	
	/**
	 * 用户注册第二步，验证邮箱、手机验证码
	 * 从session中获取第一步中的邮箱、手机
	 * 通过邮箱获取application中的code并验证
	 *
	 * @param code 用户填写的验证码
	 * @return json
	 */
	@RequestMapping("registerApp3")
	@ResponseBody
	public JSONObject registerApp3(HttpSession session, String userName,String userPassword) {
		Object account = session.getAttribute("registerAccount");
		if (userName == null || "".equals(userName) || userPassword == null || "".equals(userPassword)) {
			return CommonUtil.constructResponse(0, "用户信息错误", null);
		}
		int registerType = 0;
		User user = new User();
		if (!StringUtil.checkPhoneNumber(account.toString())) {
			return CommonUtil.constructResponse(0, "请输入正确的手机号码", null);
		}
		registerType = 1;
		if (registerType == 1) {
			user.setUserPhone(account.toString());
		}
		user.setUserName(userName);
		user.setUserPassword(userPassword);
		user.setUserRegisterTime(new Date());
		if (userService.addUser(registerType, user) == 1) {
			JSONObject jb = new JSONObject();
			jb.put("account", account);
			return CommonUtil.constructResponse(1, "success", jb);
		} else {
			return CommonUtil.constructResponse(0, "系统错误", null);
		}
	}

}
