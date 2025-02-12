package com.starbank.recommender.controller;

import com.starbank.recommender.dto.DynamicRuleDTO;
import com.starbank.recommender.model.DynamicRule;
import com.starbank.recommender.service.DynamicRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("rule")
@RequiredArgsConstructor
public class DynamicRuleController {
    private final DynamicRuleService dynamicRuleService;

    @GetMapping()
    public List<DynamicRule> getAllDynamicRules() {
        return dynamicRuleService.getAllDynamicRules();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<DynamicRule> createDynamicRule(@RequestBody DynamicRuleDTO dynamicRuleDTO) {
        DynamicRule rule = dynamicRuleService.addDynamicRule(dynamicRuleDTO);
        return ResponseEntity.status(HttpStatus.OK).body(rule);
    }

    @DeleteMapping("{ruleId}")
    public void deleteDynamicRule(@PathVariable UUID ruleId) {
        dynamicRuleService.deleteRule(ruleId);
    }
}
