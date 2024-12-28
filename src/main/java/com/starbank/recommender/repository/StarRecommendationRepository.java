package com.starbank.recommender.repository;

import com.starbank.recommender.model.RecommendationRule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface StarRecommendationRepository extends JpaRepository<RecommendationRule, UUID> {
    @Query("SELECT r FROM RecommendationRule r LEFT JOIN FETCH r.rules")
    @EntityGraph(attributePaths = "rules")
    List<RecommendationRule> findAllWithRules();
}
