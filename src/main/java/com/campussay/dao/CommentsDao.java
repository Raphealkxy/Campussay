package com.campussay.dao;

import com.campussay.model.Comments;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface CommentsDao {

    int insertSelective(Comments record);
    List<HashMap> getCommentsByAnswerId(int answerId,int page,int size);
    
}