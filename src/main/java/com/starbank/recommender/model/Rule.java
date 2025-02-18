package com.starbank.recommender.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "rule")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(hidden = true)
    private UUID rule_id;

    private String query;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "argument", joinColumns = @JoinColumn(name = "rule_id"))
    @Column(name = "text")
    private List<String> arguments;


    @ManyToOne()
    @Schema(hidden = true)
    @JoinColumn(name = "dynamic_rule_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private DynamicRule dynamicRule;

    private Boolean negate;

    public Boolean isNegate() {
        return this.negate;
    }

    public UUID getRuleId() {
        return this.rule_id;
    }
}
