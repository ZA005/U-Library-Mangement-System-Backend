package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;

public interface WeedingStatusService {

    List<WeedInfoDTO> fetchAllWeedingStatus();

    void updateWeedingStatus(WeedInfoDTO weedInfoDTO);

}
