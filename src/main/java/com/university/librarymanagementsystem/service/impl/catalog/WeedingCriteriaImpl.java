package com.university.librarymanagementsystem.service.impl.catalog;

import java.util.List;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.WeedingCriteriaDTO;
import com.university.librarymanagementsystem.entity.catalog.WeedingCriteria;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.WeedingCriteriaMapper;
import com.university.librarymanagementsystem.repository.catalog.WeedingCriteriaRepository;
import com.university.librarymanagementsystem.service.catalog.WeedingCriteriaService;

@Service
public class WeedingCriteriaImpl implements WeedingCriteriaService {

    private final WeedingCriteriaRepository weedingCriteriaRepository;

    public WeedingCriteriaImpl(WeedingCriteriaRepository weedingCriteriaRepository) {
        this.weedingCriteriaRepository = weedingCriteriaRepository;
    }

    @Override
    public WeedingCriteriaDTO createWeedingCriteria(WeedingCriteriaDTO weedingCriteriaDTO) {
        WeedingCriteria wc = WeedingCriteriaMapper.mapToWeedingCriteriaEntity(weedingCriteriaDTO);
        WeedingCriteria savedWeedingCriteria = weedingCriteriaRepository.save(wc);
        return WeedingCriteriaMapper.mapToWeedingCriteriaDTO(savedWeedingCriteria);
    }

    @Override
    public WeedingCriteriaDTO updateWeedingCriteria(WeedingCriteriaDTO weedingCriteriaDTO) {
        WeedingCriteria existingCriteria = weedingCriteriaRepository.findById(
                weedingCriteriaDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Weeding Criteria not found for id: "
                        + weedingCriteriaDTO
                                .getId()));
        updateNonNullFields(existingCriteria, weedingCriteriaDTO);

        WeedingCriteria updateWeedingCriteria = weedingCriteriaRepository.save(existingCriteria);
        return WeedingCriteriaMapper.mapToWeedingCriteriaDTO(updateWeedingCriteria);
    }

    @Override
    public boolean deleteCriteria(int id) {
        try {
            if (!weedingCriteriaRepository.existsById(id)) {
                return false;
            }
            weedingCriteriaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting criteria with id " + id + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<WeedingCriteriaDTO> fetchAllWeedingCriteria() {
        return weedingCriteriaRepository.findAll().stream()
                .map(WeedingCriteriaMapper::mapToWeedingCriteriaDTO)
                .toList();
    }

    @Override
    public WeedingCriteriaDTO fetchWeedingCriteriaById(int id) {
        WeedingCriteria existingCriteria = weedingCriteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Weeding Criteria not found for id: " + id));

        return WeedingCriteriaMapper.mapToWeedingCriteriaDTO(existingCriteria);
    }

    // HELPER METHODS
    private void updateNonNullFields(WeedingCriteria target, WeedingCriteriaDTO source) {
        if (source.getDdcCategory() != null)
            target.setDdcCategory(source.getDdcCategory());
        if (source.getYearsBeforeWeeding() != null)
            target.setYearsBeforeWeeding(source.getYearsBeforeWeeding());
        if (source.getConditionThreshold() != null)
            target.setConditionThreshold(source.getConditionThreshold());
        if (source.getUsageThreshold() != null)
            target.setUsageThreshold(source.getUsageThreshold());
        if (source.getLanguage() != null)
            target.setLanguage(source.getLanguage());
        if (source.getDuplicationCheck() != null)
            target.setDuplicationCheck(source.getDuplicationCheck());
        if (source.getCriteriaStatus() != null)
            target.setCriteriaStatus(source.getCriteriaStatus());
    }

}
