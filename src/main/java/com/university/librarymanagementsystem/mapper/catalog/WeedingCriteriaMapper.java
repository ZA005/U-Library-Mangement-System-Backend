package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.WeedingCriteriaDTO;
import com.university.librarymanagementsystem.entity.catalog.WeedingCriteria;

@Component
public class WeedingCriteriaMapper {

    public static WeedingCriteriaDTO mapToWeedingCriteriaDTO(WeedingCriteria wc) {
        return new WeedingCriteriaDTO(
                wc.getId(),
                wc.getDdcCategory(),
                wc.getYearsBeforeWeeding(),
                wc.getConditionThreshold(),
                wc.getUsageThreshold(),
                wc.getLanguage(),
                wc.getDuplicationCheck(),
                wc.getCriteriaStatus());
    }

    public static WeedingCriteria mapToWeedingCriteriaEntity(WeedingCriteriaDTO wcDTO) {
        return new WeedingCriteria(
                wcDTO.getId(),
                wcDTO.getDdcCategory(),
                wcDTO.getYearsBeforeWeeding(),
                wcDTO.getConditionThreshold(),
                wcDTO.getUsageThreshold(),
                wcDTO.getLanguage(),
                wcDTO.getDuplicationCheck(),
                wcDTO.getCriteriaStatus());
    }
}
