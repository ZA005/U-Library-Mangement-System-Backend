package com.university.librarymanagementsystem.service.impl.catalog;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.AcquisitionDTO;
import com.university.librarymanagementsystem.entity.catalog.Acquisition;
import com.university.librarymanagementsystem.mapper.catalog.AcquisitionMapper;
import com.university.librarymanagementsystem.repository.catalog.AcquisitionRepository;
import com.university.librarymanagementsystem.service.catalog.AcquisitionService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AcquisitionServiceImpl implements AcquisitionService {
    private AcquisitionRepository repository;

    @Override
    public List<AcquisitionDTO> addRecords(List<AcquisitionDTO> acquisitionDTOs) {

        acquisitionDTOs.forEach(dto -> {
            dto.setStatus(0);
        });

        List<Acquisition> acquisitions = acquisitionDTOs.stream()
                .map(AcquisitionMapper::mapToAcquisitionEntity)
                .collect(Collectors.toList());

        for (Acquisition acquisition : acquisitions) {
            Acquisition existingAcquisition = repository.findById(acquisition.getId()).orElse(null);

            if (existingAcquisition != null) {
                throw new IllegalStateException(
                        "Duplicate acquisition found with ID: " + acquisition.getId() + ", \nTitle: "
                                + acquisition.getBook_title() + ", \nISBN: " + acquisition.getIsbn()
                                + ". Process stopped.");
            }
        }

        List<Acquisition> savedAcquisitions = repository.saveAll(acquisitions);

        return savedAcquisitions.stream()
                .map(AcquisitionMapper::mapToAcquisitionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AcquisitionDTO> getPendingCatalogRecords(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Acquisition> pendingRecords = repository.getPendingRecords(pageable);

        return pendingRecords.map(AcquisitionMapper::mapToAcquisitionDTO);
    }

    @Override
    @Transactional
    public boolean updateStatus(Integer id) {
        return repository.updatePendingStatus(id) > 0;
    }
}
