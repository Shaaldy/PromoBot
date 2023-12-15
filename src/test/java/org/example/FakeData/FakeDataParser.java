package org.example.FakeData;

import org.example.JsonPars.Filterable;
import org.example.JsonPars.JsonProducts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FakeDataParser implements Filterable {
    private final String keyWord;
    private List<JsonProducts.Item> fakeData;

    public FakeDataParser(String filePath, String keyWord) {
        try {
            fakeData = readItemsFromFile(filePath);
            for (JsonProducts.Item item : fakeData) {
                System.out.println(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.keyWord = keyWord.toLowerCase();
    }

    private static List<JsonProducts.Item> readItemsFromFile(String filePath) throws IOException {
        List<JsonProducts.Item> items = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String name = null;
            double price1 = 0.0;
            double price2 = 0.0;
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
                        price1 = Double.parseDouble(line);
                        break;
                    case 2:
                        price2 = Double.parseDouble(line);
                        break;
                    case 3:
                        data1 = line;
                        break;
                    case 4:
                        data2 = line;
                        break;
                    case 5:
                        image = line;
                        items.add(new JsonProducts.Item(name, price1, price2, data1, data2, image));
                        break;
                }
                lineCount++;
            }
        }

        return items;
    }
    @Override
    public List<JsonProducts.Item> filter() {
        if (keyWord.isEmpty()) return this.fakeData;
        List<JsonProducts.Item> isFiltered = new ArrayList<>();
        for (JsonProducts.Item item : fakeData) {
            String curItem = item.getName().toLowerCase();
            boolean contains = curItem.contains(keyWord);
            if (contains) isFiltered.add(item);
        }
        if (isFiltered.isEmpty()) isFiltered.add(new JsonProducts.Item());
        return Sort(isFiltered);
    }

    @Override
    public List<JsonProducts.Item> Sort(List<JsonProducts.Item> itemList) {
        List<JsonProducts.Item> sortedList = new ArrayList<>(itemList);
        sortedList.sort(Comparator.comparingDouble(JsonProducts.Item::getPriceafter));
        return sortedList;
    }

}