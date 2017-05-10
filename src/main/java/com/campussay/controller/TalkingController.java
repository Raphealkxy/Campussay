package com.campussay.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.campussay.model.Talking;
import com.campussay.model.TalkingPublish;
import com.campussay.model.User;
import com.campussay.service.CampusService;
import com.campussay.service.OrderService;
import com.campussay.service.TalkingService;
import com.campussay.service.UserService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;
import com.campussay.util.EnumUtil.TalkingStatus;
import com.campussay.util.PropertiesUtils;
import com.campussay.util.SftpUtils;

@Controller
@RequestMapping("/talking")
public class
TalkingController {
	@Autowired
	private TalkingService talkingService;

	@Autowired
	private CampusService campusService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;

	/**
	 * 指定talking的图片
	 * @return
	 */
	@RequestMapping("getBannerPicture")
	@ResponseBody
	public JSONObject getBannerPicture(){
		return CommonUtil.constructResponse(1, "获取成功", talkingService.getBannerPicture());
	}

    /**
     * wangwenxiang
     * 获取用户的发布的talking
     * @param userId 用户id
     * @param page 分页
     * 陆栋写的，其他人要调用请署名或者自己去写一个，这个方法我都改了几次了，每次都被改回来，希望大神们慎重！
     */

	@RequestMapping(value = "getUserTalking")
	@ResponseBody
	public JSONObject getUserTalking(@RequestParam("userId") Integer userId,
									 @RequestParam(required = false) Integer page,
									 @RequestParam(required = false)Integer state,
									 HttpSession session){
		if (userId == null || "".equals(userId)){
            return CommonUtil.constructResponse(0, "用户参数错误",null);
        }
		if (userId == 0){
			User u = (User)session.getAttribute("user");
			if (u == null){
				return CommonUtil.constructResponse(-1, null,null);
			}
			userId = u.getUserId();
		}
		JSONObject jo = new JSONObject();
		int count = talkingService.getUserTalkingCount(userId,state);
		jo.put("count",count);
		if (count>0){
			jo.put("rows",talkingService.getUserTalking(userId,page,state));
		}

        return CommonUtil.constructResponse(1, null,jo);
	}

	/**
	 * wangwenxiang
	 * 获取他所参与（购买）的talking信息列表
	 * @param userId 用户id
	 * @param page 分页
	 */

	@RequestMapping(value = "getUserBuyTalking")
	@ResponseBody
	public JSONObject getUserBuyTalking(@RequestParam("userId") Integer userId,
										@RequestParam(required = false) Integer page,
										HttpSession session){
		if (userId == null || "".equals(userId)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		if (userId == 0){
			User u = (User)session.getAttribute("user");
			if (u == null){
				return CommonUtil.constructResponse(-1, null,null);
			}
			userId = u.getUserId();
		}
		JSONObject jo = new JSONObject();
		int count = talkingService.getUserBuyTalkingCount(userId,10);
		jo.put("count",count);
		if (count > 0){
			jo.put("rows",talkingService.getUserBuyTalking(userId,page,10));
		}else {
			jo.put("rows",null);
		}

		return CommonUtil.constructResponse(1, null,jo);
	}

	/**
	 * wangwenxiang
	 * 获取用户所发布的talking的数量
	 * @param userId 用户id
	 */

	@RequestMapping(value = "getUserTalkingCount")
	@ResponseBody
	public JSONObject getUserTalkingCount(Integer userId){
		if (userId == null || "".equals(userId)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		return CommonUtil.constructResponse(1, null,userService.getUserTalkingCount(userId));
	}
	
	/**
	 * 返回听听堂首页根节点与其下面的talings
	 * @return
	 * @author JavaGR_ais
	 */
	@RequestMapping(value = "ShowHomePageNode")
	@ResponseBody
	public JSONObject ShowHomePageNode() {

		return CommonUtil.constructHtmlResponse(1,"返回听听堂首页根节点",
				talkingService.ShowHomePageNode());
	} 

	
	/**
	 * 听听堂分类列表,一次性封装好并返回分类,城市,学校。
	 * @return
	 * @author JavaGR_ais
	 */
	@RequestMapping(value = "ShowTalkingCf")
	@ResponseBody
	public JSONObject ShowTalkingCf() {

		return CommonUtil.constructResponse(1, "听"
				+ "听堂分类列表,一次性封装好并返回分类,城市,学校",
				talkingService.ShowTalkingCf());
	}

	/**
	 * 听听堂分类,组合查询课程或者说友。
	 * @param LeafNode_id 叶子id
	 * @param RootNode_id 跟几点id
	 * @param city_id 城市id
	 * @param campus_id 学校id
	 * @param pageCount 跳转到第几页
	 * @param sex		男女筛选
	 * @param style     线上线下筛选
	 * @param type
	 * @return
	 * @author JavaGR_ais
	 */
	@RequestMapping(value = "ShowTalkingByCfOrCyOrCs")
	@ResponseBody
	public JSONObject ShowTalkingByCfOrCyOrCs(int LeafNode_id, int RootNode_id,
			 int city_id, int campus_id, int pageCount,int sex,int type) {

		return CommonUtil.constructResponse(1, "听听堂分类,组合查询", talkingService
				.ShowTalkingByCfOrCyOrCs(LeafNode_id, RootNode_id, 
						city_id, campus_id, pageCount,sex,type));
	}
	
	/**
	 *SelectT_taking_leafNodesByNode_id 
	 */
	@RequestMapping(value = "SelectT_taking_leafNodesByNode_id")
	@ResponseBody
	public JSONObject SelectT_taking_leafNodesByNode_id(int taking_type_id) {

		return CommonUtil.constructResponse(1, "根据第二级节点返回叶子节点", talkingService
				.SelectT_taking_leafNodesByNode_id(taking_type_id));
	}
	
	
	/**
	 * author:wzh
	 * 课程详情展示
	 * @throws Exception 
	 */
	@RequestMapping(value="/talkingshow")
	@ResponseBody
	public JSONObject talkingshow(@RequestParam("TalkingId") Integer TalkingId, HttpServletRequest request) throws Exception{
		int userid = 0;
		String msg=null;
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		if(user==null||"".equals(user)){
				msg="NotLogin";
		}else{
			userid=user.getUserId();
		}
		if (TalkingId == null || "".equals(TalkingId)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		Integer order_id;
		Integer user_id;
		try{
		user_id=talkingService.getUserID(TalkingId);
		}catch(Exception e){
			e.printStackTrace();
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
		}
		if (user_id == null || "".equals(user_id)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		JSONObject jo=new JSONObject();
		Map<String,Object> map=new HashMap();
		Map<String,Object> map1=new HashMap();
		Map<String,Object> map2=new HashMap();
		try{
			map=talkingService.TalkingShow(TalkingId);
			map1=talkingService.getUserDetails(user_id);
			if(msg==null||"".equals(msg)){
				map2=orderService.isorderstate(userid, TalkingId);
			}
		}catch(Exception e){
			e.printStackTrace();
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
		}
		if(map==null){
			return CommonUtil.constructResponse(0, "该talking已删除",null);
		}
		jo.put("talkingshow", map);
		jo.put("UserDetails", map1);
		if(map2!=null&&map2.get("order_state")!=null){
			jo.put("paystate",map2);
		}else{
			Map<String,Object> map3=new HashMap();
			map3.put("order_state",10);
			map3.put("order_id", 0);
		jo.put("paystate",map3);
		}
		return CommonUtil.constructResponse(1, msg,jo);
	}
	
	/**
	 * author:wzh
	 * 课程评价展示
	 * @throws Exception 
	 */
	@RequestMapping(value="talkingcomment")
	@ResponseBody
	public JSONObject talkingcomment(@RequestParam("TalkingId") Integer TalkingId,@RequestParam(value="page",required=false) Integer page) throws Exception{
		if (TalkingId == null || "".equals(TalkingId)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		if(page==null||"".equals(page)){
			page=1;
		}
		JSONObject jo=new JSONObject();
		List<Map<String, Object>> list=new ArrayList();
		int i;
		try{
		list=talkingService.TalkingComment(TalkingId, page);
		i=talkingService.countComment(TalkingId);
		}catch(Exception e){
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
		}
		jo.put("list",list);
		jo.put("all",i);
		return CommonUtil.constructResponse(1, null,jo);
	}
	/**
	 * author:wzh
	 * 参加课程
	 * @throws Exception 
	 */
	@RequestMapping(value="jointalking")
	@ResponseBody
	public JSONObject jointalking(HttpServletRequest request,@RequestParam("TalkingId") Integer TalkingId) throws Exception{
		Integer user_id=((User) request.getAttribute("user")).getUserId();
		if (TalkingId == null || "".equals(TalkingId)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		int i;
		try{
		i=talkingService.joinTalking(TalkingId, user_id);
		}catch(Exception e){
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
		}
		if(i==1){
		return CommonUtil.constructResponse(1,"成功",null);
		}else{
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
	}
	
	/**
	 * author:wzh
	 * 说有其他课程
	 * @throws Exception 
	 */
	@RequestMapping(value="/getothertalking")
	@ResponseBody
	public JSONObject getothertalking(@RequestParam("user_id") Integer user_id) throws Exception{
		if (user_id == null || "".equals(user_id)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		 List<Map<String, Object>> list=new ArrayList();
		 try{
		 list=talkingService.getothertalking(user_id);
		 }catch(Exception e){
			 e.printStackTrace();
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
			}
		return CommonUtil.constructResponse(1, null,list);
	}
	/**
	 * author:wzh
	 * 说有介绍
	 * @throws Exception 
	 */
	@RequestMapping(value="/getUserDetails")
	@ResponseBody
	public JSONObject getUserDetails(@RequestParam("user_id") Integer user_id) throws Exception{
		if (user_id == null || "".equals(user_id)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		Map<String,Object> map=new HashMap();
		try{
			map=talkingService.getUserDetails(user_id);
		 }catch(Exception e){
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
			}
		return CommonUtil.constructResponse(1, null,map);
	}

	/**
	 * author:wzh
	 * taking发布页
	 * 取得第一层节点
	 * @throws Exception 
	 */
	@RequestMapping(value="/getfirsttalking")
	@ResponseBody
	public JSONObject getfirsttalking() throws Exception{
		List<Map<String, Object>> list=new ArrayList();
		 try{
		 list=talkingService.firsttakingType();
		 }catch(Exception e){
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
			}
		return CommonUtil.constructResponse(1, null,list);
	}
	
	/**
	 * author:wzh
	 * taking发布页
	 * 取得子节点
	 * @throws Exception 
	 */
	@RequestMapping(value="/getTalkingType")
	@ResponseBody
	public JSONObject getTalkingType(@RequestParam("talking_type_parent") Integer talking_type_parent) throws Exception{
		if (talking_type_parent == null || "".equals(talking_type_parent)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		List<Map<String, Object>> list=new ArrayList();
		 try{
		 list=talkingService.TalkingType(talking_type_parent);
		 }catch(Exception e){
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
			}
		return CommonUtil.constructResponse(1, null,list);
	}
	
	/**
	 * author:wzh
	 * taking发布页
	 * 取得用户通信信息
	 * @throws Exception 
	 */
	@RequestMapping(value="/getuserContact")
	@ResponseBody
	public JSONObject userContact(HttpServletRequest request) throws Exception{
		Integer user_id=((User) request.getAttribute("user")).getUserId();
		if (user_id == null || "".equals(user_id)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		Map<String,Object> map=new HashMap();
		try{
			map=talkingService.userContact(user_id);
		 }catch(Exception e){
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
			}
		return CommonUtil.constructResponse(1, null,map);
	}
	/**
	 * author:wzh
	 * taking发布页
	 * 发布课程
	 * @throws Exception 
	 */
	@RequestMapping(value="/relasetalking")
	@ResponseBody
	public JSONObject relasetalking(HttpServletRequest request,TalkingPublish data
			) throws Exception{
		User user=(User) request.getAttribute("user");
		if (user == null){
			return CommonUtil.constructResponse(0, "未登陆",null);
		}
		int user_id = user.getUserId();
		Integer campus = campusService.getStudentCheckCampus(user_id);
		if (campus != 1){
			return CommonUtil.constructResponse(0, "未认证用户",null);
		}
		Talking talking=new Talking();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			talking.setTalkingTitle(data.getTalkingTitle());
			talking.setTalkingType(Integer.parseInt(data.getTalkingType()));
			talking.setTalkingRootType(Integer.parseInt(data.getTalkingRootType()));
			talking.setTalkingMaxPersion(Integer.valueOf(data.getTalkingMaxPersion()));
			talking.setTalkingPrice(Double.parseDouble(data.getTalkingPrice()));
			talking.setTalkingAddress(data.getTalkingAddress());
			talking.setTalkingMainPicture(data.getTalkingMainPicture());
			talking.setTalkingTarget(data.getTalkingTarget());
			talking.setTalkingInfo(data.getTalkingInfo());
			talking.setTalkingStartTime(sdf.parse(sdf.format(Long.parseLong(data.getTalkingStartTime()))));
			talking.setTalkingEndTime(sdf.parse(sdf.format(Long.parseLong(data.getTalkingEndTime()))));
			talking.setTalkingCampus(campus);
			talking.setTalkingUser(user_id);
			
			String date1=sdf.format(new Date());
			Date date2=sdf.parse(date1);
			Date date3=sdf.parse(sdf.format(Long.parseLong(data.getTalkingStartTime())));
			if(date2.getTime()>=date3.getTime()){
				return CommonUtil.constructResponse(10001, "课程过期",null);
			}
			
			long s=(sdf.parse(sdf.format(Long.parseLong(data.getTalkingEndTime()))).getTime() - System.currentTimeMillis()) / (1000 * 60);
			if(s<30){
				return CommonUtil.constructResponse(10002, "课程时间小于30min",null);
			}
			
//			刘鹏彦增加设置交流工具信息开始
			String talkingTool = data.getTalkingTool();
			if(talkingTool!=null &&talkingTool.length()<255){
				talking.setTalking_tool(talkingTool);
			}else{
				return CommonUtil.constructResponse(0, "参数错误",null);
			}
			String talking_tool_num = data.getTalkingToolNum();
			if(talkingTool!=null &&talkingTool.length()<255){
				talking.setTalking_tool_num(talking_tool_num);
			}else{
				return CommonUtil.constructResponse(0, "参数错误",null);
			}
			String talkingIsOnline = data.getTalkingIsOnline();
			if(talkingIsOnline!=null &&talkingIsOnline.length()<255){
				talking.setTalking_is_online(talkingIsOnline);
			}else{
				return CommonUtil.constructResponse(0, "参数错误",null);
			}

		}catch (Exception e){
			return CommonUtil.constructResponse(1, "参数错误",null);
		}
		
		//这里应该修改 到service层 添加事物	
			int i;
			try{
				i=talkingService.CreateNewTalking(talking);
			}catch(Exception e){
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
			}
			if(i==1){
	
				return CommonUtil.constructResponse(1, "成功",talking.getTalkingId());
			}else{
				return CommonUtil.constructResponse(0, "失败",null);
			}
	}
	/**
	 * author:wzh
	 * taking发布页
	 * 上传图片
	 */
	@RequestMapping(value="/talkingpicupload")
	@ResponseBody
	public JSONObject talkingpicupload(HttpServletRequest request,
			HttpServletResponse response){
		SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyyMMdd");// 等价于now.toLocaleString()
	    Calendar now = Calendar.getInstance();  
        int year=now.get(Calendar.YEAR);
        int mouth=(now.get(Calendar.MONTH) + 1);
        String date=year+"."+mouth;
		//时间                             
		String Releasetalking_user="talking_main_picture";
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
	/*
	 * @author:wzh
	 * talking发布页
	 * 返回talking修改数据
	 */
	@RequestMapping("gettalkingdetails")
	@ResponseBody
	public JSONObject gettalkingdetails(Integer talkingId) throws Exception{
		if (talkingId == null || "".equals(talkingId)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		Map<String,Object> map=new HashMap();
		try{
			map=talkingService.gettalkingdetails(talkingId);
		 }catch(Exception e){
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
			}
		return CommonUtil.constructResponse(1, "成功",map);
	}
	/*
	 * @author:wzh
	 * talking发布页
	 * talking修改上传
	 */
	@RequestMapping("updatetalkingdetails")
	@ResponseBody
	public JSONObject updatetalkingdetails(HttpServletRequest request,TalkingPublish data
			) throws Exception{
		User user=(User) request.getAttribute("user");
		if (user == null){
			return CommonUtil.constructResponse(0,"未登陆",null);
		}
		int user_id = user.getUserId();
		Integer campus = campusService.getStudentCheckCampus(user_id);
		if (campus == null){
			return CommonUtil.constructResponse(0, "未认证用户",null);
		}
		Talking talking=new Talking();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			talking.setTalkingId(Integer.parseInt(data.getTalkingId()));
			talking.setTalkingTitle(data.getTalkingTitle());
			talking.setTalkingType(Integer.parseInt(data.getTalkingType()));
			talking.setTalkingRootType(Integer.parseInt(data.getTalkingRootType()));
			talking.setTalkingMaxPersion(Integer.valueOf(data.getTalkingMaxPersion()));
			talking.setTalkingPrice(Double.parseDouble(data.getTalkingPrice()));
			talking.setTalkingAddress(data.getTalkingAddress());
			talking.setTalkingMainPicture(data.getTalkingMainPicture());
			talking.setTalkingTarget(data.getTalkingTarget());
			talking.setTalkingInfo(data.getTalkingInfo());
			talking.setTalkingStartTime(sdf.parse(sdf.format(Long.parseLong(data.getTalkingStartTime()))));
			talking.setTalkingEndTime(sdf.parse(sdf.format(Long.parseLong(data.getTalkingEndTime()))));
			talking.setTalkingCampus(campus);
			talking.setTalkingUser(user_id);
//			刘鹏彦增加设置交流工具信息开始
			String talkingTool = data.getTalkingTool();
			if(talkingTool!=null &&talkingTool.length()<255){
				talking.setTalking_tool(talkingTool);
			}else{
				return CommonUtil.constructResponse(0, "参数错误",null);
			}
			String talking_tool_num = data.getTalkingToolNum();
			if(talkingTool!=null &&talkingTool.length()<255){
				talking.setTalking_tool_num(talking_tool_num);
			}else{
				return CommonUtil.constructResponse(0, "参数错误",null);
			}
			String talkingIsOnline = data.getTalkingIsOnline();
			if(talkingIsOnline!=null &&talkingIsOnline.length()<255){
				talking.setTalking_is_online(talkingIsOnline);
			}else{
				return CommonUtil.constructResponse(0, "参数错误",null);
			}
		
		}catch (Exception e){
			return CommonUtil.constructResponse(1, "参数错误",null);
		}
		int i;
		try{
			i=talkingService.updatetalkingdetails(talking);
		}catch(Exception e){
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
		}
		if(i==1){
			return CommonUtil.constructResponse(1, "成功",null);
		}else if(i==0){
			return CommonUtil.constructResponse(0, "已有报名人数",null);
		}else{
			return CommonUtil.constructResponse(0, "失败",null);
		}
		
	}
	

	@RequestMapping("delTalking")
	@ResponseBody
	public JSONObject delTalking(HttpServletRequest request, int talkingId){
		User user = (User) request.getAttribute("user");
		if(talkingService.judgeIsDelTalking(talkingId)>0){
			return  CommonUtil.constructResponse(0, "删除失败", null);
		}else{
			if (talkingService.delTalking(user.getUserId(),talkingId)){
				userService.reducTalkingCount(user.getUserId());
				return  CommonUtil.constructResponse(1, null, null);
			}else {
				return  CommonUtil.constructResponse(0, "删除失败", null);
			}
		}
	}
	
	
	/**
	 * @author liupengyan
	 * @Title: checktalkingStatus 
	 * @Description: 检查talking当前的状态，状态可能为过期或者已经爆满</br>
	 * @param talkingId
	 * @return
	 * @return: JSONObject
	 */
	@RequestMapping("checkTalkingStatus")
	@ResponseBody
	public JSONObject checkTalkingStatus(Integer talkingId){
		if (talkingId == null || "".equals(talkingId)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		try {
			TalkingStatus  talkingStatus  = talkingService.checkTalkingStatus(talkingId);
			
			JSONObject result = null;
			switch(talkingStatus){
//				CANUSER,//可以下单
//				HADOVERDUE,//已经过期
//				HADFULL,//经爆满
//				NOTFOUND,//没有找到该课程
//				UNKOWNERRRO,//未知错误
			
//				SYSTEMERROR//系统错误
				case CANUSER:
					result = CommonUtil.constructResponse(EnumUtil.OK, "该课程可以学习",null);	
					break;
				case HADOVERDUE:
					result= CommonUtil.constructResponse(EnumUtil.TALKING_OVERDUE, "该课程已经过期",null);	
					break;
				case HADFULL:
					result= CommonUtil.constructResponse(EnumUtil.TALKING_FULL, "该课程已经爆满",null);	
					break;
				case NOTFOUND:
					result= CommonUtil.constructResponse(EnumUtil.TALKING_NOT_FOUND, "没找到该课程",null);	
					break;
				case UNKOWNERRRO:
					result= CommonUtil.constructResponse(EnumUtil.UNKOWN_ERROR, "未知错误",null);	
					break;
				case SYSTEMERROR:
					result= CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误",null);	
					break;
				default:
					result= CommonUtil.constructResponse(EnumUtil.UNKOWN_ERROR, "未知错误",null);	
					break;
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误",null);	
		}
		
	}
	
	
	
	/**
	 * 
	 * @Title: getTalkingInfoForCreateOrder 
	 * @Description: 创建订单前的到课程部分信息
	 * @param talkingId
	 * @return
	 * @return: JSONObject
	 */
	@RequestMapping("getTalkingInfoForCreateOrder")
	@ResponseBody
	public JSONObject getTalkingInfoForCreateOrder(HttpServletRequest request,Integer talkingId){
		
		User user=(User) request.getAttribute("user");
		if (user == null){
			return CommonUtil.constructResponse(EnumUtil.NOT_LOGIN, "未登陆",null);
		}
		if (talkingId == null || "".equals(talkingId)){
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "talkingId必须填写",null);
		}
		try {
			HashMap<String,Object>  talkingInfo  = talkingService.getTalkingInfoForCreateOrder(talkingId);
			if (talkingInfo==null){
				return  CommonUtil.constructResponse(EnumUtil.TALKING_NOT_FOUND, "课程不存在",null);
			}else{
				Date date = (Date)talkingInfo.get("talking_start_time");
				
				SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String talkingStatTime = dateFormater.format(date);
				talkingInfo.put("talking_start_time", talkingStatTime);
				String now_user_phone = user.getUserPhone();
				String now_user_name =user.getUserName();
				talkingInfo.put("now_user_phone", now_user_phone);
				talkingInfo.put("now_user_name", now_user_name);
				
				return  CommonUtil.constructResponse(EnumUtil.OK, "操作成功",talkingInfo);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误",null);	
		}
		
	}
	
	/*
	 * @author:wzh
	 * talking发布页
	 * 返回talking修改数据
	 */
	@RequestMapping("Specialtalking")
	@ResponseBody
	public JSONObject Specialtalking(Integer campus_id) throws Exception{
		if (campus_id == null || "".equals(campus_id)){
			return CommonUtil.constructResponse(0, "用户参数错误",null);
		}
		List<Map<String, Object>> list=new ArrayList();
		 try{
		 list=talkingService.Specialtalking(campus_id);
		 }catch(Exception e){
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
			}
		return CommonUtil.constructResponse(1, "成功",list);
	}
	
	
	///Test
	@RequestMapping("SelectCityNameByCity_id")
	@ResponseBody
	public JSONObject SelectCityNameByCity_id(int city_id){
		
		return CommonUtil.constructResponse(1, "成功",talkingService.SelectCityNameByCity_id(city_id));
	}
}

