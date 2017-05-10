/**
 * @author effine
 * @Date 2016年1月10日  下午9:11:28
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.campussay.dao.TalkingTypeDao;
import com.campussay.model.Order;
import com.campussay.service.TalkingTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor=Exception.class)
public class TalkingTypeServiceImpl implements TalkingTypeService {

	@Autowired
	private TalkingTypeDao talkingtypeDao;

	/*
	 * 说说分类
	 * Author：汪治宏
	 */
	public JSONObject TakingTypedetail() throws Exception {
		JSONObject joall = new JSONObject();
		
		List<Map<String, Object>> firstlist =new ArrayList();
		try{
		firstlist=talkingtypeDao.talkingType_floor();
		}catch(Exception e){
			throw new Exception("数据库错误");
		}
		JSONObject jo = new JSONObject();
		for (int i = 0; i < firstlist.size(); i++) {
			jo.put("first", firstlist.get(i));
			if ((int) firstlist.get(i).get("talking_type_is_leaf") == 1) {
				JSONObject jos = new JSONObject();
				List<Map<String, Object>> seclist = talkingtypeDao.talkingType_floor_list((int) firstlist.get(i).get("talking_type_id"));
				for (int j = 0; j < seclist.size(); j++) {
					JSONObject jo2 = new JSONObject();
					jo2.put("second", seclist.get(i));
					if ((int) seclist.get(i).get("talking_type_is_leaf") == 1) {
						List<Map<String, Object>> trilist = talkingtypeDao.talkingType_floor_list((int) seclist.get(i).get("talking_type_id"));
						jo2.put("trithdate", trilist);
					}
					jos.put("sec", jo2);
				}
				jo.put("secdate", jos);
			}
			joall.put("firsth", jo);
		}
		return joall;
	}

	@Override
	public List<HashMap<String, Object>> getUserSkill(int userId) {
		// TODO unimplements method stub
		return talkingtypeDao.getUserSkill(userId);
	}
	  /*
    * 话题社部分
    *
    * */

	/*
      * 领域动态（对应界面：没有感兴趣领域）
      *
      * */
	public List<Map<String, Object>> selectUnconTalkType(Integer id, Integer page) {
		if (page == null && page <= -1) {
			page = 1;
		}
		return talkingtypeDao.selectUnconTalkType(id, (page - 1) * 10, 10);
	}

	/*
     * 下面部分功能是一级领域的操作过程
     *
     * */
	@Override
	public List<Map<String, Object>> selectAllOneTimesTalkType() {

		return talkingtypeDao.selectAllOneTimeType();

	}

	/*
	 * 查询二级领域
	 * @param 输入，user_id
	 * @param 输出，如fieldSecFind等于1的时候用户关注，用户等于0的时候用户没关注
	 * */
	@Override
	public Map<String, Object> selectAllSecondTalkType(int user_id,int page) {
		List<Map<String, Object>> listAllByMyIdField=null;
		//限制输出数目
		int p = 10;
		int q=(page-1)*p;
		//查询出所有二级领域内容 Map<String, String> m1 = new HashMap<String, String>();
		List<Map<String, Object>> listAllTwoField = talkingtypeDao.selectSecTimesTalkType();
		HashMap result = new HashMap();
		if (listAllTwoField.size() > 0) {
			HashMap first = (HashMap) listAllTwoField.get(0);
			result.put("rows", first.get("totalLine"));
		}

		//判断是否关注，默认为未关注
		for (Map<String, Object> maps : listAllTwoField) {
			if (maps.get("talking_type_description") == null) {
				maps.put("talking_type_description", " ");
			}
			maps.put("follow", 0);
			maps.remove("totalLine");
		}
		//判断是否有用户信息，如果有
		if (user_id >= 0) {
			//Map<String, String> m2 = new HashMap<String, String>();
			listAllByMyIdField = talkingtypeDao.selectAllSecondTimeType(user_id);
		} else {
			List<Map<String, Object>> listAllTwoFields=null;
			if(p+q<=listAllTwoField.size()) {
				listAllTwoFields= listAllTwoField.subList(q, q + p);
			}else {
				listAllTwoFields=listAllTwoField.subList(q,listAllTwoField.size());
			}
			result.put("list", listAllTwoFields);
			return result;
		}
		//判断是否关注
		for (Map<String, Object> maps : listAllTwoField) {
			for (Map<String, Object> map : listAllByMyIdField) {
				if (maps.get("talking_type_description") == null) {
					maps.put("talking_type_description", " ");
				}
				String m = map.get("taking_type_id").toString();
				String mp = maps.get("talking_type_id").toString();
				if (m.equals(mp)) {
					maps.put("follow", 1);
				}
			}
		}
		List<Map<String,Object>> listAllTwoFields=null;
		if(p+q<=listAllTwoField.size()){
		     listAllTwoFields=listAllTwoField.subList(q,p+q);

		}else {
			 listAllTwoFields=listAllTwoField.subList(q,listAllTwoField.size());
		}
		result.put("list", listAllTwoFields);
		return result;
	}


	/*
	* 根据领域一，查询领域二内容
	*
	* */
	@Override
	public Map<String, Object> selectSecondTalkType(int user_id, int page, int parent) {
		List<Map<String, Object>> listAllByMyIdField;

		//限制输出数目
		int p = 10;

		//根据一级领域查询出二级领域内容
		List<Map<String, Object>> listAllTwoField = talkingtypeDao.selectTimesTalkType(parent, (page - 1) * p, p);
		HashMap result = new HashMap();
		if (listAllTwoField.size() > 0) {
			HashMap first = (HashMap) listAllTwoField.get(0);
			result.put("rows", first.get("totalLine"));
		}

		//判断是否关注，默认为未关注
		for (Map<String, Object> maps : listAllTwoField) {

			if (maps.get("talking_type_description") == null) {
				maps.put("talking_type_description", " ");
			}
			maps.put("follow", 0);
			maps.remove("totalLine");
		}


		//判断是否有用户信息，如果有
		if (user_id >= 0) {
			listAllByMyIdField = talkingtypeDao.selectAllSecondTimeType(user_id);
		} else {
			result.put("list", listAllTwoField);
			return result;
		}


		for (Map<String, Object> maps : listAllTwoField) {
			String mg = maps.get("talking_type_id").toString();
			for (Map<String, Object> map : listAllByMyIdField) {
				if (maps.get("talking_type_description") == null) {
					maps.put("talking_type_description", " ");
				}

				String mgs = map.get("taking_type_id").toString();
				if (mg.toString().equals(mgs)) {
					maps.put("follow", 1);
				}
			}
		}

        result.put("list",listAllTwoField);
		return result;
	}


	/*
	 yangchun 得到一级领域下拉菜单
	 */
	public List<HashMap> getFirstTalkingType(){
		return talkingtypeDao.getFirstTalkingType();
	}

	/*
	 yangchun 通过一级领域id 得到二级领域下拉菜单
	 */
	public List<HashMap> getSecondTalkingType(int talkingTypeId){
		return talkingtypeDao.getSecondTalkingType(talkingTypeId);
	}


	public Map<String,Object> getTalkingTypeDetailById(int talkingTypeId){
		return talkingtypeDao.getTalkingTypeDetailById(talkingTypeId);
	}

	@Override
	public JSONObject Order_list() {
		// TODO Auto-generated method stub
		return null;
	}



}
