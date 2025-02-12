package com.starbank.recommender.service.utility;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.starbank.recommender.model.Recommendation;
import com.starbank.recommender.model.Transaction;
import com.starbank.recommender.repository.h2.RecommendationRepository;
import com.starbank.recommender.repository.h2.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.starbank.recommender.service.utility.constant.ProductType.CREDIT;
import static com.starbank.recommender.service.utility.constant.ProductType.DEBIT;

@Component
@Qualifier("simpleCredit")
@RequiredArgsConstructor
public class SimpleCreditRuleSet implements RecommendationRuleSet {
    private final RecommendationRepository recommendationRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionListDataUtility utility;

    Logger logger = LoggerFactory.getLogger(SimpleCreditRuleSet.class);

    @Override
    public Optional<Recommendation> validateRecommendationRule(UUID userId) {
        logger.info("Invoke method validateRecommendationRule for 'Simple credit' recommendation");
        List<Transaction> transactions = transactionRepository.findAllTransactionByUserId(userId);

        // Проверка 1. Пользователь не использует продукты с типом CREDIT.
        boolean checkRule1 = !utility.productUsage(transactions, CREDIT);
        logger.debug("Check rule 1: {}", checkRule1);

        // Проверка 2. Сумма пополнений по всем продуктам типа DEBIT больше, чем сумма трат по всем продуктам типа DEBIT.
        boolean checkRule2 = utility.totalDeposit(transactions, DEBIT) > utility.totalWithdraw(transactions, DEBIT);
        logger.debug("Check rule 2: {}", checkRule2);

        // Проверка 3. Сумма трат по всем продуктам типа DEBIT больше, чем 100 000 ₽.
        boolean checkRule3 = utility.totalWithdraw(transactions, DEBIT) > 100_000;
        logger.debug("Check rule 3: {}", checkRule3);

        if (checkRule1 && checkRule2 && checkRule3) {
            return recommendationRepository.findByName("Простой кредит");
        }
        return Optional.empty();
    }
}