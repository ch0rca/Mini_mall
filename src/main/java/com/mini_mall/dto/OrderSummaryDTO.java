package com.mini_mall.dto;

import java.sql.Timestamp;

public class OrderSummaryDTO {

    private int orderId;
    private int userId;
    private String userName;
    private Timestamp orderDate;
    private String status;

    public OrderSummaryDTO() {}

    public OrderSummaryDTO(int orderId, int userId, String userName,
                           Timestamp orderDate, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.userName = userName;
        this.orderDate = orderDate;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
