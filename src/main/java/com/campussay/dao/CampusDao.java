package com.campussay.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;



/**
 * Created by wangwenxiang on 15-12-4.
 */
@Repository
public interface CampusDao {

    /**
     * 获得 所有的 学校名称 学校id
     * @return
     */
    public List<Map<String, Object>> getAllCampusName();
    /**
     * @author wzh
     * 根据id返回校名
     * @return
     */
    public String getCampusNameByid(@Param("campus_id")int campus_id);
    Integer getStudentCheckCampus(int userId);

}
