package com.mini_mall.service;

import com.mini_mall.dto.ProductDTO;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductServiceTest {

    private final ProductService productService = new ProductService();

    @Test
    void 상품목록_조회된다() {
        List<ProductDTO> products = productService.getProductList();
        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    void 상품상세_조회된다() {
        int productId = 1;
        ProductDTO detail = productService.getProductDetail(productId);
        assertNotNull(detail);
        assertEquals(productId, detail.getProductId());
    }
}
