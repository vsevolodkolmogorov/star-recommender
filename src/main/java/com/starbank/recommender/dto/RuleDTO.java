package com.starbank.recommender.dto;

import lombok.Data;

import java.util.List;

@Data
public class RuleDTO {
    private String query;
    private List<String> arguments;
    private Boolean negate;
}
