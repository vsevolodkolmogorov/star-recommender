package com.starbank.recommender.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class Recommendation {
    private String name;
    private UUID id;
    private String text;
}