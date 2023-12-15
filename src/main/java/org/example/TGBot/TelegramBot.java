package org.example.TGBot;

import org.example.JsonPars.JsonProducts;
import org.example.JsonPars.ParseStoresJsonMethod;
import org.example.Keyboards.ReplyKeyboard;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TelegramBot extends TelegramLongPollingBot {
    private final Map<Long, List<List<JsonProducts.Item>>> userData = new HashMap<>();
    private final Map<Long, Integer> userCurrentPage = new HashMap<>();

    private final Map<Long, Integer> userCurrentStore = new HashMap<>();
    private List<List<JsonProducts.Item>> data = new ArrayList<>();
    private int currentPage = 0;
    private int currentStore = 0;
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
                messageProcessing(chatId, messageText, update);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery()) {
            try {
                handleCallbackQuery(update.getCallbackQuery());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) throws IOException {
        String data = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

        if (data.startsWith("prev_")) {
            currentPage = Integer.parseInt(data.substring(5));
            if (currentPage > 0) {
                currentPage--;
                userCurrentPage.put(chatId, currentPage);
                List<List<JsonProducts.Item>> dataForPage = userData.get(chatId);
                sendMessageWithKeyboard(chatId, messageId, dataForPage.get(currentStore).get(currentPage).toString(), getInlineKeyBoard(chatId, currentPage, currentStore));
            }
            else if (currentPage == 0 && currentStore > 0){
                currentStore--;
                List<List<JsonProducts.Item>> dataForPage = userData.get(chatId);
                currentPage = dataForPage.get(currentStore).size() - 1;
                userCurrentPage.put(chatId, currentPage);
                userCurrentStore.put(chatId, currentStore);
                sendMessageWithKeyboard(chatId, messageId, dataForPage.get(currentStore).get(currentPage).toString(), getInlineKeyBoard(chatId, currentPage, currentStore));

            }
        }
        else if (data.startsWith("next_")) {
            currentPage = Integer.parseInt(data.substring(5));
            List<List<JsonProducts.Item>> dataForPage = userData.get(chatId);
            if (currentPage < dataForPage.get(0).size() - 1) {
                currentPage++;
                userCurrentPage.put(chatId, currentPage);
                sendMessageWithKeyboard(chatId, messageId, dataForPage.get(currentStore).get(currentPage).toString(), getInlineKeyBoard(chatId, currentPage, currentStore));

            } else {
                if (currentStore < dataForPage.get(0).size() - 1) {
                    currentPage = 0;
                    userCurrentPage.put(chatId, currentPage);
                    currentStore++;
                    sendMessageWithKeyboard(chatId, messageId, dataForPage.get(currentStore).get(currentPage).toString(), getInlineKeyBoard(chatId, currentPage, currentStore));

                }
            }
        }
        else if (data.startsWith("prevStore_")) {
            currentStore = Integer.parseInt(data.substring(10));
            List<List<JsonProducts.Item>> dataForStore = userData.get(chatId);
            if (currentStore > 0){
                currentStore--;
                userCurrentStore.put(chatId, currentStore);
                currentPage = 0;
                userCurrentPage.put(chatId, currentPage);
                sendMessageWithKeyboard(chatId, messageId, dataForStore.get(currentStore).get(currentPage).toString(), getInlineKeyBoard(chatId, currentPage, currentStore));

            }


        }
        else if (data.startsWith("nextStore_")){
            currentStore = Integer.parseInt(data.substring(10));
            List<List<JsonProducts.Item>> dataForStore = userData.get(chatId);
            if (currentStore < dataForStore.size() - 1){
                currentStore++;
                userCurrentStore.put(chatId, currentStore);
                currentPage = 0;
                userCurrentPage.put(chatId, currentPage);
                sendMessageWithKeyboard(chatId, messageId, dataForStore.get(currentStore).get(currentPage).toString(), getInlineKeyBoard(chatId, currentPage, currentStore));
            }
        }
    }

    private void messageProcessing(long chatId, String messageText, Update update) throws IOException {
        System.out.println(messageText + "\n");

        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        } else if (messageText.startsWith("/")) {
            Command command = new Command();
            String botResponse = command.hasCommand(messageText) ? command.getResponse(messageText) : command.getResponse("default");
            sendMessage(chatId, botResponse);
        } else {
            data = getDataForPage(messageText);
            userData.put(chatId, data);
            userCurrentPage.put(chatId, 0);
            userCurrentStore.put(chatId, 0);
            sendMessage(chatId, data.get(0).get(0).toString());

        }
    }

    private List<List<JsonProducts.Item>> getDataForPage(String messageText) throws IOException {
        List<List<JsonProducts.Item>> curData = new ArrayList<>();
        if (messageText.length() <= 100 && messageText.trim().split("\\s+").length >= 3 && messageText.contains("@")) {
            List<String> words = Arrays.asList(messageText.split("\\s+"));
            int atIndex = words.indexOf("@");
            List<String> stores = new ArrayList<>();
            String keyWord = "";
            if (atIndex != -1 && atIndex < words.size() - 1) {
                stores = words.subList(0, atIndex);
                keyWord = String.join(" ", words.subList(atIndex + 1, words.size()));
            }
            curData = ParseStoresJsonMethod.JsonParser(stores, keyWord);
        } else if (messageText.length() <= 100 && !messageText.contains("@")) {
            String[] words = messageText.split("\\s+");
            String storesFromFile = fileRead();
            List<String> stores = new ArrayList<>();

            for (String word : words) {
                if (storesFromFile.contains(word)) {
                    stores.add(word);
                }
            }
            if (stores.isEmpty()) {
                String keyword = String.join(" ", words);
                curData = ParseStoresJsonMethod.JsonParser(keyword);
            } else {
                curData = ParseStoresJsonMethod.JsonParser(stores);
            }
        }
        return curData;
    }

    private void sendMessage(long chatId, String textToSend) {
        int maxMessageLength = 4096;
        int textLength = textToSend.length();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.setParseMode(ParseMode.MARKDOWN);

        ReplyKeyboard kb = new ReplyKeyboard();
        kb.setReplyKeyboard(sendMessage);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup = getInlineKeyBoard(chatId, currentPage, currentStore);
        if (currentPage == userData.get(chatId).get(currentStore).size() - 1 && currentStore < userData.get(chatId).size()) {
            currentPage = 0;
            currentStore++;
        } else {
            currentPage++;
        }


        sendMessage.setReplyMarkup(markup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Bot: " + textToSend);
    }

    private InlineKeyboardMarkup getInlineKeyBoard(long chatId, int currentPageNumber, int currentStore) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton button0 = new InlineKeyboardButton();
        button0.setText("<<");
        button0.setCallbackData("prevStore_" + currentStore);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("<");
        button1.setCallbackData("prev_" + currentPageNumber);

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(">");
        button3.setCallbackData("next_" + currentPageNumber);

        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText(">>");
        button4.setCallbackData("nextStore_" + currentStore);


        row.add(button0);
        row.add(button1);
        row.add(button3);
        row.add(button4);
        keyboard.add(row);

        markup.setKeyboard(keyboard);

        return markup;
    }
    private void sendMessageWithKeyboard(long chatId, int messageId, String text, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(text);
        editMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String fileRead() throws IOException {
        Path path = Paths.get("src/main/java/org/example/JsonPars/Stores.txt");
        return new String(Files.readAllBytes(path));
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
