package com.university.librarymanagementsystem.repository.catalog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.catalog.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
    Section findBySectionName(String sectionName);

    List<Section> findByLocationId(Integer locationId);
}
