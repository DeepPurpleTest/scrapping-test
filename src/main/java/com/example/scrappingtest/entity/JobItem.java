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
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

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

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;

		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();

		if (thisEffectiveClass != oEffectiveClass) return false;

		JobItem jobItem = (JobItem) o;

		return Objects.equals(jobPageUrl, jobItem.jobPageUrl) &&
				Objects.equals(positionName, jobItem.positionName) &&
				Objects.equals(organizationUrl, jobItem.organizationUrl) &&
				Objects.equals(logo, jobItem.logo) &&
				Objects.equals(title, jobItem.title) &&
				Objects.equals(laborFunction, jobItem.laborFunction) &&
				Objects.equals(address, jobItem.address) &&
				Objects.equals(postedDate, jobItem.postedDate) &&
				Objects.equals(description, jobItem.description) &&
				Objects.equals(tags, jobItem.tags);
	}

	@Override
	public final int hashCode() {
		if (this instanceof HibernateProxy) {
			return System.identityHashCode(((HibernateProxy) this).getHibernateLazyInitializer().getImplementation());
		}
		return Objects.hash(jobPageUrl, positionName, organizationUrl, logo, title, laborFunction, address, postedDate, description, tags);
	}
}
