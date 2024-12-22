package com.starbank.recommender.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.starbank.recommender.mapper.UserMapper;
import com.starbank.recommender.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper mapper;

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE ID = ?", mapper, id));
    }
}