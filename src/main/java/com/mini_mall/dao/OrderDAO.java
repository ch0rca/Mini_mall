package com.mini_mall.dao;

import com.mini_mall.DBConnection;
import com.mini_mall.dto.OrderItemViewDTO;
import com.mini_mall.dto.OrderSummaryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // 주문 목록 조회 (관리자용)
    public List<OrderSummaryDTO> findAllOrders() {
        String sql = "SELECT o.order_id, o.user_id, u.name AS user_name, " +
                     "o.order_date, o.status " +
                     "FROM Orders o " +
                     "LEFT JOIN Users u ON o.user_id = u.user_id " +
                     "ORDER BY o.order_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<OrderSummaryDTO> orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(new OrderSummaryDTO(
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getString("user_name"),
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
    public List<OrderItemViewDTO> findOrderItemsByOrderId(int orderId) {
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, " +
                     "p.product_name, oi.order_quantity, oi.order_price " +
                     "FROM Order_Item oi " +
                     "LEFT JOIN Product p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_id = ? " +
                     "ORDER BY oi.order_item_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            List<OrderItemViewDTO> items = new ArrayList<>();
            while (rs.next()) {
                items.add(new OrderItemViewDTO(
                    rs.getInt("order_item_id"),
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
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
