package com.starbank.recommender.service.components;

import com.starbank.recommender.dto.RecommenderDto;
import com.starbank.recommender.service.interfaces.RecommendationRuleSet;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RecommenderTopSaving implements RecommendationRuleSet {

    @Override
    public RecommenderDto recommend(UUID userId) {

        // TODO: Нужно проверить, подходит ли пользователь под рекомендацию Top Saving.
        //  Подключить RecommendationsRepository для использования базы данных.
        //  Создать экземпляр Top Saving - простой кредит или вернуть null.

        return null;
    }
}
