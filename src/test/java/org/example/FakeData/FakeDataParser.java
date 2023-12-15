package org.example.FakeData;

import org.example.JsonPars.Filterable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;

import static org.example.JsonPars.JsonProducts.Item;

public class FakeDataParser implements Filterable {
    private final String keyWord;
    private List<Item> fakeData;

    public FakeDataParser(String filePath, String keyWord) {
        try {
            fakeData = readItemsFromFile(filePath);
            for (Item item : fakeData) {
                System.out.println(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.keyWord = keyWord.toLowerCase();
    }

    private static List<Item> readItemsFromFile(String filePath) throws IOException {
        List<Item> items = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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

        return items;
    }
    @Override
    public List<Item> filter() {
        if (keyWord.isEmpty()) return this.fakeData;
        List<Item> isFiltered = new ArrayList<>();
        for (Item item : fakeData) {
            String curItem = item.getName().toLowerCase();
            boolean contains = curItem.contains(keyWord);
            if (contains) isFiltered.add(item);
        }
        if (isFiltered.isEmpty()) isFiltered.add(new Item("null"));
        return Sort(isFiltered);
    }

    @Override
    public List<Item> Sort(List<Item> itemList) {
        List<Item> sortedList = new ArrayList<>(itemList);
        sortedList.sort(Comparator.comparingDouble((ToDoubleFunction<? super Item>) item -> Double.parseDouble(item.getPriceafter())));
        return sortedList;
    }

}