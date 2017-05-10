package com.campussay.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.campussay.dao.ApplayToCashRecodeDao;
import com.campussay.service.ApplayToCashRecodeService;
@Service
public class ApplayToCashRecodeServiceImpl implements ApplayToCashRecodeService {
	
	@Resource
	private ApplayToCashRecodeDao applayToCashRecodeDao;
	@Override
	public HashMap selectUserApplayToCashRecode(String userID, int start, int pageSize) {

		try {
			HashMap restult = new HashMap();
			List<HashMap> resultList = applayToCashRecodeDao.selectRecode(userID, start, pageSize);
			if  (resultList == null||resultList.size()<=0){
				return null;
			}else{
		        int dataCount = applayToCashRecodeDao.selectRecodeCount(userID);
				restult.put("resultList", resultList);
				restult.put("dataCount", dataCount);
				restult.put("pageSize", pageSize);
				return restult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		
	}
}
