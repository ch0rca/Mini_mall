package com.mini_mall.dao;

import com.mini_mall.DBConnection;
import com.mini_mall.dto.OrderDTO;
import com.mini_mall.dto.OrderItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // 주문 목록 조회 (관리자용)
    public List<OrderDTO> findAllOrders() {
        String sql = "SELECT order_id, user_id, order_date, status FROM Orders";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<OrderDTO> orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(new OrderDTO(
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("order_date"),
                    rs.getString("status")
                ));
            }
            return orders;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 주문별 상품 목록 조회
    public List<OrderItemDTO> findOrderItemsByOrderId(int orderId) {
        String sql = "SELECT order_item_id, order_id, product_id, order_quantity, order_price " +
                     "FROM Order_Item WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            List<OrderItemDTO> items = new ArrayList<>();
            while (rs.next()) {
                items.add(new OrderItemDTO(
                    rs.getInt("order_item_id"),
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getInt("order_quantity"),
                    rs.getInt("order_price")
                ));
            }
            return items;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
