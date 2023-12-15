package org.example.TGBot;

<<<<<<< HEAD
import org.example.JsonPars.JsonProducts.Item;
import org.example.UserState.UserState;
=======
import org.example.JsonPars.JsonProducts;
import org.example.JsonPars.ParseStoresJsonMethod;
>>>>>>> origin/master

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
<<<<<<< HEAD

import static org.example.JsonPars.ParseStoresJsonMethod.JsonParser;

public class Service {
    static List<List<Item>> getDataForPage(String messageText) throws IOException {
        List<List<Item>> curData = new ArrayList<>();
=======
import java.util.Map;

public class Service {
    static List<List<JsonProducts.Item>> getDataForPage(String messageText) throws IOException {
        List<List<JsonProducts.Item>> curData = new ArrayList<>();
>>>>>>> origin/master
        if (messageText.length() <= 100 && messageText.trim().split("\\s+").length >= 3 && messageText.contains("@")) {
            List<String> words = Arrays.asList(messageText.split("\\s+"));
            int atIndex = words.indexOf("@");
            List<String> stores = new ArrayList<>();
            String keyWord = "";
            if (atIndex != -1 && atIndex < words.size() - 1) {
                stores = words.subList(0, atIndex);
                keyWord = String.join(" ", words.subList(atIndex + 1, words.size()));
            }
<<<<<<< HEAD
            curData = JsonParser(stores, keyWord);
=======
            curData = ParseStoresJsonMethod.JsonParser(stores, keyWord);
>>>>>>> origin/master
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
<<<<<<< HEAD
                curData = JsonParser(keyword);
            } else {
                curData = JsonParser(stores);
=======
                curData = ParseStoresJsonMethod.JsonParser(keyword);
            } else {
                curData = ParseStoresJsonMethod.JsonParser(stores);
>>>>>>> origin/master
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
<<<<<<< HEAD
            UserState curState = telegramBot.getUserState(chatId);
            List<List<Item>> dataForPage = curState.getUserData();
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
           }
=======
            List<List<JsonProducts.Item>> dataForPage = telegramBot.getUserData().get(chatId);
            int currentPage = telegramBot.getCurrentPage();
            int currentStore = telegramBot.getCurrentStore();
            Map<Long, Integer> CurPage = telegramBot.getUserCurrentPage();
            Map<Long, Integer> CurStore = telegramBot.getUserCurrentStore();

            if (currentPage < 0) {
                currentPage = dataForPage.get(currentStore).size() - 1;
                if (currentStore > 0) {
                    currentStore--;
                }
            } else if (currentPage >= dataForPage.get(0).size()) {
                currentPage = 0;
                if (currentStore < dataForPage.get(0).size() - 1) {
                    currentStore++;
                }
            }
            currentPage += pageChange;
            currentStore += storeChange;

            CurPage.put(chatId, currentPage);
            CurStore.put(chatId, currentStore);
            telegramBot.setCurrentPage(currentPage);
            telegramBot.setCurrentStore(currentStore);
            telegramBot.setUserCurrentPage(CurPage);
            telegramBot.setUserCurrentStore(CurStore);
            telegramBot.setElementUserData(chatId, dataForPage);

        }
>>>>>>> origin/master

    }
}
