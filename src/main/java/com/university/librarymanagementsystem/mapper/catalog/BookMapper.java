package com.university.librarymanagementsystem.mapper.catalog;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.BarcodeRequestDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.book.Author;
import com.university.librarymanagementsystem.entity.catalog.book.Books;

@Component
public class BookMapper {

    public static BookDTO mapToBookDTO(Books book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setAccessionNumber(book.getAccessionNumber());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthors(book.getAuthors().stream().map(Author::getName).toList());
        bookDTO.setIsbn10(book.getIsbn10());
        bookDTO.setIsbn13(book.getIsbn13());
        bookDTO.setCategories("");
        bookDTO.setDescription(book.getDescription());
        bookDTO.setPages(book.getPages());
        bookDTO.setThumbnail(book.getThumbnail());
        bookDTO.setEdition(book.getEdition());
        bookDTO.setSeries(book.getSeries());
        bookDTO.setLanguage(book.getLanguage());
        bookDTO.setPublishedDate(book.getPublishedDate().toString());
        bookDTO.setPublisher(book.getPublisher());
        bookDTO.setCopyRight(book.getCopyRight());
        bookDTO.setPrintType(book.getPrintType());
        bookDTO.setFormat(book.getFormat());
        bookDTO.setStatus(book.getStatus());
        bookDTO.setCondition(book.getCondition());
        bookDTO.setBookCatalog(BookCatalogMapper.mapToBookCatalogDTO(book.getBookCatalog()));
        return bookDTO;
    }

    public static Books mapToBook(BookDTO bookDTO, BookCatalog bookCatalog) {
        Books book = new Books();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setIsbn10(bookDTO.getIsbn10());
        book.setIsbn13(bookDTO.getIsbn13());
        book.setDescription(bookDTO.getDescription());
        book.setPages(bookDTO.getPages());
        book.setThumbnail(bookDTO.getThumbnail());
        book.setEdition(bookDTO.getEdition());
        book.setSeries(bookDTO.getSeries());
        book.setLanguage(bookDTO.getLanguage());
        book.setPublishedDate(LocalDate.parse(bookDTO.getPublishedDate()));
        book.setPublisher(bookDTO.getPublisher());
        book.setCopyRight(bookDTO.getCopyRight());
        book.setPrintType(bookDTO.getPrintType());
        book.setFormat(bookDTO.getFormat());
        book.setStatus(bookDTO.getStatus());
        book.setCondition(bookDTO.getCondition());
        book.setBookCatalog(bookCatalog);

        return book;
    }

    public static BarcodeRequestDTO mapToBarcodeRequestDTO(Books book) {
        return new BarcodeRequestDTO(
                book.getAccessionNumber(),
                book.getBookCatalog().getSection().getSectionName());
    }

}
