package com.campussay.dao;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.WorkExperience;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by wangwenxiang on 15-12-4.
 */
@Repository
public interface WorkExprienceDao {

    /**
     * 添加用户的工作经验
     * @param workExperience
     * @return
     */
    public int userSettingWorkExperience(WorkExperience workExperience);

    /**
     * 根据用户id查询工作经验
     * @param userId
     * @return
     */
    public List<HashMap<String, Object>> getUserSettingWorkExperience(int userId,int state);

    /**
     * 获取用户工作经历
     * @param userId
     * @param state
     * @return
     */
    List<HashMap<String,Objects>> getUserWorkExperience(int userId,int state);

    void updateWorkExprience(JSONObject jsonObject);

    void addWorkExprience(JSONObject jsonObject);

    void delWorkExprience(@Param("del") String[] del,int userId);
}
