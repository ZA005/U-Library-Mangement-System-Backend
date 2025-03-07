package com.university.librarymanagementsystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "google.books.api")
@Getter
@Setter
public class GoogleBooksProperties {
    private String key;
    private String baseUrl = "https://www.googleapis.com/books/v1/volumes";

}