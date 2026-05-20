package com.mini_mall.view;

import com.mini_mall.dto.OrderDTO;
import com.mini_mall.dto.OrderItemViewDTO;
import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.OrderService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderHistoryView extends JFrame {

    private final OrderService orderService =
            new OrderService();

    public OrderHistoryView(UserDTO user) {

        setTitle("주문 내역");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] orderColumns = {
                "주문번호",
                "주문날짜",
                "주문상태",
                "총 금액"
        };

        DefaultTableModel orderModel =
                new DefaultTableModel(
                        orderColumns,
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        JTable orderTable = new JTable(orderModel);

        String[] detailColumns = {
                "상품명",
                "주문가격",
                "수량",
                "합계"
        };

        DefaultTableModel detailModel =
                new DefaultTableModel(
                        detailColumns,
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        JTable detailTable = new JTable(detailModel);

        List<OrderDTO> orderList = orderService.getMyOrders(user);

        if(orderList != null) {

            for(OrderDTO order
                    : orderList) {

                orderModel.addRow(
                        new Object[]{
                                order.getOrderId(),
                                order.getOrderDate(),
                                order.getStatus(),
                                order.getTotalPrice() + "원"
                        }
                );
            }
        }

        orderTable.getSelectionModel()
                .addListSelectionListener(e -> {

                    if(e.getValueIsAdjusting()) {
                        return;
                    }

                    int row =
                            orderTable.getSelectedRow();

                    if(row == -1) {

                        return;
                    }

                    int orderId =
                            (int) orderModel.getValueAt(
                                    row,
                                    0
                            );

                    // 상세 테이블 초기화
                    detailModel.setRowCount(0);

                    // 상세 조회
                    List<OrderItemViewDTO> detailList =
                            orderService.getOrderDetail(
                                    orderId
                            );

                    if(detailList != null) {

                        for(OrderItemViewDTO item : detailList) {

                            int total = item.getOrderPrice() * item.getOrderQuantity();

                            detailModel.addRow(
                                    new Object[]{

                                            item.getProductName(),
                                            item.getOrderPrice(),
                                            item.getOrderQuantity(),
                                            total
                                    }
                            );
                        }
                    }
                });

        JButton closeButton = new JButton("닫기");

        closeButton.addActionListener(e -> {
            dispose();
        });

        JPanel bottomPanel = new JPanel();

        bottomPanel.add(closeButton);

        JSplitPane splitPane =
                new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        new JScrollPane(orderTable),
                        new JScrollPane(detailTable)
                );

        splitPane.setDividerLocation(220);

        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}