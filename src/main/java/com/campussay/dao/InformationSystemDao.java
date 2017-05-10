package com.campussay.dao;

import org.springframework.stereotype.Repository;

import com.campussay.model.Information;

@Repository
public interface InformationSystemDao {
	public void insertInformationSystem(Information information);
}
