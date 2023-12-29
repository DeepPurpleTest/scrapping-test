package com.example.scrappingtest.service;

import com.example.scrappingtest.entity.JobFunction;

import java.util.List;
import java.util.Optional;

public interface JobFunctionService {
	Optional<JobFunction> findByName(String name);

	List<JobFunction> findAll();
}
