package com.starbank.recommender.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
public class Argument {
    @Id
    @GeneratedValue
    private UUID argument_id;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private Rule rule;

    private String text;
}
