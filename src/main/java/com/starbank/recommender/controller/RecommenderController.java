package com.starbank.recommender.controller;

import com.starbank.recommender.dto.RecommenderDto;
import com.starbank.recommender.service.RecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommenderController {

    @Autowired
    private RecommenderService recommenderService;

    @GetMapping("{id}")
    public Collection<RecommenderDto> deleteStudent(@PathVariable UUID id) {
        return recommenderService.getRecommendations(id);
    }
}
