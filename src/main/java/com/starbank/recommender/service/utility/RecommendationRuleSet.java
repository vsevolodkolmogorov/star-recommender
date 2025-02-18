package com.starbank.recommender.service.utility;

import com.starbank.recommender.model.Recommendation;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional <Recommendation> validateRecommendationRule(UUID userId);
}