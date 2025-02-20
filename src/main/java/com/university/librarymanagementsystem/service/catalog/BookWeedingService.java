package com.university.librarymanagementsystem.service.catalog;

import com.university.librarymanagementsystem.dto.catalog.BookWeedingDTO;
import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.entity.catalog.Book;
import com.university.librarymanagementsystem.entity.catalog.BookWeeding;
import com.university.librarymanagementsystem.entity.catalog.WeedingCriteria;

public interface BookWeedingService {

    void flagBooksForWeeding(Long processId);

    void annualWeedingFlagging();

    void manualWeedingFlagging();

    void flagBook(Book book, WeedingCriteria weedingCriteria, BookWeeding bookWeeding);

    void updateBookWeeding(WeedInfoDTO weedInfoDTO);
}
