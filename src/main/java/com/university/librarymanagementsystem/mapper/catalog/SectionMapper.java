package com.university.librarymanagementsystem.mapper.catalog;

import com.university.librarymanagementsystem.dto.catalog.SectionDTO;
import com.university.librarymanagementsystem.entity.catalog.Location;
import com.university.librarymanagementsystem.entity.catalog.Section;

public class SectionMapper {

    public static SectionDTO mapToDto(Section section) {
        return new SectionDTO(
                section.getId(),
                section.getLocation().getId(),
                section.getSectionName());
    }

    public static Section mapToEntity(SectionDTO sectionDTO) {
        Section sec = new Section();
        sec.setId(sectionDTO.getId());

        // Initialize location before setting ID
        Location location = new Location();
        location.setId(sectionDTO.getLocationId());
        sec.setLocation(location);

        sec.setSectionName(sectionDTO.getSectionName());
        return sec;
    }

}
