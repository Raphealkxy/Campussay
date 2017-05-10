/**
 * @author effine
 * @Date 2016年1月10日  下午9:45:57
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;


import com.campussay.dao.AreaDao;
import com.campussay.model.Area;
import com.campussay.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("AreaService")
public class AreaServiceImpl implements AreaService {
	@Autowired
	private AreaDao areaDao;

	@Override
	public Area getAreaById(int id) {
		return areaDao.getAreaById(id);
	}
	
	
	@Override
	public int insert(int area_id, int area_city, String area_name) {
		return 0;
	}

}
