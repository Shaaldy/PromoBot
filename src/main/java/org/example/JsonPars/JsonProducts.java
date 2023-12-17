package org.example.JsonPars;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

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

        private String storeName;


        @JsonProperty("imagefull")
        private Image image;


        public Item(String storeName) {
            this.id = "";
            this.startdate = "";
            this.enddate = "";
            this.name = "Такого товара нет";
            this.image = new Image();
            this.storeName = storeName;
            this.pricebefore = "";
            this.priceafter = "";

        }
        public Item() {
            this.id = "";
            this.startdate = "";
            this.enddate = "";
            this.name = "Такого товара нет";
            this.pricebefore = "";
            this.priceafter = "";
            this.image = new Image();
        }

        public Item(String name, String price1, String price2, String data1, String data2, String img){
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

        public String getPricebefore() {
            return this.pricebefore;
        }

        public String getPriceafter() {
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
            if (!Objects.equals(getPriceafter(), "")) {
                return getStoreName() + "\n" + getName() + "\nЦена до скидки " + getPricebefore() + " \nпосле скидки " + getPriceafter()
                        +"\n" + getStartdate() + " - " + getEnddate() + "\n" + getImage();
            }
            return getStoreName() + "\n" + getName() + "\n"+ getStartdate() + " - " + getEnddate() + "\n" + getImage();
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreName() {
            return storeName;
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

