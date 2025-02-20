package com.university.librarymanagementsystem.service.impl.catalog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.LocationDTO;
import com.university.librarymanagementsystem.entity.catalog.Location;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.LocationMapper;
import com.university.librarymanagementsystem.repository.catalog.LocationRepository;
import com.university.librarymanagementsystem.service.catalog.LocationService;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(LocationMapper::mapToDto)
                .toList();
    }

    @Override
    public LocationDTO addLocation(LocationDTO locDTO) {
        Location loc = LocationMapper.mapToEntity(locDTO);
        if (locationRepository.findByLocationName(loc.getLocationName()) != null) {
            throw new RuntimeException("Location already exists");
        }
        loc = locationRepository.save(loc);
        return LocationMapper.mapToDto(loc);
    }

    @Override
    public void deleteLocation(Integer id) {
        if (!locationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Location not found for id: " + id);
        }
        locationRepository.deleteById(id);
    }

    @Override
    public Location updateLocationStatus(int id, boolean status) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        location.setStatus(status);
        return locationRepository.save(location);
    }

}
