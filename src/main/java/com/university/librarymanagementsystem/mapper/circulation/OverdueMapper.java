package com.university.librarymanagementsystem.mapper.circulation;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.circulation.OverdueDTO;
import com.university.librarymanagementsystem.entity.circulation.Overdue;
import com.university.librarymanagementsystem.entity.user.Account;

@Component
public class OverdueMapper {
    public static OverdueDTO mapToOverdueDTO(Overdue overdue) {
        return new OverdueDTO(
                overdue.getId(),
                overdue.getAccount().getAccount_id(),
                overdue.getAccount().getUsername(),
                overdue.getDueDate(),
                overdue.getReturnedDate(),
                overdue.getTotalHoursOverdue(),
                overdue.getTotalDaysOverdue());
    }

    public static Overdue mapToOverdue(OverdueDTO overdueDTO) {
        Account account = new Account();
        return new Overdue(
                overdueDTO.getId(),
                account,
                overdueDTO.getDueDate(),
                overdueDTO.getReturnedDate(),
                overdueDTO.getTotalHoursOverdue(),
                overdueDTO.getTotalDaysOverdue());
    }
}
