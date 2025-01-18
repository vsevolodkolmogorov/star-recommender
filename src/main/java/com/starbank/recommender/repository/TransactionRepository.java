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

    // TODO: переработать дальнейшее
    public boolean isUserOfProductType(UUID userId, String productType) {
        String sql = "SELECT COUNT(*) FROM transactions t JOIN products p ON t.product_id = p.id WHERE t.user_id = ? AND p.type = ?";
        Integer count = transactionDataSource.queryForObject(sql, Integer.class, userId.toString(), productType);
        return count != null && count > 0;
    }

    public boolean isUserActiveOfProductType(UUID userId, String productType) {
        String sql = "SELECT COUNT(*) FROM transactions t JOIN products p ON t.product_id = p.id WHERE t.user_id = ? AND p.type = ? GROUP BY t.product_id HAVING COUNT(*) >= 5";
        Integer count = transactionDataSource.queryForObject(sql, Integer.class, userId.toString(), productType);
        return count != null && count > 0;
    }

    public boolean compareTransactionSum(UUID userId, String productType, String transactionType, String comparison, int amount) {
        String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND product_type = ? AND transaction_type = ?";
        Integer sum = transactionDataSource.queryForObject(sql, Integer.class, userId.toString(), productType, transactionType);

        if (sum == null) {
            sum = 0;
        }

        return switch (comparison) {
            case ">" -> sum > amount;
            case "<" -> sum < amount;
            case "=" -> sum.equals(amount);
            case ">=" -> sum >= amount;
            case "<=" -> sum <= amount;
            default -> throw new IllegalArgumentException("");
        };
    }

    public boolean compareDepositWithdrawSum(UUID userId, String productType, String comparison) {
        String depositSql = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND product_type = ? AND transaction_type = 'DEPOSIT'";
        String withdrawSql = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND product_type = ? AND transaction_type = 'WITHDRAW'";

        Integer depositSum = transactionDataSource.queryForObject(depositSql, Integer.class, userId.toString(), productType);
        Integer withdrawSum = transactionDataSource.queryForObject(withdrawSql, Integer.class, userId.toString(), productType);

        if (depositSum == null) {
            depositSum = 0;
        }

        if (withdrawSum == null) {
            withdrawSum = 0;
        }

        return switch (comparison) {
            case ">" -> depositSum > withdrawSum;
            case "<" -> depositSum < withdrawSum;
            case "=" -> depositSum.equals(withdrawSum);
            case ">=" -> depositSum >= withdrawSum;
            case "<=" -> depositSum <= withdrawSum;
            default -> throw new IllegalArgumentException("");
        };
    }

}