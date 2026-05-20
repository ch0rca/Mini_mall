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
        String sql = "SELECT product_id, product_name, price, stock, is_active FROM Product";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<ProductDTO> products = new ArrayList<>();
            while (rs.next()) {
                products.add(new ProductDTO(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("price"),
                    rs.getInt("stock"),
                    rs.getBoolean("is_active")
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
        String sql = "SELECT product_id, product_name, price, stock, is_active FROM Product WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new ProductDTO(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("price"),
                    rs.getInt("stock"),
                    rs.getBoolean("is_active")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 상품 등록
    public int insertProduct(ProductDTO product) {
        String sql = "INSERT INTO Product (product_name, price, stock) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, product.getProductName());
            pstmt.setInt(2, product.getPrice());
            pstmt.setInt(3, product.getStock());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 상품 수정
    public int updateProduct(ProductDTO product) {
        String sql = "UPDATE Product SET product_name = ?, price = ?, stock = ? WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, product.getProductName());
            pstmt.setInt(2, product.getPrice());
            pstmt.setInt(3, product.getStock());
            pstmt.setInt(4, product.getProductId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 상품 판매중지
    public int deactivateProduct(int productId) {
        String sql = "UPDATE Product SET is_active = 0 WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // 재고 감소
    public boolean decreaseStock(Connection con,
                                 int productId,
                                 int quantity) {

        String sql =
                "UPDATE Product " +
                "SET stock = stock - ? " +
                "WHERE product_id = ? " +
                "AND stock >= ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
