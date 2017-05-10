package com.campussay.service;

import com.alibaba.fastjson.JSONObject;

public interface StudentCheckService {

    /**
     * 查询学生认证信息
     * @param userId 用户id
     * @return
     */
    public JSONObject getStudentCheck(int userId);
}
