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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "argument_id")
    private UUID argumentId;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private Rule rule;

    private String text;
}
