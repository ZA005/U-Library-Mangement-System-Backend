package com.university.librarymanagementsystem.service.impl.circulation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.circulation.TransactionDTO;
import com.university.librarymanagementsystem.entity.circulation.TransactionHistory;
import com.university.librarymanagementsystem.mapper.circulation.TransactionMapper;
import com.university.librarymanagementsystem.repository.circulation.TransactionRepository;
import com.university.librarymanagementsystem.service.circulation.TransactionService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionImpl implements TransactionService {

    private TransactionRepository transactionRepo;

    @Override
    public List<TransactionDTO> getTransactionHistory() {
        List<TransactionHistory> transactionHistory = transactionRepo.findAllByOrderByIdDesc();

        return transactionHistory.stream().map((transactions) -> TransactionMapper.mapToTransactionDTO(transactions))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionHistoryByUserId(String userId) {
        List<TransactionHistory> transactionHistory = transactionRepo.findAllByUserId(userId);

        return transactionHistory.stream().map((transactions) -> TransactionMapper.mapToTransactionDTO(transactions))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionHistoryByFilter(String filter) {
        List<TransactionHistory> transactionHistory = transactionRepo.findAllByType(filter);

        return transactionHistory.stream().map((transactions) -> TransactionMapper.mapToTransactionDTO(transactions))
                .collect(Collectors.toList());
    }
}
