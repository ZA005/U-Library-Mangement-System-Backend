package com.university.librarymanagementsystem.entity.user;

import com.university.librarymanagementsystem.entity.curriculum.Program;

import java.io.Serializable;

import com.university.librarymanagementsystem.entity.curriculum.Department;

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
@Table(name = "stakeholders")
@Data
public class StakeHolders {

    @Id
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name", nullable = true)
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "suffix", nullable = true)
    private String suffix;

    @ManyToOne
    @JoinColumn(name = "departments", referencedColumnName = "dept_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "programs", referencedColumnName = "program_id")
    private Program program;

    @Column(name = "contact_num", nullable = false)
    private String contactNum;

    @Column(name = "email_add", nullable = false)
    private String emailAdd;

    @Column(name = "status", nullable = false)
    private String status;

}
