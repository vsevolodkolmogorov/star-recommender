package com.starbank.recommender.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
public class RecommendationRule {
    @Id
    @GeneratedValue
    private UUID recommendation_id;

    private String product_name;
    private UUID product_id;
    private String text;

    @OneToMany(mappedBy = "recommendationRule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Rule> rules = new ArrayList<>();
}
