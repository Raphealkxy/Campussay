package com.campussay.service;

import java.util.HashMap;
import java.util.List;

import com.campussay.model.Information;

public interface InformationService {
	
	public void insertInfomation(Information information);
	
	public void insertSystemInfomation(Information information);
	
	void updateNewSystemInformation(String userId);
	public HashMap selectUserInformation(String userID, Integer informationType,Integer informationStatus, int start, int pageSize);
	void batchSetInformationRead(List<Information> list);
	void batchSetInformationRead(Information item);
	void batchDeleteInformation(List<Information> list);
}
