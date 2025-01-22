package com.starbank.recommender.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties("dynamicRule")
public class DynamicRuleDTO {
    private String product_name;
    private UUID product_id;
    private String product_text;
    private List<RuleDTO> rule;
}
