package com.campussay.service;

import com.campussay.model.Topic;

import java.lang.*;
import java.util.List;
import java.util.Map;

public interface TopicService {
	
	/*
	 * 得到最新topic,用户未登录
     * @param page 分页，为null时默认为1
	 */
	public Map<String, Object> getNewTopicsUnLogin(int page, int size);
	/*
	 * 得到最新topic
     * @param page 分页，为null时默认为1
	 */
    public Map<String,Object> getNewTopics(int userId,int page,int size) throws Exception;

    /*
   * 根据用户关注领域得到最新topic
   * @param page 分页，为null时默认为1
   */
    public Map<String,Object> getNewTopicsByUser(int userId,int page,int size) throws Exception;

    
    
    /*
     * 得到还未回答的话题
     */
    public List<Map<String,Object>> getUnanswerTopics(int page,int size) throws Exception;
    
    
    /*
     * 通过用户得到所有话题
     */
    public Map<String,Object> getAllTopicByUserID(int sessionUserId,int userId,int page,int size)  throws Exception;
    
    
    /*
     * 添加新的话题
     */
    public int AddATopic(Topic topic,int userId) throws Exception;
    
    /*
     * 通过topic_id得到topic
     */

    /*
    *通过field_id得到话题
    */
    public Map<String,Object> getAllTopicByFieldID(int field_id,int page,int size) throws Exception;


    /*
     *通过领域id得到领域图片
     */
     public String getPictureUrl(int id) throws Exception;

     public List<Map<String,Object>> getDetailTopic(int userId,int topic_id) throws Exception;

     public Map<String,Object> getUserDetailMsgById(int userId) throws Exception;

     public Map<String,Object> getUserDetailMsgByOtherId(int ownId,int userId) throws Exception;
     
     /*
      *通过field_id得到话题
      */
      public Map<String,Object> getAllTopicByFirstFieldID(int field_id,int page,int size) throws Exception;


}
