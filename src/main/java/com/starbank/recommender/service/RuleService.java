package com.starbank.recommender.service;

import com.starbank.recommender.dto.RecommendationRuleDTO;
import com.starbank.recommender.dto.RuleDTO;
import com.starbank.recommender.model.Argument;
import com.starbank.recommender.model.RecommendationRule;
import com.starbank.recommender.model.Rule;
import com.starbank.recommender.repository.ArgumentRepository;
import com.starbank.recommender.repository.RecommendationRuleRepository;
import com.starbank.recommender.repository.RuleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RuleService {
    private final RecommendationRuleRepository recommendationRuleRepository;

    private final RuleRepository ruleRepository;

    private final ArgumentRepository argumentRepository;

    private static final Logger logger = LoggerFactory.getLogger(RecommendationRuleRepository.class);

    public RuleService(RecommendationRuleRepository recommendationRuleRepository, RuleRepository ruleRepository, ArgumentRepository argumentRepository) {
        this.recommendationRuleRepository = recommendationRuleRepository;
        this.ruleRepository = ruleRepository;
        this.argumentRepository = argumentRepository;
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

        return recommendationRule;
    }

    public void deleteRule(UUID ruleId) {
        logger.info("Was invoked method deleteRule");
        recommendationRuleRepository.deleteById(ruleId);
    }
}
