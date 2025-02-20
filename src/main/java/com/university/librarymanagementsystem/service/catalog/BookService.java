package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.AccessionDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDto;
import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.dto.circulation.BookLoanDetailsDTO;

public interface BookService {

    List<BookDto> getAllBooks();

    List<BookDto> getBooksByAuthorName(String authorName);

    String getLastAccessionNumber(String locationPrefix);

    BookLoanDetailsDTO getBookByAccessionNo(String accessionNo);

    String getLatestAccessionNo(String title, String isbn10, String isbn13, String locationPrefix);

    String fetchLastAccessionNumber();

    void weedBook(WeedInfoDTO weedInfoDTO);

    List<AccessionDTO> getAllAccessionNumbers();

    List<BookDto> getBooksByIsbn13(String isbn13);

}
