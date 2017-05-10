package com.campussay.dao;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by wangwenxiang on 15-12-4.
 */
@Repository
public interface StudentCheckDao {

    /**
     * 根据用户编号查找认证设置
     * @param userId 需要查找的用户编号
     * @return
     */
    public HashMap<String, Objects> getStudentCheck(int userId);


}
