package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseStoresJsonMethod {
    public static void main(String[] args) throws IOException {
        ToParse toParse = new ToParse("кировский".toLowerCase());
        String par = toParse.parse();
        toParse.makeItem(par);
    }

    static class ToParse {

        private final String url;
        private final String storeName;

        ToParse(String storeName) {
            Integer shopId = getShopId(storeName);

            this.url = String.format("https://skidkaonline.ru/apiv3/products/?limit=600&offset=0&shop_id=%s&city_id=49&fields=id,name,name2,shops_ids,url,noted,discount_url,discount_name,date,notalladdr,image336,imagefull,brands,pricebefore,priceafter,discount_type,discount,externalurl,countPlus,countMinus,comments,desc,color,daystitle,liked,started_today,published_today", shopId);
            this.storeName = storeName;
        }

        private static int getShopId(String storeName) {
            Map<String, Integer> idByStore = new HashMap<>();
            idByStore.put("пятерочка", 9);
            idByStore.put("магнит", 2);
            idByStore.put("магнит семейный", 26);
            idByStore.put("манит экстра", 2391);
            idByStore.put("аленка", 1840);
            idByStore.put("ариант", 852);
            idByStore.put("ашан", 1465);
            idByStore.put("бристоль", 720);
            idByStore.put("кировский", 112);
            idByStore.put("верный", 39);
            idByStore.put("кб", 100);
            idByStore.put("лента", 361);
            idByStore.put("монетка", 96);
            idByStore.put("окей", 41);
            idByStore.put("пивко", 1357);
            if (idByStore.get(storeName) == null) {
                throw new IllegalArgumentException("Магазин с именем " + storeName + " не найден.");
            }
            return idByStore.get(storeName);
        }


        private final ObjectMapper objectMapper = new ObjectMapper();


        static int getCityId(String url) {
            String regex = "&city_id=(\\d+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String city_id = matcher.group(1);
                return Integer.parseInt(city_id);
            }
            return -1;
        }

        String parse() throws IOException {

            Connection.Response response = Jsoup.connect(url)
                    .header("Accept", "*/*")
                    .header("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("Connection", "keep-alive")
                    .cookie("visitorcity", "yekaterinburg")
                    .cookie("_ym_uid", "1701783376617140761")
                    .cookie("_ym_d", "1701783376")
                    .cookie("_gid", "GA1.2.1211614043.1701783376")
                    .cookie("_gat", "1")
                    .cookie("_ym_isad", "2")
                    .cookie("_ym_visorc", "b")
                    .cookie("subscribe_showed", "1")
                    .cookie("PHPSESSID", "60b3nh24s178h6otq6l8ug4sav")
                    .cookie("_ga_YL18X8Z772", "GS1.1.1701783376.1.1.1701783410.0.0.0")
                    .cookie("_ga", "GA1.1.1156499981.1701783376")
                    .cookie("_ga_VXB631NEPB", "GS1.2.1701783376.1.1.1701783411.0.0.0")
                    .header("Referer", "https://skidkaonline.ru/yekaterinburg/shops/pyaterochka/")
                    .header("Sec-Fetch-Dest", "empty")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Site", "same-origin")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36 OPR/104.0.0.0")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("sec-ch-ua", "\"Chromium\";v=\"118\", \"Opera GX\";v=\"104\", \"Not=A?Brand\";v=\"99\"")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("sec-ch-ua-platform", "\"Windows\"")
                    .method(org.jsoup.Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute();

            return response.parse().text();
        }

        public void makeItem(String resParse) throws JsonProcessingException {
            Items items = objectMapper.readValue(resParse, Items.class);
            List<Item> itemList = items.getProducts();
            System.out.println(storeName);
            for (Item item : itemList) {
                String id = item.getId();
                String name = item.getName();
                String pricebefore = item.getPricebefore();
                String priceafter = item.getPriceafter();
                String startdate = item.getStartdate();
                String enddate = item.getEnddate();
                String image = item.getImage();
                System.out.println("Id: " + id + '\n' + "Name: " + name + '\n' + "Цена до " + pricebefore + '\n' + "Цена после " + priceafter + '\n' + "Даты - " + startdate + " - " + enddate + '\n' + image);
            }
            if (itemList.isEmpty()) {
                System.out.println("Акции не найдены");
            } else System.out.println(itemList.size());
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Item {

        @JsonProperty("id")
        private String id;
        @JsonProperty("pricebefore")
        private String pricebefore;
        @JsonProperty("priceafter")
        private String priceafter;
        @JsonProperty("startdate")
        private String startdate;
        @JsonProperty("enddate")
        private String enddate;
        @JsonProperty("name")
        private String name;

        @JsonProperty("imagefull")
        private Image image;

        Item() {
        }

        public String getId() {
            return this.id;
        }

        public String getPricebefore() {
            return this.pricebefore;
        }

        public String getPriceafter() {
            return this.priceafter;
        }

        public String getStartdate() {
            return this.startdate;
        }

        public String getEnddate() {
            return this.enddate;
        }

        public String getName() {
            return this.name;
        }

        public String getImage() {
            return image.getSrs();
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Items {
        @JsonProperty("products")
        private List<Item> products;

        public List<Item> getProducts() {
            return products;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Image {
        @JsonProperty("src")
        private String srs;

        public String getSrs() {
            return this.srs;
        }
    }
}