package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.BarcodeRequestDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;

public interface BookService {

    List<Books> saveBook(BookDTO bookDTO);

    String fetchLatestBaseAccession(String isbn13, String locationCodeName);

    List<BookDTO> fetchAllBooks();

    List<BookDTO> fetchNewlyAcquiredBooks();

    List<BookDTO> fetchBooksByAuthor(String authorName);

    List<BarcodeRequestDTO> fetchAllAccessionNumberWithSection();

    List<BookDTO> fetchBookByIsbn13(String isbn13);

    BookDTO fetchBookByID(int book_id);

    BookDTO fetchBookByAccessionNumber(String accessionNumber);

    void weedBook(WeedInfoDTO weedInfoDTO);

    String generateCallNumber(String category, List<String> authors,
            String publishedDate, String title);

    List<BookDTO> fetchBy4Query(String query);
}
