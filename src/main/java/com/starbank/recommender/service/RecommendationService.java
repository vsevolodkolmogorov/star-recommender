package com.starbank.recommender.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.starbank.recommender.dto.UserRecommendationSet;
import com.starbank.recommender.exception.UserNotFoundException;
import com.starbank.recommender.model.Recommendation;
import com.starbank.recommender.repository.RecommendationRepository;
import com.starbank.recommender.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;

    public UserRecommendationSet getUserRecommendationSet(UUID userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        UserRecommendationSet userRecommendationSet = new UserRecommendationSet(userId);
        userRecommendationSet.addRecommendation(new Recommendation());
        return userRecommendationSet;
    }
}