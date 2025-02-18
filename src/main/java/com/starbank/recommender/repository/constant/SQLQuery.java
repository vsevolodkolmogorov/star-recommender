package com.starbank.recommender.repository.constant;

public final class SQLQuery {
    public static final String FIND_RECOMMENDATION_BY_NAME = "SELECT * FROM RECOMMENDATIONS WHERE NAME = ?";
    public static final String FIND_ALL_TRANSACTION_BY_USER_ID = "SELECT t.ID AS transaction_id, t.TYPE AS transaction_type, t.AMOUNT, u.ID AS user_id, u.USERNAME, u.FIRST_NAME, u.LAST_NAME, p.ID AS product_id, p.NAME, p.TYPE AS product_type FROM TRANSACTIONS t JOIN USERS u ON t.USER_ID = u.ID JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID WHERE u.ID = ?";
    public static final String FIND_USER_BY_ID = "SELECT * FROM USERS WHERE ID = ?";
    public static final String FIND_USER_BY_USERNAME = "SELECT * FROM USERS WHERE USERNAME = ?";
}