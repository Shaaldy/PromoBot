package org.example;

import org.example.StorePars.FakeData.FakeDataParser;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class ParseStoreTest {
    String file = "C:\\Users\\shald\\IdeaProjects\\PromoBot\\src\\main\\java\\org\\example\\StorePars\\FakeData\\TestData.txt";

    @Test
    public void testFilter1() {
        String[] expectedResults = {
                "31.10-06.11.2023 40% 12499 20899 Шоколад Pobeda Charged Slim&Fit молоч б/сах 100г",
                "31.10-06.11.2023 44% 6999 12499 Шоколад Аленка молочный Фундук 90г",
        };
        FakeDataParser fakeDataParser = new FakeDataParser(file, "шоколад".toLowerCase());
        List<String> filterData = fakeDataParser.filter();
        assertEquals(expectedResults.length, filterData.size());
        IntStream.range(0, expectedResults.length).forEach(i -> assertEquals(expectedResults[i], filterData.get(i)));
    }

    @Test
    public void testFilter2() {
        String[] expectedResults = {
                "31.10-06.11.2023 14% 2699 3149 Корм д/к пауч Purina One 75г д/взрослых говядина",
                "31.10-06.11.2023 14% 2699 3149 Корм д/к пауч Purina One 75г д/домашних курица",
                "31.10-06.11.2023 17% 34999 41999 Корм д/к сух Purina One 750г c лососем д/стер",
                "31.10-06.11.2023 17% 34999 41999 Корм д/к сух Purina ONE 750г говяд-цельн.злаки(х8)",
                "31.10-06.11.2023 17% 34999 41999 Корм д/к сух Purina ONE 750г инд с цельн.злак(х8)",
                "31.10-06.11.2023 21% 21999 27999 Корм д/с сух Purina ONE 600r д/мелк пор кур-рис(х8"
        };

        FakeDataParser fakeDataParser = new FakeDataParser(file, "Корм".toLowerCase());
        List<String> filterData = fakeDataParser.filter();
        assertEquals(expectedResults.length, filterData.size());
        IntStream.range(0, expectedResults.length).forEach(i -> assertEquals(expectedResults[i], filterData.get(i)));
    }

    @Test
    public void testFilter3() {
        String[] expectedResults = {
                "31.10-06.11.2023 29% 28999 40999 Дезодор.муж Axe Анархия 150мл (св/у)",
                "31.10-06.11.2023 29% 28999 40999 Дезодор.муж Axe Дарк Темптейшн 150мл (св/у)",
                "31.10-06.11.2023 29% 28999 40999 Дезодор.муж Axe Эксайт 150мл (св/у)",
                "31.10-06.11.2023 26% 24999 33999 Дезодор.муж Rexona а/п Кобальт аэроз 150мл"
        };
        FakeDataParser fakeDataParser = new FakeDataParser(file, "ДЕЗОДОР".toLowerCase());
        List<String> filterData = fakeDataParser.filter();
        assertEquals(expectedResults.length, filterData.size());
        IntStream.range(0, expectedResults.length).forEach(i -> assertEquals(expectedResults[i], filterData.get(i)));
    }

    @Test
    public void testFilter4(){
        String expectedResult = "Такого товара нет или ошибка в названии, для того чтобы вывести весь список товаров напишите /ALL";
        FakeDataParser fakeDataParser = new FakeDataParser(file, "мечта программиста".toLowerCase());
        List<String> filterData = fakeDataParser.filter();
        assertEquals(1, filterData.size());
        assertEquals(expectedResult, filterData.get(0));
    }

}
