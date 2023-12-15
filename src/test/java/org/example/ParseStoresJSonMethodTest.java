package org.example;

import org.example.FakeData.FakeDataParser;
import org.example.JsonPars.JsonProducts;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParseStoresJSonMethodTest {
    String filePath = "src/test/java/org/example/FakeData/TestData.txt";

    @Test
    public void testFilterSorted1() {
        List<JsonProducts.Item> expectedResults = new ArrayList<>();
        expectedResults.add(new JsonProducts.Item("Кетчуп Махеевъ Чили 300г", "99.99", "74.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602958-488793-ketchup-makheev-chili-300g.jpg?t=t1702350137"));
        expectedResults.add(new JsonProducts.Item("Кетчуп Heinz итальянский 320г", "139.99", "94.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602960-488793-ketchup-heinz-italyanskijj-320g.jpg?t=t1702350137"));
        expectedResults.add(new JsonProducts.Item("Кетчуп Heinz томатный 320г", "190.99", "120.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602959-488793-ketchup-heinz-tomatnyjj-320g.jpg?t=t1702350137"));
        FakeDataParser fakeDataParser = new FakeDataParser(filePath, "кетчуп");
        List<JsonProducts.Item> filterSortedData = fakeDataParser.filter();
        assertEquals(3, filterSortedData.size());
        for (int i = 0; i < filterSortedData.size(); ++i) {
            assertEquals(expectedResults.get(i).getName(), filterSortedData.get(i).getName());
            assertEquals(expectedResults.get(i).getPriceafter(), filterSortedData.get(i).getPriceafter());
            assertEquals(expectedResults.get(i).getPricebefore(), filterSortedData.get(i).getPricebefore());
            assertEquals(expectedResults.get(i).getEnddate(), filterSortedData.get(i).getEnddate());
            assertEquals(expectedResults.get(i).getStartdate(), filterSortedData.get(i).getStartdate());
            assertEquals(expectedResults.get(i).getImage(), filterSortedData.get(i).getImage());
        }
    }

    @Test
    public void testFilterSorted2() {
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


        FakeDataParser fakeDataParser = new FakeDataParser(filePath, "конфет");
        List<JsonProducts.Item> filterSortedData = fakeDataParser.filter();
        assertEquals(5, filterSortedData.size());
        for (int i = 0; i < filterSortedData.size(); ++i) {
            assertEquals(expectedResults.get(i).getName(), filterSortedData.get(i).getName());
            assertEquals(expectedResults.get(i).getPriceafter(), filterSortedData.get(i).getPriceafter());
            assertEquals(expectedResults.get(i).getPricebefore(), filterSortedData.get(i).getPricebefore());
            assertEquals(expectedResults.get(i).getEnddate(), filterSortedData.get(i).getEnddate());
            assertEquals(expectedResults.get(i).getStartdate(), filterSortedData.get(i).getStartdate());
            assertEquals(expectedResults.get(i).getImage(), filterSortedData.get(i).getImage());
        }
    }

}
