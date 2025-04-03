package com.university.librarymanagementsystem.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SRUFormData {

    private String keyword;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String lccn;
}
