package com.campussay.service;

import java.util.List;

public interface FollowTopicService {
	public int addAConcernTopic(int userId,int topicId);
	public int cancelConcernTopic(int userId,int topicId);
	public List<Integer> getFollowUsersByTopicId(int topicId);
}
