package com.starbank.recommender.controller;

import com.starbank.recommender.model.RecommendationRule;
import com.starbank.recommender.service.RuleService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping()
    public RecommendationRule addRule(@RequestBody RecommendationRule rule) {
        return ruleService.addRule(rule);
    }

    @DeleteMapping("{ruleId}")
    public void deleteRule(@PathVariable UUID ruleId) {
        ruleService.deleteRule(ruleId);
    }
}
