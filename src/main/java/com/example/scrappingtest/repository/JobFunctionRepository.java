package com.example.scrappingtest.repository;

import com.example.scrappingtest.entity.JobFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobFunctionRepository extends JpaRepository<JobFunction, Long> {
	Optional<JobFunction> findByName(String name);
}
