package com.campussay.service.impl;

import com.campussay.dao.FollowDao;
import com.campussay.service.FollowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangchun on 2016/1/23.
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class FollowServiceImpl implements FollowService {
    @Autowired
    private FollowDao followDao;

    public int addAConcernField(int userId,int taking_type_id) throws Exception{
        int n=0;
    	try{
    		n=followDao.addAConcernField(userId,taking_type_id);
    	}catch(Exception e){
    		throw new Exception("数据库操作异常");
    	}
    	return n;
    }

    public List<HashMap> getFollowsByUserId(int userId){
    	List<HashMap> result=null;
    	try{
    		result=followDao.getFollowsByUserId(userId);
    	}catch(Exception e){
    		throw e;
    	}
        return result;
    }
    
    public HashMap getUnFollowsByUserId(int userId,int page,int size){
    	List<HashMap> list=null;
    	HashMap result=new HashMap();
    	try{
         list=followDao.getUnFollowsByUserId(userId,page,size);
    	}catch(Exception e){
    		throw e;
    	}
        if(list!=null&&list.size()>0){
            HashMap first=list.get(0);
            result.put("rows",first.get("totalLine"));
        }
        if(list!=null&&list.size()>0){
        	for(HashMap mp:list){
        		mp.remove("totalLine");
        	}
        }
        result.put("list",list);
        return result;
    }
    
    
    public HashMap getSkillByUserId(int userId,int page,int size){
        HashMap result=new HashMap();
        List<HashMap> list=null;
        try{
        	list=followDao.getSkillByUserId(userId,page,size);
        }catch(Exception e){
    		throw e;
    	}
        if(list!=null&&list.size()>0){
            HashMap first=list.get(0);
            result.put("rows",first.get("totalLine"));
        }
        if(list!=null&&list.size()>0){
        	for(Map<String,Object> obj:list) {
        		obj.remove("totalLine");
        		if (obj.get("intro") != null) {
        			byte[] b = (byte[]) obj.get("intro");
        			obj.put("intro", new String(b,Charset.forName("utf-8")));
        		} else {
        			obj.put("intro", "nothing");
        		}
        	}
        }
        result.put("list",list);
        return result;
    }
    //用户擅长领域topic数量
    public HashMap getSkillTopicNum(int userId){
    	HashMap hmp=null;
    	try{
    		hmp=followDao.getSkillTopicNum(userId);
        }catch(Exception e){
    		throw e;
    	}
        return hmp;
    }
    //用户取消关注领域
    public int cancelConcernField(int userId,int taking_type_id){
        int n=0;
    	try{
    		n=followDao.cancelConcernField(userId,taking_type_id);
        }catch(Exception e){
    		throw e;
    	}
    	return n;
    }

    //判断用户是否关注
    public int isConcernField(int userId,int taking_type_id){
    	int n=0;
    	try{
    		n=followDao.isConcernField(userId,taking_type_id);
        }catch(Exception e){
    		throw e;
    	}
    	return n;
    }

}
