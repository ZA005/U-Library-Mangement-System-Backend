package com.university.librarymanagementsystem.service.impl.catalog;

import java.util.List;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.SectionDTO;
import com.university.librarymanagementsystem.entity.catalog.Location;
import com.university.librarymanagementsystem.entity.catalog.Section;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.SectionMapper;
import com.university.librarymanagementsystem.repository.catalog.LocationRepository;
import com.university.librarymanagementsystem.repository.catalog.SectionRepository;
import com.university.librarymanagementsystem.service.catalog.SectionService;

@Service
public class SectionImpl implements SectionService {

    private final SectionRepository sectionRepository;
    private final LocationRepository locationRepository;

    public SectionImpl(SectionRepository sectionRepository, LocationRepository locationRepository) {
        this.sectionRepository = sectionRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<SectionDTO> fetchAllSections(Integer locationId) {

        if (locationId == null) {
            return sectionRepository.findAll().stream()
                    .map(SectionMapper::mapToSectionDTO)
                    .toList();
        } else {
            return sectionRepository.findByLocationId(locationId).stream()
                    .map(SectionMapper::mapToSectionDTO)
                    .toList();
        }
    }

    @Override
    public SectionDTO addSection(SectionDTO sectionDTO) {
        Section sec = SectionMapper.mapToSectionEntity(sectionDTO);
        // Ensure that location is set before saving
        if (sec.getLocation() == null) {
            Location location = locationRepository.findById(sectionDTO.getLocation().getId())
                    .orElseThrow(
                            () -> new RuntimeException("Location not found with id: " + sectionDTO.getLocation()
                                    .getId()));
            sec.setLocation(location);
        }
        if (sectionRepository.findBySectionNameAndLocation(sec.getSectionName(), sec.getLocation()) != null) {
            throw new RuntimeException(
                    "Section with name " + sec.getSectionName() + " already exists under the specified location");
        }
        sec = sectionRepository.save(sec);
        return SectionMapper.mapToSectionDTO(sec);
    }

    @Override
    public boolean deleteSection(int id) {
        sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + id));
        sectionRepository.deleteById(id);
        return true;
    }

    @Override
    public Section updateSectionStatus(int id, boolean status) {
        Section sectionToUpdate = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + id));
        sectionToUpdate.setStatus(status);
        return sectionRepository.save(sectionToUpdate);
    }

}
