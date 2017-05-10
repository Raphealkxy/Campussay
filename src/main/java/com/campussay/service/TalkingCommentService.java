/**
 * @author effine
 * @Date 2016年1月10日  下午8:50:07
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.TalkingComment;

import java.util.List;
import java.util.Map;

public interface TalkingCommentService {

	/**
	 * 根据userId获取talkingComment
	 * @param userId
	 * @param page
     * @return
     */
	List<Map<String, Object>> getAllCommentByUser(int userId, Integer page);

	/**
	 * 获取comment数量
	 * @param userId
	 * @return
     */
	int getAllCommentCountByUser(int userId);

	/**
	 * 添加talking评论
	 * @param talkingComment
	 * @return
	 */
	JSONObject saveTalkingComment(TalkingComment talkingComment, String orderId) throws Exception;
}
