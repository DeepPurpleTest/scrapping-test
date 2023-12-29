package com.example.scrappingtest.controller;

import com.example.scrappingtest.entity.JobItem;
import com.example.scrappingtest.service.impl.CsvExportServiceImpl;
import com.example.scrappingtest.service.impl.JobItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/csv/export")
@RequiredArgsConstructor
public class CsvController {
	private final CsvExportServiceImpl csvExportService;
	private final JobItemServiceImpl jobItemService;

	@GetMapping("/all")
	public ResponseEntity<byte[]> exportJobItemsToCsv() {
		List<JobItem> jobItems = jobItemService.findAll();

		String csvData = csvExportService.exportToCsv(jobItems);

		return buildResponse(csvData);
	}

	private ResponseEntity<byte[]> buildResponse(String data) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=job_items.csv");

		return new ResponseEntity<>(data.getBytes(), headers, HttpStatus.OK);
	}
}
