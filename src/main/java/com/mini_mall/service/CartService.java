package com.mini_mall.service;

import com.mini_mall.dto.CartDTO;
import com.mini_mall.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;

public class CartService {

    private final List<CartDTO> cartList =
            new ArrayList<>();

    // 장바구니 추가
    public void addToCart(ProductDTO product,
                          int quantity) {

        // 이미 담긴 상품인지 검사
        for (CartDTO item : cartList) {

            if (item.getProduct().getProductId()
                    == product.getProductId()) {

                item.setQuantity(
                        item.getQuantity() + quantity
                );

                return;
            }
        }

        // 신규 상품
        cartList.add(
                new CartDTO(product, quantity)
        );
    }

    // 장바구니 목록 조회
    public List<CartDTO> getCartList() {
        return cartList;
    }

    // 총 금액
    public int getTotalPrice() {

        int total = 0;

        for (CartDTO item : cartList) {
            total += item.getTotalPrice();
        }

        return total;
    }

    // 장바구니 비우기
    public void clearCart() {
        cartList.clear();
    }
}