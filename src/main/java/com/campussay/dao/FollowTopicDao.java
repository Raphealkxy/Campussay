package com.campussay.dao;

import java.util.List;

public interface FollowTopicDao {
	public int addAConcernTopic(int userId,int topicId);
	
	public int cancelConcernTopic(int userId,int topicId);
	
	public List<Integer> getFollowUsersByTopicId(int topicId);
	
}
