package com.university.librarymanagementsystem.mapper.catalog;

import com.university.librarymanagementsystem.dto.catalog.LocationDTO;
import com.university.librarymanagementsystem.entity.catalog.Location;

public class LocationMapper {

    public static LocationDTO mapToDto(Location location) {
        return new LocationDTO(
                location.getId(),
                location.getLocationCodeName(),
                location.getLocationName(),
                location.getStatus());
    }

    public static Location mapToEntity(LocationDTO locDto) {
        Location loc = new Location();
        loc.setId(locDto.getId());
        loc.setLocationCodeName(locDto.getLocationCodeName());
        loc.setLocationName(locDto.getLocationName());
        loc.setStatus(locDto.getStatus());
        return loc;

    }
}
