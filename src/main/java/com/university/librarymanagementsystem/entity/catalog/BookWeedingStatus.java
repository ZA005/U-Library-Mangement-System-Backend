package com.university.librarymanagementsystem.entity.catalog;

import java.time.LocalDate;

import com.university.librarymanagementsystem.enums.WeedStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_weeding_status")
public class BookWeedingStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "weeding_criteria_id", referencedColumnName = "id")
    private WeedingCriteria weedingCriteria;

    @ManyToOne
    @JoinColumn(name = "book_weeding_id", referencedColumnName = "id")
    private BookWeeding bookWeeding;

    @Enumerated(EnumType.STRING)
    @Column(name = "weed_status", nullable = false)
    private WeedStatus weedStatus;

    @Column(name = "review_date")
    private LocalDate reviewDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

}
