/**
 * @author effine
 * @Date 2016年1月10日  下午9:54:31
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.dao.EducationDao;
import com.campussay.model.Education;
import com.campussay.model.StudentCheck;
import com.campussay.service.EducationService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional(rollbackFor=Exception.class)
public class EducationServiceImpl implements EducationService {

    @Resource
    private EducationDao educationDao;

    @Override
    public int userSettingEducation(Education education) {
    	int n=0;
    	try{
    		n=educationDao.userSettingEducation(education);
    	}catch(Exception e){
    		throw e;
    	}
        return n;
    }

    @Override
    public JSONObject getUserSettingEducation(int userId,int state) {

        List lists = educationDao.getUserSettingEducation(userId,state);
        int code = 0;
        String result = "error";
        if(lists.size() != 0) {
            code = 1;
            result = "succeed";
        }
        return CommonUtil.constructResponse(code, result, lists);
    }

    @Override
    public List<HashMap<String, Objects>> getUserEducation(int userId, int state) {
    	List<HashMap<String, Objects>> list=null;
    	try{
    		list=educationDao.getUserEducation(userId,state);
    	}catch(Exception e){
    		throw e;
    	}
    	return list;
    }

    @Override
    public JSONObject getAllAcademy(String aca)
    {
        List<HashMap<String,Objects>> lists= null;
        try {
            lists = educationDao.getAllAcademy("%"+aca+"%");
        } catch (Exception e) {
           throw e;
        }
        int code=0;
        String result="没有匹配项";
        if(lists.size()!=0) {
            code=1;
            result="success";
        }
        return CommonUtil.constructHtmlResponse(code,result,lists);
    }

    @Override
    public JSONObject getAllMajor(String m)
    {
        String major="%"+m+"%";

        List lists=educationDao.getAllMajor(major);
        int code=0;
        String result="没有匹配项";
        if(lists.size()!=0) {
            code=1;
            result="success";
        }
        return CommonUtil.constructHtmlResponse(code,result,lists);
    }

    @Override
    public int insertStudInform(StudentCheck sc) {
        if(sc!=null){
            sc.getStudentCheckUer();
            educationDao.insStudInform(sc);
            educationDao.updateUserState(sc.getStudentCheckUer(),0);
            return 1;
        }
        return 0;
    }

    @Override
    public void updateEducation(JSONArray changeArray,int userId) {
    	try{
    		for (Object o : changeArray){
    			JSONObject j = JSONObject.parseObject(o.toString());
    			j.put("user_id",userId);
    			educationDao.updateEducation(j);
    		}
    	} catch (Exception e) {
    		throw e;
    	}
    }

    @Override
    public void addEducation(JSONArray addArray,int userId) {
    	try{	
    		for (Object o : addArray){
    			JSONObject j = JSONObject.parseObject(o.toString());
    			j.put("user_id",userId);
    			educationDao.addEducation(j);
    		}
    	} catch (Exception e) {
    		throw e;
    	}
    }

    @Override
    public void delEducation(String del, int userId) {
        String[] ids = del.split(",");
        try{	
        	educationDao.delEducation(ids,userId);
        } catch (Exception e) {
		throw e;
        }
    }

    @Override
    public List<Map> studentPicture(int userId) {
        return educationDao.studentPicture(userId);
    }
}
