package com.starbank.recommender;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class StarRecommenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarRecommenderApplication.class, args);
	}

}
