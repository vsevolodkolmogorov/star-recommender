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
    private final RuleStatisticRepository ruleStatisticRepository;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
    }

    private static final Logger logger = LoggerFactory.getLogger(DynamicRuleRepository.class);

    public DynamicRuleService(DynamicRuleRepository dynamicRuleRepository, RuleRepository ruleRepository, ArgumentRepository argumentRepository,
                              RuleStatisticRepository ruleStatisticRepository) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.ruleRepository = ruleRepository;
        this.argumentRepository = argumentRepository;
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
