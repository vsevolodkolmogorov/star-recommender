package com.starbank.recommender.service;

import com.starbank.recommender.dto.DynamicRuleDTO;
import com.starbank.recommender.dto.RuleDTO;
import com.starbank.recommender.model.*;
import com.starbank.recommender.model.RuleStatistic;
import com.starbank.recommender.repository.h2.TransactionRepository;
import com.starbank.recommender.repository.h2.UserRepository;
import com.starbank.recommender.repository.jpa.ArgumentRepository;
import com.starbank.recommender.repository.jpa.DynamicRuleRepository;
import com.starbank.recommender.repository.jpa.RuleRepository;
import com.starbank.recommender.repository.jpa.RuleStatisticRepository;
import com.starbank.recommender.service.utility.UserProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DynamicRuleService {
    private final DynamicRuleRepository dynamicRuleRepository;
    private final RuleRepository ruleRepository;
    private final ArgumentRepository argumentRepository;
    private final UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private final UserProductService userProductService;
    private final RuleStatisticRepository ruleStatisticRepository;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
    }

    private static final Logger logger = LoggerFactory.getLogger(DynamicRuleRepository.class);

    public DynamicRuleService(DynamicRuleRepository dynamicRuleRepository, RuleRepository ruleRepository, ArgumentRepository argumentRepository,
                              UserRepository userRepository, RuleStatisticRepository ruleStatisticRepository, @Lazy UserProductService userProductService) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.ruleRepository = ruleRepository;
        this.argumentRepository = argumentRepository;
        this.userRepository = userRepository;
        this.userProductService = userProductService;
        this.ruleStatisticRepository = ruleStatisticRepository;
    }

    public List<DynamicRule> getAllDynamicRules() {
        logger.info("Was invoked method getAllDynamicRules");
        return dynamicRuleRepository.findAll();
    }

    @Transactional
    public DynamicRule addDynamicRule(DynamicRuleDTO dynamicRuleDTO) {
        DynamicRule dynamicRule = new DynamicRule()
                .setProduct_name(dynamicRuleDTO.getProduct_name())
                .setProduct_id(dynamicRuleDTO.getProduct_id())
                .setProduct_text(dynamicRuleDTO.getProduct_text());

        dynamicRule = dynamicRuleRepository.save(dynamicRule);
        List<Rule> ruleList = new ArrayList<>();

        for (RuleDTO ruleDTO : dynamicRuleDTO.getRule()) {
            Rule rule = new Rule()
                    .setQuery(ruleDTO.getQuery())
                    .setNegate(ruleDTO.getNegate())
                    .setDynamicRule(dynamicRule);

            for (String argumentText : ruleDTO.getArguments()) {
                Argument argument = new Argument()
                        .setText(argumentText)
                        .setRule(rule);

                argumentRepository.save(argument);
            }

            rule = ruleRepository.save(rule);

            ruleList.add(rule);
        }

        dynamicRule.setRule(ruleList);



        clearAllCaches();
        return dynamicRule;
    }

    public void deleteRule(UUID ruleId) {
        logger.info("Was invoked method deleteRule");
        clearAllCaches();
        dynamicRuleRepository.deleteById(ruleId);
    }


    @CacheEvict(value = {"userOfCache", "activeUserOfCache", "transactionSumCompareCache", "transactionSumCompareDepositWithdrawCache"}, allEntries = true)
    public void clearAllCaches() {
    }

    public Optional<Recommendation> validateRule(DynamicRule rule, UUID userId) {
        logger.info("Validating rule for user {}", userId);
        boolean isValid = true;

        for (Rule r : rule.getRule()) {
            boolean ruleResult = switch (r.getQuery()) {
                case "USER_OF" -> userProductService.isUserOfProductType(userId, r.getArguments().get(0)); // Изменено
                case "ACTIVE_USER_OF" ->
                        userProductService.isUserActiveOfProductType(userId, r.getArguments().get(0)); // Изменено
                case "TRANSACTION_SUM_COMPARE" -> userProductService.compareTransactionSum(
                        userId,
                        r.getArguments().get(0),
                        r.getArguments().get(1),
                        r.getArguments().get(2),
                        Integer.parseInt(r.getArguments().get(3))
                );
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> userProductService.compareDepositWithdrawSum(
                        userId,
                        r.getArguments().get(0),
                        r.getArguments().get(1)
                );
                default -> throw new IllegalArgumentException("Unknown rule query: " + r.getQuery());
            };

            if (r.isNegate()) {
                ruleResult = !ruleResult;
            }

            if (!ruleResult) {
                isValid = false;
                break;
            }
        }

        if (isValid) {
            incrementRuleStatistic(rule.getId());
            return Optional.of(new Recommendation()
                    .setName(rule.getProduct_name())
                    .setId(rule.getProduct_id())
                    .setText(rule.getProduct_text()));
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
    @Transactional
    public void incrementRuleStatistic(UUID rule_Id) {
        Optional<RuleStatistic> existingStatOpt = ruleStatisticRepository.findByRule_Rule_Id(rule_Id);
        if (existingStatOpt.isPresent()) {
            RuleStatistic existingStat = existingStatOpt.get();
            existingStat.setTriggerCount(existingStat.getTriggerCount() + 1);
            ruleStatisticRepository.save(existingStat);
        } else {
            Rule rule = ruleRepository.findById(rule_Id)
                    .orElseThrow(() -> new IllegalArgumentException("Rule not founb by ID: " + rule_Id));
            RuleStatistic newStat = new RuleStatistic();
            newStat.setRule(rule);
            newStat.setTriggerCount(1);
            ruleStatisticRepository.save(newStat);
        }
    }

}
