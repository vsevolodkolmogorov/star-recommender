package com.starbank.recommender.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.starbank.recommender.mapper.TransactionMapper;
import com.starbank.recommender.model.Transaction;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TransactionMapper mapper;

    public List<Transaction> findAllTransactionByUserId(UUID id) {
        String query = "SELECT t.ID, t.TYPE, t.AMOUNT, u.ID, u.USERNAME, u.FIRST_NAME, u.LAST_NAME, p.ID, P.NAME, P.TYPE FROM TRANSACTIONS t JOIN USERS u ON t.USER_ID = u.ID JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID WHERE u.ID = ?";
        return jdbcTemplate.queryForStream(query, mapper, id)
                .toList();
    }
}