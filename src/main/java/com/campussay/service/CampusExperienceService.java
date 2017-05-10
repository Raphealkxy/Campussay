package com.campussay.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.model.CampusExperience;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public interface CampusExperienceService {

    /**
     * 个人设置 添加用户校园工作经历
     * @param campusExperience
     * @return
     */
    public int userSettingCampusExperience(CampusExperience campusExperience);

    /**
     * 个人设置 查询用户的校园工作经历
     * @param userId
     * @param state
     * @return
     */
    public List<HashMap<String,Objects>> getCampusExperience(int userId,int state);

    /**
     * 修改校园经历
     * @throws Exception 
     */
    void updateCampusExperience(JSONArray changeArray,int userId) throws Exception;


    /**
     * 修改校园经历
     */
    void addCampusExperience(JSONArray addArray,int userId);

    void delCampusExperience(String del,int userId);
}