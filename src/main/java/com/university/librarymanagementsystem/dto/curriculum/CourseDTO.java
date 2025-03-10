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
public class CourseDTO {
    private int course_id;

    @JsonProperty("curr_id")
    private String curr_id;
    private int revision_no;

    @JsonProperty("program_id")
    private int program_id;
    private String program_code;
    private String program_description;

    private String course_code;
    private String course_name;
    private int year_level;
    private int sem;
}