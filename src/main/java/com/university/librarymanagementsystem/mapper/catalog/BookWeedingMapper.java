package com.university.librarymanagementsystem.mapper.catalog;

import com.university.librarymanagementsystem.dto.catalog.BookWeedingDTO;
import com.university.librarymanagementsystem.entity.catalog.BookWeeding;

public class BookWeedingMapper {

    public static BookWeedingDTO maptoDto(BookWeeding bw) {
        return new BookWeedingDTO(
                bw.getId(),
                bw.getStartDate(),
                bw.getEndDate(),
                bw.getStatus(),
                bw.getInitiator(),
                bw.getNotes());
    }

    public static BookWeeding mapToEntity(BookWeedingDTO bwDto) {
        BookWeeding bw = new BookWeeding();
        bw.setId(bwDto.getId());
        bw.setStartDate(bwDto.getStarDate());
        bw.setEndDate(bwDto.getEndDate());
        bw.setStatus(bwDto.getProcessStatus());
        bw.setInitiator(bwDto.getInitiator());
        bw.setNotes(bwDto.getNotes());
        return bw;
    }
}
