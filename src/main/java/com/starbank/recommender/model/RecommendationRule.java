package com.starbank.recommender.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(hidden = true)
    private UUID id;

    private String product_name;
    private UUID product_id;
    private String text;

    @OneToMany(mappedBy = "recommendationRule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Rule> rules = new ArrayList<>();
}
