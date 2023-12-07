package org.example.StorePars;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParseStore {
    public static String parse(String name, String keyWord) throws IOException {
        return switch (name) {
            case "Верный" ->
                    parsingProcess(name, "https://www.verno-info.ru/products", "body > div > main > section > div.products-list > div > div", keyWord);
            case "Пятерочка" ->
                    parsingProcess(name, "https://promo.5ka.ru", "body > section > div > div > div.product-item", keyWord);
            case "Перекресток" ->
                    parsingProcess(name, "https://www.perekrestok.ru/cat/d", "body > div > div > main > div > div > div > div > div > div > div > div > div > div > div > div.product-card__content", keyWord);
            default -> "Store not exist";
        };
    }

    public static String parsingProcess(String name, String url, String cssQuery, String keyWord) throws IOException {
        ParsStore Shop = new ParsStore(name, url, keyWord.toLowerCase());
        Shop.setProduct(cssQuery);
        Shop.dataPush();
        return Shop.toString();
    }

    static private class ParsStore {
        String storeName;
        String keyWord;
        String url;
        private Elements product;
        List<String> data = new ArrayList<>();


        public ParsStore(String storeName, String url, String keyWord) {
            this.storeName = storeName;
            this.url = url;
            this.keyWord = keyWord;
        }

        void setProduct(String cssQuery) throws IOException {
            Document pages = getDocumentFromUrl(); //
            product = pages.select(cssQuery);
        }

        public List<String> filter() {
            if (keyWord.isEmpty()) return this.data;
            List<String> isFiltered = new ArrayList<>();
            for (String currentData : this.data) {
                String toLower = currentData.toLowerCase();
                boolean contains = toLower.contains(keyWord);
                if (contains) isFiltered.add(currentData);
            }
            if (isFiltered.isEmpty()) {
                isFiltered.add("Такого товара нет или ошибка в названии, для того чтобы вывести весь список товаров напишите /ALL");
            }
            return isFiltered;

        }

        public void dataPush() {

            for (Element element : product) {
                data.add(element.text());
            }
        }

        private Document getDocumentFromUrl() throws IOException {
            return Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 OPR/99.0.0.0")
                    .referrer("https://www.google.com").get();
        }
        public String toString() {
            return storeName + "\n" + String.join("\n", filter());
        }
    }
}