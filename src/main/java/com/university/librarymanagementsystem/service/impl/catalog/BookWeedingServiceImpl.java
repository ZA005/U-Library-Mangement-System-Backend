package com.university.librarymanagementsystem.service.impl.catalog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.entity.catalog.Book;
import com.university.librarymanagementsystem.entity.catalog.BookWeeding;
import com.university.librarymanagementsystem.entity.catalog.BookWeedingStatus;
import com.university.librarymanagementsystem.entity.catalog.WeedingCriteria;
import com.university.librarymanagementsystem.enums.ProcessStatus;
import com.university.librarymanagementsystem.enums.WeedStatus;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.catalog.BookWeedingRepository;
import com.university.librarymanagementsystem.repository.catalog.BookWeedingStatusRepository;
import com.university.librarymanagementsystem.repository.catalog.WeedingCriteriaRepository;
import com.university.librarymanagementsystem.repository.circulation.LoanRepository;
import com.university.librarymanagementsystem.service.catalog.BookWeedingService;

import jakarta.transaction.Transactional;

@Service
public class BookWeedingServiceImpl implements BookWeedingService {

    @Autowired
    private BookWeedingRepository bookWeedingRepository;

    @Autowired
    private WeedingCriteriaRepository weedingCriteriaRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookWeedingStatusRepository bookWeedingStatusRepository;

    @Override
    @Scheduled(cron = "0 0 0 1 1 ?")
    @Transactional
    public void annualWeedingFlagging() {
        initiateWeedingProcess("System");
    }

    @Override
    @Transactional
    public void manualWeedingFlagging() {
        initiateWeedingProcess("Manual");
    }

    private void initiateWeedingProcess(String initiator) {
        BookWeeding weed = new BookWeeding();
        weed.setStartDate(LocalDate.now());
        weed.setStatus(ProcessStatus.SCHEDULED);
        weed.setInitiator(initiator);
        BookWeeding savedWeed = bookWeedingRepository.save(weed);
        flagBooksForWeeding(savedWeed.getId());
        // Update the process status to IN_PROGRESS after flagging books
        savedWeed.setStatus(ProcessStatus.IN_PROGRESS);
        bookWeedingRepository.save(savedWeed);
    }

    @Override
    @Transactional
    public void flagBooksForWeeding(Long processId) {
        BookWeeding weed = bookWeedingRepository.findById(processId)
                .orElseThrow(() -> new ResourceNotFoundException("Book Weeding not found with id: " + processId));

        List<WeedingCriteria> criteriaList = weedingCriteriaRepository.findAllByCriteriaStatusTrue();

        for (WeedingCriteria criteria : criteriaList) {
            int ddcStart = Integer.parseInt(criteria.getDdc());
            int ddcEnd = ddcStart + 99;
            List<Book> books = bookRepository.findByDdcAndLanguage(ddcStart, ddcEnd, criteria.getLanguage());

            for (Book book : books) {
                // Check if the book has already been weeded
                Optional<BookWeedingStatus> existingWeedingStatus = bookWeedingStatusRepository
                        .findByBookId(book.getId());
                if (existingWeedingStatus.isPresent()
                        &&
                        (book.getStatus().equals(WeedStatus.WEEDED.toString())
                                || book.getStatus().equals(WeedStatus.ARCHIVED.toString()))) {
                    continue; // Skip this book if it's already weeded or archived
                }

                if (shouldBeWeeded(book, criteria)) {
                    flagBook(book, criteria, weed);
                }
            }
        }
    }

    private boolean shouldBeWeeded(Book book, WeedingCriteria criteria) {

        int bookAge = 0;
        try {
            // Assuming the book's published date is in "yyyy-MM-dd" format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate publishedDate = LocalDate.parse(book.getPublishedDate(), formatter);

            // Get the current year and calculate the book's age
            int currentYear = LocalDate.now().getYear();
            bookAge = currentYear - publishedDate.getYear();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle parsing or calculation errors here (e.g., invalid date format)
        }
        Optional<String> bookCondition = bookRepository.getBookConditionByBookId(book.getId());
        boolean conditionCheck = bookCondition.isPresent() && bookCondition.get().toLowerCase()
                .contains(criteria.getConditionThreshold().toLowerCase());
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        long borrowCount = loanRepository.countLoansByBookIdWithinLastYear(book.getId(), oneYearAgo);
        boolean usageCheck = borrowCount < criteria.getUsageThreshold();
        boolean ageCheck = bookAge >= criteria.getMinYearsOld();
        boolean duplication = !criteria.getDuplicationCheck() ||
                (bookRepository.getTotalDuplicatedBooks(
                        book.getTitle(), book.getIsbn10(), book.getIsbn13()) != null &&
                        bookRepository.getTotalDuplicatedBooks(
                                book.getTitle(), book.getIsbn10(), book.getIsbn13()) == 0);

        return conditionCheck && usageCheck && ageCheck && duplication;
    }

    @Override
    public void flagBook(Book book, WeedingCriteria weedingCriteria, BookWeeding bookWeeding) {
        Optional<BookWeedingStatus> existingBookWeedingStatus = bookWeedingStatusRepository.findByBookId(book.getId());

        BookWeedingStatus bookWeedingStatus;
        if (existingBookWeedingStatus.isPresent()) {
            bookWeedingStatus = existingBookWeedingStatus.get();
            if (bookWeedingStatus.getWeedStatus() != WeedStatus.FLAGGED) {
                // Update existing status if not already flagged
                bookWeedingStatus.setWeedStatus(WeedStatus.FLAGGED);
                bookWeedingStatus.setReviewDate(LocalDate.now());
                bookWeedingStatus.setWeedingCriteria(weedingCriteria);
                bookWeedingStatus.setBookWeeding(bookWeeding);
            }
        } else {
            // If no existing weeding status, create a new one
            bookWeedingStatus = new BookWeedingStatus();
            bookWeedingStatus.setBook(book);
            bookWeedingStatus.setWeedingCriteria(weedingCriteria);
            bookWeedingStatus.setWeedStatus(WeedStatus.FLAGGED);
            bookWeedingStatus.setReviewDate(LocalDate.now());
            bookWeedingStatus.setBookWeeding(bookWeeding);
        }
        bookWeedingStatusRepository.save(bookWeedingStatus);
    }

    @Override
    @Transactional
    public void updateBookWeeding(WeedInfoDTO weedInfoDTO) {
        BookWeeding bookWeedToUpdate = bookWeedingRepository.findById(
                weedInfoDTO.getWeedProcessId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book Weeding not found with id: " + weedInfoDTO.getWeedProcessId()));

        bookWeedToUpdate.setEndDate(weedInfoDTO.getProcessEndDate());
        bookWeedToUpdate.setNotes(weedInfoDTO.getProcessNotes());
        bookWeedToUpdate.setStatus(weedInfoDTO.getProcessStatus());

        bookWeedingRepository.save(bookWeedToUpdate);

    }

}
