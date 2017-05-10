/**
 * @author effine
 * @Date 2016年1月10日  下午9:57:56
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.dao.PrizeDao;
import com.campussay.model.Prize;
import com.campussay.service.PrizeService;
import com.campussay.util.CommonUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackFor=Exception.class)
public class PrizeServiceImpl implements PrizeService {

    @Resource
    private PrizeDao prizeDao;

    /**
     * 查询 个人获奖情况
     * @param userId
     * @return
     */
    @Override
    public JSONObject getUserSettingPrize(int userId,int state) {
    	List list=null;
    	try{
    		list=prizeDao.getUserSettingPrize(userId,state);
    	}catch(Exception e){
    		throw e;
    	}
        return CommonUtil.constructResponse(1, null, list);
    }

    @Override
    public List<HashMap<String, Objects>> getUserPrize(int userId, int state) {
    	List<HashMap<String, Objects>> list=null;
    	try{
    		list=prizeDao.getUserPrize(userId,state);
    	}catch(Exception e){
    		throw e;
    	}
    	return list;
    }

    /**
     * 添加 个人获奖情况
     * @param prize
     * @return
     */
    @Override
    public int userSettingPrize(Prize prize) {
        if(prize.getPrizeDescript()==null&prize.getPrizeTime()==null&prize.getPrizeTitle()==null)
            return 0;
        int n=0;
        try{
    		n=prizeDao.userSettingPrize(prize);
    	}catch(Exception e){
    		throw e;
    	}
        return n;
    }

    @Override
    public void updatePrize(JSONArray changeArray,int userId) {
    	try{
    	for (Object o : changeArray){
           JSONObject j = JSONObject.parseObject(o.toString());
           j.put("user_id",userId);
           	prizeDao.updatePrize(j);
    		}
    	}catch(Exception e){
    		throw e;
    	}
    }

    @Override
    public void addPrize(JSONArray addArray,int userId) {
    	try{
    	 for (Object o : addArray){
             JSONObject j = JSONObject.parseObject(o.toString());
             j.put("user_id",userId);
             prizeDao.addPrize(j);
         }
    	}catch(Exception e){
    		throw e;
    	}
    }

    @Override
    public void delPrize(String del, int userId) {
        String[] ids = del.split(",");
        prizeDao.delPrize(ids, userId);
    }
}
