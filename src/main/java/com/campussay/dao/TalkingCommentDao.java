package com.campussay.dao;

import com.campussay.model.TalkingComment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by wangwenxiang on 15-12-4.
 */
@Repository
public interface TalkingCommentDao {
    List<Map<String,Object>> getAllCommentByUser(int user_id,int first,int size);

    int getAllCommentCountByUser(int userId);

	int saveTalkingComment(TalkingComment talkingComment);
}
