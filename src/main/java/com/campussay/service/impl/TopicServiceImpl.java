package com.campussay.service.impl;

import com.campussay.dao.*;
import com.campussay.model.Topic;
import com.campussay.service.TopicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service("TopicService")
@Transactional(rollbackFor=Exception.class)
public class TopicServiceImpl implements TopicService {
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private AnswerDao answerDao;
	@Autowired
	private FollowDao followDao;
	@Autowired
	private TalkingTypeDao talkingTypeDao;
	@Autowired
	private UserDao userDao;

	public Map<String, Object> getNewTopics(int userId,int page, int size) throws Exception{
		HashMap<String, Object> result = new HashMap<>();
		List<Map<String, Object>> list=null;
		if(topicDao.getNewTopics(userId,page,size).size()>0){
			list = topicDao.getNewTopics(userId,page,size);
		}
		if (list!=null) {
			HashMap first = (HashMap) list.get(0);
			result.put("rows", first.get("totalLine"));
		}
		if(list!=null){
			for (Map<String, Object> obj : list) {
				int k=0;
				obj.remove("totalLine");
				if (obj.get("context") != null) {
					byte[] b = (byte[]) obj.get("context");
					obj.put("context", new String(b, Charset.forName("utf-8")));
				} else {
					obj.put("answerId", 0);
					obj.put("context", "现在还没有回答...");
				}
				if(answerDao.getUserIsLike(userId,(int)obj.get("answerId"))>0){
					obj.put("userIsLike", 1);
				}else{
					obj.put("userIsLike", 0);
				}
			}
			result.put("list", list);
		}
		if(list==null){
			result.put("rows", 0);
		}
		return result;
	}

	/*
	 * 根据用户关注领域得到最新topic
	 * 
	 * @param page 分页，为null时默认为1
	 */
	public Map<String, Object> getNewTopicsByUser(int userId, int page, int size) throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		List<Map<String, Object>> list=null;
		try{
			list = topicDao.getNewTopicsByUser(userId,
				page, size);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
		if (list!=null&&list.size() > 0) {
			HashMap first = (HashMap) list.get(0);
			result.put("rows", first.get("totalLine"));
		}
		if (list!=null&&list.size() > 0) {
			for (Map<String, Object> obj : list) {
				obj.remove("totalLine");
				if (obj.get("context") != null) {
					byte[] b = (byte[]) obj.get("context");
					obj.put("context", new String(b, Charset.forName("utf-8")));
				} else {
					obj.put("context", "现在还没有回答...");
				}
			}
			result.put("list", list);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getUnanswerTopics(int page, int size) throws Exception {
		// TODO Auto-generated method stub
		try{
			return topicDao.getUnanswerTopics(page, size);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
	}

	@Override
	public Map<String, Object> getAllTopicByUserID(int sessionUserId,int userId, int page,
			int size) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> result = new HashMap<>();
		List<Map<String, Object>> list=null;
		try{
			list = topicDao.getAllTopicByUserID(sessionUserId,userId,
				page, size);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
		if (list!=null&&list.size() > 0) {
			result.put("rows", list.size());
		}
		
		if (list!=null&&list.size() > 0) {
			for (Map<String, Object> mp : list) {
				mp.remove("totalLine");
				if (mp.get("answerContent") == null)
					continue;
				byte[] b = (byte[]) mp.get("answerContent");
				mp.put("answerContent", new String(b, Charset.forName("utf-8")));
			}
		}
		result.put("list", list);
		return result;
		
	}

	/*
	 * 添加一个Topic
	 */
	@Override
	public int AddATopic(Topic topic,int userId) throws Exception {
		// TODO Auto-generated method stub
		int n=0;
	try{
		topicDao.insertSelective(topic);
		n=topicDao.getNewTopicId(userId);
		return n;
	}catch (Exception e) {
		throw new Exception("数据库操作异常");
	}
	}

	/*
	 * 得到一个详细的topic通过topic_id
	 */
	public List<Map<String, Object>> getDetailTopic(int userId,int topic_id) throws Exception {
		List<Map<String, Object>> list=null;
		try{
			list = topicDao.getDetailTopic(userId,topic_id);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
		if(list!=null){
			for (Map<String, Object> obj : list) {
				if (obj.get("intro") != null) {
					byte[] b = (byte[]) obj.get("intro");
					obj.put("intro", new String(b, Charset.forName("utf-8")));
				}
			}
		}
		return list;
	}

	/*
	 * 通过field_id得到所有的话题
	 */
	public Map<String, Object> getAllTopicByFieldID(int field_id, int page,
			int size) throws Exception {
		List<Map<String, Object>> list=null;
		HashMap<String, Object> result = new HashMap<>();
		try{
			list = topicDao.getAllTopicByFieldID(field_id, page, size);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
		if (list!=null&&list.size() > 0) {
			HashMap first = (HashMap) list.get(0);
			result.put("rows", first.get("totalLine"));
		}
		if (list!=null&&list.size() > 0){
			for (Map<String, Object> obj : list) {
				obj.remove("totalLine");
				if (obj.get("context") != null) {
					byte[] b = (byte[]) obj.get("context");
				obj.put("context", new String(b, Charset.forName("utf-8")));
				}
			}
		}
		result.put("list", list);
		return result;
	}

	/*
	 * 通过领域id得到领域图片
	 */
	public String getPictureUrl(int id) throws Exception {
		try{
			return talkingTypeDao.getPictureUrl(id);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
	}
	
	// 访问自己的话题社
	public Map<String, Object> getUserDetailMsgById(int userId) throws Exception {
		Map<String, Object> mp=null;
		List<HashMap> concernField=null;
		List<HashMap> skillField=null;
		try{
			mp = userDao.getUserInfo(userId);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
		if(mp!=null){
			mp.put("attention", -1);
			mp.put("edit", "1");
		}
		try{
			concernField = followDao.getFollowsByUserId(userId);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
		if (concernField!=null&&concernField.size() > 0)
			mp.put("concernName", concernField);
		try{
			 skillField = followDao.getSkillNameByUserId(userId);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
		if (skillField!=null&&skillField.size() > 0)
			mp.put("skillsName", skillField);
		return mp;
	}
	// 访问别人的话题社
	public Map<String, Object> getUserDetailMsgByOtherId(int ownId, int userId) throws Exception {
		Integer n = 0;
		Map<String, Object> mp=null;
		List<HashMap> concernField=null;
		List<HashMap> skillField=null;
		try{
			mp = userDao.getUserInfo(userId);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
		if (ownId == 0) {
			mp.put("attention", "-1");
			mp.put("edit", "1");
		} else {
			try{
				n = userDao.getAttention(ownId, userId);
			}catch (Exception e) {
				throw new Exception("数据库操作异常");
			}
			if (n.intValue() > 0) {
				mp.put("attention", "1");
			} else {
				mp.put("attention", "0");
			}
			if (ownId == userId) {
				mp.put("edit", "1");
			} else {
				mp.put("edit", "0");
			}
		}
		try{
			concernField = followDao.getFollowsByUserId(userId);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
		if (concernField!=null&&concernField.size() > 0)
			mp.put("concernName", concernField);
		
		try{
			skillField = followDao.getSkillNameByUserId(userId);
		}catch (Exception e) {
			throw new Exception("数据库操作异常");
		}
		if (skillField!=null&&skillField.size() > 0)
			mp.put("skillsName", skillField);
		return mp;
	}

	@Override
	public  Map<String, Object> getNewTopicsUnLogin(int page, int size) {
		HashMap<String, Object> result = new HashMap<>();
		List<Map<String, Object>> list=null;
		if(topicDao.getNewTopicsUnLogin(page, size).size()>0){
			list = topicDao.getNewTopicsUnLogin(page,size);
		}
		if (list!=null) {
			HashMap first = (HashMap) list.get(0);
			result.put("rows", first.get("totalLine"));
		}
		if(list!=null){
			for (Map<String, Object> obj : list) {
				int k=0;
				obj.remove("totalLine");
				if (obj.get("context") != null) {
					byte[] b = (byte[]) obj.get("context");
					obj.put("context", new String(b, Charset.forName("utf-8")));
				} else {
					obj.put("answerId", 0);
					obj.put("context", "现在还没有回答...");
				}
					obj.put("userIsLike", 0);
			}
			result.put("list", list);
		}
		if(list==null){
			result.put("rows", 0);
		}
		return result;
	}

	@Override
	public Map<String, Object> getAllTopicByFirstFieldID(int field_id,
			int page, int size) throws Exception {
		// TODO Auto-generated method stub
		
		
		
		
		
		
		
		
		
		
		return null;
	}

}
