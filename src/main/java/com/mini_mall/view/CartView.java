package com.mini_mall.view;

import com.mini_mall.dto.CartDTO;
import com.mini_mall.service.CartService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CartView extends JFrame {

    private final CartService cartService;

    public CartView(CartService cartService) {

        this.cartService = cartService;

        setTitle("장바구니");
        setSize(500, 400);
        setLocationRelativeTo(null);

        String[] columns = {
                "상품명",
                "가격",
                "수량",
                "합계"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);

        for(CartDTO item : cartService.getCartList()) {

            model.addRow(new Object[] {
                    item.getProduct().getProductName(),
                    item.getProduct().getPrice(),
                    item.getQuantity(),
                    item.getTotalPrice()
            });
        }

        JLabel totalLabel =
                new JLabel(
                        "총 금액 : "
                        + cartService.getTotalPrice()
                        + "원"
                );

        JButton orderButton = new JButton("주문하기");
        JPanel bottomPanel = new JPanel();

        bottomPanel.add(totalLabel);
        bottomPanel.add(orderButton);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}