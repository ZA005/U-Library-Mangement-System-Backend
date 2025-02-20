package com.university.librarymanagementsystem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.AcquisitionDTO;
import com.university.librarymanagementsystem.entity.Acquisition;
import com.university.librarymanagementsystem.mapper.AcquisitionMapper;
import com.university.librarymanagementsystem.repository.AcquisitionRepository;
import com.university.librarymanagementsystem.service.AcquisitionService;
import com.university.librarymanagementsystem.exception.DuplicateEntryException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AcquisitionServiceImpl implements AcquisitionService {

    private AcquisitionRepository acquisitionRepository;

    @Override
    public List<AcquisitionDTO> addRecords(List<AcquisitionDTO> acquisitionDTOs) {

        acquisitionDTOs.forEach(dto -> {
            // Set a default value for status, e.g., 0 (not added) or 1 (added)
            dto.setStatus(0);
        });

        List<Acquisition> acquisitions = acquisitionDTOs.stream()
                .map(AcquisitionMapper::mapToAcquisition)
                .collect(Collectors.toList());

        for (Acquisition acquisition : acquisitions) {
            Acquisition existingAcquisition = acquisitionRepository.findById(acquisition.getId()).orElse(null);

            if (existingAcquisition != null) {
                throw new IllegalStateException(
                        "Duplicate acquisition found with ID: " + acquisition.getId() + ", \nTitle: "
                                + acquisition.getBook_title() + ", \nISBN: " + acquisition.getIsbn()
                                + ". Process stopped.");
            }
        }

        List<Acquisition> savedAcquisitions = acquisitionRepository.saveAll(acquisitions);

        return savedAcquisitions.stream()
                .map(AcquisitionMapper::mapToAcquisitionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AcquisitionDTO> getPendingCatalogRecords() {
        // Fetch the pending records from the repository
        List<Acquisition> pendingRecords = acquisitionRepository.getPendingRecords();

        // Return the list of pending catalog records
        return pendingRecords.stream().map((pendingRecord) -> AcquisitionMapper.mapToAcquisitionDTO(pendingRecord))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // Ensures the update query runs within a transaction
    public boolean updateStatus(Integer id) {
        return acquisitionRepository.updatePendingStatus(id) > 0; // Ensure the update count is > 0 for success
    }

}
