package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Author;
import com.university.librarymanagementsystem.entity.catalog.book.Books;

@Component
public class BookMapper {

    public static BookDTO toBookDTO(Books book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthors().stream().map(Author::getName).toList(),
                book.getIsbn10(),
                book.getIsbn13(),
                book.getDescription(),
                book.getPages(),
                book.getThumbnail(),
                book.getEdition(),
                book.getSeries(),
                book.getLanguage(),
                book.getPublishedDate(),
                book.getPublisher(),
                book.getCopyRight(),
                book.getPrintType(),
                book.getFormat());
    }

    public static Books toBook(BookDTO bookDTO) {
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
        book.setPublishedDate(bookDTO.getPublishedDate());
        book.setPublisher(bookDTO.getPublisher());
        book.setCopyRight(bookDTO.getCopyRight());
        book.setPrintType(bookDTO.getPrintType());
        book.setFormat(bookDTO.getFormat());
        return book;
    }

}
