package org.example.StorePars;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Verniy {
    public static void main(String[] args) throws MalformedURLException {
        String url = "https://www.verno-info.ru/products";
        List<String> data = new ArrayList<>();

        Document pages = JsoupDocumentUtil.getDocumentFromUrl(url);
        Elements product = pages.select("body > div > main > section > div.products-list > div > div");

        for (Element element : product) {
            data.add(element.text());
        }

        for (var el : data)
            System.out.println(el + '\n');
    }
}
