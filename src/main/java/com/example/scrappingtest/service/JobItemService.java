package com.example.scrappingtest.service;

import com.example.scrappingtest.entity.JobItem;

import java.util.List;

public interface JobItemService {
	List<JobItem> findAll();

	List<JobItem> findByJobFunction(String name);
}
