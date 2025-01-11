package com.starbank.recommender.repository;

import com.starbank.recommender.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RuleRepository extends JpaRepository<Rule, UUID> { }
