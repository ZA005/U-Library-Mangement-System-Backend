package com.university.librarymanagementsystem.service.catalog;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.Set;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.librarymanagementsystem.config.GoogleBooksProperties;
import com.university.librarymanagementsystem.dto.catalog.AuthorDto;
import com.university.librarymanagementsystem.dto.catalog.GoogleBooksDto;
import com.university.librarymanagementsystem.entity.catalog.Author;
import com.university.librarymanagementsystem.entity.catalog.Book;
import com.university.librarymanagementsystem.entity.catalog.DdcClassification;
import com.university.librarymanagementsystem.repository.catalog.AuthorRepository;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.catalog.DdcRepository;
import com.university.librarymanagementsystem.repository.catalog.GoogleBooksRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class GoogleBooksService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private GoogleBooksProperties googleBooksProperties;
    @Autowired
    private GoogleBooksRepository googleBooksRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private DdcRepository ddcRepository;

    private Map<String, String> cutterMap;

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

    public String searchBooks(String query) {
        URI uri = UriComponentsBuilder.fromHttpUrl(googleBooksProperties.getBaseUrl())
                .queryParam("q", query)
                .queryParam("key", googleBooksProperties.getKey())
                .queryParam("maxResults", 20)
                .build()
                .toUri();
        return restTemplate.getForObject(uri, String.class);
    }

    @Transactional
    public Book saveBook(GoogleBooksDto bookDto) {

        if (bookDto == null) {
            throw new IllegalArgumentException("bookDto cannot be null");
        }

        // Convert DTO to Entity
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAccessionNo(bookDto.getAccessionNo());
        book.setPublisher(bookDto.getPublisher());
        book.setPublishedDate(bookDto.getPublishedDate());
        book.setDescription(bookDto.getDescription());
        book.setPageCount(bookDto.getPageCount());
        book.setCategories(bookDto.getCategories() != null ? String.join(",", bookDto.getCategories()) : null);
        book.setLanguage(bookDto.getLanguage());
        book.setIsbn10(bookDto.getIsbn10());
        book.setIsbn13(bookDto.getIsbn13());
        book.setThumbnail(bookDto.getThumbnail());
        book.setPrintType(bookDto.getPrintType());
        book.setStatus(bookDto.getStatus());
        book.setCallNumber(bookDto.getCallNumber());
        book.setPurchasePrice(bookDto.getPurchasePrice());
        book.setSection(bookDto.getSection());
        book.setDateAcquired(bookDto.getDateAcquired());
        book.setNotes(bookDto.getNotes());
        book.setLocation(bookDto.getLocation());
        book.setVendor(bookDto.getVendor());
        book.setFundingSource(bookDto.getFundingSource());
        book.setSubjects(bookDto.getSubjects() != null ? String.join(",", bookDto.getSubjects()) : null);
        book.setCollectionType(bookDto.getCollectionType());

        // Print book data before saving

        // Handle authors
        List<Author> authors = (bookDto.getAuthors() != null ? bookDto.getAuthors() : new ArrayList<AuthorDto>())
                .stream()
                .map(authorDto -> authorRepository.findByName(authorDto.getName())
                        .orElseGet(() -> {
                            Author newAuthor = new Author();
                            newAuthor.setName(authorDto.getName());
                            return newAuthor;
                        }))
                .toList();
        book.setAuthors(authors);
        Book savedBook = googleBooksRepository.save(book);

        // Save book condition if provided
        if (bookDto.getBookCondition() != null) {
            bookRepository.saveBookCondition(savedBook.getId(), bookDto.getBookCondition());
        }
        return savedBook;
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        try {
            // Load the JSON file from the classpath
            File file = new ClassPathResource("static/cutter-data.json").getFile();
            ObjectMapper objectMapper = new ObjectMapper();
            cutterMap = objectMapper.readValue(file, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading Cutter numbers from file", e);
        }
    }

    public String generateCutterSanbornNumber(String lastName) {
        // last name to lowercase for comparison
        String normalizedLastName = lastName.toLowerCase();

        // Initialize variables to track the best match
        String bestMatchPrefix = null;
        int bestMatchLength = 0;

        // Loop through each prefix in the cutterMap
        for (String prefix : cutterMap.keySet()) {
            // Check the length of the matching substring
            int matchLength = getMatchLength(normalizedLastName, prefix);

            // If this prefix matches more characters, update the best match
            if (matchLength > bestMatchLength) {
                bestMatchLength = matchLength;
                bestMatchPrefix = prefix;
            }
        }

        // If a best match is found, return the corresponding cutter number
        if (bestMatchPrefix != null) {
            return bestMatchPrefix.substring(0, 1).toUpperCase()
                    + cutterMap.get(bestMatchPrefix);
        }

        // If no match is found, fallback to default Cutter number
        return "99";
    }

    // Helper method to calculate the match length between the last name and the
    // Cutter prefix
    private int getMatchLength(String lastName, String prefix) {
        int matchLength = 0;
        int minLength = Math.min(lastName.length(), prefix.length());

        // Compare the characters one by one from the start
        for (int i = 0; i < minLength; i++) {
            if (lastName.charAt(i) == prefix.charAt(i)) {
                matchLength++;
            } else {
                break; // Stop as soon as characters no longer match
            }
        }

        return matchLength;
    }

    public String getClassNumber(String category) {
        List<DdcClassification> allClassifications = ddcRepository.findAll(); // Fetch all categories
        JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
        String bestMatchClassNumber = null;
        double highestSimilarity = 0.0;

        for (DdcClassification classification : allClassifications) {
            double similarityScore = similarity.apply(category.toLowerCase(),
                    classification.getDescription().toLowerCase());
            if (similarityScore > highestSimilarity) {
                highestSimilarity = similarityScore;
                bestMatchClassNumber = classification.getClassNumber();
            }
        }

        return highestSimilarity >= 0.75 ? bestMatchClassNumber : null; // Return only if similarity is above threshold
    }

    // Generate Cutter Number based on authors
    public String generateCutterNumber(List<String> authors) {
        if (authors == null || authors.isEmpty()) {
            return "A99"; // Default Cutter Number if no author is provided
        }

        String authorName = authors.get(0); // Take the first author
        String[] nameParts = authorName.split(" ");
        String lastName = nameParts[nameParts.length - 1]; // Get the last name
        return generateCutterSanbornNumber(lastName.toLowerCase());
    }

    // Function to generate the Call Number
    public String generateCallNumber(String category, List<String> authors, String publishedDate, String title) {
        String classNumber = getClassNumber(category); // Get the class number for the category
        String cutterNumber = generateCutterNumber(authors); // Generate Cutter Number
        String publicationYear = (publishedDate != null && !publishedDate.isEmpty())
                ? publishedDate.split("-")[0]
                : "0000"; // Default to 0000 if no year is provided

        if (classNumber == null) {
            return "Class number not found"; // If no class number, return an error message
        }

        // Process the title to get the first relevant letter
        String normalizedTitle = getRelevantTitleLetter(title);

        return classNumber + "." + cutterNumber + normalizedTitle + " " + publicationYear; // Return the full call
                                                                                           // number
    }

    // Helper method to extract the first relevant letter from the title
    private String getRelevantTitleLetter(String title) {
        if (title == null || title.isEmpty()) {
            return ""; // Return an empty string if the title is null or empty
        }

        // Split the title into words
        String[] words = title.split("\\s+");

        // Articles to ignore
        Set<String> articles = Set.of("a", "an", "the");

        for (String word : words) {
            // Check if the word is not an article
            if (!articles.contains(word.toLowerCase())) {
                return word.substring(0, 1).toLowerCase(); // Return the first letter of the first relevant word
            }
        }

        return ""; // Return an empty string if no relevant letter is found
    }
}
