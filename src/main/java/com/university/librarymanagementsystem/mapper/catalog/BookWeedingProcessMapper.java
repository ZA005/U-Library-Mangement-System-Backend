package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.WeedingProcessDTO;
import com.university.librarymanagementsystem.entity.catalog.WeedingProcess;

@Component
public class BookWeedingProcessMapper {

    public static WeedingProcessDTO mapToWeedingProcessDTO(WeedingProcess bw) {
        return new WeedingProcessDTO(
                bw.getId(),
                bw.getStartDate(),
                bw.getEndDate(),
                bw.getStatus(),
                bw.getInitiator(),
                bw.getNotes());
    }

    public static WeedingProcess mapToWeedingProcessEntity(WeedingProcessDTO bwDto) {
        WeedingProcess bw = new WeedingProcess();
        bw.setId(bwDto.getId());
        bw.setStartDate(bwDto.getStarDate());
        bw.setEndDate(bwDto.getEndDate());
        bw.setStatus(bwDto.getProcessStatus());
        bw.setInitiator(bwDto.getInitiator());
        bw.setNotes(bwDto.getNotes());
        return bw;
    }
}
