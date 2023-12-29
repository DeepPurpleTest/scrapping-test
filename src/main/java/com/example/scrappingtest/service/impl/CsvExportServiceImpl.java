package com.example.scrappingtest.service.impl;

import com.example.scrappingtest.entity.JobItem;
import com.example.scrappingtest.service.CsvExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvExportServiceImpl implements CsvExportService {

	@Override
	public String exportToCsv(List<JobItem> jobItems) {
		try (StringWriter writer = new StringWriter()) {
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader("id", "jobPageUrl", "positionName", "organizationUrl", "logo", "title",
							"laborFunction", "address", "postedDate", "description", "tags", "jobFunction"));

			for (JobItem jobItem : jobItems) {
				csvPrinter.printRecord(
						jobItem.getId(),
						jobItem.getJobPageUrl(),
						jobItem.getPositionName(),
						jobItem.getOrganizationUrl(),
						jobItem.getLogo(),
						jobItem.getTitle(),
						jobItem.getLaborFunction(),
						jobItem.getAddress(),
						jobItem.getPostedDate(),
						jobItem.getDescription(),
						jobItem.getTags(),
						jobItem.getJobFunction().getName()
				);
			}

			csvPrinter.flush();
			return writer.toString();
		} catch (IOException e) {
			log.error("Exception while exportCsv", e);
			return null;
		}
	}
}
