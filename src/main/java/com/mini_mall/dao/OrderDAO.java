package com.mini_mall.dao;


import com.mini_mall.DBConnection;
import com.mini_mall.dto.CartDTO;
import com.mini_mall.dto.OrderItemViewDTO;
import com.mini_mall.dto.OrderSummaryDTO;
import com.mini_mall.dao.ProductDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

	private final ProductDAO productDAO = new ProductDAO();

	// 주문 생성
	public boolean createOrder(int userId, List<CartDTO> cartList) {

	    String orderSql =
	            "INSERT INTO Orders(user_id, status) " +
	            "VALUES(?, ?)";

	    String itemSql =
	            "INSERT INTO Order_Item(" +
	            "order_id, " +
	            "product_id, " +
	            "order_quantity, " +
	            "order_price) " +
	            "VALUES(?, ?, ?, ?)";

	    Connection con = null;

	    try {

	        con = DBConnection.getConnection();
	        con.setAutoCommit(false);

	        // Orders INSERT
	        PreparedStatement orderPstmt =
	                con.prepareStatement(
	                        orderSql,
	                        PreparedStatement.RETURN_GENERATED_KEYS
	                );

	        orderPstmt.setInt(1, userId);
	        orderPstmt.setString(2, "주문완료");
	        orderPstmt.executeUpdate();

	        // 생성된 order_id 가져오기
	        ResultSet rs = orderPstmt.getGeneratedKeys();

	        int orderId = 0;
	        if(rs.next()) {
	            orderId = rs.getInt(1);
	        }

	        // Order_Item INSERT
	        PreparedStatement itemPstmt = con.prepareStatement(itemSql);

	        for(CartDTO item : cartList) {

	            itemPstmt.setInt(1,orderId);
	            itemPstmt.setInt(2,item.getProduct().getProductId());
	            itemPstmt.setInt(3,item.getQuantity());
	            itemPstmt.setInt(4,item.getProduct().getPrice()); // 주문 당시 가격 저장
	            itemPstmt.executeUpdate();

	            // 재고 감소
	            boolean stockResult =
	                    productDAO.decreaseStock(
	                            con,
	                            item.getProduct().getProductId(),
	                            item.getQuantity()
	                    );

	            // 재고 부족
	            if(!stockResult) {
	                con.rollback();
	                return false;
	            }
	        }

	        con.commit();
	        return true;

	    } catch (Exception e) {
	        try {
	            if(con != null) {
	                con.rollback();
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	        return false;

	    } finally {

	        try {
	            if(con != null) {
	                con.setAutoCommit(true);
	                con.close();
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

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

    // 주문 상태 변경 (관리자용)
    public int updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
