package com.university.librarymanagementsystem.service.catalog;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.librarymanagementsystem.config.GoogleBooksProperties;
import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Author;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.entity.catalog.book.Categories;
import com.university.librarymanagementsystem.enums.BookStatus;

@Service
public class GoogleBookService {

    private final RestTemplate restTemplate = new RestTemplate();

    private GoogleBooksProperties googleBooksProperties;

    public GoogleBookService(GoogleBooksProperties googleBooksProperties) {
        this.googleBooksProperties = googleBooksProperties;
    }

    public List<BookDTO> searchBooks(String query) {
        URI uri = UriComponentsBuilder.fromHttpUrl(googleBooksProperties.getBaseUrl())
                .queryParam("q", query)
                .queryParam("key", googleBooksProperties.getKey())
                .queryParam("maxResults", 20)
                .build()
                .toUri();

        String response = restTemplate.getForObject(uri, String.class);

        return mapResponseToBooks(response);
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

    private List<BookDTO> mapResponseToBooks(String response) {
        List<BookDTO> booksList = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode itemsNode = rootNode.get("items");

            if (itemsNode != null && itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    JsonNode volumeInfoNode = itemNode.get("volumeInfo");

                    if (volumeInfoNode != null) {
                        BookDTO book = new BookDTO();

                        // Basic fields
                        book.setTitle(
                                volumeInfoNode.has("title") ? volumeInfoNode.get("title").asText() : "Unknown Title");

                        // Authors (as List<String>)
                        List<String> authors = new ArrayList<>();
                        JsonNode authorsNode = volumeInfoNode.get("authors");
                        if (authorsNode != null && authorsNode.isArray()) {
                            for (JsonNode authorNode : authorsNode) {
                                authors.add(authorNode.asText());
                            }
                        } else {
                            authors.add("Unknown Author");
                        }
                        book.setAuthors(authors);

                        // ISBNs
                        book.setIsbn10(extractIsbn(volumeInfoNode, "ISBN_10"));
                        book.setIsbn13(extractIsbn(volumeInfoNode, "ISBN_13"));

                        String category = null;
                        JsonNode categoriesNode = volumeInfoNode.get("categories");
                        if (categoriesNode != null && categoriesNode.isArray()) {
                            List<String> categories = new ArrayList<>();
                            for (JsonNode categoryNode : categoriesNode) {
                                categories.add(categoryNode.asText());
                            }
                            category = categories.isEmpty() ? null : categories.get(0);

                        }
                        book.setCategories(category);

                        // Other fields
                        book.setDescription(
                                volumeInfoNode.has("description") ? volumeInfoNode.get("description").asText()
                                        : "No description available");
                        book.setPublisher(
                                volumeInfoNode.has("publisher") ? volumeInfoNode.get("publisher").asText()
                                        : "Unknown Publisher");

                        // Published Date (as String in BookDTO)
                        String publishedDate = volumeInfoNode.has("publishedDate")
                                ? volumeInfoNode.get("publishedDate").asText()
                                : null;
                        book.setPublishedDate(publishedDate);

                        // Pages
                        book.setPages(
                                volumeInfoNode.has("pageCount") ? volumeInfoNode.get("pageCount").asInt() : 0);

                        // Thumbnail
                        JsonNode imageLinksNode = volumeInfoNode.get("imageLinks");
                        book.setThumbnail(
                                imageLinksNode != null && imageLinksNode.has("thumbnail")
                                        ? imageLinksNode.get("thumbnail").asText()
                                        : "");

                        // Print Type
                        book.setPrintType(
                                volumeInfoNode.has("printType") ? volumeInfoNode.get("printType").asText() : "Unknown");

                        // Language
                        book.setLanguage(
                                volumeInfoNode.has("language") ? volumeInfoNode.get("language").asText() : "Unknown");

                        // Default fields from BookDTO
                        book.setStatus(BookStatus.AVAILABLE);
                        book.setCondition("New");
                        book.setFormat("Paperback");

                        // Fields not provided by Google Books API (set defaults)
                        book.setId(0); // Default, should be set by database
                        book.setAccessionNumber(null); // Not available from API
                        book.setEdition(null); // Not directly available
                        book.setSeries(null); // Not directly available
                        book.setCopyRight(null); // Not directly available
                        book.setBookCatalog(null); // Not available from API

                        booksList.add(book);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return booksList;
    }

    private String extractIsbn(JsonNode volumeInfoNode, String type) {
        JsonNode industryIdentifiersNode = volumeInfoNode.get("industryIdentifiers");

        if (industryIdentifiersNode != null && industryIdentifiersNode.isArray()) {
            // Use stream to find the ISBN based on the type
            Optional<JsonNode> matchingNode = StreamSupport.stream(industryIdentifiersNode.spliterator(), false)
                    .filter(idNode -> idNode.has("type") && idNode.get("type").asText().equals(type))
                    .findFirst();

            // If a matching node is found, return the identifier
            if (matchingNode.isPresent() && matchingNode.get().has("identifier")) {
                return matchingNode.get().get("identifier").asText();
            }
        }

        return null;
    }

}
