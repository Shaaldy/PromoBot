package org.example.JsonPars;

import java.io.IOException;
import java.util.List;

public interface JsonParserInterface {
    List<List<JsonProducts.Item>> JsonParser() throws IOException;
    public void setKeyWord(String keyWord);

    public void setStoreName(List<String> storeName);

}