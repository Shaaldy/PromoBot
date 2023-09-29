package org.example.StorePars;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Magnit {
    private static Document getPage() throws IOException {
        //String url = "https://www.hltv.org/forums/threads/2789978/cloud-9-official#r60442477";
        String url = "https://magnit.ru/promo/?utm_source=magnit.ru&utm_campaign=navbar&utm_medium=promo";
        Document page;
        page = (Document) Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 OPR/99.0.0.0")
                .referrer("https://www.google.com").maxBodySize(0).get();
        return page;
    }
    public static void main(String[] args) throws IOException {
        Document page = getPage();
        int i = 0;
        //x
        //div.new-card-product__wrap
        Elements tables;
        tables = page.select("section.main-container");


        // System.out.println(page);
        System.out.println(tables);
    }
}
