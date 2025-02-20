package com.university.librarymanagementsystem.entity.catalog;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Index;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books", indexes = {
        @Index(name = "idx_book_unique_fields", columnList = "title, isbn10, isbn13")
})
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String accessionNo;

    @ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
    private List<Author> authors = new ArrayList<>();

    private String publisher;
    private String publishedDate;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Integer pageCount;
    private String categories;
    private String language;
    private String isbn10;
    private String isbn13;
    private String thumbnail;
    private String printType;

    private String status;
    private String callNumber;
    private Double purchasePrice;
    private String section;
    private LocalDate dateAcquired;
    private String notes;
    private String location;
    private String vendor;
    private String fundingSource;
    private String subjects;
    private String collectionType;
    private String bookCondition;

}
