package org.example.StorePars;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

import javax.print.Doc;
import java.io.IOException;


public class JsoupDocumentUtil {
    public static Document getDocumentFromUrl(String url){
        try{
            return Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 OPR/99.0.0.0")
                    .referrer("https://www.google.com").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
