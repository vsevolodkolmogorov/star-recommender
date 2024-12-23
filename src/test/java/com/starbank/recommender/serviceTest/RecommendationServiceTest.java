package com.starbank.recommender.serviceTest;

import com.starbank.recommender.dto.UserRecommendationSet;
import com.starbank.recommender.exception.UserNotFoundException;
import com.starbank.recommender.model.Recommendation;
import com.starbank.recommender.model.User;
import com.starbank.recommender.repository.UserRepository;
import com.starbank.recommender.service.RecommendationService;
import com.starbank.recommender.service.utility.RecommendationRuleSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RecommendationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecommendationRuleSet invest500RuleSet;

    @Mock
    private RecommendationRuleSet simpleCreditRuleSet;

    @Mock
    private RecommendationRuleSet topSavingRuleSet;

    @InjectMocks
    private RecommendationService recommendationService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
    }

    @Test
    void RecommendationServiceCheckExistAndInvest500Test() {
        Recommendation invest500Recommendation = new Recommendation()
                .setName("Invest 500")
                .setText("Тестовое описание Invest 500")
                .setId(UUID.randomUUID());

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        when(invest500RuleSet.validateRecommendationRule(userId)).thenReturn(Optional.of(invest500Recommendation));
        UserRecommendationSet result = recommendationService.checkRecommendation(userId);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertThat(result.getRecommendations().contains(invest500Recommendation));
    }

    @Test
    void RecommendationServiceCheckExistAndSimpleCreditTest() {
        Recommendation SimpleCreditRecommendation = new Recommendation()
                .setName("SimpleCredit")
                .setText("Тестовое описание SimpleCredit")
                .setId(UUID.randomUUID());

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        when(invest500RuleSet.validateRecommendationRule(userId)).thenReturn(Optional.of(SimpleCreditRecommendation));
        UserRecommendationSet result = recommendationService.checkRecommendation(userId);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertThat(result.getRecommendations().contains(SimpleCreditRecommendation));
    }

    @Test
    void RecommendationServiceCheckExistAndTopSavingTest() {
        Recommendation TopSavingRecommendation = new Recommendation()
                .setName("TopSaving")
                .setText("Тестовое описание TopSaving")
                .setId(UUID.randomUUID());

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        when(invest500RuleSet.validateRecommendationRule(userId)).thenReturn(Optional.of(TopSavingRecommendation));
        UserRecommendationSet result = recommendationService.checkRecommendation(userId);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertThat(result.getRecommendations().contains(TopSavingRecommendation));
    }

    @Test
    void RecommendationServiceCheckExistAndNonvalidTest() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(invest500RuleSet.validateRecommendationRule(userId)).thenReturn(Optional.empty());
        when(simpleCreditRuleSet.validateRecommendationRule(userId)).thenReturn(Optional.empty());
        when(topSavingRuleSet.validateRecommendationRule(userId)).thenReturn(Optional.empty());
        UserRecommendationSet result = recommendationService.checkRecommendation(userId);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertTrue(result.getRecommendations().isEmpty());
    }
    @Test
    void RecommendationServiceNonexistTest() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> recommendationService.checkRecommendation(userId));
    }
}