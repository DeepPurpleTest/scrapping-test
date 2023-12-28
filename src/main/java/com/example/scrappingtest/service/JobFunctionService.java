package com.example.scrappingtest.service;

import com.example.scrappingtest.entity.JobFunction;
import com.example.scrappingtest.repository.JobFunctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobFunctionService {
	private final JobFunctionRepository jobFunctionRepository;

	public Optional<JobFunction> findByName(String name) {
		return jobFunctionRepository.findByName(name);
	}

	public List<JobFunction> findAll() {
		return jobFunctionRepository.findAll();
	}
}
