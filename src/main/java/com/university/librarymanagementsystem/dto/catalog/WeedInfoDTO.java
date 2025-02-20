package com.university.librarymanagementsystem.dto.catalog;

import java.time.LocalDate;
import java.util.List;

import com.university.librarymanagementsystem.enums.ProcessStatus;
import com.university.librarymanagementsystem.enums.WeedStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeedInfoDTO {

    // BookWeedingStatus
    private Long id;
    private String bookWeedingStatusNotes;
    private LocalDate reviewDate;
    private WeedStatus weedStatus;

    // for book
    private Long bookId;
    private String accessionNo;
    private String callNumber;
    private String bookTitle;
    private List<String> authors;

    // weeding criteria
    private String weedingCriteriaDdc;

    // BookWeeding the process
    private Long weedProcessId;
    private LocalDate processStartDate;
    private LocalDate processEndDate;
    private String processInitiator;
    private String processNotes;
    private ProcessStatus processStatus;
}
