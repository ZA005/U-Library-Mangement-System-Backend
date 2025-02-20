package com.university.librarymanagementsystem.mapper.catalog;

import java.util.List;
import java.util.stream.Collectors;

import com.university.librarymanagementsystem.dto.catalog.AccessionDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDto;
import com.university.librarymanagementsystem.dto.circulation.BookLoanDetailsDTO;
import com.university.librarymanagementsystem.entity.catalog.Author;
import com.university.librarymanagementsystem.entity.catalog.Book;

public class BookMapper {

    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAccessionNo(),
                book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()),
                book.getPublisher(),
                book.getPublishedDate(),
                book.getDescription(),
                book.getPageCount(),
                book.getCategories(),
                book.getLanguage(),
                book.getIsbn10(),
                book.getIsbn13(),
                book.getThumbnail(),
                book.getPrintType(),
                book.getStatus(),
                book.getCallNumber(),
                book.getPurchasePrice(),
                book.getSection(),
                book.getDateAcquired(),
                book.getNotes(),
                book.getLocation(),
                book.getVendor(),
                book.getFundingSource(),
                book.getSubjects(),
                book.getCollectionType(),
                book.getBookCondition());

    }

    public static AccessionDTO mapToAccessionDTO(Book book) {
        AccessionDTO accessionDTO = new AccessionDTO();
        accessionDTO.setAccessionNo(book.getAccessionNo());
        accessionDTO.setSection(book.getSection());
        return accessionDTO;
    }


    public static BookLoanDetailsDTO mapToBookLoanDetails(Book book) {
        BookLoanDetailsDTO bookLoanDetails = new BookLoanDetailsDTO();
        bookLoanDetails.setTitle(book.getTitle());
      
        List<String> authorsList = book.getAuthors().stream()
                .map(Author::getName)
                .toList();

        bookLoanDetails.setAuthors(authorsList);
        bookLoanDetails.setCallNumber(book.getCallNumber());
        bookLoanDetails.setBookStatus(book.getStatus());
        return bookLoanDetails;
    }
}