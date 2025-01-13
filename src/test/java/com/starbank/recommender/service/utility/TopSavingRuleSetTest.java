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
import static com.starbank.recommender.service.utility.constant.ProductType.DEBIT;
import static com.starbank.recommender.service.utility.constant.ProductType.SAVING;
@ExtendWith(MockitoExtension.class)
class TopSavingRuleSetTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private RecommendationRepository recommendationRepository;
    @Mock
    private TransactionListDataUtility utility;
    @InjectMocks
    private TopSavingRuleSet topSavingRuleSet;
    private UUID userId;
    private String recommendationName;
    private Recommendation topSaving;
    private List<Transaction> transactions;
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        recommendationName = "Top Saving";
        topSaving = mock(Recommendation.class);
        transactions = List.of(mock(Transaction.class));
    }
    @Test
    void validateRecommendationRule_whenCorrectTotalDepositDEBITAmountAndOtherRulesFollowed_shouldReturnOptionalWithCorrectRecommendations() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(recommendationRepository.findByName(recommendationName)).thenReturn(Optional.of(topSaving));
        when(utility.productUsage(transactions, DEBIT)).thenReturn(true);
        when(utility.totalDeposit(transactions, DEBIT)).thenReturn(50_000);
        when(utility.totalWithdraw(transactions, DEBIT)).thenReturn(20_000);
        Optional<Recommendation> actualOption = topSavingRuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        verify(recommendationRepository).findByName(recommendationName);
        assertThat(actualOption.isPresent()).isTrue();
        assertThat(actualOption.get()).isEqualTo(topSaving);
    }
    @Test
    void validateRecommendationRule_whenCorrectTotalDepositSAVINGAmountAndOtherRulesFollowed_shouldReturnOptionalWithCorrectRecommendations() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(recommendationRepository.findByName(recommendationName)).thenReturn(Optional.of(topSaving));
        when(utility.productUsage(transactions, DEBIT)).thenReturn(true);
        when(utility.totalDeposit(transactions, DEBIT)).thenReturn(20_000);
        when(utility.totalDeposit(transactions, SAVING)).thenReturn(50_000);
        when(utility.totalWithdraw(transactions, DEBIT)).thenReturn(10_000);
        Optional<Recommendation> actualOption = topSavingRuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        verify(recommendationRepository).findByName(recommendationName);
        assertThat(actualOption.isPresent()).isTrue();
        assertThat(actualOption.get()).isEqualTo(topSaving);
    }
    @Test
    void validateRecommendationRule_whenNotUsageProductTypeDEBIT_shouldReturnOptionalEmpty() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(utility.productUsage(transactions, DEBIT)).thenReturn(false);
        when(utility.totalDeposit(transactions, DEBIT)).thenReturn(50_000);
        when(utility.totalWithdraw(transactions, DEBIT)).thenReturn(10_000);
        Optional<Recommendation> actualOption = topSavingRuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        assertThat(actualOption.isEmpty()).isTrue();
    }
    @Test
    void validateRecommendationRule_whenInvalidTotalDepositDEBITAmount_shouldReturnOptionalEmpty() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(utility.productUsage(transactions, DEBIT)).thenReturn(true);
        when(utility.totalDeposit(transactions, DEBIT)).thenReturn(20_000);
        when(utility.totalDeposit(transactions, SAVING)).thenReturn(50_000);
        when(utility.totalWithdraw(transactions, DEBIT)).thenReturn(10_000);
        Optional<Recommendation> actualOption = topSavingRuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        assertThat(actualOption.isEmpty()).isTrue();
    }
    @Test
    void validateRecommendationRule_whenInvalidTotalWithdraw_shouldReturnOptionalEmpty() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(utility.productUsage(transactions, DEBIT)).thenReturn(true);
        when(utility.totalDeposit(transactions, DEBIT)).thenReturn(20_000);
        when(utility.totalDeposit(transactions, SAVING)).thenReturn(50_000);
        when(utility.totalWithdraw(transactions, DEBIT)).thenReturn(25_000);
        Optional<Recommendation> actualOption = topSavingRuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        assertThat(actualOption.isEmpty()).isTrue();
    }
}