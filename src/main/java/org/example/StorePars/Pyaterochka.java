package org.example.StorePars;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Pyaterochka {
    public static void main(String[] args) throws MalformedURLException {
        List<String> data = new ArrayList<>();
        URL url = new URL("https://promo.5ka.ru");
        Document pages = JsoupDocumentUtil.getDocumentFromUrl(String.valueOf(url));
        Elements product = pages.select("body > section > div > div > div.product-item");
        for (Element element : product) {
            data.add(element.text());
        }
    for(var el: data)
        System.out.println(el + '\n');
    }
}
