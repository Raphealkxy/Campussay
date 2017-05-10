package com.campussay.dao;


public interface UserAnswerIsLikeDao {
	
	public int addUserAnswerIsLike(int userId, int answerId, int isLike);
	public int getIsLikeByUser(int userId, int answerId, int isLike);    //判断用户是否已经点过赞了
	public int delUserAnswerLike(int userId, int answerId, int isLike);
}
