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
    
    // 수량 증가
    public void increaseQuantity(int index) {
        CartDTO item = cartList.get(index);
        item.setQuantity(item.getQuantity() + 1);
    }
    
    // 수량 감소
    public void decreaseQuantity(int index) {
        CartDTO item = cartList.get(index);

        // 1 이하로 안 내려감
        if(item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        }
    }
    
    // 장바구니 상품 삭제
    public void removeCartItem(int index) {

        if(index >= 0 &&
           index < cartList.size()) {
            cartList.remove(index);
        }
    }
    
    // 특정 장바구니 상품 조회
    public CartDTO getCartItem(int index) {
        return cartList.get(index);
    }

    // 장바구니 비우기
    public void clearCart() {
        cartList.clear();
    }
}