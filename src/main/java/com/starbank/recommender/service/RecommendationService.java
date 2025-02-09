package com.starbank.recommender.service;

import com.starbank.recommender.model.*;
import com.starbank.recommender.repository.jpa.ArgumentRepository;
import com.starbank.recommender.repository.jpa.RuleRepository;
import com.starbank.recommender.service.utility.UserProductService;
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
    private final UserProductService userProductService;
    private final RuleRepository ruleRepository;
    private final ArgumentRepository argumentRepository;
    private final RecommendationRuleSet invest500;
    private final RecommendationRuleSet simpleCredit;
    private final RecommendationRuleSet topSaving;
    private final DynamicRuleService dynamicRuleService;

    private final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    public RecommendationService(UserRepository userRepository,
                                 UserProductService userProductService,
                                 RuleRepository ruleRepository,
                                 ArgumentRepository argumentRepository,
                                 @Qualifier("topSaving") RecommendationRuleSet topSaving,
                                 @Qualifier("simpleCredit") RecommendationRuleSet simpleCredit,
                                 @Qualifier("invest500") RecommendationRuleSet invest500,
                                 DynamicRuleService dynamicRuleService) {
        this.userProductService = userProductService;
        this.ruleRepository = ruleRepository;
        this.userRepository = userRepository;
        this.argumentRepository = argumentRepository;
        this.invest500 = invest500;
        this.simpleCredit = simpleCredit;
        this.topSaving = topSaving;
        this.dynamicRuleService = dynamicRuleService;
    }

    @Cacheable(value = "recommendationCache", key = "#userId.toString()")
    public UserRecommendationSet checkRecommendation(UUID userId) {
        logger.info("Invoke method checkRecommendation");
        validateUserId(userId);
        UserRecommendationSet userRecommendationSet = new UserRecommendationSet(userId);
        Recommendation rec = new Recommendation();
        rec.setId(UUID.randomUUID());

        invest500.validateRecommendationRule(userId).ifPresent(recommendation -> {
            logger.debug("Invest500 recommendation: {}", recommendation);
            userRecommendationSet.addRecommendation(recommendation);
        });
        simpleCredit.validateRecommendationRule(userId).ifPresent(recommendation -> {
            logger.debug("SimpleCredit recommendation: {}", recommendation);
            userRecommendationSet.addRecommendation(recommendation);
        });
        topSaving.validateRecommendationRule(userId).ifPresent(recommendation -> {
            logger.debug("TopSaving recommendation: {}", recommendation);
            userRecommendationSet.addRecommendation(recommendation);
        });

        for (Rule r : ruleRepository.findAll()) {
            List<Argument> argumentList = argumentRepository.findAll().stream()
                    .filter(argument -> r.getRule_id() == argument.getRule().getRule_id())
                    .toList();

            boolean ruleTriggered = false;

            switch (r.getQuery()) {
                case "USER_OF":
                    ruleTriggered = r.getNegate() != userProductService.isUserOfProductType(userId, argumentList.get(0).getText());
                    break;
                case "ACTIVE_USER_OF":
                    ruleTriggered = r.getNegate() != userProductService.isUserActiveOfProductType(userId, argumentList.get(0).getText());
                    break;
                case "TRANSACTION_SUM_COMPARE":
                    ruleTriggered = r.getNegate() != userProductService.compareTransactionSum(
                            userId,
                            argumentList.get(0).getText(),
                            argumentList.get(1).getText(),
                            argumentList.get(2).getText(),
                            Integer.parseInt(argumentList.get(3).getText())
                    );
                    break;
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                    ruleTriggered = r.getNegate() != userProductService.compareDepositWithdrawSum(
                            userId,
                            argumentList.get(0).getText(),
                            argumentList.get(1).getText()
                    );
                    break;
            }

            if (ruleTriggered) {
                dynamicRuleService.incrementRuleStatistic(r.getRule_id());
                rec.setName(r.getDynamicRule().getProduct_name());
                rec.setText(r.getDynamicRule().getProduct_text());
                userRecommendationSet.addRecommendation(rec);
            }
        }

        return userRecommendationSet;
    }

    private void validateUserId(UUID userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}