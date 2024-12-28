package com.starbank.recommender.service;

import com.starbank.recommender.model.RecommendationRule;
import com.starbank.recommender.repository.StarRecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RuleService {
    private final StarRecommendationRepository ruleRepository;

    private static final Logger logger = LoggerFactory.getLogger(StarRecommendationRepository.class);

    public RuleService(StarRecommendationRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public List<RecommendationRule> getAllRules() {
        logger.info("Was invoked method getAllRules");

        // TODO: не выдает список rule, только сведенья о продукте
        return ruleRepository.findAllWithRules();
    }

    public RecommendationRule addRule(RecommendationRule rule) {
        logger.info("Was invoked method addRule");
        return ruleRepository.save(rule);
    }

    public void deleteRule(UUID ruleId) {
        logger.info("Was invoked method deleteRule");
        ruleRepository.deleteById(ruleId);

    }
}
