package com.mini_mall.view;

import com.mini_mall.dto.OrderDTO;
import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.OrderService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderHistoryView extends JFrame {

    private final OrderService orderService = new OrderService();

    public OrderHistoryView(UserDTO user) {

        setTitle("주문 내역");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 컬럼
        String[] columns = {
                "주문번호",
                "주문날짜",
                "상태"
        };

        DefaultTableModel model =
                new DefaultTableModel(
                        columns,
                        0
                );

        JTable table = new JTable(model);

        // 주문 목록 조회
        List<OrderDTO> orderList = orderService.getMyOrders(user);

        if(orderList != null) {

            for(OrderDTO order : orderList) {

                model.addRow(
                        new Object[]{
                                order.getOrderId(),
                                order.getOrderDate(),
                                order.getStatus()
                        }
                );
            }
        }

        // 닫기 버튼
        JButton closeButton = new JButton("닫기");

        closeButton.addActionListener(e -> {
            dispose();
        });

        JPanel bottomPanel = new JPanel();

        bottomPanel.add(closeButton);

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
}