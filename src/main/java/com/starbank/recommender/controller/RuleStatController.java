package com.starbank.recommender.controller;

import com.starbank.recommender.model.RuleStatistic;
import com.starbank.recommender.repository.jpa.RuleStatisticRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class RuleStatController {

    private final RuleStatisticRepository ruleStatisticRepository;

    public RuleStatController(RuleStatisticRepository ruleStatisticRepository) {
        this.ruleStatisticRepository = ruleStatisticRepository;
    }

    @GetMapping("/stats")
    public RuleStatsResponse getRuleStatistics() {
        List<RuleStatistic> allStats = ruleStatisticRepository.findAll();

        List<RuleStatDto> stats = allStats.stream()
                .map(stat -> new RuleStatDto(
                        stat.getRule().getRuleId(),
                        stat.getTriggerCount()
                ))
                .collect(Collectors.toList());

        return new RuleStatsResponse(stats);
    }

    private record RuleStatsResponse(List<RuleStatDto> stats) {}

    private record RuleStatDto(UUID rule_id, int count) {}
}
