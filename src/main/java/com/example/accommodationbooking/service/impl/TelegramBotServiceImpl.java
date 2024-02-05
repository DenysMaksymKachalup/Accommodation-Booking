package com.example.accommodationbooking.service.impl;

import com.example.accommodationbooking.config.TelegramBotConfig;
import com.example.accommodationbooking.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl extends TelegramLongPollingBot implements TelegramBotService {
    private static final String DEFAULT_CHAT_ID = "-451264499";
    private final TelegramBotConfig botConfig;

    @Override
    public void onUpdateReceived(Update update) {

    }

    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void sendMessage(String text) {
        SendMessage message = new SendMessage(DEFAULT_CHAT_ID, text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}