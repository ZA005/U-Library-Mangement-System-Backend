package com.university.librarymanagementsystem;

import java.util.Properties;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.university.librarymanagementsystem.config.GoogleBooksProperties;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(scanBasePackages = "com.university.librarymanagementsystem")
@EnableConfigurationProperties(GoogleBooksProperties.class)
@EnableScheduling
@EnableAsync
public class LibraryManagementSystemApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		Properties props = System.getProperties();
		props.setProperty("DB_URL", dotenv.get("DB_URL"));
		props.setProperty("DB_USER", dotenv.get("DB_USER"));
		props.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		props.setProperty("GOOGLE_BOOKS_API_KEY", dotenv.get("GOOGLE_BOOKS_API_KEY"));
		props.setProperty("FRONTEND_URL", dotenv.get("FRONTEND_URL"));
		props.setProperty("MAIL_PORT", dotenv.get("MAIL_PORT"));
		props.setProperty("MAIL_USER", dotenv.get("MAIL_USER"));
		props.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		props.setProperty("jwt.secret", dotenv.get("JWT_KEY"));

		SpringApplication.run(LibraryManagementSystemApplication.class, args);

		TimeZone.setDefault(TimeZone.getTimeZone("GMT+08:00"));
	}

}
