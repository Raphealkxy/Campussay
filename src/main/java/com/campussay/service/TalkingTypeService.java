/**
 * @author effine
 * @Date 2016年1月10日  下午9:04:57
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TalkingTypeService {

	/*
	 * 说说分类
	 * Author：汪治宏
	 */
	public abstract Map<String,Object> TakingTypedetail() throws Exception;

	//返回同学们干啥以及用户name
	public abstract JSONObject Order_list();

	/**
	 * 获取用户的擅长领域
	 * @param userId 被获取者的id
	 * @return json
	 */
	public abstract List<HashMap<String, Object>> getUserSkill(int userId);



	List<Map<String, Object>> selectAllOneTimesTalkType();

	Map<String, Object> selectAllSecondTalkType(int user_id,int page);

	Map<String, Object> selectSecondTalkType(int user_id,int page,int parent);

	/*
	 yangchun 得到一级领域下拉菜单
	 */
	public List<HashMap> getFirstTalkingType();

	/*
	 yangchun 通过一级领域id 得到二级领域下拉菜单
	 */
	public List<HashMap> getSecondTalkingType(int talkingTypeId);

	public Map<String,Object> getTalkingTypeDetailById(int talkingTypeId);


}
