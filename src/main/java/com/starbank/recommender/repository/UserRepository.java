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
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            logger.info("User with id '{}' not found", id);
            return Optional.empty();
        }
    }
}