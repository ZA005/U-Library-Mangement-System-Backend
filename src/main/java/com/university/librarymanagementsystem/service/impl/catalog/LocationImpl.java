package com.university.librarymanagementsystem.service.impl.catalog;

import java.util.List;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.LocationDTO;
import com.university.librarymanagementsystem.entity.catalog.Location;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.LocationMapper;
import com.university.librarymanagementsystem.repository.catalog.LocationRepository;
import com.university.librarymanagementsystem.service.catalog.LocationService;

@Service
public class LocationImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<LocationDTO> fetchAllLibraryLocations() {
        return locationRepository.findAll().stream().map(LocationMapper::mapToLocationDTO).toList();
    }

    @Override
    public LocationDTO addLocation(LocationDTO locDTO) {
        Location loc = LocationMapper.mapToLocationEntity(locDTO);

        loc = locationRepository.save(loc);
        return LocationMapper.mapToLocationDTO(loc);

    }

    @Override
    public boolean deleteLocation(Integer id) {
        locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        locationRepository.deleteById(id);
        return true;
    }

    @Override
    public Location updateLocationStatus(int id, boolean status) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        location.setStatus(status);
        return locationRepository.save(location);
    }

}
