package com.university.librarymanagementsystem.entity.catalog;

import java.util.List;

import com.university.librarymanagementsystem.enums.ProcessStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "weeding_process")
public class WeedingProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_date", nullable = false)
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_status", nullable = false)
    private ProcessStatus status;

    @Column(name = "initiator")
    private String initiator; // Who started the process

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "weedingProcess", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeedingStatus> booksInvolved;
}
