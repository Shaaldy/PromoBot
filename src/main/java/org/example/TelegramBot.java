package org.example;

import org.example.StorePars.BD_test;
import org.example.StorePars.Pyaterochka;
import org.example.StorePars.Verniy;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;

public class TelegramBot extends TelegramLongPollingBot {
    private final String botToken;
    private final String botName;

    public TelegramBot() {
        Properties props = new Properties();
        try (InputStream input = TelegramBot.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.botToken = props.getProperty("bot.token");
        this.botName = props.getProperty("bot.name");
//
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (messageText.startsWith("/")) {
                Command command = new Command();
                if (command.hasCommand(messageText)) {
                    String botResponse = command.getResponse(messageText);
                    sendMessage(chatId, botResponse);
                } else {
                    sendMessage(chatId, command.getResponse("default"));
                }
            } else {
                String data = parseStoreData(messageText);
                sendMessage(chatId, data);
            }

        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Bot: " + textToSend);
    }

    private String parseStoreData(String storeName) {
        try {
            if ("Пятерочка".equalsIgnoreCase(storeName)) {
                Pyaterochka pyaterochka = new Pyaterochka();
                return pyaterochka.parseFiveyorochka();
            } else if ("Верный".equalsIgnoreCase(storeName)) {
                Verniy verniy = new Verniy();
                return verniy.parseVerniy();
            } else return "Магазин не найден";
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
