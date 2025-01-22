package com.starbank.recommender.service.utility;

import com.starbank.recommender.model.Recommendation;
import com.starbank.recommender.model.DynamicRule;
import com.starbank.recommender.model.Transaction;
import com.starbank.recommender.repository.h2.RecommendationRepository;
import com.starbank.recommender.repository.h2.TransactionRepository;
import com.starbank.recommender.service.DynamicRuleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.starbank.recommender.service.utility.constant.ProductType.DEBIT;

@Component
@Qualifier("universal")
@RequiredArgsConstructor
public class UniversalRuleSet implements RecommendationRuleSet {
    private final RecommendationRepository recommendationRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionListDataUtility utility;
    private final DynamicRuleService dynamicRuleService;

    Logger logger = LoggerFactory.getLogger(TopSavingRuleSet.class);

    public Optional<Recommendation> validateRecommendationRule(UUID userId) {
        logger.info("Invoke method validateRecommendationRule recommendation");
        List<Transaction> transactions = transactionRepository.findAllTransactionByUserId(userId);
        List<DynamicRule> list = dynamicRuleService.getAllDynamicRules();

        // Проверка 1: Пользователь использует как минимум один продукт с типом DEBIT
        boolean checkRule1 = utility.productUsage(transactions, DEBIT);
        logger.debug("Check rule 1: {}", checkRule1);

        if (checkRule1 ) {
            return recommendationRepository.findByName("Top Saving");
        }
        return Optional.empty();
    }
}
