package com.university.librarymanagementsystem.service.catalog;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.university.librarymanagementsystem.config.GoogleBooksProperties;

@Service
public class GoogleBookService {

    private final RestTemplate restTemplate = new RestTemplate();

    private GoogleBooksProperties googleBooksProperties;

    public GoogleBookService(GoogleBooksProperties googleBooksProperties) {
        this.googleBooksProperties = googleBooksProperties;
    }

    public String searchBooks(String query) {
        URI uri = UriComponentsBuilder.fromHttpUrl(googleBooksProperties.getBaseUrl())
                .queryParam("q", query)
                .queryParam("key", googleBooksProperties.getKey())
                .queryParam("maxResults", 20)
                .build()
                .toUri();
        return restTemplate.getForObject(uri, String.class);
    }

    public String buildQuery(String keyword, String title, String author, String publisher, String isbn, String lccn) {
        StringBuilder queryBuilder = new StringBuilder();

        if (keyword != null && !keyword.isBlank()) {
            queryBuilder.append(keyword).append("+");
        }
        if (title != null && !title.isBlank()) {
            queryBuilder.append("intitle:").append(title).append("+");
        }
        if (author != null && !author.isBlank()) {
            queryBuilder.append("inauthor:").append(author).append("+");
        }
        if (publisher != null && !publisher.isBlank()) {
            queryBuilder.append("inpublisher:").append(publisher).append("+");
        }
        if (isbn != null && !isbn.isBlank()) {
            queryBuilder.append("isbn:").append(isbn).append("+");
        }
        if (lccn != null && !lccn.isBlank()) {
            queryBuilder.append("lccn:").append(lccn).append("+");
        }

        // Remove trailing "+"
        if (queryBuilder.length() > 0) {
            queryBuilder.setLength(queryBuilder.length() - 1);
        }
        return queryBuilder.toString();
    }

}
