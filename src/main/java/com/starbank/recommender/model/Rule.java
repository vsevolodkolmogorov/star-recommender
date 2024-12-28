package com.starbank.recommender.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private UUID id;

    private String query;
    private Boolean negate;

    @ElementCollection
    @CollectionTable(name = "rule_arguments", joinColumns = @JoinColumn(name = "rule_id"))
    @Column(name = "argument")
    private List<String> arguments;

    @ManyToOne
    @JoinColumn(name = "recommendation_id")
    @JsonIgnore
    private RecommendationRule recommendationRule;
}
