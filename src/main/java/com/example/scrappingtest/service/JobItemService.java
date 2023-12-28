package com.example.scrappingtest.service;

import com.example.scrappingtest.entity.JobFunction;
import com.example.scrappingtest.entity.JobItem;
import com.example.scrappingtest.repository.JobItemRepository;
import com.example.scrappingtest.scrapper.JobItemScrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobItemService {
	private final JobItemRepository jobItemRepository;
	private final JobFunctionService jobFunctionService;
	private final ObjectMapper objectMapper;
	private final JobItemScrapper jobItemScrapper;

	@Value("${scrapping-url}")
	private String url;

	public List<JobItem> findByJobFunction(String name) {
		String decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8);
		Optional<JobFunction> byName = jobFunctionService.findByName(decodedName);

		if(byName.isEmpty()) {
			throw new RuntimeException("JobFunction is not found");
		}

		JobFunction jobFunction = byName.get();
		return jobItemRepository.findByJobFunctionId(jobFunction.getId());
	}

	@Scheduled(initialDelay = 0, fixedRate = 24 * 60 * 60 * 1000)
	public void updateJobsByAllJobFunctions() throws IOException {
		List<JobFunction> jobFunctions = jobFunctionService.findAll();

		log.info("<< Update is started >>");
		long startTime = System.currentTimeMillis();

		for (JobFunction jobFunction : jobFunctions) {
			updateJobsForJobFunction(jobFunction);
		}

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("<< Update complete with time: " + duration + " ms >>");
	}

	private void updateJobsForJobFunction(JobFunction jobFunction) throws IOException {
		String jsonJobFunction = buildJson(List.of(jobFunction.getName()));
		String encodedJobFunction = encode(jsonJobFunction);
		String urlWithFilter = url + "?filter=" + encodedJobFunction;

		List<JobItem> updatedJobItems = jobItemScrapper.getData(urlWithFilter, jobFunction);
		jobItemRepository.saveAll(updatedJobItems);
	}

	private String encode(String s) {
		return Base64.encodeBase64String(s.getBytes());
	}

	private String buildJson(List<String> params) throws JsonProcessingException {
		Map<String, List<String>> data = new HashMap<>();
		data.put("job_functions", params);

		return objectMapper.writeValueAsString(data);
	}
}
