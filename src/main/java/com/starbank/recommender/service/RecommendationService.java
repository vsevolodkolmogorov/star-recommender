package com.starbank.recommender.service;

import com.starbank.recommender.model.DynamicRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.starbank.recommender.dto.UserRecommendationSet;
import com.starbank.recommender.exception.UserNotFoundException;
import com.starbank.recommender.repository.h2.UserRepository;
import com.starbank.recommender.service.utility.RecommendationRuleSet;


import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {
    private final UserRepository userRepository;
    private final DynamicRuleService dynamicRuleService;
    private final RecommendationRuleSet invest500;
    private final RecommendationRuleSet simpleCredit;
    private final RecommendationRuleSet topSaving;
    private final RecommendationRuleSet universal;

    private final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    public RecommendationService(UserRepository userRepository,
                                 DynamicRuleService dynamicRuleService,
                                 @Qualifier("topSaving") RecommendationRuleSet topSaving,
                                 @Qualifier("simpleCredit") RecommendationRuleSet simpleCredit,
                                 @Qualifier("invest500") RecommendationRuleSet invest500,
                                 @Qualifier("universal") RecommendationRuleSet universal){
        this.userRepository = userRepository;
        this.dynamicRuleService = dynamicRuleService;
        this.invest500 = invest500;
        this.simpleCredit = simpleCredit;
        this.topSaving = topSaving;
        this.universal = universal;
    }

    @Cacheable()
    public UserRecommendationSet checkRecommendation(UUID userId) {
        logger.info("Invoke method checkRecommendation");
        validateUserId(userId);
        UserRecommendationSet userRecommendationSet = new UserRecommendationSet(userId);

        List<DynamicRule> dynamicRuleList = dynamicRuleService.getAllDynamicRules();

        universal.validateRecommendationRule(userId).ifPresent(recommendation -> {
            logger.debug("iniversal recommendation: {}", recommendation);
            userRecommendationSet.addRecommendation(recommendation);
        });

        // TODO: ЛОГИКА РАБОТЫ С POSTGRESQL

//        invest500.validateRecommendationRule(userId).ifPresent(recommendation -> {
//            logger.debug("Invest500 recommendation: {}", recommendation);
//            userRecommendationSet.addRecommendation(recommendation);
//        });
//        simpleCredit.validateRecommendationRule(userId).ifPresent(recommendation -> {
//            logger.debug("SimpleCredit recommendation: {}", recommendation);
//            userRecommendationSet.addRecommendation(recommendation);
//        });
//        topSaving.validateRecommendationRule(userId).ifPresent(recommendation -> {
//            logger.debug("TopSaving recommendation: {}", recommendation);
//            userRecommendationSet.addRecommendation(recommendation);
//        });

        /////////////////////////////

        // TODO: правильно прописать валидаци
//        ruleService.getAllRules().forEach(rule -> ruleService.validateRule(rule, userId)
//                .ifPresent(userRecommendationSet::addRecommendation));

        return userRecommendationSet;
    }

    private void validateUserId(UUID userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}