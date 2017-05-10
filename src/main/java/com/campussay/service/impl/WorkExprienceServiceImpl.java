/**
 * @author effine
 * @Date 2016年1月10日  下午10:08:30
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.campussay.dao.WorkExprienceDao;
import com.campussay.model.WorkExperience;
import com.campussay.service.WorkExprienceService;
import com.campussay.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class WorkExprienceServiceImpl implements WorkExprienceService {

    @Resource
    private WorkExprienceDao workExprienceDao;

    @Override
    public int userSettingWorkExperience(WorkExperience workExperience) {
        return workExprienceDao.userSettingWorkExperience(workExperience);
    }

    @Override
    public JSONObject getUserSettingWorkExperience(int userId , int state) {
        return CommonUtil.constructResponse(1, null, workExprienceDao.getUserSettingWorkExperience(userId,state));
    }

    @Override
    public List<HashMap<String, Objects>> getUserWorkExperience(int userId, int state) {
        return workExprienceDao.getUserWorkExperience(userId,state);
    }

    @Override
    public void updateWorkExprience(JSONObject jsonObject) {
        workExprienceDao.updateWorkExprience(jsonObject);
    }

    @Override
    public void addWorkExprience(JSONObject jsonObject) {
        workExprienceDao.addWorkExprience(jsonObject);
    }

    @Override
    public void delWorkExprience(String del, int userId) {
        String[] ids = del.split(",");
        workExprienceDao.delWorkExprience(ids,userId);
    }
}
