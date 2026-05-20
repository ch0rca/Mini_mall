package com.mini_mall.service;

import com.mini_mall.dao.OrderDAO;
import com.mini_mall.dto.OrderItemViewDTO;
import com.mini_mall.dto.OrderSummaryDTO;
import com.mini_mall.dto.UserDTO;

import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();

    // 주문 목록 조회 (관리자만)
    public List<OrderSummaryDTO> getOrderList(UserDTO user) {
        if (!isAdmin(user)) {
            return null;
        }
        return orderDAO.findAllOrders();
    }

    // 주문별 상품 조회 (관리자만)
    public List<OrderItemViewDTO> getOrderItems(UserDTO user, int orderId) {
        if (!isAdmin(user)) {
            return null;
        }
        return orderDAO.findOrderItemsByOrderId(orderId);
    }

    private boolean isAdmin(UserDTO user) {
        return user != null && "ADMIN".equals(user.getRole());
    }
}
