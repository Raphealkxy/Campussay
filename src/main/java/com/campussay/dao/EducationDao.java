package com.campussay.dao;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Education;
import com.campussay.model.StudentCheck;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Repository
public interface EducationDao {

    /**
     * 添加教育经验
     * @param education
     * @return
     */
    public int userSettingEducation(Education education);

    /**
     * 查询教育经验
     * @param userId
     * @return
     */
    public List<HashMap<String,Objects>> getUserSettingEducation(int userId,int state);

    /**
     * 获取用户的教育经历
     * @param userId 用户id
     * @param state 状态
     * @return
     */
    List<HashMap<String,Objects>> getUserEducation(int userId,int state);

    /**
     * 获取所有学院名称
     */
    List<HashMap<String,Objects>> getAllAcademy(@Param("academy") String academy);

    /**
     * 获取所有系别名称
     */
    List<String> getAllMajor(String major);

    /**
     * 个人设置添加信息
     *
     * */
    public void insStudInform(StudentCheck sc);

    void updateUserState(int userId,int state);

    void updateEducation(JSONObject jsonObject);

    void addEducation(JSONObject jsonObject);

    void delEducation(@Param("del") String[] del,int userId);

    List<Map> studentPicture(int userId);

}
