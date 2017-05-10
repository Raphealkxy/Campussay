package com.campussay.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Information;
import com.campussay.model.User;
import com.campussay.service.InformationService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;
@Controller
@RequestMapping("/information")
public class InformationController {
	@Resource
	private InformationService informationService;
	
	
	private static final int pageSize=10;
	
	/**
	 * 
	 * @Title: getUserInformationList 
	 * @Description: 分页查询用户消息
	 * @param session
	 * @param informationType
	 * @param pageNum
	 * @return
	 * @return: JSONObject
	 */
	@RequestMapping("getuserInformationList")
	@ResponseBody
	public JSONObject getUserInformationList(HttpSession session,Integer informationType,Integer pageNum,Integer informationStatus){
		User user= (User) session.getAttribute("user");
		if(user==null){
			return  CommonUtil.constructResponse(EnumUtil.NOT_LOGIN,"请登录",null);
		}
		int userId = user.getUserId();
		if (pageNum == null){
			pageNum = new Integer(1);
		}
		int i_pageNum = pageNum;
		i_pageNum -=1;
		
		if (i_pageNum<0){
			i_pageNum=0;
		}
	    int start=	i_pageNum*EnumUtil.PAGE_SIZE;
		try {
			Map result = informationService.selectUserInformation(userId+"",informationType,informationStatus, start, EnumUtil.PAGE_SIZE);
			if(result == null ){
				return  CommonUtil.constructResponse(EnumUtil.NO_DATA, "暂无数据",null);
				
			}else{
				return  CommonUtil.constructResponse(EnumUtil.OK, "查询成功",result);
			}
		} catch (Exception e) {
			return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"系统错误",null);
		}
	}
	
	/**
	 * 设置消息为已读
	 * @Title: setInformationRead 
	 * @Description: TODO
	 * @param session
	 * @param informationIds ["dd",""]
	 * @return
	 * @return: JSONObject
	 */
	@RequestMapping("setInformationRead")
	@ResponseBody
	public JSONObject setInformationRead(HttpSession session,String informationIds){
		User user= (User) session.getAttribute("user");
		if(user==null){
			return  CommonUtil.constructResponse(EnumUtil.NOT_LOGIN,"请登录",null);
		}
		int userId = user.getUserId();
		if(informationIds==null ||"".equals(informationIds.trim())){
//			return  CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "informationIds必须填写",null);
			informationIds = "[]";
		}
		
		try {
			JSONArray informationIds_json = (JSONArray) JSONObject.parse(informationIds);
			
			if (informationIds_json!=null&&informationIds_json.size()>0){
				List<Information> list = new ArrayList<Information>();
				for(int i = 0 ;i<informationIds_json.size();i++){
					String informationID = (String)informationIds_json.get(i);
					System.out.println(informationID);
					Information item = new Information();
					item.setInformationReadtime(CommonUtil.getSystemTime());
					item.setInformationIsread(Information.INFORMATION_HAD_READ);
					item.setInformationId(informationID);
					item.setInformationUser(userId+"");
					list.add(item);
				}
				informationService.batchSetInformationRead(list);
				
				return CommonUtil.constructResponse(EnumUtil.OK, "操作成功",null);
			}else{
				Information item = new Information();
				item.setInformationReadtime(CommonUtil.getSystemTime());
				item.setInformationIsread(Information.INFORMATION_HAD_READ);
				
				item.setInformationUser(userId+"");
				informationService.batchSetInformationRead(item);
				return CommonUtil.constructResponse(EnumUtil.OK, "操作成功",null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "informationIds 参数 json错误",null);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误",null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误",null);
		}
		
		
	}
	
	
	
	/**
	 * 删除消息
	 * @Title: deleteInformation 
	 * @Description: TODO
	 * @param session
	 * @param informationIds ["dd",""]
	 * @return
	 * @return: JSONObject
	 */
	@RequestMapping("deleteInformation")
	@ResponseBody
	public JSONObject deleteInformation(HttpSession session,String informationIds){
		User user= (User) session.getAttribute("user");
		if(user==null){
			return  CommonUtil.constructResponse(EnumUtil.NOT_LOGIN,"请登录",null);
		}
		int userId = user.getUserId();
		if(informationIds==null ||"".equals(informationIds.trim())){
			return  CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "informationIds必须填写",null);
		}
		
		try {
			JSONArray informationIds_json = (JSONArray) JSONObject.parse(informationIds);
			
			if (informationIds_json!=null&&informationIds_json.size()>0){
				List<Information> list = new ArrayList<Information>();
				for(int i = 0 ;i<informationIds_json.size();i++){
					String informationID = (String)informationIds_json.get(i);
					System.out.println(informationID);
					Information item = new Information();
					item.setInformationId(informationID);
					item.setInformationUser(userId+"");
					list.add(item);
				}
				informationService.batchDeleteInformation(list);
				
				return CommonUtil.constructResponse(EnumUtil.OK, "操作成功",null);
			}else{
				return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "至少一个informationId",null);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return CommonUtil.constructResponse(EnumUtil.CAN_NOT_NULL, "informationIds 参数 json错误",null);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误",null);
		}
		
		
	}
	
	
	
	
	
	
	@RequestMapping("test")
	@ResponseBody
	public JSONObject test(String type) throws ParseException{
		
		Information information = new Information();
		information.setInformationId(CommonUtil.GUID().replace("_", ""));
		information.setInfomationIsDelete(Information.INFORMATION_STATUS_CAN_USE);
		information.setInformationContent("转账成功了"+(Math.random()*100));
		information.setInformationCrateTime(CommonUtil.getSystemTime());
		information.setInformationType(1);
		information.setInformationUser("1111");
		if ("1".equals(type)){
			informationService.insertInfomation(information);
			return  CommonUtil.constructResponse(EnumUtil.OK, "操作成功",null);
		}
		Information informationsystem = new Information();
		informationsystem.setInformationId(CommonUtil.GUID().replace("_", ""));
		informationsystem.setInfomationIsDelete(Information.INFORMATION_STATUS_CAN_USE);
		informationsystem.setInformationContent("系统消息"+(Math.random()*100));
		informationsystem.setInformationCrateTime(CommonUtil.getSystemTime());
		informationsystem.setInformationType(Information.SYSTEM_INFORMATION);
		if ("2".equals(type)){
			informationService.insertSystemInfomation(informationsystem);
			return  CommonUtil.constructResponse(EnumUtil.OK, "操作成功",null);
		}
		if ("3".equals(type)){
			informationService.updateNewSystemInformation("1111");
			return  CommonUtil.constructResponse(EnumUtil.OK, "操作成功",null);
		}
		return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"系统错误",null);
		
	}
	
	
	
	/*
	 * 用户获取回答信息  yangchun
	 */
	@RequestMapping("getUserAnswerInfo")
	@ResponseBody
	public JSONObject getUserAnswerInfo(HttpSession session,@RequestParam(required = false)Integer page){
		User user= (User) session.getAttribute("user");
		if(user==null){
			return  CommonUtil.constructResponse(EnumUtil.NOT_LOGIN,"请登录",null);
		}
		if(page==null||page<0)
    		page=1;
		int userId=user.getUserId();
		HashMap list=null;
		try{
			list=informationService.selectUserInformation(String.valueOf(userId), 4, 0, (page-1)*pageSize, pageSize);
		}catch(Exception e){
			return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库操作异常",null);
		}
		if(list==null){
			return CommonUtil.constructResponse(1, "数据库没有数据", list);
		}
		return CommonUtil.constructResponse(1, "获取数据成功", list);
	}
	
	
	
	
	
	
}
