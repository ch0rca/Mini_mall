package com.mini_mall.dto;

public class CartDTO {

    private ProductDTO product;

    private int quantity;

    public CartDTO() {}

    public CartDTO(ProductDTO product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return product.getPrice() * quantity;
    }
}