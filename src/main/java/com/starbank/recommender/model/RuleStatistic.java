package com.starbank.recommender.model;

import jakarta.persistence.*;
import lombok.Data;


import java.util.UUID;

@Data
@Entity
@Table(name = "rule_statistics")
public class RuleStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", referencedColumnName = "rule_id")
    private Rule rule;

    @Column(nullable = false)
    private int triggerCount = 0;

}
