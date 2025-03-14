package com.university.librarymanagementsystem.entity.curriculum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "courses")
@Data
public class Course {

    @Id
    @Column(name = "course_id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "curr_id", nullable = false)
    private Curriculum curriculum;

    @Column(name = "course_code", length = 20)
    private String course_code;

    @Column(name = "course_name", length = 100)
    private String course_name;

    @Column(name = "year_level", nullable = false)
    private int year_level;

    @Column(name = "sem", nullable = false)
    private int sem;
}