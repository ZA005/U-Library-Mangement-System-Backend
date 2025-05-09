package com.university.librarymanagementsystem.service.circulation;

import java.util.List;

import com.university.librarymanagementsystem.dto.circulation.TransactionDTO;

public interface TransactionService {
    List<TransactionDTO> getTransactionHistory();

    List<TransactionDTO> getTransactionHistoryByUserId(String userId);

    List<TransactionDTO> getTransactionHistoryByFilter(String filter);
}
