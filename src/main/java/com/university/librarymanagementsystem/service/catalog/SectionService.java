package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.SectionDTO;

public interface SectionService {

    List<SectionDTO> getAllSections(Integer locationId);

    SectionDTO addSection(SectionDTO sectionDTO);

    void deleteSection(Integer id);
}
