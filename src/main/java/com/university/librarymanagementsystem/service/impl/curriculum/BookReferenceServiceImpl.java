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

        // Get all courses with the same course code (for minor subjects)
        List<Course> sameCourses = subjectRepository.findByCourseCodeMultiple(subject.getCourse_code());

        // Store book references for all matching courses
        List<BookReference> bookReferences = new ArrayList<>();

        for (Course sameCourse : sameCourses) {
            BookReference bookRef = BookReferenceMapper.mapToBookRef(bookRefDTO);
            bookRef.setCourse(sameCourse);
            bookReferences.add(bookRef);
        }

        // Save all book references
        List<BookReference> savedBookRefs = bookRefRepository.saveAll(bookReferences);

        // Convert and return the first saved reference
        return BookReferenceMapper.mapToBookRefDTO(savedBookRefs.get(0));
    }

    @Override
    public List<BookReferenceDTO> addMultipleBookRef(List<BookReferenceDTO> bookReferenceDTOs) {
        List<BookReference> bookReferences = bookReferenceDTOs.stream().map(bookRefDTO -> {
            Course subject = subjectRepository.findById(bookRefDTO.getCourse_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found!"));

            List<Course> sameCourses = subjectRepository.findByCourseCodeMultiple(subject.getCourse_code());

            if (sameCourses.size() > 1) {
                List<BookReference> existingReferences = bookRefRepository
                        .findAllBookReferenceByCourse(sameCourses.get(0).getId());

                if (!existingReferences.isEmpty()) {
                    existingReferences.forEach(ref -> {
                        BookReference newRef = new BookReference();
                        newRef.setCourse(subject);
                        newRef.setBook(ref.getBook());
                        bookRefRepository.save(newRef);
                    });
                }
            }

            BookReference bookRef = BookReferenceMapper.mapToBookRef(bookRefDTO);
            bookRef.setCourse(subject);
            return bookRef;
        }).collect(Collectors.toList());

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
    public void removeBookRef(Integer bookRefId) {
        if (bookRefRepository.existsById(bookRefId)) {
            bookRefRepository.deleteById(bookRefId);
        } else {
            throw new RuntimeException("Book reference not found with ID: " + bookRefId);
        }
    }
}