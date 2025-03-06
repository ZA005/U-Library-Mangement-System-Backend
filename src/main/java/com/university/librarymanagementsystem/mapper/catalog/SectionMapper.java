package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.SectionDTO;
import com.university.librarymanagementsystem.entity.catalog.Location;
import com.university.librarymanagementsystem.entity.catalog.Section;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.repository.catalog.LocationRepository;

@Component
public class SectionMapper {

    private static LocationRepository locationRepository;

    public static SectionDTO mapToSectionDTO(Section section) {
        return new SectionDTO(
                section.getId(),
                section.getLocation().getId(),
                section.getSectionName(),
                section.getStatus());
    }

    public static Section mapToSectionEntity(SectionDTO sectionDTO) {
        Section section = new Section();
        section.setId(sectionDTO.getId());
        if (sectionDTO.getLocationId() > 0) {
            Location location = locationRepository.findById(sectionDTO.getLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Location not found with ID: " + sectionDTO.getLocationId()));
            section.setLocation(location);
        }
        section.setSectionName(sectionDTO.getSectionName());
        section.setStatus(sectionDTO.getStatus());
        return section;

    }
}
