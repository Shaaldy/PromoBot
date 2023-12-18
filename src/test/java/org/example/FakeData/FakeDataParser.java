package org.example.FakeData;

import org.example.JsonPars.Filterable;
import org.example.JsonPars.JsonParserInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.example.JsonPars.JsonProducts.Item;

public class FakeDataParser implements JsonParserInterface{
    private String keyWord;
    private List<String> storeName;

    public FakeDataParser(){
        this.storeName = null;
        this.keyWord = "";
    }

    public FakeDataParser(List<String> storeNames, String keyWord) {
        this.keyWord = keyWord.toLowerCase();
        this.storeName = storeNames;
    }
    @Override
    public List<List<Item>> JsonParser() {
        List<List<Item>> fakesStore = new ArrayList<>();
        List<List<Item>> result = new ArrayList<>();
        if (storeName.isEmpty()){
            storeName.add("store1");
            storeName.add("store2");
        }
        try {
            for (String storeName : storeName) {
                List<Item> fakeStore = readItemsFromResource(storeName + ".txt");
                fakesStore.add(fakeStore);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading fake data resources");
        }
        int size = fakesStore.size();
        for (List<Item> fakeStore : fakesStore) {
            Parse parse = new Parse(fakeStore, this.keyWord);
            List<Item> items = parse.filter();
            result.add(items);
        }
        return result;
    }

    private List<Item> readItemsFromResource(String resourceName) throws IOException {
        List<Item> items = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String name = null;
                String price1 = null;
                String price2 = null;
                String data1 = null;
                String data2 = null;
                String image = null;

                String line;
                int lineCount = 0;

                while ((line = reader.readLine()) != null) {
                    switch (lineCount % 6) {
                        case 0:
                            name = line;
                            break;
                        case 1:
                            price1 = line;
                            break;
                        case 2:
                            price2 = line;
                            break;
                        case 3:
                            data1 = line;
                            break;
                        case 4:
                            data2 = line;
                            break;
                        case 5:
                            image = line;
                            items.add(new Item(name, price1, price2, data1, data2, image));
                            break;
                    }
                    lineCount++;
                }
            }
        }

        return items;
    }




    @Override
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public void setStoreName(List<String> storeName){
        this.storeName = storeName;
    }
    private static class Parse implements Filterable{
        private final List<Item> fakeStore;
        private final String keyWord;

        Parse(List<Item> fakeStore, String keyWord){
            this.fakeStore = fakeStore;
            this.keyWord = keyWord;
        }
        @Override
        public List<Item> filter() {
            if (keyWord.isEmpty()) return this.fakeStore;
            List<Item> isFiltered = new ArrayList<>();
            for (Item item : fakeStore) {
                String curItem = item.getName().toLowerCase();
                boolean contains = curItem.contains(keyWord);
                if (contains) isFiltered.add(item);
            }
            if (isFiltered.isEmpty()) isFiltered.add(new Item("null"));
            return sort(isFiltered);
        }

        @Override
        public List<Item> sort(List<Item> itemList) {
            List<Item> sortedList = new ArrayList<>(itemList);
            sortedList.sort(Comparator.comparingDouble(item -> {
                String priceAfter = item.getPriceafter();
                try {
                    return Double.parseDouble(priceAfter);
                } catch (NumberFormatException e) {
                    return Double.MAX_VALUE;
                }
            }));
            return sortedList;
        }
    }
}
