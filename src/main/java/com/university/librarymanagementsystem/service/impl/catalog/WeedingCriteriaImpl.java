package com.university.librarymanagementsystem.service.impl.catalog;

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
    public void deleteCriteria(int id) {
        weedingCriteriaRepository.deleteById(id);
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
