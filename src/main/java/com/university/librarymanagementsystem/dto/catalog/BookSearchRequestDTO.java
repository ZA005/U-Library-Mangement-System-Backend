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
public class BookSearchRequestDTO {

    private List<SearchCriterion> criteria;
    private String yearRange;
    private String language;
    private Boolean isAvailableOnly;
    private String library;
    private String sortOrder;
    private List<String> sections;
    private List<String> collection;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchCriterion {
        private String idx;
        private String searchTerm;
        private String operator;
    }

}
