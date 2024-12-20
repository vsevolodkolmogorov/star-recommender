package com.starbank.recommender.dto;

import java.util.UUID;

public class RecommenderDto {
    private String name;
    private UUID id;
    private String text;

    public RecommenderDto(String name, UUID id, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
