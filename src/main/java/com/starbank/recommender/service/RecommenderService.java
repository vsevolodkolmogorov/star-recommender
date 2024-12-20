package com.starbank.recommender.service;

import com.starbank.recommender.dto.RecommenderDto;;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommenderService {

    public List<RecommenderDto> getRecommendations(UUID userId) {

        // Массив рекомендаций
        List<RecommenderDto> recommendations = new ArrayList<>();

        // TODO: Использовать RecommenderInvest500, RecommenderSimpleCredit, RecommenderTopSaving
        //  Заполнить массив recommendations или отправить пустой массив.

        return recommendations;
    }
}
