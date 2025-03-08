package com.university.librarymanagementsystem.service.catalog;

import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.dto.catalog.WeedingProcessDTO;

public interface WeedingProcessService {

    void flagBooksForWeeding(int processId);

    void manualWeedingFlagging(String initiator);

    WeedingProcessDTO updateWeedingProcess(WeedInfoDTO weedInfoDTO);
}
