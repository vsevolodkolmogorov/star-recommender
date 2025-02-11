package com.starbank.recommender.repository.jpa;

import com.starbank.recommender.model.Rule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RuleRepository extends JpaRepository<Rule, UUID> {
    @Query("SELECT r FROM Rule r LEFT JOIN FETCH r.arguments")
    List<Rule> findAllWithArguments();

}
