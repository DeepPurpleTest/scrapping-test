package com.example.scrappingtest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "job_item")
public class JobItem {
	@Id
	@Column(name = "job_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "hob_page_url", columnDefinition = "TEXT")
	private String jobPageUrl;

	@Column(name = "position_name")
	private String positionName;

	@Column(name = "organization_url", columnDefinition = "TEXT")
	private String organizationUrl;

	@Column(name = "logo", columnDefinition = "TEXT")
	private String logo;

	@Column(name = "title")
	private String title;

	@Column(name = "labor_function")
	private String laborFunction;

	@Column(name = "address")
	private String address;

	@Column(name = "posted_date")
	private String postedDate;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "tags")
	private String tags;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "job_function_id")
	private JobFunction jobFunction;
}
