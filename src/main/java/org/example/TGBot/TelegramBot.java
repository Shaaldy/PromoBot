package org.example.TGBot;

import org.example.JsonPars.JsonProducts;
import org.example.JsonPars.ParseStoresJsonMethod;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    private final String botToken;
    private final String botName;

    public TelegramBot(String botName, String botToken) {
        this.botToken = botToken;
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().toLowerCase();
            long chatId = update.getMessage().getChatId();
            try {
                messageProcessing(chatId, messageText);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private String fileRead() throws IOException {
        Path path = Paths.get("src/main/java/org/example/JsonPars/Stores.txt");
        return new String(Files.readAllBytes(path));
    }

    private void messageProcessing(long chatId, String messageText) throws IOException {

        System.out.println(messageText + "\n");
        if (messageText.startsWith("/")) {
            Command command = new Command();
            if (command.hasCommand(messageText)) {
                String botResponse = command.getResponse(messageText);
                sendMessage(chatId, botResponse);
            } else {
                sendMessage(chatId, command.getResponse("default"));
            }
        } else {
            List<List<JsonProducts.Item>> data = new ArrayList<>();
            if (messageText.length() <= 100 &&
                    messageText.trim().split("\\s+").length >= 3 &&
                    messageText.contains("@")) {

                List<String> words = Arrays.asList(messageText.split("\\s+"));
                int atIndex = words.indexOf("@");
                List<String> stores = new ArrayList<>();
                String keyWord = "";
                if (atIndex != -1 && atIndex < words.size() - 1) {
                    stores = words.subList(0, atIndex);
                    keyWord = String.join(" ", words.subList(atIndex + 1, words.size()));
                }
                data = ParseStoresJsonMethod.JsonParser(stores, keyWord);

            } else if(messageText.length() <= 100 && !messageText.contains("@")) {
                String[] words = messageText.split("\\s+");
                String storesFromFile = fileRead();
                List<String> stores = new ArrayList<>();

                for(String word: words){
                    if(storesFromFile.contains(word)){
                        stores.add(word);
                    }
                }
                if (stores.isEmpty()){
                    String keyword = String.join(" ", words);
                    data = ParseStoresJsonMethod.JsonParser(keyword);
                }
                else {
                    data = ParseStoresJsonMethod.JsonParser(stores);
                }
            }
            for(List<JsonProducts.Item> itemList: data){
                for(JsonProducts.Item item: itemList){
                    sendSingleMessage(chatId, item.toString());
                }
            }
        }

    }

    private void sendSingleMessage(long chatId, String textToSend) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.setParseMode(ParseMode.MARKDOWN);

        org.example.ReplyKeyboard kb = new org.example.ReplyKeyboard();
        kb.setReplyKeyboard(sendMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Bot: " + textToSend);
    }

    private void sendMessage(long chatId, String textToSend) {
        int maxMessageLength = 4096;
        int textLength = textToSend.length();

        if (textLength <= maxMessageLength) {
            sendSingleMessage(chatId, textToSend);
        } else {
            for (int i = 0; i < textLength; i += maxMessageLength) {
                int end = Math.min(i + maxMessageLength, textLength);
                String chunk = textToSend.substring(i, end);
                sendSingleMessage(chatId, chunk);
            }
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
