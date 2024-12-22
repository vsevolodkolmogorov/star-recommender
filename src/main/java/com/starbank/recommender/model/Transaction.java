package com.starbank.recommender.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class Transaction {
    private UUID id;
    private Product product;
    private User user;
    private String type;
    private int amount;
}