package com.campussay.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campussay.dao.InformationDao;
import com.campussay.dao.InformationSystemDao;
import com.campussay.model.Information;
import com.campussay.service.InformationService;
@Service
@Transactional
public class InformationServiceImpl implements InformationService{

	@Resource
	private InformationDao informationDao;
	@Resource
	private InformationSystemDao informationSystemDao;
	
	@Override
	public void insertInfomation(Information information) {
		
		informationDao.insertInformation(information);
		
	}

	@Override
	public void insertSystemInfomation(Information information) {
		// TODO Auto-generated method stub
		informationSystemDao.insertInformationSystem(information);
	}

	
	public HashMap selectUserInformation(String userID, Integer informationType,Integer informationStatus,  int start, int pageSize) {
		
		try {
			HashMap restult = new HashMap();
			informationDao.insertSystemInfomationToInformation(userID);
			List<HashMap> resultList = informationDao.selectInformation(userID, informationType,informationStatus, start, pageSize);
			
			if  (resultList == null||resultList.size()<=0){
				return null;
			}else{
				int dataCount =	informationDao.selectInformationCount(userID, informationType,informationStatus);        
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
	
	
	
	

	@Override
	public void updateNewSystemInformation(String userId) {
		// TODO Auto-generated method stub
		informationDao.insertSystemInfomationToInformation(userId);
		
	}

	@Override
	public void batchSetInformationRead(List<Information> list) {
		try {
			informationDao.batchSetInformationRead(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
		
		
	}
	@Override
	public void batchSetInformationRead(Information item) {
		try {
			informationDao.setAllUserInformationRead(item);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
		
		
	}
	@Override
	public void batchDeleteInformation(List<Information> list) {
		try {
			informationDao.batchDeleteInformation(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
	}
	
	
}
