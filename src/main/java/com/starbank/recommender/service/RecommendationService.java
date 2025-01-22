package com.starbank.recommender.service;

import com.starbank.recommender.model.DynamicRule;
import com.starbank.recommender.model.Recommendation;
import com.starbank.recommender.model.Rule;
import com.starbank.recommender.model.Transaction;
import com.starbank.recommender.repository.h2.TransactionRepository;
import com.starbank.recommender.repository.jpa.RuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.starbank.recommender.dto.UserRecommendationSet;
import com.starbank.recommender.exception.UserNotFoundException;
import com.starbank.recommender.repository.h2.UserRepository;
import com.starbank.recommender.service.utility.RecommendationRuleSet;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final RuleRepository ruleRepository;
    private final RecommendationRuleSet invest500;
    private final RecommendationRuleSet simpleCredit;
    private final RecommendationRuleSet topSaving;

    private final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    public RecommendationService(UserRepository userRepository,
                                 DynamicRuleService dynamicRuleService,
                                 TransactionRepository transactionRepository,
                                 RuleRepository ruleRepository,
                                 @Qualifier("topSaving") RecommendationRuleSet topSaving,
                                 @Qualifier("simpleCredit") RecommendationRuleSet simpleCredit,
                                 @Qualifier("invest500") RecommendationRuleSet invest500) {
        this.transactionRepository = transactionRepository;
        this.ruleRepository = ruleRepository;
        this.userRepository = userRepository;
        this.invest500 = invest500;
        this.simpleCredit = simpleCredit;
        this.topSaving = topSaving;
    }

    @Cacheable()
    public UserRecommendationSet checkRecommendation(UUID userId) {
        logger.info("Invoke method checkRecommendation");
        validateUserId(userId);
        UserRecommendationSet userRecommendationSet = new UserRecommendationSet(userId);

        for (Rule r: ruleRepository.findAll()) {
            switch (r.getQuery()) {
                case "USER_OF":
                    if (transactionRepository.isUserOfProductType(userId, r.getArguments().get(0)) == r.isNegate()) {
                        Recommendation rec = new Recommendation();
                        rec.setName(r.getDynamicRule().getProduct_name());
                        rec.setText(r.getDynamicRule().getProduct_text());
                        userRecommendationSet.addRecommendation(rec);
                    }
                    break;
                case "ACTIVE_USER_OF":
                    if (transactionRepository.isUserActiveOfProductType(userId, r.getArguments().get(0)) == r.isNegate()) {
                        Recommendation rec = new Recommendation();
                        rec.setName(r.getDynamicRule().getProduct_name());
                        rec.setText(r.getDynamicRule().getProduct_text());
                        userRecommendationSet.addRecommendation(rec);
                    }
                    break;
                case "TRANSACTION_SUM_COMPARE":
                    if (transactionRepository.compareTransactionSum(userId, r.getArguments().get(0), r.getArguments().get(1), r.getArguments().get(2), Integer.parseInt(r.getArguments().get(3))) == r.isNegate()) {
                        Recommendation rec = new Recommendation();
                        rec.setName(r.getDynamicRule().getProduct_name());
                        rec.setText(r.getDynamicRule().getProduct_text());
                        userRecommendationSet.addRecommendation(rec);
                    }
                    break;
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                    if (transactionRepository.compareDepositWithdrawSum(userId, r.getArguments().get(0), r.getArguments().get(1)) == r.isNegate()) {
                        Recommendation rec = new Recommendation();
                        rec.setName(r.getDynamicRule().getProduct_name());
                        rec.setText(r.getDynamicRule().getProduct_text());
                        userRecommendationSet.addRecommendation(rec);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("");
            }
        }


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


        return userRecommendationSet;
    }

    private void validateUserId(UUID userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}