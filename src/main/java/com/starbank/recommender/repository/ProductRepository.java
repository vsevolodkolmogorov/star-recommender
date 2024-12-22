package com.starbank.recommender.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.starbank.recommender.mapper.ProductMapper;
import com.starbank.recommender.model.Product;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ProductMapper mapper;

    public Product findByProductId(UUID id) {
        return jdbcTemplate.queryForObject("SELECT * FROM PRODUCTS WHERE ID = ?", mapper, id);
    }
}