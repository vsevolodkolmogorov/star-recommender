package com.starbank.recommender.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.starbank.recommender.model.Recommendation;

import java.util.Optional;

import static com.starbank.recommender.repository.constant.SQLQuery.FIND_RECOMMENDATION_BY_NAME;

@Repository
public class RecommendationRepository {
    private final JdbcTemplate recommendationJdbcTemplate;
    private final RowMapper<Recommendation> mapper;

    public RecommendationRepository(@Qualifier("recommendationJdbcTemplate") JdbcTemplate recommendationJdbcTemplate,
                                    RowMapper<Recommendation> mapper) {
        this.recommendationJdbcTemplate = recommendationJdbcTemplate;
        this.mapper = mapper;
    }

    public Optional<Recommendation> findByName(String name) {
        try {
            return Optional.ofNullable(recommendationJdbcTemplate.queryForObject(FIND_RECOMMENDATION_BY_NAME, mapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}