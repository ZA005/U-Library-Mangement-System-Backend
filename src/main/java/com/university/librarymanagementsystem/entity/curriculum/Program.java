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
@Table(name = "programs")
@Data
public class Program {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int program_id;

    @ManyToOne
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;

    @Column(name = "prog_code", nullable = false)
    private String code;
    @Column(name = "prog_desc", nullable = false)
    private String description;
    @Column(name = "status", nullable = false)
    private byte status;
}