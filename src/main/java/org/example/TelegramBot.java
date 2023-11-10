package org.example;

import org.example.StorePars.ParseStore;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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
        if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            handleButtonPress(data, callbackQuery.getMessage().getChatId());
        }
        else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().toLowerCase();
            long chatId = update.getMessage().getChatId();
            messageProcessing(chatId, messageText);

        }
    }

   public void sendInlineKeyBoard(long chatId){

       InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

       List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

       List<InlineKeyboardButton> row1 = new ArrayList<>();

       InlineKeyboardButton button11 = new InlineKeyboardButton();

       button11.setText("Start");
       button11.setCallbackData("/start");

       InlineKeyboardButton button12 = new InlineKeyboardButton();
       button12.setText("Help");
       button12.setCallbackData("/help");

       InlineKeyboardButton button13 = new InlineKeyboardButton();
       button13.setText("Stores");
       button13.setCallbackData("/store");
       row1.add(button11);
       row1.add(button12);
       row1.add(button13);

       List<InlineKeyboardButton> row2 = new ArrayList<>();

       InlineKeyboardButton button21 = new InlineKeyboardButton();
       button21.setText("Пятёрочка");
       button21.setCallbackData("пятерочка");

       InlineKeyboardButton button22 = new InlineKeyboardButton();
       button22.setText("Верный");
       button22.setCallbackData("верный");

       InlineKeyboardButton button23 = new InlineKeyboardButton();
       button23.setText("Перекрёсток");
       button23.setCallbackData("перекресток");

       row2.add(button21);
       row2.add(button22);
       row2.add(button23);

       keyboard.add(row1);
       keyboard.add(row2);

       markup.setKeyboard(keyboard);

       SendMessage message = new SendMessage();
       message.setChatId(chatId);
       message.setText("Buttons");
       message.setReplyMarkup(markup);
       try{
           execute(message);
       }catch (TelegramApiException e){
           e.printStackTrace();
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
            } else if (messageText.contains("/keyboard")) {
                sendInlineKeyBoard(chatId);
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
