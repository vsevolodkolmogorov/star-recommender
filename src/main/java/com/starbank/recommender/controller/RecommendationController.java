package com.starbank.recommender.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.starbank.recommender.dto.UserRecommendationSet;
import com.starbank.recommender.service.RecommendationService;

import java.util.UUID;

@RestController
@RequestMapping("recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("{userId}")
    public UserRecommendationSet getRecommendations(@PathVariable UUID userId) {
        return recommendationService.checkRecommendation(userId);
    }
}