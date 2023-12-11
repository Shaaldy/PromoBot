package org.example.JsonPars;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JsonProducts {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

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
            this.id = "";
            this.pricebefore = "";
            this.priceafter = "";
            this.startdate = "";
            this.enddate = "";
            this.name = "Такого товара нет";
            this.image = new Image();

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

        public String toString(){
            return "Название " + getName() + "\nЦена до " + getPricebefore() + " после " + getPriceafter()
                    + "\nВремя действия акции " + getStartdate() + " - " + getEnddate() + "\nКартинка " + getImage();
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Items {
        @JsonProperty("products")
        private List<Item> products;

        public List<Item> getProducts() {
            return products;
        }

        private String storeName;

        public String getStoreName(){ return  this.storeName; }

        public void setStoreName(String storeName){ this.storeName = storeName; }


    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Image {
        @JsonProperty("src")
        private String srs;

        public String getSrs() {
            return this.srs;
        }

        Image() {
            this.srs = "";
        }

    }

}
