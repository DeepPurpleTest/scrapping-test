package com.example.scrappingtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScrappingTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrappingTestApplication.class, args);
	}
}
