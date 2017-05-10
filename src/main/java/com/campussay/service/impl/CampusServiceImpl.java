/**
 * @author effine
 * @Date 2016年1月10日  下午9:53:28
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import com.campussay.dao.CampusDao;
import com.campussay.service.CampusService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor=Exception.class)
public class CampusServiceImpl implements CampusService {

    @Resource
    private CampusDao campusDao;

    /**
     * 获得 所有 学校名称 学校id
     * @return
     * @throws Exception 
     */
    @Override
    public List<Map<String, Object>> getAllCampusName() throws Exception {
    	 List<Map<String, Object>> list=new ArrayList();
    	 try{
    	 list=campusDao.getAllCampusName();
    	 }catch(Exception e){
 			throw new Exception("数据库错误");
 		}	
         return list;
    }
    
    /**
     * @author wzh
     * 根据ID返回校名
     * @return
     * @throws Exception 
     */
    @Override
    public String getCampusNameByid(int campus_id) throws Exception {
    	String string;
    	try{
        string=campusDao.getCampusNameByid(campus_id);
    	 }catch(Exception e){
  			throw new Exception("数据库错误");
  		}	
        return string;
    }

    @Override
    public Integer getStudentCheckCampus(int userId) {
        return campusDao.getStudentCheckCampus(userId);
    }

}
