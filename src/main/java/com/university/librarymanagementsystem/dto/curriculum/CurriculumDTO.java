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
public class CurriculumDTO {
    @JsonProperty("curr_id")
    private String id;

    @JsonProperty("program_id")
    private int program_id;
    private String program_code;
    private String program_description;

    private int revision_no;
    private String effectivity_sy;
    private byte status;
}