package com.university.librarymanagementsystem.mapper.catalog;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.dto.catalog.WeedingStatusDTO;
import com.university.librarymanagementsystem.entity.catalog.WeedingStatus;
import com.university.librarymanagementsystem.entity.catalog.book.Author;
import com.university.librarymanagementsystem.enums.WeedStatus;

@Component
public class WeedingStatusMapper {

    public static WeedingStatusDTO mapToWeedingStatusDTO(WeedingStatus status) {
        return new WeedingStatusDTO(
                status.getId(),
                status.getBook().getId(),
                status.getWeedingCriteria().getId(),
                status.getWeedingProcess().getId(),
                status.getWeedStatus().name(),
                status.getReviewDate().toString(),
                status.getNotes());
    }

    public static WeedingStatus mapToWeedingStatusEntity(WeedingStatusDTO statusDTO) {
        WeedingStatus status = new WeedingStatus();
        status.setId(statusDTO.getId());
        status.getBook().setId(statusDTO.getBookId());
        status.getWeedingCriteria().setId(statusDTO.getWeedingCriteriaId());
        status.getWeedingProcess().setId(statusDTO.getWeedingProcessId());
        status.setWeedStatus(WeedStatus.valueOf(statusDTO.getWeedStatus()));
        status.setReviewDate(LocalDate.parse(statusDTO.getReviewDate()));
        status.setNotes(statusDTO.getNotes());
        return status;
    }

    public static WeedInfoDTO mapToWeedInfoDTO(WeedingStatus status) {
        WeedInfoDTO weedInfo = new WeedInfoDTO();
        // BookWeedingStatus
        weedInfo.setId(status.getId());
        weedInfo.setBookWeedingStatusNotes(status.getNotes());
        weedInfo.setReviewDate(status.getReviewDate());
        weedInfo.setWeedStatus(status.getWeedStatus());

        // for book
        weedInfo.setBookId(status.getBook().getId());
        weedInfo.setAccessionNumber(status.getBook().getAccessionNumber());
        weedInfo.setCallNumber(status.getBook().getBookCatalog().getCallNumber());
        weedInfo.setBookTitle(status.getBook().getTitle());
        weedInfo.setAuthors(status.getBook().getAuthors().stream()
                .map(Author::getName)
                .toList());

        // weeding criteria
        weedInfo.setWeedingCriteriaDdc(status.getWeedingCriteria().getDdcCategory());

        // BookWeeding the process
        weedInfo.setWeedProcessId(status.getWeedingProcess().getId());
        weedInfo.setProcessStartDate(status.getWeedingProcess().getStartDate());
        weedInfo.setProcessEndDate(status.getWeedingProcess().getEndDate());
        weedInfo.setProcessInitiator(status.getWeedingProcess().getInitiator());
        weedInfo.setProcessNotes(status.getWeedingProcess().getNotes());
        weedInfo.setProcessStatus(status.getWeedingProcess().getStatus());
        return weedInfo;
    }
}
