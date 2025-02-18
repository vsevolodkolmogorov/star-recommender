package com.starbank.recommender.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.starbank.recommender.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ProductMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Product()
                .setId(rs.getObject("ID", UUID.class))
                .setName(rs.getString("NAME"))
                .setType(rs.getString("TYPE"));
    }
}