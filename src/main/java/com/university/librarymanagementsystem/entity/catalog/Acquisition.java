package com.university.librarymanagementsystem.entity.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "acquisition")
@Data
public class Acquisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "book_title", nullable = false)
    private String book_title;

    @Column(name = "isbn", nullable = false)
    private String ISBN;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "edition", nullable = false)
    private String edition;

    @Column(name = "published_date", nullable = false)
    private String published_date;

    @Column(name = "purchase_date", nullable = false)
    private String purchase_date;

    @Column(name = "acquired_date", nullable = false)
    private String acquired_date;

    @Column(name = "vendor", nullable = false)
    private String vendor;

    @Column(name = "vendor_location", nullable = false)
    private String vendor_location;

    @Column(name = "funding_source", nullable = false)
    private String funding_source;

    @Column(name = "status", nullable = false)
    private int status;
}
