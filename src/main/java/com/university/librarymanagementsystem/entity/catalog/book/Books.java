package com.university.librarymanagementsystem.entity.catalog.book;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
@Data
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "book_id", nullable = false)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
    private List<Author> authors = new ArrayList<>();

    @Column(name = "ISBN10", nullable = false)
    private String isbn10;

    @Column(name = "ISBN13", nullable = false)
    private String isbn13;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "pages", nullable = false)
    private int pages;

    @Column(name = "thumbnail", nullable = true)
    private String thumbnail;

    @Column(name = "edition", nullable = true)
    private String edition;

    @Column(name = "series", nullable = true)
    private String series;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "copyright", nullable = false)
    private LocalDate copyRight;

    @Column(name = "print_type", nullable = false)
    private String printType;

    @Column(name = "format", nullable = false)
    private String format;

}
