package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.WeedingProcessDTO;
import com.university.librarymanagementsystem.entity.catalog.WeedingProcess;

@Component
public class WeedingProcessMapper {

    public static WeedingProcessDTO mapToWeedingProcessDTO(WeedingProcess wProcess) {
        return new WeedingProcessDTO(
                wProcess.getId(),
                wProcess.getStartDate(),
                wProcess.getEndDate(),
                wProcess.getStatus(),
                wProcess.getInitiator(),
                wProcess.getNotes());
    }

    public static WeedingProcess mapToWeedingProcessEntity(WeedingProcessDTO wProcessDTO) {
        return new WeedingProcess(
                wProcessDTO.getId(),
                wProcessDTO.getStarDate(),
                wProcessDTO.getEndDate(),
                wProcessDTO.getProcessStatus(),
                wProcessDTO.getInitiator(),
                wProcessDTO.getNotes(),
                null);
    }

}
