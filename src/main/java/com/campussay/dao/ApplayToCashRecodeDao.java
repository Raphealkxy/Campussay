package com.campussay.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.campussay.model.ApplayToCashRecode;
@Repository
public interface ApplayToCashRecodeDao {
	
	public void addRecode(ApplayToCashRecode applayToCashRecode);
	
    public List<HashMap> selectRecode(@Param(value = "userID")String userID, @Param(value = "start")int start, @Param(value = "pageSize")int pageSize);
	  
	public int selectRecodeCount(@Param(value = "userID")String userID);
	
	public List<HashMap> getOneApplyToCashRecord(@Param(value = "userID")String userID);
	
}
