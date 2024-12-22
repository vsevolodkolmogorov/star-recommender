package com.starbank.recommender.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.starbank.recommender.mapper.TransactionMapper;
import com.starbank.recommender.model.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.starbank.recommender.repository.constant.SQLQuery.FIND_ALL_TRANSACTION_BY_USER_ID;

@Repository
public class TransactionRepository {
    private final JdbcTemplate transactionDataSource;
    private final TransactionMapper mapper;

    public TransactionRepository(@Qualifier("transactionJdbcTemplate") JdbcTemplate transactionDataSource,
                                 TransactionMapper mapper) {
        this.transactionDataSource = transactionDataSource;
        this.mapper = mapper;
    }

    public List<Transaction> findAllTransactionByUserId(UUID id) {
        try {
            return transactionDataSource.queryForStream(FIND_ALL_TRANSACTION_BY_USER_ID, mapper, id)
                    .toList();
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }
}