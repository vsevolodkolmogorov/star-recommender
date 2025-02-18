package com.starbank.recommender.controller;

import com.starbank.recommender.model.DynamicRule;
import com.starbank.recommender.model.Rule;
import com.starbank.recommender.repository.jpa.DynamicRuleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DynamicRuleControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private DynamicRuleRepository dynamicRuleRepository;

    @Test
    void testGetAllDynamicRules() {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/rule", String.class))
                .isNotNull();
    }

    @Test
    void testCreateDynamicRule() {
        List<String> argList = new ArrayList<>();
        argList.add(">");

        Rule rule = new Rule();
        rule.setRule_id(UUID.randomUUID());
        rule.setQuery("Query");
        rule.setNegate(true);
        rule.setArguments(argList);

        List<Rule> ruleList = new ArrayList<>();
        ruleList.add(rule);

        DynamicRule dynamicRule = new DynamicRule();
        dynamicRule.setId(UUID.randomUUID());
        dynamicRule.setProduct_id(UUID.randomUUID());
        dynamicRule.setProduct_text("text");
        dynamicRule.setProduct_name("product");
        dynamicRule.setRule(ruleList);


        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/rule", dynamicRule, String.class))
                .isNotNull();
    }

    @Test
    void testDeleteDynamicRule() {
        UUID id = UUID.randomUUID();

        ResponseEntity<DynamicRule> response = this.restTemplate.exchange(
                "http://localhost:" + port + "/rule/" + id,
                HttpMethod.DELETE,
                null,
                DynamicRule.class
        );

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}