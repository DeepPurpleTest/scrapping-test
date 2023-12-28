package com.example.scrappingtest.scrapper;

import com.example.scrappingtest.entity.JobFunction;
import com.example.scrappingtest.entity.JobItem;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JobItemScrapper {

	public List<JobItem> getData(String url, JobFunction jobFunction) throws IOException {
		Document document = Jsoup.connect(url).get();

		Elements jobItemElements = document.select("div[data-testid=job-list-item]");

		List<JobItem> jobItems = new ArrayList<>();
		for (Element jobItemElement : jobItemElements) {
			JobItem jobItem = new JobItem();
			jobItem.setJobFunction(jobFunction);

//			log.info("<< Fill jobPageUrls >>");
			Element linkElement = jobItemElement.select("a[data-testid=read-more]").first();
			String jobPageUrl = linkElement.absUrl("href");
			jobItem.setJobPageUrl(jobPageUrl);

//			log.info("<< Fill job positionName >>");
			Element positionNameElement = jobItemElement.select("div[itemprop=title]").first();
			String positionName = positionNameElement.text();
			jobItem.setPositionName(positionName);

//			log.info("<< Try to fill title >>");
			Element titleElement = jobItemElement.select("a[data-testid=link]").first();
			String title = titleElement.text();
			jobItem.setTitle(title);

//			log.info("<< Try to fill logo >>");
			Element logoElement = jobItemElement.select("img[data-testid]").first();
			String logo = logoElement.absUrl("src");
			jobItem.setLogo(logo);

//			log.info("<< Fill tags >>");
			Elements tags = jobItemElement.select("div[data-testid=tag]");
			StringBuilder jobItemTags = new StringBuilder();

			for (Element tag : tags) {
				jobItemTags.append(tag.text()).append(",");
			}

			if (jobItemTags.length() > 0) {
				jobItemTags.deleteCharAt(jobItemTags.length() - 1);
			}
			jobItem.setTags(jobItemTags.toString());


			jobItems.add(jobItem);
		}

		for (JobItem jobItem : jobItems) {
//			log.info("<< Try connect to jobPageUrl >>");
			try {
				document = Jsoup.connect(jobItem.getJobPageUrl()).get();

//				log.info("<< Try to fill organizationUrl >>");
				Element organizationUrlElement = document.select("a[data-testid=button]").first();
				if (organizationUrlElement != null) {
					String organizationUrl = organizationUrlElement.absUrl("href");
					jobItem.setOrganizationUrl(organizationUrl);
				} else {
					jobItem.setOrganizationUrl("NOT_FOUND");
				}

//				log.info("<< Try fill logo >>");
//				Element logoElement = document.select("img[data-testid=image]").first();
//				if (logoElement != null) {
//					String logoUrl = logoElement.absUrl("src");
//					jobItem.setLogo(logoUrl);
//				}

//				log.info("<< Try to fill labor function with address and posted date >>");
				Element element = document.select("div[class=sc-beqWaB sc-gueYoa dmdAKU MYFxR]").first();
				if (element != null) {
					Elements elements = element.children();
					jobItem.setLaborFunction(elements.get(0).text());
					jobItem.setAddress(elements.get(1).text());
					jobItem.setPostedDate(elements.get(2).text());
				} else {
					jobItem.setLaborFunction("NOT_FOUND");
					jobItem.setAddress("NOT_FOUND");
					jobItem.setPostedDate("NOT_FOUND");
				}

//				log.info("<< Try to fill description >>");
				Element descriptionElement = document.select("div[class=sc-beqWaB fmCCHr]").first();
				if (descriptionElement != null) {
					jobItem.setDescription(descriptionElement.text());
				} else {
					jobItem.setDescription("NOT_FOUND");
				}

			} catch (HttpStatusException | UnsupportedMimeTypeException e) {
				log.warn("<< incorrect job details url or private for job_function: "+ jobFunction.getName() +
						" and job item " + jobItem.getTitle() +">>");
				fillEmptyFields(jobItem);
			}

		}

		log.info("<< Complete job function: " + jobFunction.getName() + " >>");
		return jobItems;
	}

	private void fillEmptyFields(JobItem jobItem) {
		jobItem.setOrganizationUrl("NOT_FOUND");
		jobItem.setLaborFunction("NOT_FOUND");
		jobItem.setAddress("NOT_FOUND");
		jobItem.setPostedDate("NOT_FOUND");
		jobItem.setDescription("NOT_FOUND");
	}
}
