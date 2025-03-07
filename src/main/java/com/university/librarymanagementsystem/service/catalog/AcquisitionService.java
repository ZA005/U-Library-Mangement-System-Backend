package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import org.springframework.data.domain.Page;

import com.university.librarymanagementsystem.dto.catalog.AcquisitionDTO;

public interface AcquisitionService {
    List<AcquisitionDTO> addRecords(List<AcquisitionDTO> acquisitionDTOs);

    Page<AcquisitionDTO> getPendingCatalogRecords(int page, int size);

    boolean updateStatus(Integer id);
}
