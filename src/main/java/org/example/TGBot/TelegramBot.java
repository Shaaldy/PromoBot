package org.example.TGBot;

import org.example.JsonPars.JsonParserInterface;
import org.example.JsonPars.JsonProducts;
import org.example.JsonPars.ParseStoresJsonMethod;
import org.example.UserState.UserState;
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
    private Map<Long, UserState> userState = new HashMap<>();
    public UserState getUserState(Long chatId){return this.userState.get(chatId); }
    private final String botToken;
    private final String botName;
    public void setUserData(Long chatId, UserState userData){ this.userState.put(chatId, userData); }

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
                processUserMessage(chatId, messageText, update);
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
        int currentStore = userState.get(chatId).getUserCurrentStore();
        int currentPage = userState.get(chatId).getUserCurrentPage();
        editMessage(chatId, messageId, userState.get(chatId).getUserData().get(currentStore).get(currentPage).toString(),
                getInlineKeyBoard(chatId, currentPage, currentStore));

    }


    private void processUserMessage(long chatId, String messageText, Update update) throws IOException {
        System.out.println(messageText + "\n");

        if (messageText.startsWith("/")) {
            Command command = new Command();
            String botResponse = command.hasCommand(messageText) ? command.getResponse(messageText) : command.getResponse("default");
            Boolean isCommand = true;
            sendMessage(chatId, botResponse, isCommand);
        }
        else {
            JsonParserInterface jsonParser = new ParseStoresJsonMethod();
            Service service = new Service(jsonParser);
            List<List<JsonProducts.Item>> data = service.getDataForPage(messageText);
            userState.put(chatId, new UserState(data, 0, 0));
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
        markup = getInlineKeyBoard(chatId, userState.get(chatId).getUserCurrentPage(), userState.get(chatId).getUserCurrentStore());
        List<List<JsonProducts.Item>> curData = userState.get(chatId).getUserData();
        int currentPage = userState.get(chatId).getUserCurrentPage();
        int currentStore = userState.get(chatId).getUserCurrentStore();
        if (currentPage == curData.get(currentStore).size() - 1 && currentStore < curData.size()) {
            currentPage = 0;
            currentStore++;
        } else {
            currentPage++;
        }
        userState.put(chatId, new UserState(curData, currentPage, currentStore));
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
