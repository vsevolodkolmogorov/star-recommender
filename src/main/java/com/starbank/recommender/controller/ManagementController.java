package com.starbank.recommender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final BuildProperties buildProperties;

    @Autowired
    public ManagementController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @PostMapping("/clear-caches")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(value = {"userOfCache", "activeUserOfCache", "transactionSumCompareCache", "transactionSumCompareDepositWithdrawCache",
            "recommendationCache"}, allEntries = true)
    public void clearCaches() {
        System.out.println("All cache deleted");

    }

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        return Map.of(
                "name", buildProperties.getName(),
                "version", buildProperties.getVersion()
        );
    }
}
