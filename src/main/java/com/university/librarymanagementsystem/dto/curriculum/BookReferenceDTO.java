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

public class BookReferenceDTO {
    private int id;

    @JsonProperty("course_id")
    private int course_id;
    private String course_name;

    // @JsonProperty("books_id")
    private int book_id;
    private String book_name;
    private String isbn10;
    private String isbn13;
    private String copyright;
    private String language;
    private int status;
}