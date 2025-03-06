package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.AcquisitionDTO;
import com.university.librarymanagementsystem.entity.catalog.Acquisition;

@Component
public class AcquisitionMapper {
    public static AcquisitionDTO mapToAcquisitionDTO(Acquisition acquisition) {
        return new AcquisitionDTO(
                acquisition.getId(),
                acquisition.getBook_title(),
                acquisition.getIsbn(),
                acquisition.getPublisher(),
                acquisition.getEdition(),
                acquisition.getPublished_date(),
                acquisition.getPurchase_price(),
                acquisition.getPurchase_date(),
                acquisition.getAcquired_date(),
                acquisition.getVendor(),
                acquisition.getVendor_location(),
                acquisition.getFunding_source(),
                acquisition.getStatus());

    }

    public static Acquisition mapToAcquisition(AcquisitionDTO acquisitionDTO) {
        return new Acquisition(
                acquisitionDTO.getId(),
                acquisitionDTO.getBook_title(),
                acquisitionDTO.getIsbn(),
                acquisitionDTO.getPublisher(),
                acquisitionDTO.getEdition(),
                acquisitionDTO.getPublished_date(),
                acquisitionDTO.getPurchase_price(),
                acquisitionDTO.getPurchase_date(),
                acquisitionDTO.getAcquired_date(),
                acquisitionDTO.getVendor(),
                acquisitionDTO.getVendor_location(),
                acquisitionDTO.getFunding_source(),
                acquisitionDTO.getStatus());
    }
}
