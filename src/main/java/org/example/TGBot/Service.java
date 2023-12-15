package org.example.TGBot;

import org.example.JsonPars.JsonProducts;
import org.example.JsonPars.ParseStoresJsonMethod;
import org.example.UserState.UserState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Service {
    static List<List<JsonProducts.Item>> getDataForPage(String messageText) throws IOException {
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

    private static String fileRead() throws IOException {
        Path path = Paths.get("src/main/java/org/example/JsonPars/Stores.txt");
        return new String(Files.readAllBytes(path));
    }

    public static class PaginationHandler {
        private static TelegramBot telegramBot;

        public PaginationHandler(TelegramBot telegramBot) {
            PaginationHandler.telegramBot = telegramBot;
        }

        public static void pagination(long chatId, String data) {
            if (data.startsWith("prev_")) {
                handlePagination(chatId, -1, 0);
            } else if (data.startsWith("next_")) {
                handlePagination(chatId, 1, 0);
            } else if (data.startsWith("prevStore_")) {
                handlePagination(chatId, 0, -1);
            } else if (data.startsWith("nextStore_")) {
                handlePagination(chatId, 0, 1);
            }
        }

        public static void handlePagination(long chatId, int pageChange, int storeChange) {
            UserState curState = telegramBot.getUserState(chatId);
            List<List<JsonProducts.Item>> dataForPage = curState.getUserData();
            int currentPage = curState.getUserCurrentPage();
            int currentStore = curState.getUserCurrentStore();

            currentPage += pageChange;
            currentStore += storeChange;

            if (currentPage < 0) {
                if (currentStore > 0) {
                    currentStore--;
                    currentPage = dataForPage.get(currentStore).size() - 1;
                }
            } else if (currentPage >= dataForPage.get(currentStore).size()) {
                if (currentStore < dataForPage.size() - 1) {
                    currentStore++;
                    currentPage = 0;
                }
            }

            curState.setUserCurrentPage(currentPage);
            curState.setUserCurrentStore(currentStore);
            telegramBot.setUserData(chatId, curState);
            System.out.println("Current page: " + telegramBot.getUserState(chatId).getUserCurrentPage());
            System.out.println("Current store: " + telegramBot.getUserState(chatId).getUserCurrentStore() + " " + telegramBot.getUserState(chatId).getUserData().get(currentStore).get(currentPage).getStoreName());
        }

    }
}
