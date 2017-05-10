package com.campussay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campussay.dao.FollowTopicDao;
import com.campussay.service.FollowTopicService;
@Service
@Transactional(rollbackFor=Exception.class)
public class FollowTopicServiceImpl implements FollowTopicService {
	
	
	@Autowired
	private FollowTopicDao followTopicDao;
	
	
	@Override
	public int addAConcernTopic(int userId, int topicId) {
		// TODO Auto-generated method stub
		int n=0;
		try{
			n=followTopicDao.addAConcernTopic(userId, topicId);
		}catch(Exception e){
			throw e;
		}
		return n;
	}

	@Override
	public int cancelConcernTopic(int userId, int topicId) {
		// TODO Auto-generated method stub
		int n=0;
		try{
			n=followTopicDao.cancelConcernTopic(userId, topicId);
		}catch(Exception e){
			throw e;
		}
		return n;
	}

	@Override
	public List<Integer> getFollowUsersByTopicId(int topicId) {
		// TODO Auto-generated method stub
		return followTopicDao.getFollowUsersByTopicId(topicId);
	}

}
