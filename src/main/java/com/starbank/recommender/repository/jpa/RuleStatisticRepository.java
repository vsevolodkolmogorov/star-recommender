package com.starbank.recommender.repository.jpa;

import com.starbank.recommender.model.RuleStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuleStatisticRepository extends JpaRepository<RuleStatistic, UUID> {

    @Query("SELECT rs FROM RuleStatistic rs WHERE rs.rule.rule_id = :ruleId")
    Optional<RuleStatistic> findByRule_Rule_Id(@Param("ruleId") UUID ruleId);
}
