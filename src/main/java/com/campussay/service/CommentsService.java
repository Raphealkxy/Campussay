package com.campussay.service;

import com.campussay.model.Comments;

import java.util.HashMap;

public interface CommentsService {
	
	public int addAComments(Comments comm,int commentUserId,String commentUser) throws Exception;
	HashMap getCommentsByAnswerId(int answerId,int page,int size);
}
