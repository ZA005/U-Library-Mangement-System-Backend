package com.university.librarymanagementsystem.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;

    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String role;

    private String contactNo;
    private String emailAdd;

    private int status;

    private String department;
    private String program;
}
