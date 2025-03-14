package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.LocationDTO;
import com.university.librarymanagementsystem.entity.catalog.Location;

@Component
public class LocationMapper {

    public static LocationDTO mapToLocationDTO(Location location) {
        return new LocationDTO(
                location.getId(),
                location.getCodeName(),
                location.getName(),
                location.getStatus());
    }

    public static Location mapToLocationEntity(LocationDTO locationDTO) {
        Location location = new Location();
        location.setId(locationDTO.getId());
        location.setCodeName(locationDTO.getCodeName());
        location.setName(locationDTO.getName());
        location.setStatus(locationDTO.getStatus());
        return location;
    }
}
