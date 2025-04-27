package com.university.librarymanagementsystem.service.curriculum;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.dto.curriculum.BookReferenceDTO;

public interface BookReferenceService {
    BookReferenceDTO addBookReference(BookReferenceDTO bookRefDTO);

    List<BookReferenceDTO> addMultipleBookRef(List<BookReferenceDTO> bookReferenceDTOs);

    List<BookReferenceDTO> getAllBookReference();

    List<BookReferenceDTO> getAllBookRefByCourse(Integer courseId);

    List<BookDTO> getAllUniqueBooks();

    List<BookDTO> getAllBooksNotReferenced(Integer courseId);

    List<Object[]> exportBookReferences(Integer revisionNo, Integer programId);

    void removeBookRef(Integer bookRefId);
}