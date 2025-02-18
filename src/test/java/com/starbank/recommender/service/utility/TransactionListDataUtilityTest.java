package com.starbank.recommender.service.utility;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.starbank.recommender.model.Product;
import com.starbank.recommender.model.Transaction;
import com.starbank.recommender.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
class TransactionListDataUtilityTest {
    private final TransactionListDataUtility utility = new TransactionListDataUtility();
    private List<Transaction> transactionList;
    private String productType;
    int depositAmount1;
    int depositAmount2;
    int withdrawAmount1;
    int withdrawAmount2;
    @BeforeEach
    void setUp() {
        String deposit = "DEPOSIT";
        String withdraw = "WITHDRAW";
        productType = "PRODUCT_TYPE";
        depositAmount1 = 100;
        depositAmount2 = 300;
        withdrawAmount1 = 120;
        withdrawAmount2 = 200;
        transactionList = new ArrayList<>(List.of(
                transaction(deposit, productType, depositAmount1),
                transaction(deposit, productType, depositAmount2),
                transaction(withdraw, productType, withdrawAmount1),
                transaction(withdraw, productType, withdrawAmount2)
        ));
    }
    @Test
    void testProductUsage_positiveTest() {
        boolean productUsage = utility.productUsage(transactionList, productType);
        assertTrue(productUsage);
    }
    @Test
    void testProductUsage_negativeTest() {
        String anotherProductType = "ANOTHER_TYPE";
        boolean productUsage = utility.productUsage(transactionList, anotherProductType);
        assertFalse(productUsage);
    }
    @Test
    void totalDeposit() {
        int expected = depositAmount1 + depositAmount2;
        int actual = utility.totalDeposit(transactionList, productType);
        assertEquals(expected, actual);
    }
    @Test
    void totalWithdraw() {
        int expected = withdrawAmount1 + withdrawAmount2;
        int actual = utility.totalWithdraw(transactionList, productType);
        assertEquals(expected, actual);
    }
    private Transaction transaction(String transactionType, String productType, int amount) {
        return new Transaction()
                .setId(UUID.randomUUID())
                .setUser(new User())
                .setProduct(new Product()
                        .setType(productType))
                .setType(transactionType)
                .setAmount(amount);
    }
}