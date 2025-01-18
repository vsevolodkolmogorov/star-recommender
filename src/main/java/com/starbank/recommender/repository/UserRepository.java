package com.starbank.recommender.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.starbank.recommender.mapper.UserMapper;
import com.starbank.recommender.model.User;

import java.util.Optional;
import java.util.UUID;

import static com.starbank.recommender.repository.constant.SQLQuery.FIND_USER_BY_ID;

@Repository
public class UserRepository {
    private final JdbcTemplate transactionDataSource;
    private final UserMapper mapper;

    Logger logger= LoggerFactory.getLogger(UserRepository.class);

    public UserRepository(@Qualifier("transactionJdbcTemplate") JdbcTemplate transactionDataSource,
                          UserMapper mapper) {
        this.transactionDataSource = transactionDataSource;
        this.mapper = mapper;
    }

    public Optional<User> findById(UUID id) {
        try {
            User user = transactionDataSource.queryForObject(FIND_USER_BY_ID, mapper, id);
            logger.info("User found: {}", user);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            logger.info("User with id '{}' not found", id);
            return Optional.empty();
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
}