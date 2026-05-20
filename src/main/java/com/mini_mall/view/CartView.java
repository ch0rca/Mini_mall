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
    
    private DefaultTableModel model;
    private JTable table;
    private JLabel totalLabel;

    public CartView(CartService cartService,
                    UserDTO user,
                    ProductView productView) {

        this.cartService = cartService;
        this.loginUser = user;
        this.productView = productView;

        setTitle("장바구니");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 컬럼
        String[] columns = {
                "상품명",
                "가격",
                "수량",
                "합계"
        };

        // 모델
        model =
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
        table = new JTable(model);
        table.setRowHeight(30);

        // 총 금액
        totalLabel =
                new JLabel(
                        "총 금액 : "
                                + cartService.getTotalPrice()
                                + "원"
                );

        totalLabel.setFont(
                new Font(
                        "Dialog",
                        Font.BOLD,
                        15
                )
        );
        
        refreshCartTable();

        JButton plusButton = new JButton("+");

        plusButton.addActionListener(e -> {

            int row = table.getSelectedRow();

            if(row == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "상품을 선택하세요."
                );

                return;
            }

            CartDTO item = cartService.getCartItem(row);

            // 재고 초과 검사
            if(item.getQuantity()
                    >= item.getProduct().getStock()) {

                JOptionPane.showMessageDialog(
                        this,
                        "재고보다 많이 담을 수 없습니다."
                );
                return;
            }

            cartService.increaseQuantity(row);
            refreshCartTable();
        });

        JButton minusButton = new JButton("-");

        minusButton.addActionListener(e -> {

            int row = table.getSelectedRow();

            if(row == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "상품을 선택하세요."
                );
                return;
            }

            cartService.decreaseQuantity(row);
            refreshCartTable();
        });


        JButton deleteButton =
                new JButton("삭제");

        deleteButton.addActionListener(e -> {

            int row = table.getSelectedRow();

            if(row == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "상품을 선택하세요."
                );
                return;
            }

            cartService.removeCartItem(row);
            refreshCartTable();
        });

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
                // 상품 목록 새로고침
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

        JButton closeButton = new JButton("닫기");

        closeButton.addActionListener(e -> {
            dispose();
        });

        // 하단 패널
        JPanel bottomPanel = new JPanel();

        bottomPanel.add(totalLabel);
        bottomPanel.add(plusButton);
        bottomPanel.add(minusButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(orderButton);
        bottomPanel.add(closeButton);

        // 화면 배치
        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        setVisible(true);
    }


    private void refreshCartTable() {
        model.setRowCount(0);

        // 다시 출력
        for(CartDTO item : cartService.getCartList()) {
            model.addRow(new Object[]{
                    item.getProduct().getProductName(),
                    item.getProduct().getPrice(),
                    item.getQuantity(),
                    item.getTotalPrice()
            });
        }

        // 총 금액 갱신
        totalLabel.setText(
                "총 금액 : "
                        + cartService.getTotalPrice()
                        + "원"
        );
    }
}