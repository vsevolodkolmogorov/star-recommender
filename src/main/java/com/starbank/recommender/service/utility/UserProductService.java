package com.starbank.recommender.service.utility;

import com.starbank.recommender.repository.h2.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserProductService {
    private static final Logger logger = LoggerFactory.getLogger(UserProductService.class);
    private final TransactionRepository transactionRepository;

    public UserProductService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Запросы к базе данных

    // # Является пользователем продукта — USER_OF

    @Cacheable(value = "userOfCache", key = "#userId.toString() + '-' + #productType")
    public boolean isUserOfProductType(UUID userId, String productType) {
        logger.info("Database call for userOfProductType with userId: {} and productType: {}", userId, productType);
        return transactionRepository.isUserOfProductType(userId, productType);
    }

    // # Является активным пользователем продукта — ACTIVE_USER_OF.
    // Активный пользователь это пользователь, у которого есть хотя бы пять транзакций по продуктам данного типа X.

    @Cacheable(value = "activeUserOfCache", key = "#userId.toString() + '-' + #productType")
    public boolean isUserActiveOfProductType(UUID userId, String productType) {
        logger.info("Database call for activeUserOfProductType with userId: {} and productType: {}", userId, productType);
        return transactionRepository.isUserActiveOfProductType(userId, productType);
    }

    // # Сравнение суммы транзакций с константой — TRANSACTION_SUM_COMPARE
    // Этот запрос сравнивает сумму всех транзакций типа Y по продуктам типа X с некоторой константой C.

    @Cacheable(value = "transactionSumCompareCache", key = "#userId.toString() + '-' + #productType + '-' + #transactionType + '-' + #comparison + '-' + #amount")
    public boolean compareTransactionSum(UUID userId, String productType, String transactionType, String comparison, int amount) {
        logger.info("Database call for compareTransactionSum with userId: {}, productType: {}", userId, productType);
        return transactionRepository.compareTransactionSum(userId, productType, transactionType, comparison, amount);
    }

    // # Сравнение суммы пополнений с тратами по всем продуктам одного типа - TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW
    // Этот запрос сравнивает сумму всех транзакций типа DEPOSIT с суммой всех транзакций типа WITHDRAW по продукту X.


    @Cacheable(value = "transactionSumCompareDepositWithdrawCache", key = "#userId.toString() + '-' + #productType + '-' + #comparison")
    public boolean compareDepositWithdrawSum(UUID userId, String productType, String comparison) {
        logger.info("Database call for compareDepositWithdrawSum with userId: {}, productType: {}", userId, productType);
        return transactionRepository.compareDepositWithdrawSum(userId, productType, comparison);
    }
}
