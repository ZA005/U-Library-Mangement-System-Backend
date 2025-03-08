package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.WeedingCriteriaDTO;

public interface WeedingCriteriaService {

    WeedingCriteriaDTO createWeedingCriteria(WeedingCriteriaDTO weedingCriteriaDTO);

    WeedingCriteriaDTO updateWeedingCriteria(WeedingCriteriaDTO weedingCriteriaDTO);

    boolean deleteCriteria(int id);

    List<WeedingCriteriaDTO> fetchAllWeedingCriteria();

    WeedingCriteriaDTO fetchWeedingCriteriaById(int id);
}
