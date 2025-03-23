package com.university.librarymanagementsystem.entity.circulation;

import java.time.LocalDateTime;
import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.university.librarymanagementsystem.entity.user.Account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name = "overdue")
public class Overdue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private Account account;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;

    @Column(name = "total_hours_overdue")
    private long totalHoursOverdue;

    @Column(name = "total_days_overdue")
    private long totalDaysOverdue;

    @PrePersist
    @PreUpdate
    public void calculateOverdueDuration() {
        LocalDateTime referenceTime = (returnedDate != null) ? returnedDate : LocalDateTime.now();

        if (referenceTime.isAfter(dueDate)) {
            Duration overdueDuration = Duration.between(dueDate, referenceTime);
            this.totalHoursOverdue = overdueDuration.toHours();
            this.totalDaysOverdue = overdueDuration.toDays();
        } else {
            this.totalHoursOverdue = 0;
            this.totalDaysOverdue = 0;
        }
    }

}
