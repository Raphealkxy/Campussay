package com.campussay.dao;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;



import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface StarDao {

	/**
	 * Author:wangzhihong 
	 */
	//返回主题集合
	public List<Map<String,Object>> Star_list(@Param("campus_name")String campus_name);
	
	//返回学校
	public String campus_name(int campus_id);
			
	//返回学院
			public String Star_academe(int star_user_id);
}
