package com.starbank.recommender.service.components;

import com.starbank.recommender.dto.RecommenderDto;
import com.starbank.recommender.service.interfaces.RecommendationRuleSet;

import java.util.List;
import java.util.UUID;

public class RecommenderSimpleCredit implements RecommendationRuleSet {

    @Override
    public RecommenderDto recommend(UUID userId) {

        // TODO: Нужно проверить, подходит ли пользователь под рекомендацию простой кредит.
        //  Подключить RecommendationsRepository для использования базы данных.
        //  Создать экземпляр DTO - простой кредит или вернуть null.

        return null;
    }
}
