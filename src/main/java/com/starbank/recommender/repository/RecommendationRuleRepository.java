package com.starbank.recommender.repository;

import com.starbank.recommender.model.RecommendationRule;;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecommendationRuleRepository extends JpaRepository<RecommendationRule, UUID> { }
