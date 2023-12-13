package org.example.JsonPars;

import java.util.HashMap;
import java.util.Map;

public class Stores {
    public static final Map<String, Integer> idByStore = new HashMap<>();

    static {
        idByStore.put("пятерочка", 9);
        idByStore.put("магнит", 2);
        idByStore.put("магнит семейный", 26);
        idByStore.put("магнит экстра", 2391);
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
    }

    public static String getKeyByValue(Integer value) {
        for (Map.Entry<String, Integer> entry : idByStore.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
