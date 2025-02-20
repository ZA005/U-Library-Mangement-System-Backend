package com.university.librarymanagementsystem.service.impl.catalog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.SectionDTO;
import com.university.librarymanagementsystem.entity.catalog.Location;
import com.university.librarymanagementsystem.entity.catalog.Section;
import com.university.librarymanagementsystem.mapper.catalog.SectionMapper;
import com.university.librarymanagementsystem.repository.catalog.LocationRepository;
import com.university.librarymanagementsystem.repository.catalog.SectionRepository;
import com.university.librarymanagementsystem.service.catalog.SectionService;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<SectionDTO> getAllSections(Integer locationId) {
        // If locationId is null, you might want to return all sections or handle it
        // differently
        if (locationId == null) {
            return sectionRepository.findAll().stream()
                    .map(SectionMapper::mapToDto)
                    .toList();
        } else {
            return sectionRepository.findByLocationId(locationId).stream()
                    .map(SectionMapper::mapToDto)
                    .toList();
        }
    }

    @Override
    public SectionDTO addSection(SectionDTO sectionDTO) {
        Section sec = SectionMapper.mapToEntity(sectionDTO);
        // Ensure that location is set before saving
        if (sec.getLocation() == null) {
            Location location = locationRepository.findById(sectionDTO.getLocationId())
                    .orElseThrow(
                            () -> new RuntimeException("Location not found with id: " + sectionDTO.getLocationId()));
            sec.setLocation(location);
        }
        if (sectionRepository.findBySectionName(sec.getSectionName()) != null) {
            throw new RuntimeException("Section already exists");
        }
        sec = sectionRepository.save(sec);
        return SectionMapper.mapToDto(sec);
    }

    @Override
    public void deleteSection(Integer id) {
        if (!sectionRepository.existsById(id)) {
            throw new RuntimeException("Section not found");
        }
        sectionRepository.deleteById(id);
    }

}
