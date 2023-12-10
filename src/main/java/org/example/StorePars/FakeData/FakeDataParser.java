package org.example.StorePars.FakeData;

import org.example.StorePars.Filterable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FakeDataParser implements Filterable {
    private List<String> fakeData;
    private final String keyWord;

    public FakeDataParser(String filePath, String keyWord) {
        try {
            this.fakeData = Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.keyWord = keyWord;
    }

    @Override
    public List<String> filter() {
        if (keyWord.isEmpty()) return this.fakeData;
        List<String> isFiltered = new ArrayList<>();
        for (String currentData : this.fakeData) {
            String toLower = currentData.toLowerCase();
            boolean contains = toLower.contains(keyWord);
            if (contains) isFiltered.add(currentData);
        }
        if (isFiltered.isEmpty()) {
            isFiltered.add("Такого товара нет или ошибка в названии, для того чтобы вывести весь список товаров напишите /ALL");
        }
        return isFiltered;
    }

}
