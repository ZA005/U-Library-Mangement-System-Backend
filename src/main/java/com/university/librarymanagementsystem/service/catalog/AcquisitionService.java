package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.AcquisitionDTO;

public interface AcquisitionService {
    List<AcquisitionDTO> addRecords(List<AcquisitionDTO> acquisitionDTOs);

    List<AcquisitionDTO> getPendingCatalogRecords();

    boolean updateStatus(Integer id);
}
