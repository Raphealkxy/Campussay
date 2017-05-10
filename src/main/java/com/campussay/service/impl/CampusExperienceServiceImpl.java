/**
 * @author effine
 * @Date 2016年1月10日  下午9:51:32
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.dao.CampusExperienceDao;
import com.campussay.model.CampusExperience;
import com.campussay.service.CampusExperienceService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor=Exception.class)
public class CampusExperienceServiceImpl implements CampusExperienceService {

    @Autowired
    private CampusExperienceDao campusExperienceDao;

    public int userSettingCampusExperience(CampusExperience campusExperience) {
    	int n=0;
    	try{
    		n=campusExperienceDao.setCampusExperience(campusExperience);
    	}catch(Exception e){
     	   throw e;
    	}
    	return n;
    }
    @Override
    public List<HashMap<String, Objects>> getCampusExperience(int userId,int state) {
    	List<HashMap<String, Objects>> list=null;
    	try{
    		list=campusExperienceDao.getCampusExperience(userId,state);
    	}catch(Exception e){
      	   throw e;
     	}
       return list;
    }

    @Override
    public void updateCampusExperience(JSONArray changeArray,int userId) throws Exception {
       try{
    	   for (Object o : changeArray){
    		   JSONObject j = JSONObject.parseObject(o.toString());
    		   j.put("user_id",userId);
    		   campusExperienceDao.updateCampusExperience(j);
    	   }
       }catch(Exception e){
    	   throw new Exception("数据库操作错误");
	  }
    }

    @Override
    public void addCampusExperience(JSONArray addArray,int userId) {
     try{  
    	for (Object o : addArray){
            JSONObject j = JSONObject.parseObject(o.toString());
            j.put("user_id",userId);
            campusExperienceDao.addCampusExperience(j);
        }
     }catch(Exception e){
 	   throw e;
	 }
    }

    @Override
    public void delCampusExperience(String del, int userId) {
        String[] ids = del.split(",");
        try{  
        	campusExperienceDao.delCampusExperience(ids,userId);
    	}catch(Exception e){
    		throw e;
    	}
    }


}
