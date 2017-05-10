package com.campussay.service;

import com.campussay.model.Answer;

import java.util.HashMap;

public interface AnswerService {
	
	HashMap selectAnswersByTopicId(int userId,int topic_id,Integer page) throws Exception;

	HashMap selectAllAnswerByUserID(int user_id,Integer page) throws Exception;

	//得到topic答案数量
	HashMap getAnswerNumByTopicId(int topic_id) throws Exception;
	//点赞
	int addIsLike(int userId,String username,int answerId) throws Exception;
	//减赞
	int disIsLike(int userId,int answerId) throws Exception;
	//添加topic答案
	int addAnswer(Answer answer,int replyId,String replyName) throws Exception;

	//新添加一个答案后，跳转到最后一页，返回当前answer总条数
	public int getLastPageByTopicId(int topic_id);
	
	 public HashMap<String,Object> getAnswerDetailById(int answerId);
	



}
