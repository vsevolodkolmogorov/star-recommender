package com.starbank.recommender.service.utility;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.starbank.recommender.model.Recommendation;
import com.starbank.recommender.model.Transaction;
import com.starbank.recommender.repository.RecommendationRepository;
import com.starbank.recommender.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.starbank.recommender.service.utility.constant.ProductType.DEBIT;
import static com.starbank.recommender.service.utility.constant.ProductType.SAVING;

@Component
@Qualifier("topSaving")
@RequiredArgsConstructor
public class TopSavingRuleSet implements RecommendationRuleSet {
    private final RecommendationRepository recommendationRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionListDataUtility utility;

    Logger logger = LoggerFactory.getLogger(TopSavingRuleSet.class);

    @Override
    public Optional<Recommendation> validateRecommendationRule(UUID userId) {
        logger.info("Invoke method validateRecommendationRule for 'Top saving' recommendation");
        List<Transaction> transactions = transactionRepository.findAllTransactionByUserId(userId);

        // Проверка 1: Пользователь использует как минимум один продукт с типом DEBIT
        boolean checkRule1 = utility.productUsage(transactions, DEBIT);
        logger.debug("Check rule 1: {}", checkRule1);

        // Проверка 2: Сумма пополнений по всем продуктам типа DEBIT больше или равна 50 000 ₽ ИЛИ Сумма пополнений по всем продуктам типа SAVING больше или равна 50 000 ₽.
        boolean checkRule2 = utility.totalDeposit(transactions, DEBIT) >= 50_000 || utility.totalDeposit(transactions, SAVING) >= 50_000;
        logger.debug("Check rule 2: {}", checkRule2);

        // Проверка 3. Сумма пополнений по всем продуктам типа DEBIT больше, чем сумма трат по всем продуктам типа DEBIT.
        boolean checkRule3 = utility.totalDeposit(transactions, DEBIT) > utility.totalWithdraw(transactions, DEBIT);
        logger.debug("Check rule 3: {}", checkRule3);

        if (checkRule1 && checkRule2 && checkRule3) {
            return recommendationRepository.findByName("Top Saving");
        }
        return Optional.empty();
    }
}