package com.university.librarymanagementsystem.service.curriculum;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.BookDto;
import com.university.librarymanagementsystem.dto.curriculum.BookReferenceDTO;

public interface BookReferenceService {
    BookReferenceDTO addBookReference(BookReferenceDTO bookRefDTO);

    List<BookReferenceDTO> addMultipleBookRef(List<BookReferenceDTO> bookReferenceDTOs);

    List<BookReferenceDTO> getAllBookReference();

    List<BookReferenceDTO> getAllBookRefByCourse(Integer courseId);

    List<BookDto> getAllUniqueBooks();

    List<BookDto> getAllBooksNotReferenced(Integer courseId);

    void removeBookRef(Integer bookRefId);
}
