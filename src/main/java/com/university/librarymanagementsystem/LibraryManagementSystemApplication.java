package com.university.librarymanagementsystem;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.university.librarymanagementsystem.config.GoogleBooksProperties;

@SpringBootApplication(scanBasePackages = "com.university.librarymanagementsystem")
@EnableConfigurationProperties(GoogleBooksProperties.class)
@EnableScheduling
@EnableAsync
public class LibraryManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementSystemApplication.class, args);

		TimeZone.setDefault(TimeZone.getTimeZone("GMT+08:00"));
	}

}
