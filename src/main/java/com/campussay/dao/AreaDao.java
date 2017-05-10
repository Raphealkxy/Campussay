package com.campussay.dao;

import com.campussay.model.Area;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaDao {
	Area getAreaById(int id);
	public int insert(@Param("area_id") int area_id,@Param("area_city") int area_city,@Param("area_name") String area_name);
}
