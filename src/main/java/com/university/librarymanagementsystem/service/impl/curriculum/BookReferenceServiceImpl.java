package com.university.librarymanagementsystem.service.impl.curriculum;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.BookDto;
import com.university.librarymanagementsystem.dto.curriculum.BookReferenceDTO;
import com.university.librarymanagementsystem.entity.catalog.Book;
// import com.university.librarymanagementsystem.entity.Book;
import com.university.librarymanagementsystem.entity.curriculum.BookReference;
import com.university.librarymanagementsystem.entity.curriculum.Course;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.BookMapper;
import com.university.librarymanagementsystem.mapper.curriculum.BookReferenceMapper;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
// import com.university.librarymanagementsystem.repository.BookRepository;
import com.university.librarymanagementsystem.repository.curriculum.BookReferenceRepository;
import com.university.librarymanagementsystem.repository.curriculum.CourseRepository;
import com.university.librarymanagementsystem.service.curriculum.BookReferenceService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookReferenceServiceImpl implements BookReferenceService {

    // private BookRepository bookRepository;
    private CourseRepository subjectRepository;
    private BookReferenceRepository bookRefRepository;
    private BookRepository bookRepository;

    @Override
    public BookReferenceDTO addBookReference(BookReferenceDTO bookRefDTO) {
        System.out.println("SUBJECT ID:" + bookRefDTO.getCourse_id());
        Course subject = subjectRepository.findById(bookRefDTO.getCourse_id())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found!"));

        BookReference bookRef = BookReferenceMapper.mapTBookRef(bookRefDTO);

        bookRef.setCourse(subject);

        BookReference savedBookRef = bookRefRepository.save(bookRef);

        System.out.println("test:" + savedBookRef);
        return BookReferenceMapper.mapToBookRefDTO(savedBookRef);
    }

    @Override
    public List<BookReferenceDTO> addMultipleBookRef(List<BookReferenceDTO> bookReferenceDTOs) {
        List<BookReference> bookReferences = bookReferenceDTOs.stream().map(bookRefDTO -> {
            Course subject = subjectRepository.findById(bookRefDTO.getCourse_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found!"));

            BookReference bookRef = BookReferenceMapper.mapTBookRef(bookRefDTO);
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
    public List<BookDto> getAllUniqueBooks() {
        List<Book> uniqueBooks = bookRepository.findAllBooksUniqueOnly();

        return uniqueBooks.stream().map((book) -> BookMapper.toDto(book)).collect(Collectors.toList());
    }

    @Override
    public List<BookDto> getAllBooksNotReferenced(Integer courseId) {
        List<Book> bookNotReferenced = bookRepository.findUniqueBooksNotInReference(courseId);

        return bookNotReferenced.stream().map((book) -> BookMapper.toDto(book)).collect(Collectors.toList());
    }
}
