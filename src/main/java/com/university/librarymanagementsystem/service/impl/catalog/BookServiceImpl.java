package com.university.librarymanagementsystem.service.impl.catalog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.librarymanagementsystem.dto.catalog.BarcodeRequestDTO;
import com.university.librarymanagementsystem.dto.catalog.BookCatalogDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.entity.catalog.Acquisition;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.Section;
import com.university.librarymanagementsystem.entity.catalog.book.Author;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.entity.catalog.book.Categories;
import com.university.librarymanagementsystem.enums.BookStatus;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.BookCatalogMapper;
import com.university.librarymanagementsystem.mapper.catalog.BookMapper;
import com.university.librarymanagementsystem.repository.catalog.AcquisitionRepository;
import com.university.librarymanagementsystem.repository.catalog.AuthorRepository;
import com.university.librarymanagementsystem.repository.catalog.BookCatalogRepository;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.catalog.CategoryRepository;
import com.university.librarymanagementsystem.repository.catalog.SectionRepository;
import com.university.librarymanagementsystem.service.catalog.BookService;

import jakarta.annotation.PostConstruct;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookCatalogRepository bookCatalogRepository;
    private final AuthorRepository authorRepository;
    private final SectionRepository sectionRepository;
    private final AcquisitionRepository acquisitionRepository;
    private final CategoryRepository categoryRepository;
    private Map<String, String> cutterMap;

    public BookServiceImpl(BookRepository bookRepository,
            BookCatalogRepository bookCatalogRepository,
            AuthorRepository authorRepository,
            SectionRepository sectionRepository,
            AcquisitionRepository acquisitionRepository,
            CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.bookCatalogRepository = bookCatalogRepository;
        this.authorRepository = authorRepository;
        this.sectionRepository = sectionRepository;
        this.acquisitionRepository = acquisitionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Books> saveBook(BookDTO bookDTO) {
        validateBookDTO(bookDTO);

        BookCatalogDTO bookCatalogDTO = bookDTO.getBookCatalog();
        validateBookCatalogDTO(bookCatalogDTO);

        Section section = getSection(bookCatalogDTO.getSection().getId());
        Acquisition acquisition = getAcquisition(bookCatalogDTO.getAcquisitionDetails().getId());

        int copies = getCopies(bookCatalogDTO);
        String baseAccessionNumber = bookDTO.getAccessionNumber();
        int startingCopyNumber = getStartingCopyNumber(bookDTO, baseAccessionNumber);

        BookCatalog catalog = saveBookCatalog(bookCatalogDTO, section, acquisition);

        return saveBooks(bookDTO, catalog, copies, baseAccessionNumber, startingCopyNumber);
    }

    @Override
    public List<BookDTO> fetchAllBooks() {
        List<Books> books = bookRepository.findAll();

        return filterActiveBooks(books).stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                Books::getIsbn13,
                                BookMapper::mapToBookDTO,
                                (dto1, dto2) -> dto1,
                                LinkedHashMap::new),
                        map -> new ArrayList<>(map.values())));

    }

    @Override
    public List<BookDTO> fetchNewlyAcquiredBooks() {
        List<Books> newlyAcquiredBooks = bookRepository.findAll();

        Collections.reverse(newlyAcquiredBooks);

        return filterActiveBooks(newlyAcquiredBooks).stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                Books::getIsbn13,
                                BookMapper::mapToBookDTO,
                                (dto1, dto2) -> dto1,
                                LinkedHashMap::new),
                        map -> new ArrayList<>(map.values())));

    }

    @Override
    public List<BookDTO> fetchBooksByAuthor(String authorName) {
        List<Books> books = bookRepository.findBooksByAuthorName(authorName);
        return filterActiveBooks(books).stream().map(BookMapper::mapToBookDTO).toList();
    }

    @Override
    public String fetchLatestBaseAccession(String isbn13, String locationCodeName) {
        // Validate inputs
        if (isbn13 == null || isbn13.isEmpty()) {
            throw new IllegalArgumentException("ISBN13 cannot be null or empty");
        }
        if (locationCodeName == null || locationCodeName.isEmpty()) {
            throw new IllegalArgumentException("Location code name cannot be null or empty");
        }

        // Fetch the latest book by ISBN13 (if it exists)
        Optional<Books> latestBookByIsbn = bookRepository.findTopByIsbn13OrderByAccessionNumberDesc(isbn13);

        // Case 1: Book exists by ISBN13
        if (latestBookByIsbn.isPresent()) {
            String latestAccession = latestBookByIsbn.get().getAccessionNumber();
            String baseAccession = extractBaseAccession(latestAccession);

            // Check if the base matches the provided locationCodeName
            if (baseAccession != null && baseAccession.startsWith(locationCodeName + "-")) {
                return baseAccession; // Matching location code, return existing base
            } else {
                // Different location code, fall through to generate a new base
                return generateNewBaseAccession(locationCodeName);
            }
        }

        // Case 2: No book exists for this ISBN13, generate a new base accession number
        return generateNewBaseAccession(locationCodeName);
    }

    @Override
    public List<BarcodeRequestDTO> fetchAllAccessionNumberWithSection() {
        List<Books> books = bookRepository.findAll();
        return filterActiveBooks(books).stream()
                .map(BookMapper::mapToBarcodeRequestDTO)
                .toList();
    }

    @Override
    public List<BookDTO> fetchBookByIsbn13(String isbn13) {
        List<Books> books = bookRepository.findByIsbn13(isbn13);

        return filterActiveBooks(books).stream().map(BookMapper::mapToBookDTO).toList();
    }

    @Override
    public void weedBook(WeedInfoDTO weedInfoDTO) {
        Books bookToWeed = bookRepository.findById(weedInfoDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + weedInfoDTO.getBookId()));
        BookStatus status = BookStatus.AVAILABLE;

        if (weedInfoDTO.getWeedStatus().toString().equals(BookStatus.WEEDED.name())) {
            status = BookStatus.WEEDED;
        } else if (weedInfoDTO.getWeedStatus().toString().equals(BookStatus.ARCHIVED.name())) {
            status = BookStatus.ARCHIVED;
        }

        bookToWeed.setStatus(status);
        bookRepository.save(bookToWeed);
    }

    @Override
    public String generateCallNumber(String category, List<String> authors,
            String publishedDate, String title) {
        String classNumber = getClassNumber(category);
        String cutterNumber = generateCutterNumber(authors);
        String publicationYear = (publishedDate != null && !publishedDate.isEmpty())
                ? publishedDate.split("-")[0]
                : "0000";

        if (classNumber == null) {
            return "Class number not found";
        }

        String normalizedTitle = getRelevantTitleLetter(title);

        return classNumber + "." + cutterNumber + normalizedTitle + " " + publicationYear;
    }

    // HELPER METHODS
    private List<Books> filterActiveBooks(List<Books> books) {
        return books.stream()
                .filter(book -> !BookStatus.WEEDED.equals(book.getStatus())
                        && !BookStatus.ARCHIVED.equals(book.getStatus()))
                .toList();
    }

    private int extractCopyNumber(String accessionNumber) {
        try {
            String[] parts = accessionNumber.split(" c\\.");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return 0;
        }
    }

    private String extractBaseAccession(String accessionNumber) {
        if (accessionNumber == null) {
            return null;
        }
        return accessionNumber.contains(" c.") ? accessionNumber.split(" c\\.")[0] : accessionNumber;
    }

    /**
     * Generates a new base accession number based on the location code.
     * Increments the numeric part of the latest accession number for the location.
     */
    private String generateNewBaseAccession(String locationCodeName) {
        Optional<Books> latestBookByLocation = bookRepository
                .findTopByAccessionNumberStartingWithOrderByAccessionNumberDesc(locationCodeName);

        if (latestBookByLocation.isPresent()) {
            String latestAccession = latestBookByLocation.get().getAccessionNumber();
            String basePart = extractBaseAccession(latestAccession);
            if (basePart != null) {
                String numericPart = basePart.replace(locationCodeName + "-", "");
                int maxNumericPart;
                try {
                    maxNumericPart = Integer.parseInt(numericPart);
                } catch (NumberFormatException e) {
                    maxNumericPart = 0;
                }
                return locationCodeName + "-" + String.format("%06d", maxNumericPart + 1);
            } else {
                return locationCodeName + "-000001";
            }
        }

        // No existing books with this location code, start with 000001
        return locationCodeName + "-000001";
    }

    private void validateBookDTO(BookDTO bookDTO) {
        if (bookDTO == null) {
            throw new IllegalArgumentException("BookDTO cannot be null");
        }
    }

    private void validateBookCatalogDTO(BookCatalogDTO bookCatalogDTO) {
        if (bookCatalogDTO == null) {
            throw new IllegalArgumentException("BookCatalogDTO inside BookDTO cannot be null");
        }
        if (bookCatalogDTO.getSection() == null || bookCatalogDTO.getSection().getId() <= 0) {
            throw new IllegalArgumentException("Section ID must be provided and greater than 0");
        }
        if (bookCatalogDTO.getAcquisitionDetails() == null || bookCatalogDTO.getAcquisitionDetails().getId() <= 0) {
            throw new IllegalArgumentException("Acquisition ID must be provided and greater than 0");
        }
    }

    private Section getSection(int sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Section not found with ID: " + sectionId));
    }

    private Acquisition getAcquisition(int acquisitionId) {
        return acquisitionRepository.findById(acquisitionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Acquisition not found with ID: " + acquisitionId));
    }

    private int getCopies(BookCatalogDTO bookCatalogDTO) {
        return bookCatalogDTO.getCopies() != null && bookCatalogDTO.getCopies() > 0
                ? bookCatalogDTO.getCopies()
                : 1;
    }

    private int getStartingCopyNumber(BookDTO bookDTO, String baseAccessionNumber) {
        String isbn13 = bookDTO.getIsbn13();
        List<Books> existingBooks = null;
        if (isbn13 != null && !isbn13.isEmpty()) {
            existingBooks = bookRepository.findByIsbn13(isbn13);
        }

        int startingCopyNumber = 1;
        if (existingBooks != null && !existingBooks.isEmpty()) {
            String latestAccessionNumber = existingBooks.stream()
                    .map(Books::getAccessionNumber)
                    .filter(num -> num != null && num.contains(" c."))
                    .max((a1, a2) -> {
                        int num1 = extractCopyNumber(a1);
                        int num2 = extractCopyNumber(a2);
                        return Integer.compare(num1, num2);
                    })
                    .orElse(null);

            if (latestAccessionNumber != null) {
                String[] parts = latestAccessionNumber.split(" c\\.");
                baseAccessionNumber = parts[0];
                startingCopyNumber = Integer.parseInt(parts[1]) + 1;
            }
        }
        return startingCopyNumber;
    }

    private BookCatalog saveBookCatalog(BookCatalogDTO bookCatalogDTO, Section section, Acquisition acquisition) {
        BookCatalog catalog = BookCatalogMapper.toBookCatalogEntity(bookCatalogDTO, section, acquisition);
        return bookCatalogRepository.save(catalog);
    }

    private List<Books> saveBooks(BookDTO bookDTO, BookCatalog catalog, int copies, String baseAccessionNumber,
            int startingCopyNumber) {
        List<Books> savedBooks = new ArrayList<>();
        for (int i = 0; i < copies; i++) {
            String accessionNumber = baseAccessionNumber + " c." + (startingCopyNumber + i);
            Books book = BookMapper.mapToBook(bookDTO, catalog);
            book.setAuthors(getAuthors(bookDTO, book));
            book.setAccessionNumber(accessionNumber);
            savedBooks.add(bookRepository.save(book));
        }
        return savedBooks;
    }

    private List<Author> getAuthors(BookDTO bookDTO, Books book) {
        List<Author> authors = new ArrayList<>();
        if (bookDTO.getAuthors() != null && !bookDTO.getAuthors().isEmpty()) {
            for (String authorName : bookDTO.getAuthors()) {
                Author author = authorRepository.findByName(authorName)
                        .orElseGet(() -> {
                            Author newAuthor = new Author();
                            newAuthor.setName(authorName);
                            newAuthor.setBooks(new ArrayList<>());
                            return authorRepository.save(newAuthor);
                        });
                author.getBooks().add(book);
                authors.add(author);
            }
        }
        return authors;
    }

    private String getClassNumber(String category) {
        if (category == null || category.trim().isEmpty()) {
            return null;
        }
        Categories bestMatch = categoryRepository.findBestMatchByName(category);
        return bestMatch != null ? bestMatch.getDdcCode() : null;
    }

    private String generateCutterNumber(List<String> authors) {
        if (authors == null || authors.isEmpty()) {
            return "A99";
        }

        String authorName = authors.get(0);
        String[] nameParts = authorName.split(" ");
        String lastName = nameParts[nameParts.length - 1];
        return generateCutterSanbornNumber(lastName.toLowerCase());
    }

    private String generateCutterSanbornNumber(String lastName) {
        String normalizedLastName = lastName.toLowerCase();

        String bestMatchPrefix = null;
        int bestMatchLength = 0;

        for (String prefix : cutterMap.keySet()) {
            int matchLength = getMatchLength(normalizedLastName, prefix);

            if (matchLength > bestMatchLength) {
                bestMatchLength = matchLength;
                bestMatchPrefix = prefix;
            }

        }

        if (bestMatchPrefix != null) {
            return bestMatchPrefix.substring(0, 1).toUpperCase()
                    + cutterMap.get(bestMatchPrefix);
        }

        return "99";
    }

    private int getMatchLength(String lastName, String prefix) {
        int matchLength = 0;
        int minLength = Math.min(lastName.length(), prefix.length());

        for (int i = 0; i < minLength; i++) {
            if (lastName.charAt(i) == prefix.charAt(i)) {
                matchLength++;
            } else {
                break;
            }
        }

        return matchLength;
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        try {
            File file = new ClassPathResource("static/cutter-data.json").getFile();
            ObjectMapper objectMapper = new ObjectMapper();
            cutterMap = objectMapper.readValue(file, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading Cutter numbers from file", e);
        }
    }

    private String getRelevantTitleLetter(String title) {
        if (title == null || title.isEmpty()) {
            return "";
        }

        String[] words = title.split("\\s+");

        Set<String> articles = Set.of("a", "an", "the");

        for (String word : words) {
            if (!articles.contains(word.toLowerCase())) {
                return word.substring(0, 1).toLowerCase();
            }
        }
        return "";
    }

}
