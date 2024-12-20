package com.starbank.recommender.service.interfaces;

import com.starbank.recommender.dto.RecommenderDto;

import java.util.UUID;

public interface RecommendationRuleSet {
    RecommenderDto recommend(UUID userId);
}
