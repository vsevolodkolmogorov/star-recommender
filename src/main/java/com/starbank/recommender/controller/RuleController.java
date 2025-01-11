package com.starbank.recommender.controller;

import com.starbank.recommender.dto.RecommendationRuleDTO;
import com.starbank.recommender.model.RecommendationRule;
import com.starbank.recommender.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("rule")
@RequiredArgsConstructor
public class RuleController {
    private final RuleService ruleService;

    @GetMapping()
    public List<RecommendationRule> getAllRules() {
        return ruleService.getAllRules();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<RecommendationRule> createRecommendation(@RequestBody RecommendationRuleDTO recommendationRuleDTO) {
        RecommendationRule recommendation = ruleService.addRule(recommendationRuleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(recommendation);
    }

    @DeleteMapping("{ruleId}")
    public void deleteRule(@PathVariable UUID ruleId) {
        ruleService.deleteRule(ruleId);
    }
}
