package com.university.librarymanagementsystem.service.impl.catalog;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.BookWeedingStatusDTO;
import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.entity.catalog.BookWeedingStatus;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.BookWeedingStatusMapper;
import com.university.librarymanagementsystem.repository.catalog.BookWeedingStatusRepository;
import com.university.librarymanagementsystem.service.catalog.BookWeedingStatusService;

@Service
public class BookWeedingStatusServiceImpl implements BookWeedingStatusService {

    @Autowired
    private BookWeedingStatusRepository bookWeedingStatusRepository;

    @Override
    public void updateBookWeedingStatus(WeedInfoDTO weedInfoDTO) {
        BookWeedingStatus toUpdateBookWeedStatus = bookWeedingStatusRepository.findById(weedInfoDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Book Weeding Status not found for id: " + weedInfoDTO
                        .getId()));
        toUpdateBookWeedStatus.setNotes(weedInfoDTO.getBookWeedingStatusNotes());
        toUpdateBookWeedStatus.setReviewDate(weedInfoDTO.getReviewDate());
        toUpdateBookWeedStatus.setWeedStatus(weedInfoDTO.getWeedStatus());

        bookWeedingStatusRepository.save(toUpdateBookWeedStatus);
    }

    @Override
    public void deleteBookWeedingStatus(Long id) {
        if (!bookWeedingStatusRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book Weeding Status not found for id: " + id);
        }
        bookWeedingStatusRepository.deleteById(id);
    }

    @Override
    public List<WeedInfoDTO> getAllBookWeedingStatuses() {
        return bookWeedingStatusRepository.findAll().stream()
                .map(BookWeedingStatusMapper::mapToWeedInfoDTO)
                .toList();
    }

    @Override
    public BookWeedingStatusDTO getBookWeedingStatusById(Long id) {
        Optional<BookWeedingStatus> status = bookWeedingStatusRepository.findById(id);
        return status.map(BookWeedingStatusMapper::mapToDTO).orElse(null);
    }

}
