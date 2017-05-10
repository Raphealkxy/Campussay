package com.campussay.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Education;
import com.campussay.model.StudentCheck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface EducationService {

    /**
     * 添加教育经历
     * @param education
     * @return
     */
    public int userSettingEducation(Education education);

    /**
     * 查询教育经历
     * @param userId
     * @return
     */
    public JSONObject getUserSettingEducation(int userId,int state);

    /**
     * 查询用户教育经历
     * @param userId 用户id
     * @param state 状态
     * @return
     */
    public List<HashMap<String,Objects>> getUserEducation(int userId, int state);

    /**
     * 查询所有学院
     */
    public JSONObject getAllAcademy(String academy);

    /**
     * 个人设置 教育经历 查询专业
     */
    public JSONObject getAllMajor(String major);

    /**
     * 个人认证
     *pzy
     * */
    public int insertStudInform(StudentCheck sc);

    void updateEducation(JSONArray changeArray,int userId);

    void addEducation(JSONArray addArray,int userId);

    void delEducation(String del,int userId);

    List<Map> studentPicture(int userId);
}
