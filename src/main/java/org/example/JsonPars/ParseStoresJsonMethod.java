package org.example.JsonPars;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.*;
import java.util.function.ToDoubleFunction;

import static org.example.JsonPars.JsonProducts.*;
import static org.example.JsonPars.Stores.idByStore;

public class ParseStoresJsonMethod {

    public static List<List<Item>> JsonParser(List<String> storeNames, String keyWord) throws IOException {
        List<List<Item>> result = new ArrayList<>();
        for (String storeName : storeNames) {
            ToParse toParse = new ToParse(storeName.toLowerCase(), keyWord);
            List<Item> items = toParse.filter();
            result.add(items);
        }

        return result;
    }

    public static List<List<Item>> JsonParser(List<String> storeNames) throws IOException {
        return JsonParser(storeNames, "");
    }



    public static List<List<Item>> JsonParser(String keyWord) throws IOException {
        List<String> storeNames = new ArrayList<>(idByStore.keySet());
        return JsonParser(storeNames, keyWord);
    }


    static class ToParse implements Filterable {

        private final String url;

        private final String keyWord;
        private final String storeName;

        List<Item> itemList;

        ToParse(String storeName, String keyWord) throws IOException {
            this.keyWord = keyWord.toLowerCase();
            this.storeName = storeName;
            Integer shopId = getShopId(storeName);
            this.url = String.format("https://skidkaonline.ru/apiv3/products/?limit=600&offset=0&shop_id=%s&city_id=49&fields=id,name,name2,shops_ids,url,noted,discount_url,discount_name,date,notalladdr,image336,imagefull,brands,pricebefore,priceafter,discount_type,discount,externalurl,countPlus,countMinus,comments,desc,color,daystitle,liked,started_today,published_today", shopId);
            String resParse = parse();
            this.itemList = makeItem(resParse);

        }


        @Override
        public List<Item> filter() {
            if (keyWord.isEmpty()) return this.itemList;
            List<Item> isFiltered = new ArrayList<>();
            for (Item item : itemList) {
                String curItem = item.getName().toLowerCase();
                boolean contains = curItem.contains(keyWord);
                if (contains) isFiltered.add(item);
            }
            if (isFiltered.isEmpty()) isFiltered.add(new Item());
            return Sort(isFiltered);
        }

        @Override
        public List<JsonProducts.Item> Sort(List<JsonProducts.Item> itemList) {
            List<JsonProducts.Item> sortedList = new ArrayList<>(itemList);
            sortedList.sort(Comparator.comparingDouble((ToDoubleFunction<? super Item>) item -> Double.parseDouble(item.getPriceafter())));
            return sortedList;
        }


        private static int getShopId(String storeName) {
            if (idByStore.get(storeName) == null) {
                throw new IllegalArgumentException("Магазин с именем " + storeName + " не найден.");
            }
            return idByStore.get(storeName);
        }


        private final ObjectMapper objectMapper = new ObjectMapper();


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

        public List<Item> makeItem(String resParse) throws JsonProcessingException {
            Items items = objectMapper.readValue(resParse, Items.class);
            items.setStoreName(this.storeName);
            List<Item> itemList = items.getProducts();
            for(Item item: itemList){
                item.setStoreName(this.storeName);
            }
            if (itemList.isEmpty()) {
                System.out.println("Акции не найдены");
            }
            return itemList;
        }


    }

}