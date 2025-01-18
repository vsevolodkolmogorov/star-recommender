package com.starbank.recommender.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
public class Rule {
    @Id
    @GeneratedValue
    private UUID rule_id;

    private String query;
    private Boolean negate;

    @ElementCollection
    @CollectionTable(name = "argument", joinColumns = @JoinColumn(name = "rule_id"))
    @Column(name = "text")
    private List<String> arguments;

    @ManyToOne
    @JoinColumn(name = "recommendation_id")
    private RecommendationRule recommendationRule;

    public Boolean isNegate() {
        return this.negate;
    }
}
