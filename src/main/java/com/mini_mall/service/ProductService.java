package com.mini_mall.service;

import com.mini_mall.dao.ProductDAO;
import com.mini_mall.dto.ProductDTO;

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
}
