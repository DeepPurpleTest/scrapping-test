package com.example.scrappingtest.controller;

import com.example.scrappingtest.entity.JobItem;
import com.example.scrappingtest.service.JobItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/job-item")
@RequiredArgsConstructor
public class JobItemController {
	private final JobItemService jobItemService;

	@GetMapping
	public List<JobItem> getJobItemsByJobFunction(@RequestParam(name = "job-function") String jobFunction) {
		return jobItemService.findByJobFunction(jobFunction);
	}
}
