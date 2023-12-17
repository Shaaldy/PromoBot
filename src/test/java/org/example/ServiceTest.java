package org.example;

import org.example.FakeData.FakeDataParser;
import org.example.JsonPars.JsonParserInterface;
import org.example.JsonPars.JsonProducts;
import org.example.TGBot.Service;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ServiceTest {
    @Test
    public void getDataForPage() throws IOException {
        List<List<JsonProducts.Item>> expectedResult = new ArrayList<>();
        List<JsonProducts.Item> expectedResults = new ArrayList<>();
        expectedResults.add(new JsonProducts.Item("Кетчуп Махеевъ Чили 300г", "99.99", "74.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602958-488793-ketchup-makheev-chili-300g.jpg?t=t1702350137"));
        expectedResults.add(new JsonProducts.Item("Кетчуп Heinz итальянский 320г", "139.99", "94.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602960-488793-ketchup-heinz-italyanskijj-320g.jpg?t=t1702350137"));
        expectedResults.add(new JsonProducts.Item("Кетчуп Heinz томатный 320г", "190.99", "120.99", "2023-12-12",
                "2023-12-18", "https://skidkaonline.ru/img/p/2023/12/488793/54602959-488793-ketchup-heinz-tomatnyjj-320g.jpg?t=t1702350137"));

        expectedResult.add(expectedResults);

        String messageText = "store1 @ кетчуп";

        JsonParserInterface jsonParser = new FakeDataParser();
        Service service = new Service(jsonParser);

        List<List<JsonProducts.Item>> result = service.getDataForPage(messageText);

        // Проверяем ожидаемый результат и результат вызова
        assertEquals(expectedResult.size(), result.size());
        assertEquals(3, result.get(0).size());
        for (int i = 0; i < result.size(); ++i) {
            assertEquals(expectedResult.get(0).get(i).getName(), result.get(0).get(i).getName());
            assertEquals(expectedResult.get(0).get(i).getPriceafter(), result.get(0).get(i).getPriceafter());
            assertEquals(expectedResult.get(0).get(i).getPricebefore(), result.get(0).get(i).getPricebefore());
            assertEquals(expectedResult.get(0).get(i).getEnddate(), result.get(0).get(i).getEnddate());
            assertEquals(expectedResult.get(0).get(i).getStartdate(), result.get(0).get(i).getStartdate());
            assertEquals(expectedResult.get(0).get(i).getImage(), result.get(0).get(i).getImage());
        }
    }

}
