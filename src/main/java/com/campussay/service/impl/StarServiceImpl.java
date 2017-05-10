/**
 * @author effine
 * @Date 2016年1月10日  下午8:35:22
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import com.campussay.dao.StarDao;
import com.campussay.service.StarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor=Exception.class)
public class StarServiceImpl implements StarService {
    @Autowired
	private StarDao stardao;

	public List<Map<String,Object>> Star_list(String campus_name) throws Exception{
		List<Map<String,Object>> star_list=new ArrayList();
		try{
		star_list=stardao.Star_list(campus_name);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}
		if(star_list!=null)
		{
			for(int i=0;i<star_list.size();i++){
				String academe=stardao.Star_academe((int)star_list.get(i).get("user_id"));
				star_list.get(i).put("academe", academe);
			}
		}
		return star_list;
	}
}
