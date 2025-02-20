package com.university.librarymanagementsystem.mapper.curriculum;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.curriculum.BookReferenceDTO;
import com.university.librarymanagementsystem.entity.catalog.Book;
import com.university.librarymanagementsystem.entity.curriculum.BookReference;
import com.university.librarymanagementsystem.entity.curriculum.Course;

@Component
public class BookReferenceMapper {

    public static BookReferenceDTO mapToBookRefDTO(BookReference bookRef) {
        return new BookReferenceDTO(
                bookRef.getId(),
                bookRef.getCourse().getCourse_id(),
                bookRef.getCourse().getCourse_name(),
                bookRef.getBook().getId(),
                bookRef.getBook().getTitle(),
                bookRef.getBook().getIsbn10(),
                bookRef.getBook().getIsbn13(),
                bookRef.getBook().getLanguage(),
                bookRef.getBook().getLocation(),
                bookRef.getStatus());

    }

    public static BookReference mapTBookRef(BookReferenceDTO bookRefDTO) {
        Course subject = new Course();
        Book book = new Book();
        subject.setCourse_id(bookRefDTO.getCourse_id());
        book.setId(bookRefDTO.getBook_id());
        return new BookReference(
                bookRefDTO.getId(),
                subject,
                book,
                bookRefDTO.getStatus());
    }
}
