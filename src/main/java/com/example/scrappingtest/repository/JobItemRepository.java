package com.example.scrappingtest.repository;

import com.example.scrappingtest.entity.JobItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobItemRepository extends JpaRepository<JobItem, Long> {
	List<JobItem> findByJobFunctionId(Long id);
}
