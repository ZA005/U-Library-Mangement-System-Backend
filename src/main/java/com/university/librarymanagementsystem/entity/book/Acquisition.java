package com.university.librarymanagementsystem.entity.book;

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

    private String book_title;

    private String ISBN;

    private String publisher;

    private String edition;

    private String published_date;

    private String purchase_date;

    private String acquired_date;

    private String vendor;

    private String vendor_location;

    private String funding_source;

    private int status;
}
