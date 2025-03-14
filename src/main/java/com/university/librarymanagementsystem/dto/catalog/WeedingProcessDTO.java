package com.university.librarymanagementsystem.dto.catalog;

import com.university.librarymanagementsystem.enums.ProcessStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeedingProcessDTO {

    private int id;
    private String starDate;
    private String endDate;
    private ProcessStatus processStatus;
    private String initiator;
    private String notes;

}
