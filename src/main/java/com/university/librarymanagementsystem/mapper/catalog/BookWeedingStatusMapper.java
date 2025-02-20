package com.university.librarymanagementsystem.mapper.catalog;

import com.university.librarymanagementsystem.dto.catalog.BookWeedingStatusDTO;
import java.util.stream.Collectors;
import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.entity.catalog.Author;
import com.university.librarymanagementsystem.entity.catalog.BookWeedingStatus;
import com.university.librarymanagementsystem.enums.WeedStatus;

public class BookWeedingStatusMapper {

    public static BookWeedingStatusDTO mapToDTO(BookWeedingStatus status) {
        return new BookWeedingStatusDTO(
                status.getId(),
                status.getBook().getId(),
                status.getWeedingCriteria().getId(),
                status.getBookWeeding().getId(),
                status.getWeedStatus().name(),
                status.getReviewDate(),
                status.getNotes());
    }

    public static BookWeedingStatus mapToEntity(BookWeedingStatusDTO dto) {
        BookWeedingStatus status = new BookWeedingStatus();
        status.setId(dto.getId());
        status.getBook().setId(dto.getBookId());
        status.getWeedingCriteria().setId(dto.getWeedingCriteriaId());
        status.getBookWeeding().setId(dto.getBookWeedingId());
        status.setWeedStatus(WeedStatus.valueOf(dto.getWeedStatus()));
        status.setReviewDate(dto.getReviewDate());
        status.setNotes(dto.getNotes());
        return status;
    }

    public static WeedInfoDTO mapToWeedInfoDTO(BookWeedingStatus status) {
        WeedInfoDTO weedInfo = new WeedInfoDTO();
        // BookWeedingStatus
        weedInfo.setId(status.getId());
        weedInfo.setBookWeedingStatusNotes(status.getNotes());
        weedInfo.setReviewDate(status.getReviewDate());
        weedInfo.setWeedStatus(status.getWeedStatus());

        // for book
        weedInfo.setBookId(status.getBook().getId());
        weedInfo.setAccessionNo(status.getBook().getAccessionNo());
        weedInfo.setCallNumber(status.getBook().getCallNumber());
        weedInfo.setBookTitle(status.getBook().getTitle());
        weedInfo.setAuthors(status.getBook().getAuthors().stream()
                .map(Author::getName)
                .toList());

        // weeding criteria
        weedInfo.setWeedingCriteriaDdc(status.getWeedingCriteria().getDdc());

        // BookWeeding the process
        weedInfo.setWeedProcessId(status.getBookWeeding().getId());
        weedInfo.setProcessStartDate(status.getBookWeeding().getStartDate());
        weedInfo.setProcessEndDate(status.getBookWeeding().getEndDate());
        weedInfo.setProcessInitiator(status.getBookWeeding().getInitiator());
        weedInfo.setProcessNotes(status.getBookWeeding().getNotes());
        weedInfo.setProcessStatus(status.getBookWeeding().getStatus());
        return weedInfo;
    }

}
