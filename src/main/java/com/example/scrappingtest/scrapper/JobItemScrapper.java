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
import java.net.SocketTimeoutException;
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
			String jobPageUrl = extractAbsUrlFromElement(jobItemElement, "a[data-testid=read-more]", "href");
			jobItem.setJobPageUrl(jobPageUrl);

//			log.info("<< Fill job positionName >>");
			String positionName = extractTextFromElement(jobItemElement, "div[itemprop=title]");
			jobItem.setPositionName(positionName);

//			log.info("<< Try to fill title >>");
			String title = extractTextFromElement(jobItemElement, "a[data-testid=link]");
			jobItem.setTitle(title);

//			log.info("<< Try to fill logo >>");
			String logo = extractAbsUrlFromElement(jobItemElement, "img[data-testid]", "src");
			jobItem.setLogo(logo);

//			log.info("<< Fill tags >>");
			String tags = extractTagsFromElement(jobItemElement);
			jobItem.setTags(tags);

			jobItems.add(jobItem);
		}

		for (JobItem jobItem : jobItems) {
//			log.info("<< Try connect to jobPageUrl >>");
			try {
				document = Jsoup.connect(jobItem.getJobPageUrl()).get();

//				log.info("<< Try to fill organizationUrl >>");
				String organizationUrl = extractAbsUrlFromElement(document, "a[data-testid=button]", "href");
				jobItem.setOrganizationUrl(organizationUrl);

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
				}

//				log.info("<< Try to fill description >>");
				String description = extractTextFromElement(document, "div[class=sc-beqWaB fmCCHr]");
				jobItem.setDescription(description);

			} catch (HttpStatusException | UnsupportedMimeTypeException | SocketTimeoutException e) {
				log.warn("<< Unavailable or private job url for job_function " + jobFunction.getName() +
						" with title " + jobItem.getTitle() + " and position " + jobItem.getPositionName() + " >>");
			}
		}

		log.info("<< Complete job function: " + jobFunction.getName() + " >>");
		return jobItems;
	}

	private String extractTextFromElement(Element element, String selector) {
		Element selectedElement = element.select(selector).first();
		return selectedElement != null ? selectedElement.text() : "";
	}

	private String extractAbsUrlFromElement(Element element, String selector, String attribute) {
		Element selectedElement = element.select(selector).first();
		return selectedElement != null ? selectedElement.absUrl(attribute) : "";
	}

	private String extractTagsFromElement(Element element) {
		Elements tags = element.select("div[data-testid=tag]");
		StringBuilder jobItemTags = new StringBuilder();
		for (Element tag : tags) {
			jobItemTags.append(tag.text()).append(",");
		}

		if (jobItemTags.length() > 0) {
			jobItemTags.deleteCharAt(jobItemTags.length() - 1);
		}

		return jobItemTags.toString();
	}
}
