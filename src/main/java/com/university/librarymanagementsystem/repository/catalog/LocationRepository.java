package com.university.librarymanagementsystem.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.catalog.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

}
