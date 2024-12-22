package com.starbank.recommender.service.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.starbank.recommender.model.Transaction;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionListDataUtility {

    public boolean productUsage(List<Transaction> transactions, String productType) {
        return transactions.stream()
                .anyMatch(transaction -> transaction.getProduct().getType().equals(productType));
    }

    public int totalDeposit(List<Transaction> transactions, String productType) {
        return transactions.stream()
                .filter(transaction -> transaction.getProduct().getType().equals(productType))
                .filter(transaction -> transaction.getType().equals("DEPOSIT"))
                .mapToInt(Transaction::getAmount)
                .sum();
    }

    public int totalWithdraw(List<Transaction> transactions, String productType) {
        return transactions.stream()
                .filter(transaction -> transaction.getProduct().getType().equals(productType))
                .filter(transaction -> transaction.getType().equals("WITHDRAW"))
                .mapToInt(Transaction::getAmount)
                .sum();
    }
}