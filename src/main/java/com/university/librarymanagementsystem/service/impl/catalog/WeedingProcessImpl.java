package com.university.librarymanagementsystem.service.impl.catalog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.dto.catalog.WeedingProcessDTO;
import com.university.librarymanagementsystem.entity.catalog.WeedingCriteria;
import com.university.librarymanagementsystem.entity.catalog.WeedingProcess;
import com.university.librarymanagementsystem.entity.catalog.WeedingStatus;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.enums.BookStatus;
import com.university.librarymanagementsystem.enums.ProcessStatus;
import com.university.librarymanagementsystem.enums.WeedStatus;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.WeedingProcessMapper;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.catalog.WeedingCriteriaRepository;
import com.university.librarymanagementsystem.repository.catalog.WeedingProcessRepository;
import com.university.librarymanagementsystem.repository.catalog.WeedingStatusRepository;
import com.university.librarymanagementsystem.service.catalog.WeedingProcessService;

import jakarta.transaction.Transactional;

@Service
public class WeedingProcessImpl implements WeedingProcessService {

    private final WeedingProcessRepository weedingProcessRepository;
    private final WeedingStatusRepository weedingStatusRepository;
    private final WeedingCriteriaRepository weedingCriteriaRepository;
    private final BookRepository bookRepository;

    public WeedingProcessImpl(WeedingProcessRepository weedingProcessRepository,
            WeedingStatusRepository weedingStatusRepository,
            WeedingCriteriaRepository weedingCriteriaRepository,
            BookRepository bookRepository) {
        this.weedingProcessRepository = weedingProcessRepository;
        this.weedingStatusRepository = weedingStatusRepository;
        this.weedingCriteriaRepository = weedingCriteriaRepository;
        this.bookRepository = bookRepository;

    }

    @Scheduled(cron = "0 0 0 1 1 ?")
    @Transactional
    public void annualWeedingFlagging() {
        initiateWeedingProcess("System");
    }

    @Override
    @Transactional
    public void manualWeedingFlagging(String initiator) {
        initiateWeedingProcess(initiator);
    }

    private void initiateWeedingProcess(String initiator) {
        WeedingProcess weed = new WeedingProcess();
        weed.setStartDate(LocalDate.now().toString());
        weed.setStatus(ProcessStatus.SCHEDULED);
        weed.setInitiator(initiator);
        WeedingProcess savedWeed = weedingProcessRepository.save(weed);
        flagBooksForWeeding(savedWeed.getId());
        // update the process status to in progress
        savedWeed.setStatus(ProcessStatus.IN_PROGRESS);
        weedingProcessRepository.save(savedWeed);

    }

    @Override
    @Transactional
    public void flagBooksForWeeding(int processId) {

        WeedingProcess weed = weedingProcessRepository.findById(processId)
                .orElseThrow(() -> new ResourceNotFoundException("Book Weeding not found with id: " + processId));

        List<WeedingCriteria> criteriaList = weedingCriteriaRepository.findAllByCriteriaStatusTrue();

        for (WeedingCriteria criteria : criteriaList) {
            int ddcStart = Integer.parseInt(criteria.getDdcCategory());
            int ddcEnd = ddcStart + 99;

            List<Books> books = bookRepository.findBooksByLanguageAndCallNumberRange(ddcStart,
                    ddcEnd, criteria.getLanguage());

            for (Books book : books) {
                if (book.getStatus().equals(BookStatus.WEEDED) ||
                        book.getStatus().equals(BookStatus.ARCHIVED) ||
                        book.getStatus().equals(BookStatus.LOST)) {
                    continue;
                }

                if (shouldBeWeeded(book, criteria)) {
                    flagBook(book, criteria, weed);
                }
            }
        }
    }

    // HELPER METHODS
    private boolean shouldBeWeeded(Books book, WeedingCriteria criteria) {
        int bookAge = 0;
        try {
            int currentYear = LocalDate.now().getYear();
            bookAge = currentYear - book.getPublishedDate().getYear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean conditionCheck = book.getCondition().toLowerCase()
                .contains(criteria.getConditionThreshold().toLowerCase());

        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);

        // long borrowCount =
        // loanRepository.countLoansByBookIdWithinLastYear(book.getId(), oneYearAgo);
        // boolean usageCheck = borrowCount < criteria.getUsageThreshold();
        boolean ageCheck = bookAge >= criteria.getYearsBeforeWeeding();
        boolean duplication = criteria.getDuplicationCheck() &&
                (bookRepository.getTotalDuplicatedBooks(book.getIsbn13()) != null &&
                        bookRepository.getTotalDuplicatedBooks(book.getIsbn13()) > 0);

        if (Boolean.FALSE.equals(criteria.getDuplicationCheck())) {
            return conditionCheck && ageCheck; // to be deleted wehn loan repository is done

            // return conditionCheck && ageCheck && usageCheck;
        }

        // return conditionCheck && usageCheck && ageCheck && duplication;
        return conditionCheck && ageCheck && duplication; // to be deleted wehn loan repository is done
    }

    private void flagBook(Books book, WeedingCriteria criteria, WeedingProcess weeding) {
        Optional<WeedingStatus> existingWeedingStatus = weedingStatusRepository.findByBookId(book.getId());

        WeedingStatus weedingStatus;
        if (existingWeedingStatus.isPresent()) {
            weedingStatus = existingWeedingStatus.get();
            if (weedingStatus.getWeedStatus() != WeedStatus.FLAGGED) {
                weedingStatus.setWeedStatus(WeedStatus.FLAGGED);
                weedingStatus.setReviewDate(LocalDate.now());
                weedingStatus.setWeedingCriteria(criteria);
                weedingStatus.setWeedingProcess(weeding);
            }
        } else {
            weedingStatus = new WeedingStatus();
            weedingStatus.setBook(book);
            weedingStatus.setWeedingCriteria(criteria);
            weedingStatus.setWeedStatus(WeedStatus.FLAGGED);
            weedingStatus.setReviewDate(LocalDate.now());
            weedingStatus.setWeedingProcess(weeding);
        }
        weedingStatusRepository.save(weedingStatus);
    }

    @Override
    @Transactional
    public WeedingProcessDTO updateWeedingProcess(WeedInfoDTO weedInfoDTO) {
        WeedingProcess weedingProcessToUpdate = weedingProcessRepository.findById(
                weedInfoDTO.getWeedProcessId()).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Weeding process not found with id: " + weedInfoDTO.getWeedProcessId()));

        weedingProcessToUpdate.setEndDate(weedInfoDTO.getProcessEndDate());
        weedingProcessToUpdate.setNotes(weedInfoDTO.getProcessNotes());
        weedingProcessToUpdate.setStatus(weedInfoDTO.getProcessStatus());

        WeedingProcess savedWeedingProcess;
        savedWeedingProcess = weedingProcessRepository.save(weedingProcessToUpdate);

        return WeedingProcessMapper.mapToWeedingProcessDTO(savedWeedingProcess);
    }

}
