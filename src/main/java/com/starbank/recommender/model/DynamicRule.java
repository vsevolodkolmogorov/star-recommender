package com.starbank.recommender.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "dynamic_rule")
public class DynamicRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String product_name;
    private UUID product_id;
    private String product_text;

    @OneToMany(mappedBy = "dynamicRule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Rule> rule;
}
