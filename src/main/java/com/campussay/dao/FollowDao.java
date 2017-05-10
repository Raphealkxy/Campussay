package com.campussay.dao;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangchun on 2016/1/23.
 */
public interface FollowDao {
    public int addAConcernField(int userId,int taking_type_id);
    //得到關注領域名稱
    public List<HashMap> getFollowsByUserId(int userId);
    public List<HashMap> getUnFollowsByUserId(int userId,int page,int size);
    public List<HashMap> getSkillByUserId(int userId,int page,int size);
    public HashMap getSkillTopicNum(int userId);
    //得到擅長領域的名稱
    public List<HashMap> getSkillNameByUserId(int userId);
    //用户取消关注
    public int cancelConcernField(int userId,int taking_type_id);
    //判断用户是否关注
    public int isConcernField(int userId,int taking_type_id);



}
