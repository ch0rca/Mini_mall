package com.mini_mall.service;

import com.mini_mall.dao.ProductDAO;
import com.mini_mall.dto.ProductDTO;
import com.mini_mall.dto.UserDTO;

import java.util.List;

public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();

    // 상품 목록 조회
    public List<ProductDTO> getProductList() {
        return productDAO.findAllProducts();
    }

    // 상품 상세 조회
    public ProductDTO getProductDetail(int productId) {
        return productDAO.findByProductId(productId);
    }

    // 상품 등록 (관리자만)
    public boolean createProduct(UserDTO user, ProductDTO product) {
        if (!isAdmin(user)) {
            return false;
        }
        return productDAO.insertProduct(product) == 1;
    }

    // 상품 수정 (관리자만)
    public boolean updateProduct(UserDTO user, ProductDTO product) {
        if (!isAdmin(user)) {
            return false;
        }
        return productDAO.updateProduct(product) == 1;
    }

    // 상품 판매중지 (관리자만)
    public boolean deactivateProduct(UserDTO user, int productId) {
        if (!isAdmin(user)) {
            return false;
        }
        return productDAO.deactivateProduct(productId) == 1;
    }

    private boolean isAdmin(UserDTO user) {
        return user != null && "ADMIN".equals(user.getRole());
    }
}
