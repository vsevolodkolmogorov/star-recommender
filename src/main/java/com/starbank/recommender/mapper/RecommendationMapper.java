package com.starbank.recommender.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.starbank.recommender.model.Recommendation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
@Component
public class RecommendationMapper implements RowMapper <Recommendation> {
    @Override
    public Recommendation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Recommendation()
                .setName(rs.getString("NAME"))
                .setId(rs.getObject("ID", UUID.class))
                .setText(rs.getString("DESCRIPTION"));
    }
}