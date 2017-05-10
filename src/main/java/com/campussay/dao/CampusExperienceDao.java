package com.campussay.dao;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.CampusExperience;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by wangwenxiang on 15-12-4.
 */
@Repository
public interface CampusExperienceDao {

    /**
     * 添加校园实践经历
     */
     int setCampusExperience(CampusExperience campusExperience);
    /**
     * 获取用户社团经历
     * @param userId 用户id
     * @param state 状态,状态为10的查出来
     * @return
     */
    List<HashMap<String,Objects>> getCampusExperience(int userId,int state);

    void updateCampusExperience(JSONObject jsonObject);

    void addCampusExperience(JSONObject jsonObject);

    void delCampusExperience(@Param("del") String[] del, int userId);
}
