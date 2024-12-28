package com.starbank.recommender.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RuleDTO {
    private String query;
    private List<String> arguments;
    private boolean negate;
}
