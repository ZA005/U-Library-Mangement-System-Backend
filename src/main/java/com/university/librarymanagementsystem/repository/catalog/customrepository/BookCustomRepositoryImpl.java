package com.university.librarymanagementsystem.repository.catalog.customrepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.dto.catalog.BookSearchRequestDTO;
import com.university.librarymanagementsystem.entity.catalog.Acquisition;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.book.Author;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.entity.catalog.book.Categories;
import com.university.librarymanagementsystem.enums.BookStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

@Repository
public class BookCustomRepositoryImpl implements BookCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Books> advanceSearchBooks(BookSearchRequestDTO request) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Books> query = cb.createQuery(Books.class);
        Root<Books> book = query.from(Books.class);
        Join<Books, Author> author = book.join("authors", JoinType.LEFT);
        Join<Books, BookCatalog> catalog = book.join("bookCatalog", JoinType.LEFT);
        Join<BookCatalog, Acquisition> acquisition = catalog.join("acquisition", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        for (BookSearchRequestDTO.SearchCriterion criterion : request.getCriteria()) {
            String idx = criterion.getIdx();
            String searchTerm = criterion.getSearchTerm();
            String operator = criterion.getOperator();

            Predicate predicate = null;
            switch (idx) {
                case "q":
                    predicate = cb.or(
                            cb.equal(cb.function("SOUNDEX", String.class, book.get("title")),
                                    cb.function("SOUNDEX", String.class, cb.literal(searchTerm))),
                            cb.equal(cb.function("SOUNDEX", String.class, book.get("description")),
                                    cb.function("SOUNDEX", String.class, cb.literal(searchTerm))),
                            cb.equal(cb.function("SOUNDEX", String.class, author.get("name")),
                                    cb.function("SOUNDEX", String.class, cb.literal(searchTerm))),
                            cb.equal(cb.function("SOUNDEX", String.class, book.get("publisher")),
                                    cb.function("SOUNDEX", String.class, cb.literal(searchTerm))),
                            // LIKE for substring matches
                            cb.like(cb.lower(book.get("title")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(book.get("description")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(author.get("name")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(book.get("publisher")), "%" + searchTerm.toLowerCase() + "%"));
                    break;
                case "intitle":
                    predicate = cb.or(
                            cb.like(cb.lower(book.get("title")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.equal(cb.function("SOUNDEX", String.class, book.get("title")),
                                    cb.function("SOUNDEX", String.class, cb.literal(searchTerm))));
                    break;
                case "inauthor":
                    predicate = cb.or(
                            cb.like(cb.lower(author.get("name")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.equal(cb.function("SOUNDEX", String.class, author.get("name")),
                                    cb.function("SOUNDEX", String.class, cb.literal(searchTerm))));
                    break;
                case "inpublisher":
                    predicate = cb.or(
                            cb.like(cb.lower(book.get("publisher")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.equal(cb.function("SOUNDEX", String.class, book.get("publisher")),
                                    cb.function("SOUNDEX", String.class, cb.literal(searchTerm))));
                    break;
                case "insubjects":
                    Join<Books, Categories> categories = book.join("books", JoinType.LEFT);
                    predicate = cb.or(
                            cb.like(cb.lower(categories.get("name")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.equal(cb.function("SOUNDEX", String.class, categories.get("name")),
                                    cb.function("SOUNDEX", String.class, cb.literal(searchTerm))));
                    break;
                case "isbn":
                    predicate = cb.or(
                            cb.like(cb.lower(book.get("isbn10")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.like(cb.lower(book.get("isbn13")), "%" + searchTerm.toLowerCase() + "%"),
                            cb.equal(cb.function("SOUNDEX", String.class, book.get("isbn10")),
                                    cb.function("SOUNDEX", String.class, cb.literal(searchTerm))),
                            cb.equal(cb.function("SOUNDEX", String.class, book.get("isbn13")),
                                    cb.function("SOUNDEX", String.class, cb.literal(searchTerm))));
                    break;
                default:
                    continue;
            }

            if ("AND".equalsIgnoreCase(operator)) {
                predicates.add(predicate);
            } else if ("OR".equalsIgnoreCase(operator)) {
                predicates.add(cb.or(predicate));
            } else if ("NOT".equalsIgnoreCase(operator)) {
                predicates.add(cb.not(predicate));
            }
        }

        LocalDate startDate = null;
        LocalDate endDate = null;
        if (request.getYearRange() != null && !request.getYearRange().isEmpty()) {
            String[] years = request.getYearRange().split("-");
            try {
                startDate = LocalDate.parse(years[0].trim() + "-01-01");
                endDate = LocalDate.parse(years[1].trim() + "-12-31");
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid year range format. Use YYYY-YYYY.");
            }
        }

        if (startDate != null) {
            // Convert acquired_date (String) to LocalDate for comparison
            predicates.add(cb.greaterThanOrEqualTo(
                    cb.function("STR_TO_DATE", LocalDate.class, acquisition.get("acquired_date"),
                            cb.literal("%Y-%m-%d")),
                    startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(
                    cb.function("STR_TO_DATE", LocalDate.class, acquisition.get("acquired_date"),
                            cb.literal("%Y-%m-%d")),
                    endDate));
        }
        if (request.getLanguage() != null) {
            predicates.add(cb.equal(book.get("language"), request.getLanguage()));
        }
        if (Boolean.TRUE.equals(request.getIsAvailableOnly())) {
            predicates.add(cb.equal(book.get("status"), BookStatus.AVAILABLE));
            // AVAILABLE
        }

        // Combine all predicates
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Sorting logic
        if ("Title A-Z".equalsIgnoreCase(request.getSortOrder())) {
            query.orderBy(cb.asc(book.get("title")));
        } else if ("Title Z-A".equalsIgnoreCase(request.getSortOrder())) {
            query.orderBy(cb.desc(book.get("title")));
        } else if ("Acquisition date: newest to oldest".equalsIgnoreCase(request.getSortOrder())) {
            query.orderBy(cb.desc(
                    cb.function("STR_TO_DATE", LocalDate.class, acquisition.get("acquired_date"),
                            cb.literal("%Y-%m-%d"))));
        } else if ("Acquisition date: oldest to newest".equalsIgnoreCase(request.getSortOrder())) {
            query.orderBy(cb.asc(
                    cb.function("STR_TO_DATE", LocalDate.class, acquisition.get("acquired_date"),
                            cb.literal("%Y-%m-%d"))));
        }

        return entityManager.createQuery(query).getResultList();
    }

}
