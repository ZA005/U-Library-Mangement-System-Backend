package com.university.librarymanagementsystem.service.impl.catalog;

import java.util.List;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.entity.catalog.WeedingStatus;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.WeedingStatusMapper;
import com.university.librarymanagementsystem.repository.catalog.WeedingStatusRepository;
import com.university.librarymanagementsystem.service.catalog.WeedingStatusService;

@Service
public class WeedingStatusImpl implements WeedingStatusService {

    private final WeedingStatusRepository weedingStatusRepository;

    public WeedingStatusImpl(WeedingStatusRepository weedingStatusRepository) {
        this.weedingStatusRepository = weedingStatusRepository;
    }

    @Override
    public List<WeedInfoDTO> fetchAllWeedingStatus() {
        return weedingStatusRepository.findAll().stream().map(WeedingStatusMapper::mapToWeedInfoDTO)
                .toList();
    }

    @Override
    public void updateWeedingStatus(WeedInfoDTO weedInfoDTO) {
        WeedingStatus toUpdateWeedStatus = weedingStatusRepository.findById(weedInfoDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Book Weeding Status not found for id: "
                        + weedInfoDTO.getId()));

        toUpdateWeedStatus.setNotes(weedInfoDTO.getBookWeedingStatusNotes());
        toUpdateWeedStatus.setReviewDate(weedInfoDTO.getReviewDate());
        toUpdateWeedStatus.setWeedStatus(weedInfoDTO.getWeedStatus());

        weedingStatusRepository.save(toUpdateWeedStatus);
    }

}
