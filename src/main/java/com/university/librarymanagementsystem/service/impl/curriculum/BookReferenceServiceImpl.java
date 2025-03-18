package com.university.librarymanagementsystem.service.impl.curriculum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.dto.curriculum.BookReferenceDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.entity.curriculum.BookReference;
import com.university.librarymanagementsystem.entity.curriculum.Course;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.BookMapper;
import com.university.librarymanagementsystem.mapper.curriculum.BookReferenceMapper;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.curriculum.BookReferenceRepository;
import com.university.librarymanagementsystem.repository.curriculum.CourseRepository;
import com.university.librarymanagementsystem.service.curriculum.BookReferenceService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookReferenceServiceImpl implements BookReferenceService {

    private CourseRepository subjectRepository;
    private BookReferenceRepository bookRefRepository;
    private BookRepository bookRepository;

    @Override
    public BookReferenceDTO addBookReference(BookReferenceDTO bookRefDTO) {
        System.out.println("Course:" + bookRefDTO.getCourse_name());
        System.out.println("Title:" + bookRefDTO.getBook_name());
        System.out.println("Copyright:" + bookRefDTO.getCopyright());

        Course subject = subjectRepository.findById(bookRefDTO.getCourse_id())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found!"));

        List<Course> sameCourses = subjectRepository.findByCourseCodeMultiple(subject.getCourse_code());

        List<BookReference> bookReferences = new ArrayList<>();

        for (Course sameCourse : sameCourses) {
            boolean exists = bookRefRepository.countByCourseIdAndBookId(sameCourse.getId(),
                    bookRefDTO.getBook_id()) > 0;

            if (!exists) {
                BookReference bookRef = BookReferenceMapper.mapToBookRef(bookRefDTO);
                bookRef.setCourse(sameCourse);
                bookReferences.add(bookRef);
            }
        }

        List<BookReference> savedBookRefs = bookRefRepository.saveAll(bookReferences);

        return savedBookRefs.isEmpty() ? null : BookReferenceMapper.mapToBookRefDTO(savedBookRefs.get(0));
    }

    @Override
    public List<BookReferenceDTO> addMultipleBookRef(List<BookReferenceDTO> bookReferenceDTOs) {
        List<BookReference> bookReferences = new ArrayList<>();

        for (BookReferenceDTO bookRefDTO : bookReferenceDTOs) {
            Course subject = subjectRepository.findById(bookRefDTO.getCourse_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found!"));

            List<Course> sameCourses = subjectRepository.findByCourseCodeMultiple(subject.getCourse_code());

            for (Course sameCourse : sameCourses) {
                boolean exists = bookRefRepository.countByCourseIdAndBookId(sameCourse.getId(),
                        bookRefDTO.getBook_id()) > 0;

                if (!exists) {
                    BookReference bookRef = BookReferenceMapper.mapToBookRef(bookRefDTO);
                    bookRef.setCourse(sameCourse);
                    bookReferences.add(bookRef);
                }
            }
        }

        List<BookReference> savedBookReferences = bookRefRepository.saveAll(bookReferences);

        return savedBookReferences.stream()
                .map(BookReferenceMapper::mapToBookRefDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookReferenceDTO> getAllBookReference() {
        List<BookReference> bookReferences = bookRefRepository.findAll();

        return bookReferences.stream().map((bookRef) -> BookReferenceMapper.mapToBookRefDTO(bookRef))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookReferenceDTO> getAllBookRefByCourse(Integer courseId) {
        List<BookReference> bookReferences = bookRefRepository.findAllBookReferenceByCourse(courseId);

        return bookReferences.stream().map((bookRef) -> BookReferenceMapper.mapToBookRefDTO(bookRef))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getAllUniqueBooks() {
        List<Books> uniqueBooks = bookRepository.findAllBooksUniqueOnly();

        return uniqueBooks.stream().map((book) -> BookMapper.mapToBookDTO(book)).collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> getAllBooksNotReferenced(Integer courseId) {
        List<Books> bookNotReferenced = bookRepository.findUniqueBooksNotInReference(courseId);

        return bookNotReferenced.stream().map((book) -> BookMapper.mapToBookDTO(book)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeBookRef(Integer bookRefId) {
        BookReference bookRef = bookRefRepository.findById(bookRefId)
                .orElseThrow(() -> new RuntimeException("Book reference not found with ID: " + bookRefId));

        Course subject = bookRef.getCourse();
        String bookName = bookRef.getBook().getTitle();

        bookRefRepository.deleteByCourseCodeAndBookName(subject.getCourse_code(), bookName);
    }

}