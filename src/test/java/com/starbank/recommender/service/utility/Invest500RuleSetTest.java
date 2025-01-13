package com.starbank.recommender.service.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.starbank.recommender.model.Recommendation;
import com.starbank.recommender.model.Transaction;
import com.starbank.recommender.repository.RecommendationRepository;
import com.starbank.recommender.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static com.starbank.recommender.service.utility.constant.ProductType.*;
@ExtendWith(MockitoExtension.class)
class Invest500RuleSetTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private RecommendationRepository recommendationRepository;
    @Mock
    private TransactionListDataUtility utility;
    @InjectMocks
    private Invest500RuleSet invest500RuleSet;
    private UUID userId;
    private String recommendationName;
    private Recommendation invest500;
    private List<Transaction> transactions;
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        recommendationName = "Invest 500";
        invest500 = mock(Recommendation.class);
        transactions = List.of(mock(Transaction.class));
    }
    @Test
    void validateRecommendationRule_whenAllRulesFollowed_shouldReturnOptionalWithCorrectRecommendations() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(recommendationRepository.findByName(recommendationName)).thenReturn(Optional.of(invest500));
        when(utility.productUsage(transactions, DEBIT)).thenReturn(true);
        when(utility.productUsage(transactions, INVEST)).thenReturn(false);
        when(utility.totalDeposit(transactions, SAVING)).thenReturn(1500);
        Optional<Recommendation> actualOption = invest500RuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        verify(recommendationRepository).findByName(recommendationName);
        assertThat(actualOption.isPresent()).isTrue();
        assertThat(actualOption.get()).isEqualTo(invest500);
    }
    @Test
    void validateRecommendationRule_whenNotUsageProductTypeDEBIT_shouldReturnOptionalEmpty() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(utility.productUsage(transactions, DEBIT)).thenReturn(false);
        when(utility.productUsage(transactions, INVEST)).thenReturn(false);
        when(utility.totalDeposit(transactions, SAVING)).thenReturn(1500);
        Optional<Recommendation> actualOption = invest500RuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        assertThat(actualOption.isEmpty()).isTrue();
    }
    @Test
    void validateRecommendationRule_whenUsageProductTypeINVEST_shouldReturnOptionalEmpty() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(utility.productUsage(transactions, DEBIT)).thenReturn(false);
        when(utility.productUsage(transactions, INVEST)).thenReturn(false);
        when(utility.totalDeposit(transactions, SAVING)).thenReturn(1500);
        Optional<Recommendation> actualOption = invest500RuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        assertThat(actualOption.isEmpty()).isTrue();
    }
    @Test
    void validateRecommendationRule_whenInvalidTotalDepositSAVINGAmount_shouldReturnOptionalEmpty() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(utility.productUsage(transactions, DEBIT)).thenReturn(true);
        when(utility.productUsage(transactions, INVEST)).thenReturn(false);
        when(utility.totalDeposit(transactions, SAVING)).thenReturn(500);
        Optional<Recommendation> actualOption = invest500RuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        assertThat(actualOption.isEmpty()).isTrue();
    }
}