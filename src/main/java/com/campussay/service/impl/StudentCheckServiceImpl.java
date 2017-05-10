/**
 * @author effine
 * @Date 2016年1月10日  下午10:06:37
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;


import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.campussay.dao.StudentCheckDao;
import com.campussay.service.StudentCheckService;
import com.campussay.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class StudentCheckServiceImpl implements StudentCheckService {

    @Autowired
    private StudentCheckDao studentCheckDao;

    @Override
    public JSONObject getStudentCheck(int userId) {
        HashMap hashMap = studentCheckDao.getStudentCheck(userId);
        int code = 0;
        String result = "error";
        if(hashMap.size() != 0) {
            code = 1;
            result = "succeed";
        }
        return CommonUtil.constructResponse(code, result, hashMap);
    }
}
