package com.campussay.service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangchun on 2016/1/23.
 */
public interface FollowService {

    public int addAConcernField(int userId,int taking_type_id) throws Exception;
    public List<HashMap> getFollowsByUserId(int userId);
    public HashMap getUnFollowsByUserId(int userId,int page,int size);
    public HashMap getSkillByUserId(int userId,int page,int size);
    public HashMap getSkillTopicNum(int userId);
    public int cancelConcernField(int userId,int taking_type_id);
    //判断用户是否关注
    public int isConcernField(int userId,int taking_type_id);
}
