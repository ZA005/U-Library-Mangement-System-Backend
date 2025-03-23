package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.SectionDTO;
import com.university.librarymanagementsystem.entity.catalog.Section;

public interface SectionService {

    List<SectionDTO> fetchAllSections(Integer locationId);

    SectionDTO addSection(SectionDTO sectionDTO);

    boolean deleteSection(int id);

    Section updateSectionStatus(int id, boolean status);

}
