package com.campussay.dao;

import com.campussay.model.ApplayRefundOrder;
import java.util.*;
public interface ApplayRefundOrderDao {
	
	public void addRecode(ApplayRefundOrder applayRefundOrder);
	
    public List<HashMap> getOneApplyRefundRecord(int userId);
}
