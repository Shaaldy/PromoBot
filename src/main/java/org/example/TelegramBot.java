package org.example;

import org.example.StorePars.ParseStore;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
            String messageText = update.getMessage().getText().toLowerCase();
            System.out.println(messageText + "\n");
            long chatId = update.getMessage().getChatId();
            if (messageText.startsWith("/")) {
                Command command = new Command();
                if (command.hasCommand(messageText)) {
                    String botResponse = command.getResponse(messageText);
                    sendMessage(chatId, botResponse);
                } else if (messageText.contains("/all")) {
                    dataSend(chatId, "", messageText);
                }
                else {
                    sendMessage(chatId, command.getResponse("default"));
                }
            } else {
                String noKeykeyWord = "";
                if(messageText.contains("пятерочка")){
                    dataSend(chatId, noKeykeyWord, "5");
                } else if (messageText.contains("верный")) {
                    dataSend(chatId, noKeykeyWord, "v");
                } else if (messageText.contains("перекресток")) {
                    dataSend(chatId, noKeykeyWord, "p");
                }
                else dataSend(chatId, messageText, "");
            }

        }
    }

    private void dataSend(long chatId, String keyWord, String flag) {
        List<String> data = new ArrayList<>();
        try {
            if (flag.isEmpty()) {
                data.add(parseStoreData("Пятерочка", keyWord));
                data.add(parseStoreData("Верный", keyWord));
                data.add(parseStoreData("Перекресток", keyWord));
            } else {
                if (flag.contains("5")) {
                    data.add(parseStoreData("Пятерочка", keyWord));
                }
                if (flag.contains("v")) {
                    data.add(parseStoreData("Верный", keyWord));
                }
                if (flag.contains("p")) {
                    data.add(parseStoreData("Перекресток", keyWord));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendMessage(chatId, String.join("\n", data));
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

    private String parseStoreData(String shopName, String keyWord) throws IOException {
        ParseStore shop = new ParseStore();
        return shop.ShopParser(shopName, keyWord);
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
