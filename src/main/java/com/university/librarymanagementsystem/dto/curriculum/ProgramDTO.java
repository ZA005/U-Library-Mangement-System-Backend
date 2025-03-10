package com.university.librarymanagementsystem.dto.curriculum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDTO {
    @JsonProperty("prog_id")
    private int id;

    @JsonProperty("dept_id")
    private String department_id;
    private String department_name;
    private String department_code;

    private String code;
    private String description;
    private byte status;
}