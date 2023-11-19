package org.example;

import org.example.StorePars.ParseStore;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
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
            long chatId = update.getMessage().getChatId();
            messageProcessing(chatId, messageText);

        }
    }


    private void handleButtonPress(String data, long chatId) {

        messageProcessing(chatId, data);
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
    private void sendSingleMessage(long chatId, String textToSend) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);

        ReplyKeyboard kb = new ReplyKeyboard();
        kb.setReplyKeyboard(sendMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Bot: " + textToSend);
    }

    private void sendMessage(long chatId, String textToSend){
        int maxMessageLength = 4096;
        int textLength = textToSend.length();

        if(textLength <= maxMessageLength){
            sendSingleMessage(chatId, textToSend);
        }
        else{
            for(int i = 0; i < textLength; i+=maxMessageLength){
                int end = Math.min(i + maxMessageLength, textLength);
                String chunk = textToSend.substring(i, end);
                sendSingleMessage(chatId, chunk);
            }
        }
    }

    private void messageProcessing(long chatId, String messageText){
        System.out.println(messageText + "\n");
        if (messageText.startsWith("/")) {
            Command command = new Command();
            if (command.hasCommand(messageText)) {
                String botResponse = command.getResponse(messageText);
                sendMessage(chatId, botResponse);
            } else if (messageText.contains("/all")) {
                dataSend(chatId, "", messageText.replaceFirst("/all\\s*", ""));
            } else {
                sendMessage(chatId, command.getResponse("default"));
            }
        } else {
            String NoKeyWord = "";
            if(messageText.contains("пятерочка") || messageText.contains("пятёрочка")){
                dataSend(chatId, NoKeyWord, "5");
            } else if (messageText.contains("верный")) {
                dataSend(chatId, NoKeyWord, "v");
            } else if (messageText.contains("перекресток") || messageText.contains("перекрёсток")) {
                dataSend(chatId, NoKeyWord, "p");
            }
            else dataSend(chatId, messageText, "");
        }

    }
    private String parseStoreData(String shopName, String keyWord) throws IOException {
        return ParseStore.ShopParser(shopName, keyWord);
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
