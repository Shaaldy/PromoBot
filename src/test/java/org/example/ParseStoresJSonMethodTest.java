package org.example;

import org.example.FakeData.FakeDataParser;
import org.example.JsonPars.JsonProducts;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParseStoresJSonMethodTest {


    @Test
    public void testFilterSorted1() throws IOException {
        List<List<JsonProducts.Item>> expectedResult = new ArrayList<>();
        List<JsonProducts.Item> expectedResults = new ArrayList<>();
        expectedResults.add(new JsonProducts.Item("Кетчуп Махеевъ Чили 300г", "99.99", "74.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602958-488793-ketchup-makheev-chili-300g.jpg?t=t1702350137"));
        expectedResults.add(new JsonProducts.Item("Кетчуп Heinz итальянский 320г", "139.99", "94.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602960-488793-ketchup-heinz-italyanskijj-320g.jpg?t=t1702350137"));
        expectedResults.add(new JsonProducts.Item("Кетчуп Heinz томатный 320г", "190.99", "120.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602959-488793-ketchup-heinz-tomatnyjj-320g.jpg?t=t1702350137"));

        expectedResult.add(expectedResults);
        FakeDataParser fakeDataParser = new FakeDataParser( List.of("store1"), "кетчуп");
        List<List<JsonProducts.Item>> filterSortedData = fakeDataParser.JsonParser();
        assertEquals(1, filterSortedData.size());
        assertEquals(3, filterSortedData.get(0).size());
        for (int i = 0; i < filterSortedData.size(); ++i) {
            assertEquals(expectedResult.get(0).get(i).getName(), filterSortedData.get(0).get(i).getName());
            assertEquals(expectedResult.get(0).get(i).getPriceafter(), filterSortedData.get(0).get(i).getPriceafter());
            assertEquals(expectedResult.get(0).get(i).getPricebefore(), filterSortedData.get(0).get(i).getPricebefore());
            assertEquals(expectedResult.get(0).get(i).getEnddate(), filterSortedData.get(0).get(i).getEnddate());
            assertEquals(expectedResult.get(0).get(i).getStartdate(), filterSortedData.get(0).get(i).getStartdate());
            assertEquals(expectedResult.get(0).get(i).getImage(), filterSortedData.get(0).get(i).getImage());
        }
    }

    @Test
    public void testFilterSorted2() throws IOException {
        List<List<JsonProducts.Item>> expectedResult = new ArrayList<>();
        List<JsonProducts.Item> expectedResults = new ArrayList<>();
        expectedResults.add(new JsonProducts.Item("Конфеты HALLS Mini Mints со вкусом мяты 12.5г", "48.99", "39.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602955-488793-konfety-halls-mini-mints-so-vkusom-myaty-125g.jpg?t=t1702350136"));

        expectedResults.add(new JsonProducts.Item("Конфета Nuts Duo цельный фундук 66г", "91.99", "59.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602956-488793-konfeta-nuts-duo-celnyjj-funduk-66g.jpg?t=t1702350137"));

        expectedResults.add(new JsonProducts.Item("Конфеты Рот Фронт батончики", "799.99", "332.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602942-488793-konfety-rot-front-batonchiki.jpg?t=t1702350136"));

        expectedResults.add(new JsonProducts.Item("Конфеты Рот Фронт Маска", "849.99", "379.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602943-488793-konfety-rot-front-maska.jpg?t=t1702350136"));


        expectedResults.add(new JsonProducts.Item("Конфеты Райские Облака Суфле сливочное", "549.99", "459.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602945-488793-konfety-rajjskie-oblaka-sufle-slivochnoe.jpg?t=t1702350136"));

        expectedResult.add(expectedResults);
        FakeDataParser fakeDataParser = new FakeDataParser(List.of("store1"), "конфет");
        List<List<JsonProducts.Item>> filterSortedData = fakeDataParser.JsonParser();
        assertEquals(1, filterSortedData.size());
        assertEquals(expectedResult.get(0).size(), filterSortedData.get(0).size());
        for (int i = 0; i < filterSortedData.size(); ++i) {
            assertEquals(expectedResult.get(0).get(i).getName(), filterSortedData.get(0).get(i).getName());
            assertEquals(expectedResult.get(0).get(i).getPriceafter(), filterSortedData.get(0).get(i).getPriceafter());
            assertEquals(expectedResult.get(0).get(i).getPricebefore(), filterSortedData.get(0).get(i).getPricebefore());
            assertEquals(expectedResult.get(0).get(i).getEnddate(), filterSortedData.get(0).get(i).getEnddate());
            assertEquals(expectedResult.get(0).get(i).getStartdate(), filterSortedData.get(0).get(i).getStartdate());
            assertEquals(expectedResult.get(0).get(i).getImage(), filterSortedData.get(0).get(i).getImage());
        }
    }

}
