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
import static com.starbank.recommender.service.utility.constant.ProductType.CREDIT;
import static com.starbank.recommender.service.utility.constant.ProductType.DEBIT;
@ExtendWith(MockitoExtension.class)
class SimpleCreditRuleSetTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private RecommendationRepository recommendationRepository;
    @Mock
    private TransactionListDataUtility utility;
    @InjectMocks
    private SimpleCreditRuleSet simpleCreditRuleSet;
    private UUID userId;
    private String recommendationName;
    private Recommendation simpleCredit;
    private List<Transaction> transactions;
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        recommendationName = "Простой кредит";
        simpleCredit = mock(Recommendation.class);
        transactions = List.of(mock(Transaction.class));
    }
    @Test
    void validateRecommendationRule_whenAllRulesFollowed_shouldReturnCorrectRecommendations() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(recommendationRepository.findByName(recommendationName)).thenReturn(Optional.of(simpleCredit));
        when(utility.productUsage(transactions, CREDIT)).thenReturn(false);
        when(utility.totalDeposit(transactions, DEBIT)).thenReturn(200_000);
        when(utility.totalWithdraw(transactions, DEBIT)).thenReturn(150_000);
        Optional<Recommendation> actualOption = simpleCreditRuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        verify(recommendationRepository).findByName(recommendationName);
        assertThat(actualOption.isPresent()).isTrue();
        assertThat(actualOption.get()).isEqualTo(simpleCredit);
    }
    @Test
    void validateRecommendationRule_whenUsageProductTypeCREDIT_shouldReturnOptionalEmpty() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(utility.productUsage(transactions, CREDIT)).thenReturn(true);
        when(utility.totalDeposit(transactions, DEBIT)).thenReturn(200_000);
        when(utility.totalWithdraw(transactions, DEBIT)).thenReturn(150_000);
        Optional<Recommendation> actualOption = simpleCreditRuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        assertThat(actualOption.isEmpty()).isTrue();
    }
    @Test
    void validateRecommendationRule_whenInvalidTotalDepositDEBITAmount_shouldReturnOptionalEmpty() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(utility.productUsage(transactions, CREDIT)).thenReturn(false);
        when(utility.totalDeposit(transactions, DEBIT)).thenReturn(200_000);
        when(utility.totalWithdraw(transactions, DEBIT)).thenReturn(250_000);
        Optional<Recommendation> actualOption = simpleCreditRuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        assertThat(actualOption.isEmpty()).isTrue();
    }
    @Test
    void validateRecommendationRule_whenInvalidTotalWithdrawDEBITAmount_shouldReturnOptionalEmpty() {
        when(transactionRepository.findAllTransactionByUserId(any(UUID.class))).thenReturn(transactions);
        when(utility.productUsage(transactions, CREDIT)).thenReturn(false);
        when(utility.totalDeposit(transactions, DEBIT)).thenReturn(200_000);
        when(utility.totalWithdraw(transactions, DEBIT)).thenReturn(100_000);
        Optional<Recommendation> actualOption = simpleCreditRuleSet.validateRecommendationRule(userId);
        verify(transactionRepository).findAllTransactionByUserId(userId);
        assertThat(actualOption.isEmpty()).isTrue();
    }
}