package com.university.librarymanagementsystem.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "users")
@Data
public class User {

    @Id
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "middle_name", nullable = false)
    private String middleName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "suffix", nullable = false)
    private String suffix;

    @Column(name = "contact_number", nullable = false)
    private String contactNo;
    @Column(name = "email_address", nullable = false)
    private String emailAdd;
    // 0 inactive - 1 active
    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "department", nullable = false)
    private String department;
    @Column(name = "program", nullable = false)
    private String program;
}
