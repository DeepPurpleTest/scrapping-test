package com.example.scrappingtest.controller;

import com.example.scrappingtest.entity.JobItem;
import com.example.scrappingtest.service.JobItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/job-item")
@RequiredArgsConstructor
public class JobItemController {
	private final JobItemService jobItemService;

	@GetMapping("/test")
	public ResponseEntity<String> getJobs() {
//		List<JobItem> jobItemList = jobItemScrapper.getData("https://jobs.techstars.com/jobs");
//		jobItemService.saveAll(jobItemList);

		return ResponseEntity.ok("may work");
	}

	@GetMapping
	public List<JobItem> getJobItemsByJobFunction(@Param("job-function") String jobFunction) {
		return jobItemService.findByJobFunction(jobFunction);
	}
}
