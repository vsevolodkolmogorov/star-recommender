package com.starbank.recommender.service;

import com.starbank.recommender.model.*;
import com.starbank.recommender.repository.h2.TransactionRepository;
import com.starbank.recommender.repository.jpa.ArgumentRepository;
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
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final RuleRepository ruleRepository;
    private final ArgumentRepository argumentRepository;
    private final RecommendationRuleSet invest500;
    private final RecommendationRuleSet simpleCredit;
    private final RecommendationRuleSet topSaving;

    private final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    public RecommendationService(UserRepository userRepository,
                                 TransactionRepository transactionRepository,
                                 RuleRepository ruleRepository,
                                 ArgumentRepository argumentRepository,
                                 @Qualifier("topSaving") RecommendationRuleSet topSaving,
                                 @Qualifier("simpleCredit") RecommendationRuleSet simpleCredit,
                                 @Qualifier("invest500") RecommendationRuleSet invest500) {
        this.transactionRepository = transactionRepository;
        this.ruleRepository = ruleRepository;
        this.userRepository = userRepository;
        this.argumentRepository = argumentRepository;
        this.invest500 = invest500;
        this.simpleCredit = simpleCredit;
        this.topSaving = topSaving;
    }

    // @Cacheable()
    public UserRecommendationSet checkRecommendation(UUID userId) {
        logger.info("Invoke method checkRecommendation");
        validateUserId(userId);
        UserRecommendationSet userRecommendationSet = new UserRecommendationSet(userId);
        Recommendation rec = new Recommendation();
        rec.setId(UUID.randomUUID());

        for (Rule r : ruleRepository.findAll()) {
            List<Argument> argumentList = argumentRepository.findAll().stream()
                    .filter(argument -> r.getRule_id() == argument.getRule().getRule_id())
                    .toList();

            switch (r.getQuery()) {
                case "USER_OF":
                    if (r.getNegate() != transactionRepository.isUserOfProductType(userId, argumentList.get(0).getText())) {
                        rec.setName(r.getDynamicRule().getProduct_name());
                        rec.setText(r.getDynamicRule().getProduct_text());
                    }
                    break;
                case "ACTIVE_USER_OF":
                    if (r.getNegate() != transactionRepository.isUserActiveOfProductType(userId, argumentList.get(0).getText())) {
                        rec.setName(r.getDynamicRule().getProduct_name());
                        rec.setText(r.getDynamicRule().getProduct_text());
                    }
                    break;
                case "TRANSACTION_SUM_COMPARE":
                    if (r.getNegate() != transactionRepository.compareTransactionSum(userId,
                            argumentList.get(0).getText(),
                            argumentList.get(1).getText(),
                            argumentList.get(2).getText(),
                            Integer.parseInt(argumentList.get(3).getText()))) {
                        rec.setName(r.getDynamicRule().getProduct_name());
                        rec.setText(r.getDynamicRule().getProduct_text());
                    }
                    break;
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                    if (r.getNegate() != transactionRepository.compareDepositWithdrawSum(userId,
                            argumentList.get(0).getText(),
                            argumentList.get(1).getText())) {
                        rec.setName(r.getDynamicRule().getProduct_name());
                        rec.setText(r.getDynamicRule().getProduct_text());
                    }
                    break;
            }

            if (rec.getText() != null && rec.getName() != null) {
                userRecommendationSet.addRecommendation(rec);
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