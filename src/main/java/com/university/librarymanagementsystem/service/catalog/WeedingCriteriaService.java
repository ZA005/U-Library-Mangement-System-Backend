package com.university.librarymanagementsystem.service.catalog;

import com.university.librarymanagementsystem.dto.catalog.WeedingCriteriaDTO;

public interface WeedingCriteriaService {

    WeedingCriteriaDTO createWeedingCriteria(WeedingCriteriaDTO weedingCriteriaDTO);

    WeedingCriteriaDTO updateWeedingCriteria(WeedingCriteriaDTO weedingCriteriaDTO);

    void deleteCriteria(int id);
}
