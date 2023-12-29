package com.example.scrappingtest.service.impl;

import com.example.scrappingtest.entity.JobFunction;
import com.example.scrappingtest.entity.JobItem;
import com.example.scrappingtest.repository.JobItemRepository;
import com.example.scrappingtest.scrapper.JobItemScrapper;
import com.example.scrappingtest.service.JobItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobItemServiceImpl implements JobItemService {
	private final JobItemRepository jobItemRepository;
	private final JobFunctionServiceImpl jobFunctionService;
	private final ObjectMapper objectMapper;
	private final JobItemScrapper jobItemScrapper;

	@Value("${scrapping-url}")
	private String url;

	@Override
	public List<JobItem> findAll() {
		return jobItemRepository.findAll();
	}

	@Override
	public List<JobItem> findByJobFunction(String name) {
		Optional<JobFunction> byName = jobFunctionService.findByName(name);

		if (byName.isEmpty()) {
			throw new RuntimeException("JobFunction is not found");
		}

		JobFunction jobFunction = byName.get();
		return jobItemRepository.findByJobFunctionId(jobFunction.getId());
	}

	@Scheduled(initialDelay = 0, fixedRate = 24 * 60 * 1000)
	public void updateJobsByAllJobFunctions() {
		List<JobFunction> jobFunctions = jobFunctionService.findAll();

		log.info("<< Update is started >>");
		long startTime = System.currentTimeMillis();

		ExecutorService executorService = Executors.newFixedThreadPool(jobFunctions.size());

		List<CompletableFuture<Void>> futures = new ArrayList<>();

		for (JobFunction jobFunction : jobFunctions) {
			CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
				try {
					updateJobsForJobFunction(jobFunction);
				} catch (IOException e) {
					log.error("Error updating jobs for job function: " + jobFunction.getName(), e);
				}
			}, executorService);

			futures.add(future);
		}

		CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
		allOf.join();

		executorService.shutdown();

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		log.info("<< Update complete with time: " + duration + " ms >>");
	}

	private void updateJobsForJobFunction(JobFunction jobFunction) throws IOException {
		String jsonJobFunction = buildJson(List.of(jobFunction.getName()));
		String encodedJobFunction = encode(jsonJobFunction);
		String urlWithFilter = url + "?filter=" + encodedJobFunction;

		List<JobItem> updatedJobItems = jobItemScrapper.getData(urlWithFilter, jobFunction);
		validateJobItemsBeforeSave(updatedJobItems);
		Set<JobItem> jobItems = new HashSet<>(jobItemRepository.findByJobFunctionId(jobFunction.getId()));
		jobItems.addAll(updatedJobItems);
		jobItemRepository.saveAll(jobItems);
	}

	private void validateJobItemsBeforeSave(List<JobItem> jobItems) {
		for (JobItem jobItem : jobItems) {
			validateJobItem(jobItem);
		}
	}

	private void validateJobItem(JobItem jobItem) {
		if (jobItem.getJobPageUrl() == null || jobItem.getJobPageUrl().isEmpty()) {
			jobItem.setJobPageUrl("NOT_FOUND");
		}
		if (jobItem.getPositionName() == null || jobItem.getPositionName().isEmpty()) {
			jobItem.setPositionName("NOT_FOUND");
		}
		if (jobItem.getOrganizationUrl() == null || jobItem.getOrganizationUrl().isEmpty()) {
			jobItem.setOrganizationUrl("NOT_FOUND");
		}
		if (jobItem.getLogo() == null || jobItem.getLogo().isEmpty()) {
			jobItem.setLogo("NOT_FOUND");
		}
		if (jobItem.getTitle() == null || jobItem.getTitle().isEmpty()) {
			jobItem.setTitle("NOT_FOUND");
		}
		if (jobItem.getLaborFunction() == null || jobItem.getLaborFunction().isEmpty()) {
			jobItem.setLaborFunction("NOT_FOUND");
		}
		if (jobItem.getAddress() == null || jobItem.getAddress().isEmpty()) {
			jobItem.setAddress("NOT_FOUND");
		}
		if (jobItem.getPostedDate() == null || jobItem.getPostedDate().isEmpty()) {
			jobItem.setPostedDate("NOT_FOUND");
		}
		if (jobItem.getDescription() == null || jobItem.getDescription().isEmpty()) {
			jobItem.setDescription("NOT_FOUND");
		}
		if (jobItem.getTags() == null || jobItem.getTags().isEmpty()) {
			jobItem.setTags("NOT_FOUND");
		}
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
