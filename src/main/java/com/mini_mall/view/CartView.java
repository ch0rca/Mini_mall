package com.mini_mall.view;

import com.mini_mall.dto.CartDTO;
import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.CartService;
import com.mini_mall.service.OrderService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CartView extends JFrame {

    private final CartService cartService;

    private final OrderService orderService = new OrderService();

    private final UserDTO loginUser;
    private final ProductView productView;

    public CartView(CartService cartService,
            UserDTO user,
            ProductView productView) {

        this.cartService = cartService;
        this.loginUser = user;
        this.productView = productView;

        setTitle("장바구니");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 컬럼
        String[] columns = {
                "상품명",
                "가격",
                "수량",
                "합계"
        };

        // 테이블 모델
        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        // 테이블
        JTable table = new JTable(model);
        table.setRowHeight(30);

        // 장바구니 목록 출력
        for (CartDTO item : cartService.getCartList()) {

            model.addRow(new Object[]{
                    item.getProduct().getProductName(),
                    item.getProduct().getPrice(),
                    item.getQuantity(),
                    item.getTotalPrice()
            });
        }

        // 총 금액
        JLabel totalLabel =
                new JLabel(
                        "총 금액 : "
                                + cartService.getTotalPrice()
                                + "원"
                );

        totalLabel.setFont(
                new Font("Dialog",
                        Font.BOLD,
                        15)
        );

        // 주문하기 버튼
        JButton orderButton = new JButton("주문하기");

        orderButton.addActionListener(e -> {
            // 장바구니 비어있으면
            if(cartService.getCartList().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "장바구니가 비어있습니다."
                );
                return;
            }

            // 주문 확인
            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            "주문하시겠습니까?",
                            "주문 확인",
                            JOptionPane.YES_NO_OPTION
                    );

            if(result != JOptionPane.YES_OPTION) {
                return;
            }

            // 주문 실행
            boolean orderResult =
                    orderService.order(
                            loginUser,
                            cartService.getCartList()
                    );

            // 주문 성공
            if(orderResult) {
                JOptionPane.showMessageDialog(
                        this,
                        "주문 완료!"
                );

                // 장바구니 비우기
                cartService.clearCart();
                productView.refreshProductTable();
                dispose();
            }

            // 주문 실패
            else {

                JOptionPane.showMessageDialog(
                        this,
                        "주문 실패",
                        "오류",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // 닫기 버튼
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> {
            dispose();
        });

        // 하단 패널
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(totalLabel);
        bottomPanel.add(orderButton);
        bottomPanel.add(closeButton);

        // 화면 추가
        add(new JScrollPane(table),BorderLayout.CENTER);
        add(bottomPanel,BorderLayout.SOUTH);

        setVisible(true);
    }
}