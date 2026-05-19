package com.mini_mall.dao;

import com.mini_mall.DBConnection;
import com.mini_mall.dto.ProductDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // 상품 목록 조회
    public List<ProductDTO> findAllProducts() {
        String sql = "SELECT product_id, product_name, price, stock FROM Product";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<ProductDTO> products = new ArrayList<>();
            while (rs.next()) {
                products.add(new ProductDTO(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("price"),
                    rs.getInt("stock")
                ));
            }
            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 상품 상세 조회
    public ProductDTO findByProductId(int productId) {
        String sql = "SELECT product_id, product_name, price, stock FROM Product WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new ProductDTO(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("price"),
                    rs.getInt("stock")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
