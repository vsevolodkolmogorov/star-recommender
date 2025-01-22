package com.starbank.recommender.repository.jpa;

import com.starbank.recommender.model.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> { }
