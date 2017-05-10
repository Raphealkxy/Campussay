package com.campussay.dao;

import com.campussay.model.Topic;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface TopicDao {
	
	public List<Map<String, Object>> getNewTopicsUnLogin(int page, int size);
	/*
	 * 得到最新topic
	 * 
	 * @param page 分页，为null时默认为1
	 */
	public List<Map<String, Object>> getNewTopics(int userId, int page, int size);

	/*
	 * 根据用户关注领域得到最新topic
	 * 
	 * @param page 分页，为null时默认为1
	 */
	public List<Map<String, Object>> getNewTopicsByUser(int userId, int page,
			int size);

	/*
	 * 得到所有未回答的话题
	 */
	List<Map<String, Object>> getUnanswerTopics(int page, int size);

	/*
	 * 通过用户id得到话题
	 */
	public List<Map<String, Object>> getAllTopicByUserID(int sessionUserId,
			int userId, int page, int size);

	/*
	 * 通过二级field_id得到话题
	 */
	public List<Map<String, Object>> getAllTopicByFieldID(int field_id,
			int page, int size);

	public List<Map<String, Object>> getDetailTopic(int userId, int topic_id);

	/*
	 * 添加一个话题领域
	 */
	public int insertSelective(Topic topic);

	/*
	 * 修改话题更新时间
	 */
	public int updateTopicTime(Topic topic);

	/*
	 * 得到某一個話題下參與回答的人姓名
	 */
	public List<HashMap> getAllUserByTopicId(int topicId);

	/*
	 * 用于消息系统 通过TopicId得到Topic
	 */
	public HashMap<String, Object> getTopicById(int topicId);

	public HashMap<String, Object> getTopicByAnswerId(int answerId);
	
	
	/*
	 * 通过一级field_id得到话题
	 */
	public List<Map<String, Object>> getAllTopicByFirstFieldID(int field_id,
			int page, int size);
	
	/*
	 * 添加一个新的话题之后，返回id
	 */
	public int getNewTopicId(int userId);

}