package com.university.librarymanagementsystem.entity.catalog;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.university.librarymanagementsystem.entity.catalog.book.Books;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_catalog")
@Data
public class BookCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // @ManyToOne()
    // @JoinColumn(name = "book_id", referencedColumnName = "book_id", nullable =
    // false)
    // @JsonBackReference
    // private Books book;

    @Column(name = "call_number", nullable = false)
    private String callNumber;

    @Column(name = "acquired_date", nullable = false)
    private LocalDate acquiredDate;

    @Column(name = "purchase_price", nullable = false)
    private double purchasePrice;

    @Column(name = "copies", nullable = false)
    private Integer copies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", referencedColumnName = "id", nullable = false)
    private Section section;

    @Column(name = "collection_type", nullable = false)
    private String collectionType;
}
