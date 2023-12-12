package org.example.JsonPars;

import java.util.List;

public interface Filterable {
    List<JsonProducts.Item> filter();

    List<JsonProducts.Item> Sort(List<JsonProducts.Item> itemList);

}
