package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.LocationDTO;
import com.university.librarymanagementsystem.entity.catalog.Location;

public interface LocationService {

    List<LocationDTO> getAllLocations();

    LocationDTO addLocation(LocationDTO locDTO);

    void deleteLocation(Integer id);

    Location updateLocationStatus(int id, boolean status);

}
