package org.example.TGBot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {
    public static void main(String[] args) throws TelegramApiException {
        if (args.length < 2) {
            System.out.println("Usage: java Main <username> <token> ");
            System.exit(1);
        }
        TelegramBot telegramBot = new TelegramBot(args[0], args[1]);
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramBot);
    }

}