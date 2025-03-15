package com.university.librarymanagementsystem.mapper.curriculum;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.curriculum.BookReferenceDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.entity.curriculum.BookReference;
import com.university.librarymanagementsystem.entity.curriculum.Course;

@Component
public class BookReferenceMapper {
    public static BookReferenceDTO mapToBookRefDTO(BookReference bookRef) {
        return new BookReferenceDTO(
                bookRef.getId(),
                bookRef.getCourse().getId(),
                bookRef.getCourse().getCourse_name(),
                bookRef.getBook().getId(),
                bookRef.getBook().getTitle(),
                bookRef.getBook().getIsbn10(),
                bookRef.getBook().getIsbn13(),
                bookRef.getBook().getCopyRight().toString(),
                bookRef.getBook().getLanguage(),
                bookRef.getBook().getBookCatalog().getSection().getLocation().getName(),
                bookRef.getStatus());
    }

    public static BookReference mapToBookRef(BookReferenceDTO bookRefDTO) {
        Course course = new Course();
        Books book = new Books();
        course.setId(bookRefDTO.getCourse_id());
        book.setId(bookRefDTO.getBook_id());
        return new BookReference(
                bookRefDTO.getId(),
                course,
                book,
                bookRefDTO.getStatus());
    }
}
