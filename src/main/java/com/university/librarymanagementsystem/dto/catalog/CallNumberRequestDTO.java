package com.university.librarymanagementsystem.dto.catalog;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallNumberRequestDTO {

    private String title;
    private String category;
    private List<String> authors;
    private String publishedDate;
}
