package com.mini_mall.dto;

public class OrderItemViewDTO {

    private int orderItemId;
    private int productId;
    private String productName;
    private int orderQuantity;
    private int orderPrice;

    public OrderItemViewDTO() {}

    public OrderItemViewDTO(int orderItemId, int productId, String productName,
                            int orderQuantity, int orderPrice) {
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.productName = productName;
        this.orderQuantity = orderQuantity;
        this.orderPrice = orderPrice;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
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

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }
}
