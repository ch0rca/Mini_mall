package com.mini_mall.service;

import com.mini_mall.dao.OrderDAO;
import com.mini_mall.dto.OrderDTO;
import com.mini_mall.dto.OrderItemDTO;
import com.mini_mall.dto.UserDTO;
import com.mini_mall.dto.CartDTO;

import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();
    
    // 주문하기
    public boolean order(UserDTO user, List<CartDTO> cartList) {

        if(user == null) {
            return false;
        }

        return orderDAO.createOrder(
                user.getUserId(),
                cartList
        );
    }

    // 주문 목록 조회 (관리자만)
    public List<OrderDTO> getOrderList(UserDTO user) {
        if (!isAdmin(user)) {
            return null;
        }
        return orderDAO.findAllOrders();
    }

    // 주문별 상품 조회 (관리자만)
    public List<OrderItemDTO> getOrderItems(UserDTO user, int orderId) {
        if (!isAdmin(user)) {
            return null;
        }
        return orderDAO.findOrderItemsByOrderId(orderId);
    }

    private boolean isAdmin(UserDTO user) {
        return user != null && "ADMIN".equals(user.getRole());
    }
}
