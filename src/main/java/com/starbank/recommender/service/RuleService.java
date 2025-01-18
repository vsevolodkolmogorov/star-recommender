package com.starbank.recommender.service;

import com.starbank.recommender.dto.RecommendationRuleDTO;
import com.starbank.recommender.dto.RuleDTO;
import com.starbank.recommender.model.Argument;
import com.starbank.recommender.model.Recommendation;
import com.starbank.recommender.model.RecommendationRule;
import com.starbank.recommender.model.Rule;
import com.starbank.recommender.repository.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RuleService {
    private final RecommendationRuleRepository recommendationRuleRepository;

    private final RuleRepository ruleRepository;

    private final ArgumentRepository argumentRepository;

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    private static final Logger logger = LoggerFactory.getLogger(RecommendationRuleRepository.class);

    public RuleService(RecommendationRuleRepository recommendationRuleRepository, RuleRepository ruleRepository, ArgumentRepository argumentRepository,
                       UserRepository userRepository,TransactionRepository transactionRepository) {
        this.recommendationRuleRepository = recommendationRuleRepository;
        this.ruleRepository = ruleRepository;
        this.argumentRepository = argumentRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<RecommendationRule> getAllRules() {
        logger.info("Was invoked method getAllRules");

        // TODO: не выдает список rule, только сведенья о продукте
        return recommendationRuleRepository.findAll();
    }

    @Transactional
    public RecommendationRule addRule(RecommendationRuleDTO recommendationRuleDTO) {
        RecommendationRule recommendationRule = new RecommendationRule()
                .setProduct_name(recommendationRuleDTO.getProduct_name())
                .setProduct_id(recommendationRuleDTO.getProduct_id())
                .setText(recommendationRuleDTO.getProduct_text());

        recommendationRule = recommendationRuleRepository.save(recommendationRule);

        for (RuleDTO ruleDTO : recommendationRuleDTO.getRule()) {
            Rule rule = new Rule()
                    .setQuery(ruleDTO.getQuery())
                    .setNegate(ruleDTO.getNegate())
                    .setRecommendationRule(recommendationRule); // Связываем с RecommendationRule

            rule = ruleRepository.save(rule);

            for (String argumentText : ruleDTO.getArguments()) {
                Argument argument = new Argument()
                        .setText(argumentText)
                        .setRule(rule); // Связываем с правилом

                argumentRepository.save(argument);
            }

            recommendationRule.getRules().add(rule);
        }

        clearAllCaches();
        return recommendationRule;
    }


    public void deleteRule(UUID ruleId) {
        logger.info("Was invoked method deleteRule");
        clearAllCaches();
        recommendationRuleRepository.deleteById(ruleId);
    }
    @Cacheable(value = "userOfCache", key = "#userId.toString() + '-' + #productType")
    public boolean isUserOfProductType(UUID userId, String productType) {
        return transactionRepository.isUserOfProductType(userId, productType);
    }

    @Cacheable(value = "activeUserOfCache", key = "#userId.toString() + '-' + #productType")
    public boolean isUserActiveOfProductType(UUID userId, String productType) {
        return transactionRepository.isUserActiveOfProductType(userId, productType);
    }

    @Cacheable(value = "transactionSumCompareCache", key = "#userId.toString() + '-' + #productType + '-' + #transactionType + '-' + #comparison + '-' + #amount")
    public boolean compareTransactionSum(UUID userId, String productType, String transactionType, String comparison, int amount) {
        return transactionRepository.compareTransactionSum(userId, productType, transactionType, comparison, amount);
    }

    @Cacheable(value = "transactionSumCompareDepositWithdrawCache", key = "#userId.toString() + '-' + #productType + '-' + #comparison")
    public boolean compareDepositWithdrawSum(UUID userId, String productType, String comparison) {
        return transactionRepository.compareDepositWithdrawSum(userId, productType, comparison);
    }

    @CacheEvict(value = {"userOfCache", "activeUserOfCache", "transactionSumCompareCache", "transactionSumCompareDepositWithdrawCache"}, allEntries = true)
    public void clearAllCaches() {
    }

    // TODO: не получается продумать валидацию.
    public Optional<Recommendation> validateRule(RecommendationRule rule, UUID userId) {
        logger.info("Validating rule for user {}", userId);
        boolean isValid = true;

        for (Rule r : rule.getRules()) {
            switch (r.getQuery()) {
                case "USER_OF":
                    if (!transactionRepository.isUserOfProductType(userId, r.getArguments().get(0)) == r.isNegate()) {
                        isValid = false;
                    }
                    break;
                case "ACTIVE_USER_OF":
                    if (!transactionRepository.isUserActiveOfProductType(userId, r.getArguments().get(0)) == r.isNegate()) {
                        isValid = false;
                    }
                    break;
                case "TRANSACTION_SUM_COMPARE":
                    if (!transactionRepository.compareTransactionSum(userId, r.getArguments().get(0), r.getArguments().get(1), r.getArguments().get(2), Integer.parseInt(r.getArguments().get(3))) == r.isNegate()) {
                        isValid = false;
                    }
                    break;
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                    if (!transactionRepository.compareDepositWithdrawSum(userId, r.getArguments().get(0), r.getArguments().get(1)) == r.isNegate()) {
                        isValid = false;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("");
            }
        }

        if (isValid) {
            return Optional.of(new Recommendation()
                    .setName(rule.getProduct_name())
                    .setId(rule.getProduct_id())
                    .setText(rule.getText()));
        } else {
            return Optional.empty();
        }
    }

    private boolean validateUserOf(UUID userId, String productType) {
        return userRepository.isUserOfProductType(userId, productType);
    }

    private boolean validateActiveUserOf(UUID userId, String productType) {
        return userRepository.isUserActiveOfProductType(userId, productType);
    }

    private boolean validateTransactionSumCompare(UUID userId, List<String> arguments) {
        String productType = arguments.get(0);
        String transactionType = arguments.get(1);
        String comparison = arguments.get(2);
        int amount = Integer.parseInt(arguments.get(3));
        return transactionRepository.compareTransactionSum(userId, productType, transactionType, comparison, amount);
    }

    private boolean validateTransactionSumCompareDepositWithdraw(UUID userId, List<String> arguments) {
        String productType = arguments.get(0);
        String comparison = arguments.get(1);
        return transactionRepository.compareDepositWithdrawSum(userId, productType, comparison);
    }

}
