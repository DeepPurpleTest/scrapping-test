package com.example.scrappingtest.controller;

import com.example.scrappingtest.entity.JobFunction;
import com.example.scrappingtest.service.impl.JobFunctionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/job-function")
@RequiredArgsConstructor
public class JobFunctionController {
	private final JobFunctionServiceImpl jobFunctionService;

	@GetMapping("/all")
	public List<JobFunction> findAll() {
		return jobFunctionService.findAll();
	}
}
