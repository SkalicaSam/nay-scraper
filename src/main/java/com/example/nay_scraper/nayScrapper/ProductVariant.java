package com.example.nay_scraper.nayScrapper;

public class ProductVariant {
    private String name;
    private String imageUrl;
    private String link;
    private String price;
    private String freeAmount;
    private String unpackedAmount;
    private String centralStock;

    public ProductVariant(String name, String imageUrl, String link, String price, String freeAmount, String unpackedAmount, String centralStock) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.link = link;
        this.price = price;
        this.freeAmount = freeAmount;
        this.unpackedAmount = unpackedAmount;
        this.centralStock = centralStock;
    }

    public ProductVariant() {
    }

    public ProductVariant(String name, String imageUrl, String link, String price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.link = link;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(String freeAmount) {
        this.freeAmount = freeAmount;
    }

    public String getUnpackedAmount() {
        return unpackedAmount;
    }

    public void setUnpackedAmount(String unpackedAmount) {
        this.unpackedAmount = unpackedAmount;
    }

    public String getCentralStock() {
        return centralStock;
    }

    public void setCentralStock(String centralStock) {
        this.centralStock = centralStock;
    }
}
