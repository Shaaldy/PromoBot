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
        private double pricebefore;
        @JsonProperty("priceafter")
        private double priceafter;
        @JsonProperty("startdate")
        private String startdate;
        @JsonProperty("enddate")
        private String enddate;
        @JsonProperty("name")
        private String name;

        @JsonProperty("imagefull")
        private Image image;


        public Item() {
            this.id = "";
            this.startdate = "";
            this.enddate = "";
            this.name = "Такого товара нет";
            this.image = new Image();

        }

        public Item(String name, Double price1, Double price2, String data1, String data2, String img){
            this.name = name;
            this.pricebefore = price1;
            this.priceafter = price2;
            this.startdate = data1;
            this.enddate = data2;
            this.image = new Image(img);
        }

        public String getId() {
            return this.id;
        }

        public double getPricebefore() {
            return this.pricebefore;
        }

        public double getPriceafter() {
            return this.priceafter;
        }

        public String getStartdate() {
            return this.startdate.split(" ")[0];
        }

        public String getEnddate() {
            return this.enddate.split(" ")[0];
        }

        public String getName() {
            return this.name;
        }


        public String getImage() {
            return image.getSrs();
        }

        public String toString() {
            if (getPriceafter() > 0) {
                return "Название " + getName() + "\nЦена до скидки " + getPricebefore() + " \nпосле скидки " + getPriceafter()
                        + "\nВремя действия акции " + getStartdate() + " - " + getEnddate() + "\n" + getImage();
            }
            return "Название " + getName() + "\nВремя действия акции " + getStartdate() + " - " + getEnddate() + "\n" + getImage();
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

            public String getStoreName() {
                return this.storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }


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
            Image(String srs) {
                this.srs = srs;
            }

        }

    }

