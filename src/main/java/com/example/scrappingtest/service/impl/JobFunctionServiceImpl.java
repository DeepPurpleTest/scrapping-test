package com.example.scrappingtest.service.impl;

import com.example.scrappingtest.entity.JobFunction;
import com.example.scrappingtest.repository.JobFunctionRepository;
import com.example.scrappingtest.service.JobFunctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobFunctionServiceImpl implements JobFunctionService {
	private final JobFunctionRepository jobFunctionRepository;

	@Override
	public Optional<JobFunction> findByName(String name) {
		return jobFunctionRepository.findByName(name);
	}

	@Override
	public List<JobFunction> findAll() {
		return jobFunctionRepository.findAll();
	}
}
