package com.starbank.recommender.repository.jpa;

import com.starbank.recommender.model.Argument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ArgumentRepository extends JpaRepository<Argument, UUID> { }
