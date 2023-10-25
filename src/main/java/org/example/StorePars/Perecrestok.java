package org.example.StorePars;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Perecrestok {
    public static void peekCity(String myCity, String url){
        WebDriver driver = new ChromeDriver();
        driver.get(url);

        WebElement ageConfirmationButton = driver.findElement(By.xpath("//span[contains(text(), 'Мне исполнилось 18 лет')]"));

        ageConfirmationButton.click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        driver.navigate().refresh();

        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1000);");


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.navigate().refresh();

        driver.quit();
    }


    public static void main(String[] args) throws IOException, InterruptedException {

        List<String> data = new ArrayList<>();
       /* Document page = Jsoup.connect("https://www.perekrestok.ru/delivery/ekaterinburg").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 OPR/99.0.0.0")
                .referrer("https://www.google.com").get();
        var product = page.select("body > div > div > main > div > div > div > div > div > div > div > div > div > div > div");
        for(int i = 0; i < product.size(); ++i)
        {
            System.out.println(product.get(i).text());
        }*/
        int curPage = 25;
        boolean hasMorePage = true;
        URL url = new URL(String.format("https://www.perekrestok.ru/cat/d?append=1&page=%d", curPage));
        String res = null;
        peekCity("Ярославль", String.valueOf(url));

        while (curPage >= 23) {

            url = new URL(String.format("https://www.perekrestok.ru/cat/d?append=1&page=%d", curPage));
            curPage--;
            Document pages = JsoupDocumentUtil.getDocumentFromUrl(String.valueOf(url));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            var productName = pages.select("body > div > div > main > div > div > div > div > div > div > div > div > div > div > div > div");

            for (int j = 1; j < productName.size(); j+=2){
                data.add(productName.get(j).text());
            }
        }
        for(var element: data)
        {
            System.out.println(element + '\n');
        }

    }
}
