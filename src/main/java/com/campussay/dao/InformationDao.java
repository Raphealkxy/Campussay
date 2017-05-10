package com.campussay.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.campussay.model.Information;

@Repository
public interface InformationDao {
	public void insertInformation(Information information);	
	public void insertSystemInfomationToInformation(String userId);
	public List<HashMap> selectInformation(@Param(value = "informationUser")String userID, @Param(value = "informationType")Integer informationType, @Param(value="informationStatus")Integer informationStatus,@Param(value = "start")int start, @Param(value = "pageSize")int pageSize);
	public int selectInformationCount(@Param(value = "informationUser")String userID, @Param(value = "informationType")Integer informationType,@Param(value="informationStatus")Integer informationStatus);
	
	public  void batchSetInformationRead(List list);
	public  void setAllUserInformationRead(Information item );
	public  void batchDeleteInformation(List list);
	
	
	
}
