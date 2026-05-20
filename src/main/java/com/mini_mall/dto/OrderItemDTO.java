package com.mini_mall.dto;

public class OrderItemDTO {

    private int orderItemId;
    private int orderId;
    private int productId;
    private int orderQuantity;
    private int orderPrice;

    public OrderItemDTO() {}

    public OrderItemDTO(int orderItemId, int orderId, int productId,
                        int orderQuantity, int orderPrice) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.orderQuantity = orderQuantity;
        this.orderPrice = orderPrice;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
