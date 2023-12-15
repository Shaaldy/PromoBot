package org.example.TGBot;

import org.example.JsonPars.JsonProducts;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.Keyboards.InlineKB.getInlineKeyBoard;

public class TelegramBot extends TelegramLongPollingBot {
    private Map<Long, List<List<JsonProducts.Item>>> userData = new HashMap<>();
    private Map<Long, Integer> userCurrentPage = new HashMap<>();

    private Map<Long, Integer> userCurrentStore = new HashMap<>();
    private int currentPage = 0;
    private int currentStore = 0;
    private final String botToken;
    private final String botName;

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getCurrentStore() {
        return this.currentStore;
    }

    public Map<Long, Integer> getUserCurrentPage() {
        return this.userCurrentPage;
    }

    public Map<Long, Integer> getUserCurrentStore() {
        return this.userCurrentStore;
    }

    public Map<Long, List<List<JsonProducts.Item>>> getUserData() {
        return this.userData;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setCurrentStore(int currentStore) {
        this.currentStore = currentStore;
    }

    public void setUserCurrentPage(Map<Long, Integer> userCurrentPage) {
        this.userCurrentPage = userCurrentPage;
    }

    public void setUserCurrentStore(Map<Long, Integer> userCurrentStore) {
        this.userCurrentStore = userCurrentStore;
    }

    public void setElementUserData(Long chatId, List<List<JsonProducts.Item>> item) {
        this.userData.put(chatId, item);
    }

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
        }
        else if (update.hasCallbackQuery()) {
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
        Service.PaginationHandler paginarionHadler = new Service.PaginationHandler(TelegramBot.this);
        Service.PaginationHandler.pagination(chatId, data);
        editMessage(chatId, messageId, userData.get(chatId).get(currentStore).get(currentPage).toString(),
                getInlineKeyBoard(chatId, currentPage, currentStore));

    }


    private void messageProcessing(long chatId, String messageText, Update update) throws IOException {
        System.out.println(messageText + "\n");
        if (messageText.startsWith("/")) {
            Command command = new Command();
            String botResponse = command.hasCommand(messageText) ? command.getResponse(messageText) : command.getResponse("default");
            Boolean isComand = true;
            sendMessage(chatId, botResponse, isComand);
        } else {
            List<List<JsonProducts.Item>> data = Service.getDataForPage(messageText);
            userData.put(chatId, data);
            userCurrentPage.put(chatId, 0);
            userCurrentStore.put(chatId, 0);
            sendMessage(chatId, data.get(0).get(0).toString());
        }
    }


    private void sendMessage(long chatId, String textToSend) {
        int maxMessageLength = 4096;
        int textLength = textToSend.length();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.setParseMode(ParseMode.MARKDOWN);

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
    private void sendMessage(long chatId, String textToSend, Boolean isComand) {
        int maxMessageLength = 4096;
        int textLength = textToSend.length();

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

    private void editMessage(long chatId, int messageId, String text, InlineKeyboardMarkup keyboardMarkup) {
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


    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
