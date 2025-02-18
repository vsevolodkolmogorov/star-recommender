package com.starbank.recommender.service.utility.constant;

import lombok.Getter;

@Getter
public enum ProductType {
    DEBIT("DEBIT"),
    SAVING("SAVING"),
    CREDIT("CREDIT"),
    INVEST("INVEST");

    private final String type;

    ProductType(String type) {
        this.type = type;
    }
}