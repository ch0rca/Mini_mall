package com.mini_mall.dto;

public class ProductDTO {

    private int productId;
    private String productName;
    private int price;
    private int stock;
    private boolean active;

    public ProductDTO() {
        this.active = true;
    }

    public ProductDTO(int productId, String productName, int price, int stock) {
        this(productId, productName, price, stock, true);
    }

    public ProductDTO(int productId, String productName, int price, int stock, boolean active) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.active = active;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
