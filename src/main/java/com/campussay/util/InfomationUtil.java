package com.campussay.util;

import java.text.ParseException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.campussay.model.Information;
import com.campussay.service.InformationService;

@Component
public class InfomationUtil {
	/**
	 * 消息类型：动态提醒
	 */
	public static final int MESSAGE_TYPE_DYNAMIC_REMINDER = 1;
	/**
	 * 消息类型：活动提醒
	 */
	public static final int MESSAGE_TYPE_ACTIVITY_REMINDER = 2;
	/**
	 * 消息类型：系统提醒
	 */
	public static final int MESSAGE_TYPE_SYSTEM_REMINDER = 3;
	@Resource
	private InformationService informationService;
	
	
	/**
	 * 
	 * @Title: createInformation 
	 * @Description: 创建用户消息
	 * @param content 消息内容
	 * @param userId  用户id
	 * @param messageType 消息类型  消息类型在Information有常量定义 请直接使用
	 * @throws Exception
	 * @return: void
	 */
	public void createInformation(String content,String userId,int messageType) throws Exception{
		try {
			Information information = new Information();
			information.setInformationId(CommonUtil.GUID().replace("_", ""));
			information.setInfomationIsDelete(Information.INFORMATION_STATUS_CAN_USE);
			information.setInformationContent(content);
			information.setInformationCrateTime(CommonUtil.getSystemTime());
			information.setInformationType(messageType);
			information.setInformationUser(userId);
			informationService.insertInfomation(information);
		}catch (ParseException e) {
			throw e;
		}catch (Exception e) {
			throw e;
		}
		
		
	}
	
	/**
	 * 
	 * @Title: createSystemInformation 
	 * @Description: 创建系统消息 这类型消息不针对某个用户  而是所有用户都能看到
	 * @param content
	 * @throws Exception
	 * @return: void
	 */
	public void createSystemInformation(String content) throws Exception{
	try {
		Information informationsystem = new Information();
		informationsystem.setInformationId(CommonUtil.GUID().replace("_", ""));
		informationsystem.setInfomationIsDelete(Information.INFORMATION_STATUS_CAN_USE);
		informationsystem.setInformationContent(content);
		informationsystem.setInformationCrateTime(CommonUtil.getSystemTime());
		informationsystem.setInformationType(Information.SYSTEM_INFORMATION);
		informationService.insertSystemInfomation(informationsystem);
	}catch (ParseException e) {
		throw e;
	}catch (Exception e) {
		throw e;
	}
	}
	
}
