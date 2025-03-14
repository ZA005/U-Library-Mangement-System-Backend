package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.LocationDTO;
import com.university.librarymanagementsystem.entity.catalog.Location;

public interface LocationService {

    List<LocationDTO> fetchAllLibraryLocations();

    LocationDTO addLocation(LocationDTO locDTO);

    boolean deleteLocation(Integer id);

    Location updateLocationStatus(int id, boolean status);

}
