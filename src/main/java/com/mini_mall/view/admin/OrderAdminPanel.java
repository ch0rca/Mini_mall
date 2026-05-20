package com.mini_mall.view.admin;

import com.mini_mall.dto.OrderDTO;
import com.mini_mall.dto.OrderItemViewDTO;
import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.OrderService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.Collator;
import java.util.List;
import java.util.Locale;

public class OrderAdminPanel extends JPanel {

    private final OrderService orderService = new OrderService();
    private final UserDTO adminUser;

    private static final String[] ORDER_STATUSES = {
        "주문완료",
        "배송중",
        "배송완료",
        "주문취소"
    };

    private final DefaultTableModel orderTableModel = new DefaultTableModel(
        new Object[]{"주문ID", "회원ID", "회원명", "주문일", "상태"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                case 1:
                    return Integer.class;
                case 3:
                    return java.sql.Timestamp.class;
                default:
                    return String.class;
            }
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

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                case 1:
                case 3:
                case 4:
                    return Integer.class;
                default:
                    return String.class;
            }
        }
    };
    private final JTable orderItemTable = new JTable(orderItemTableModel);

    private final JComboBox<String> statusCombo = new JComboBox<>(ORDER_STATUSES);

    public OrderAdminPanel(UserDTO adminUser) {
        this.adminUser = adminUser;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel hintLabel = new JLabel("주문을 선택하면 해당 주문의 상품 목록이 표시됩니다.");
        JButton refreshButton = new JButton("주문 새로고침");
        refreshButton.addActionListener(e -> loadOrders(null));
        header.add(hintLabel);
        header.add(refreshButton);

        JLabel statusLabel = new JLabel("상태 변경");
        JButton updateButton = new JButton("변경");
        updateButton.addActionListener(e -> handleUpdateStatus());
        header.add(statusLabel);
        header.add(statusCombo);
        header.add(updateButton);

        orderTable.setRowHeight(28);
        orderTable.getTableHeader().setReorderingAllowed(false);

        TableRowSorter<DefaultTableModel> orderSorter = new TableRowSorter<>(orderTableModel);
        Collator collator = Collator.getInstance(Locale.KOREAN);
        orderSorter.setComparator(2, collator);
        orderSorter.setComparator(4, collator);
        orderTable.setRowSorter(orderSorter);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        orderTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        orderTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        orderTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        orderTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        orderItemTable.setRowHeight(26);
        orderItemTable.getTableHeader().setReorderingAllowed(false);

        TableRowSorter<DefaultTableModel> orderItemSorter =
            new TableRowSorter<>(orderItemTableModel);
        orderItemSorter.setComparator(2, collator);
        orderItemTable.setRowSorter(orderItemSorter);
        orderItemTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        orderItemTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        orderItemTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        orderItemTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Integer orderId = getSelectedOrderId();
                if (orderId != null) {
                    loadOrderItems(orderId);
                    setStatusSelection(getSelectedOrderStatus());
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

        loadOrders(null);
    }

    private void loadOrders(Integer selectOrderId) {
        List<OrderDTO> orders = orderService.getOrderList(adminUser);
        orderTableModel.setRowCount(0);
        orderItemTableModel.setRowCount(0);

        if (orders == null) {
            JOptionPane.showMessageDialog(this, "주문 목록 조회 실패 또는 권한 없음");
            return;
        }

        for (OrderDTO order : orders) {
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
            int modelRow = 0;
            if (selectOrderId != null) {
                Integer foundRow = findRowByOrderId(selectOrderId);
                if (foundRow != null) {
                    modelRow = foundRow;
                }
            }

            int viewRow = orderTable.convertRowIndexToView(modelRow);
            if (viewRow >= 0) {
                orderTable.setRowSelectionInterval(viewRow, viewRow);
            }
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

    private void handleUpdateStatus() {
        Integer orderId = getSelectedOrderId();
        if (orderId == null) {
            JOptionPane.showMessageDialog(this, "상태를 변경할 주문을 선택하세요.");
            return;
        }

        String status = (String) statusCombo.getSelectedItem();
        if (status == null || status.isBlank()) {
            JOptionPane.showMessageDialog(this, "변경할 상태를 선택하세요.");
            return;
        }

        boolean success = orderService.updateOrderStatus(adminUser, orderId, status);
        if (success) {
            loadOrders(orderId);
        } else {
            JOptionPane.showMessageDialog(this, "주문 상태 변경 실패 (권한 또는 상태 확인)");
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

    private String getSelectedOrderStatus() {
        int viewRow = orderTable.getSelectedRow();
        if (viewRow < 0) {
            return null;
        }
        int modelRow = orderTable.convertRowIndexToModel(viewRow);
        Object value = orderTableModel.getValueAt(modelRow, 4);
        return value == null ? null : value.toString();
    }

    private void setStatusSelection(String status) {
        if (status == null || status.isBlank()) {
            return;
        }

        boolean found = false;
        for (int i = 0; i < statusCombo.getItemCount(); i++) {
            if (status.equals(statusCombo.getItemAt(i))) {
                found = true;
                break;
            }
        }

        if (!found) {
            statusCombo.addItem(status);
        }
        statusCombo.setSelectedItem(status);
    }

    private Integer findRowByOrderId(int orderId) {
        for (int row = 0; row < orderTableModel.getRowCount(); row++) {
            int value = parseIntValue(orderTableModel.getValueAt(row, 0));
            if (value == orderId) {
                return row;
            }
        }
        return null;
    }

    private int parseIntValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }
}
