package com.campussay.service;



import com.campussay.model.Area;

public interface AreaService {
	Area getAreaById(int id);
	public int insert(int area_id,int area_city, String area_name);

}
