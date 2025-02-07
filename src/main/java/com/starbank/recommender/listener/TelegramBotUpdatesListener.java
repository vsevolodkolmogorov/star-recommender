package com.starbank.recommender.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.starbank.recommender.exception.UserNotFoundException;
import com.starbank.recommender.model.User;
import com.starbank.recommender.repository.h2.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private Long chatId;
    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private UserRepository userRepository;
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message() != null || update.message().text() != null) {
                this.chatId = updates.get(0).message().chat().id();
                logger.info("Text of message: {}", update.message().text());
                String messageText = update.message().text();
                if (messageText.startsWith("/recommend")) {
                    String[] parts = messageText.split(" ", 2);
                    if (parts.length > 1) {
                        String username = parts[1];
                        sendRecommendation(username);
                    } else {
                        sendMessage("Пользователь не найден.");
                    }
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    public void sendMessage(String text) {
        var sendMessage = new SendMessage(chatId, text);
        try {
            telegramBot.execute(sendMessage);
        } catch (Exception e) {
            logger.info("Problem with send message: {}", e.getMessage());
        }
    }
    public void sendRecommendation(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        // TODO: Проверить на получение рекомендаций!
        var sendMessage = new SendMessage(chatId, "Здравствуйте " + user.getFirstName() + " " + user.getLastName());
        try {
            telegramBot.execute(sendMessage);
        } catch (Exception e) {
            logger.info("Problem with send message: {}", e.getMessage());
        }
    }
}
