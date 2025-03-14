package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.SectionDTO;
import com.university.librarymanagementsystem.entity.catalog.Section;

@Component
public class SectionMapper {

    public static SectionDTO mapToSectionDTO(Section section) {
        return new SectionDTO(
                section.getId(),
                section.getSectionName(),
                section.getStatus(),
                LocationMapper.mapToLocationDTO(section.getLocation()));
    }

    public static Section mapToSectionEntity(SectionDTO sectionDTO) {
        Section section = new Section();
        section.setId(sectionDTO.getId());
        section.setSectionName(sectionDTO.getSectionName());
        section.setStatus(sectionDTO.getStatus());
        section.setLocation(LocationMapper.mapToLocationEntity(sectionDTO.getLocation()));
        return section;

    }
}
