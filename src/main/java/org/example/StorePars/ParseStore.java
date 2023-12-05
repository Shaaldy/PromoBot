package org.example.StorePars;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseStore {
    public static String ShopParser(String name, String keyWord) throws IOException {
        return switch (name) {
            case "Верный" ->
                    ShopParse(name, "https://www.verno-info.ru/products", "body > div > main > section > div.products-list > div > div", keyWord);
            case "Пятерочка" ->
                    ShopParse(name, "https://promo.5ka.ru", "body > section > div > div > div.product-item", keyWord);
            case "Перекресток" ->
                    ShopParse(name, "https://www.perekrestok.ru/cat/d", "body > div > div > main > div > div > div > div > div > div > div > div > div > div > div > div.product-card__content", keyWord);
            default -> "Store not exist";
        };
    }

    public static String ShopParse(String name, String url, String cssQuery, String keyWord) throws IOException {
        ParseShop Shop = new ParseShop(name, url, keyWord.toLowerCase());
        Shop.setProduct(cssQuery);
        Shop.dataPush();
        return Shop.toString();
    }

}

class ParseShop {
    String storeName;
    String keyWord;
    String url;

    private Elements product;
    List<String> data = new ArrayList<>();


    public ParseShop(String storeName, String url, String keyWord) {
        this.storeName = storeName;
        this.url = url;
        this.keyWord = keyWord;
    }

    void setProduct(String cssQuery) throws IOException {
        Document pages = getDocumentFromUrl_func(); //
        product = pages.select(cssQuery);
        System.out.println(product);
    }

    public List<String> Filter() {
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
            if (storeName.equals("Пятерочка") || storeName.equals("Верный")){
                String updatePrice = formatPrice(element.text());
                data.add(updatePrice);
            }
            else
                data.add(element.text());
        }
    }

    private Document getDocumentFromUrl_func() throws IOException {
        return Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 OPR/99.0.0.0")
                .referrer("https://www.google.com").get();
    }


    private static String formatPrice(String input) {
        Pattern pattern = Pattern.compile("(\\d+)([^\\d\\s]+)?");
        Matcher matcher = pattern.matcher(input);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            int price = Integer.parseInt(matcher.group(1));
            String nextWord = matcher.group(2);

            if (price % 100 == 99 || price % 100 == 90) {
                if (nextWord == null || !nextWord.trim().isEmpty()) {
                    double valueInRubles = price / 100.0; // Переводим копейки в рубли
                    matcher.appendReplacement(result, String.format("%.2f%s", valueInRubles, nextWord != null ? nextWord : ""));
                } else {
                    matcher.appendReplacement(result, matcher.group());
                }
            } else {
                matcher.appendReplacement(result, matcher.group());
            }
        }

        matcher.appendTail(result);
        return result.toString();
    }
    public String toString() {
        return storeName + "\n" + String.join("\n", Filter());
    }
}