package com.campussay.dao;

import com.campussay.model.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by wangwenxiang on 15-12-4.
 */
@Repository
public interface TalkingTypeDao {
	/**
	 * Author:wangzhihong 
	 */
	//得到第一层级
		public List<Map<String,Object>> talkingType_floor();
		
		//返回子层级的talkingtype
		public List<Map<String,Object>> talkingType_floor_list(int talking_type_parent);
		
		

		
		//返回同学干啥
		public List<Order> Order_list();
		
		//返回同学name
		public String User_name(int User_id);

	/**
	 * 获取用户的擅长领域
	 * @param userId 被获取者的id
	 * @return json
	 */
	List<HashMap<String,Object>> getUserSkill(int userId);
	/*
	 *通过takingtype_id得到领域图片
	 */
	String getPictureUrl(int takingTypeId);
	/*pizhiyuan
	 * 查询出用户未关注的领域
	 * */
	List<Map<String, Object>> selectUnconTalkType(Integer id, int head, int size);

	/*
	 * 查询一级领域
	 *
	 * */
	List<Map<String,Object>> selectAllOneTimeType();
	/*
	 * 查询出个人所关注的所有领域
	 *
	 * */
	List<Map<String,Object>> selectAllSecondTimeType(int user_id);

	/*
	* 查询出表中所有二级领域的内容
	*
	* */
	List<Map<String,Object>> selectSecTimesTalkType();

	/*
	 yangchun 得到一级领域下拉菜单
	 */
	public List<HashMap> getFirstTalkingType();

	/*
	 yangchun 通过一级领域id 得到二级领域下拉菜单
	 */
	public List<HashMap> getSecondTalkingType(int talkingTypeId);


	/*
    * 根据一级领域来查询二级领域
    *
    * */
	List<Map<String,Object>> selectTimesTalkType(int parent,int head ,int size);

	/*
	通过id得到领域的详细信息
	 */
	Map<String,Object> getTalkingTypeDetailById(int talkingTypeId);



}

