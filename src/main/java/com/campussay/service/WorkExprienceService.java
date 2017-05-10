package com.campussay.service;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.WorkExperience;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public interface WorkExprienceService {


    /**
     * 添加用户工作经验
     * @param workExperience
     * @return
     */
    public int userSettingWorkExperience(WorkExperience workExperience);

    /**
     * 根据用户id查询工作经验
     * @param userId
     * @return
     */
    public JSONObject getUserSettingWorkExperience(int userId,int state);

    /**
     * 获取用户工作经历
     * @param userId
     * @return
     */
    List<HashMap<String,Objects>> getUserWorkExperience(int userId,int state);

    void updateWorkExprience(JSONObject jsonObject);

    void addWorkExprience(JSONObject jsonObject);

    void delWorkExprience(String del,int userId);
}
