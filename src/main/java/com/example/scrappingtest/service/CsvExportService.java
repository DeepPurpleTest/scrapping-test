package com.example.scrappingtest.service;

import com.example.scrappingtest.entity.JobItem;

import java.util.List;

public interface CsvExportService {
	String exportToCsv(List<JobItem> jobItems);
}
