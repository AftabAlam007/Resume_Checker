package com.aftab.Resume.Check;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "com.aftab.Resume.Check", "com.resume" })
@EnableJpaRepositories(basePackages = {"com.resume.repository"})
@EntityScan(basePackages = {"com.resume.model"})
@PropertySource("classpath:application.properties")
public class ResumeCheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeCheckApplication.class, args);
	}

}
