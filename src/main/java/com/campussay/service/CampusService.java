package com.campussay.service;

import java.util.List;
import java.util.Map;

public interface CampusService {

    /**
     * 获取所有状态为10的学校
     * @param
     * @return
     * @throws Exception 
     */

    public abstract List<Map<String, Object>> getAllCampusName() throws Exception;
    
    public abstract String getCampusNameByid(int campus_id) throws Exception;

    Integer getStudentCheckCampus(int userId);



}
