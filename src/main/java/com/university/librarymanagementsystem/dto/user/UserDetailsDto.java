package com.university.librarymanagementsystem.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {

    private String id;
    private String fullName;
    private String departmentId;
    private String departmentName;
    private int programId;
    private String programName;
    private String emailAdd;
    private String contactNum;
    private String role;
}
