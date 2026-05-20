package com.mini_mall.view.admin;

import com.mini_mall.dto.OrderItemViewDTO;
import com.mini_mall.dto.OrderSummaryDTO;
import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.OrderService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class OrderAdminPanel extends JPanel {

    private final OrderService orderService = new OrderService();
    private final UserDTO adminUser;

    private final DefaultTableModel orderTableModel = new DefaultTableModel(
        new Object[]{"주문ID", "회원ID", "회원명", "주문일", "상태"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable orderTable = new JTable(orderTableModel);

    private final DefaultTableModel orderItemTableModel = new DefaultTableModel(
        new Object[]{"주문상품ID", "상품ID", "상품명", "수량", "가격"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable orderItemTable = new JTable(orderItemTableModel);

    public OrderAdminPanel(UserDTO adminUser) {
        this.adminUser = adminUser;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel hintLabel = new JLabel("주문을 선택하면 해당 주문의 상품 목록이 표시됩니다.");
        JButton refreshButton = new JButton("주문 새로고침");
        refreshButton.addActionListener(e -> loadOrders());
        header.add(hintLabel);
        header.add(refreshButton);

        orderTable.setRowHeight(28);
        orderTable.setAutoCreateRowSorter(true);
        orderTable.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        orderTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        orderTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        orderTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        orderTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        orderItemTable.setRowHeight(26);
        orderItemTable.setAutoCreateRowSorter(true);
        orderItemTable.getTableHeader().setReorderingAllowed(false);
        orderItemTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        orderItemTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        orderItemTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        orderItemTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Integer orderId = getSelectedOrderId();
                if (orderId != null) {
                    loadOrderItems(orderId);
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            new JScrollPane(orderTable),
            new JScrollPane(orderItemTable)
        );
        splitPane.setResizeWeight(0.6);

        add(header, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        loadOrders();
    }

    private void loadOrders() {
        List<OrderSummaryDTO> orders = orderService.getOrderList(adminUser);
        orderTableModel.setRowCount(0);
        orderItemTableModel.setRowCount(0);

        if (orders == null) {
            JOptionPane.showMessageDialog(this, "주문 목록 조회 실패 또는 권한 없음");
            return;
        }

        for (OrderSummaryDTO order : orders) {
            String userName = order.getUserName() == null ? "-" : order.getUserName();
            orderTableModel.addRow(new Object[]{
                order.getOrderId(),
                order.getUserId(),
                userName,
                order.getOrderDate(),
                order.getStatus()
            });
        }

        if (!orders.isEmpty()) {
            orderTable.setRowSelectionInterval(0, 0);
            loadOrderItems(orders.get(0).getOrderId());
        }
    }

    private void loadOrderItems(int orderId) {
        List<OrderItemViewDTO> items = orderService.getOrderItems(adminUser, orderId);
        orderItemTableModel.setRowCount(0);

        if (items == null) {
            JOptionPane.showMessageDialog(this, "주문 상품 조회 실패 또는 권한 없음");
            return;
        }

        for (OrderItemViewDTO item : items) {
            String productName = item.getProductName() == null ? "-" : item.getProductName();
            orderItemTableModel.addRow(new Object[]{
                item.getOrderItemId(),
                item.getProductId(),
                productName,
                item.getOrderQuantity(),
                item.getOrderPrice()
            });
        }
    }

    private Integer getSelectedOrderId() {
        int viewRow = orderTable.getSelectedRow();
        if (viewRow < 0) {
            return null;
        }
        int modelRow = orderTable.convertRowIndexToModel(viewRow);
        return parseIntValue(orderTableModel.getValueAt(modelRow, 0));
    }

    private int parseIntValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }
}
