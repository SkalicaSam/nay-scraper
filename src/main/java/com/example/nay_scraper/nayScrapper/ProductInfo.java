package com.example.nay_scraper.nayScrapper;

public class ProductInfo {
    private String title;
    private String stav;
    private String freeAmount;
    private String unpackedAmount;
    private String imageUrl;
    private String price;
    private String ean;
    private String match;
    private String centralStock;
    private String productUrl; // New field


    public ProductInfo(String title, String stav, String freeAmount, String unpackedAmount,
                       String imageUrl, String price, String ean, String match, String centralStock, String productUrl) {
        this.title = title;
        this.stav = stav;
        this.freeAmount = freeAmount;
        this.unpackedAmount = unpackedAmount;
        this.imageUrl = imageUrl;
        this.price = price;
        this.ean = ean;
        this.match = match;
        this.centralStock = centralStock;
        this.productUrl = productUrl;
    }

    public ProductInfo(String productTitle, String vypredané, String number, String number1, String centralStock, String productUrl) {
    }

    // Gettery a settery
    public String getTitle() { return title; }
    public String getStav() { return stav; }
    public String getFreeAmount() { return freeAmount; }
    public String getUnpackedAmount() { return unpackedAmount; }
    public String getImageUrl() { return imageUrl; }
    public String getPrice() { return price; }
    public String getEan() { return ean; }
    public String getMatch() { return match; }
    public String getCentralStock() { return centralStock; }
    public String getProductUrl() { return productUrl; }


    public void setTitle(String title) { this.title = title; }
    public void setStav(String stav) { this.stav = stav; }
    public void setFreeAmount(String freeAmount) { this.freeAmount = freeAmount; }
    public void setUnpackedAmount(String unpackedAmount) { this.unpackedAmount = unpackedAmount; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setPrice(String price) { this.price = price; }
    public void setEan(String ean) { this.ean = ean; }
    public void setMatch(String match) { this.match = match; }
    public void setCentralStock(String centralStock) { this.centralStock = centralStock; }
    public void setProductUrl(String productUrl) { this.productUrl = productUrl; }

}
