package com.university.librarymanagementsystem.dto.circulation;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OverdueDTO {
    private int id;

    private int account_id;
    private String user_id;

    private LocalDateTime dueDate;

    private LocalDateTime returnedDate;

    private long totalHoursOverdue;

    private long totalDaysOverdue;

}
