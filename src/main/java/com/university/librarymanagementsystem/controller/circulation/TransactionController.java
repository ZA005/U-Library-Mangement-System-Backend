package com.university.librarymanagementsystem.controller.circulation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.circulation.TransactionDTO;
import com.university.librarymanagementsystem.service.circulation.TransactionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/public/transactionsHistory")
public class TransactionController {
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransaction() {
        List<TransactionDTO> transactions = transactionService.getTransactionHistory();

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionByUserId(@PathVariable String id) {
        List<TransactionDTO> transactions = transactionService.getTransactionHistoryByUserId(id);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionByType(@PathVariable String type) {
        List<TransactionDTO> transactions = transactionService.getTransactionHistoryByFilter(type);

        return ResponseEntity.ok(transactions);
    }
}
