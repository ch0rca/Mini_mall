package com.mini_mall.view;

import com.mini_mall.dto.ProductDTO;
import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.ProductService;
import com.mini_mall.service.UserService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

public class AdminView extends JFrame {

    private final ProductService productService = new ProductService();
    private final UserService userService = new UserService();
    private final UserDTO adminUser;

    private final DefaultTableModel productTableModel = new DefaultTableModel(
        new Object[]{"ID", "이름", "가격", "재고", "상태"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable productTable = new JTable(productTableModel);

    private final DefaultTableModel userTableModel = new DefaultTableModel(
        new Object[]{"ID", "아이디", "이름", "권한"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable userTable = new JTable(userTableModel);

    public AdminView(UserDTO adminUser) {
        this.adminUser = adminUser;

        if (!isAdmin(adminUser)) {
            JOptionPane.showMessageDialog(this, "관리자만 접근할 수 있습니다.");
            dispose();
            return;
        }

        setTitle("관리자 화면");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("상품 관리", createProductPanel());
        tabbedPane.addTab("회원 목록", createUserPanel());

        add(tabbedPane, BorderLayout.CENTER);

        loadProducts();
        loadUsers();
    }

    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        panel.add(createProductControlPanel(), BorderLayout.NORTH);
        panel.add(createProductTablePanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProductControlPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JLabel hintLabel = new JLabel("목록에서 상품을 선택한 후 수정/판매중지할 수 있습니다.");
        panel.add(hintLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton createButton = new JButton("등록");
        createButton.addActionListener(e -> handleCreateProduct());
        buttonPanel.add(createButton);

        JButton updateButton = new JButton("수정");
        updateButton.addActionListener(e -> handleUpdateProduct());
        buttonPanel.add(updateButton);

        JButton deactivateButton = new JButton("판매중지");
        deactivateButton.addActionListener(e -> handleDeactivateProduct());
        buttonPanel.add(deactivateButton);

        JButton refreshButton = new JButton("목록 새로고침");
        refreshButton.addActionListener(e -> loadProducts());
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createProductTablePanel() {
        productTable.setRowHeight(28);
        productTable.setAutoCreateRowSorter(true);
        productTable.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        productTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
            ) {
                java.awt.Component component = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );
                String status = value == null ? "" : value.toString();
                if ("판매중지".equals(status)) {
                    component.setForeground(new Color(127, 140, 141));
                } else if ("품절".equals(status)) {
                    component.setForeground(new Color(192, 57, 43));
                } else {
                    component.setForeground(new Color(39, 174, 96));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return component;
            }
        };
        productTable.getColumnModel().getColumn(4).setCellRenderer(statusRenderer);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton refreshButton = new JButton("회원 목록 새로고침");
        refreshButton.addActionListener(e -> loadUsers());
        header.add(refreshButton);

        userTable.setRowHeight(28);
        userTable.setAutoCreateRowSorter(true);
        userTable.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        userTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        userTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        panel.add(header, BorderLayout.NORTH);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        return panel;
    }

    private void loadProducts() {
        List<ProductDTO> products = productService.getProductList();
        productTableModel.setRowCount(0);

        if (products == null) {
            JOptionPane.showMessageDialog(this, "상품 목록 조회 실패");
            return;
        }

        for (ProductDTO product : products) {
            productTableModel.addRow(new Object[]{
                product.getProductId(),
                product.getProductName(),
                product.getPrice(),
                product.getStock(),
                statusForProduct(product)
            });
        }
    }

    private void loadUsers() {
        List<UserDTO> users = userService.getUserList(adminUser);
        userTableModel.setRowCount(0);

        if (users == null) {
            JOptionPane.showMessageDialog(this, "회원 목록 조회 실패 또는 권한 없음");
            return;
        }

        for (UserDTO user : users) {
            userTableModel.addRow(new Object[]{
                user.getUserId(),
                user.getLoginId(),
                user.getName(),
                user.getRole()
            });
        }
    }

    private void handleCreateProduct() {
        ProductFormDialog dialog = new ProductFormDialog(this, "상품 등록", null);
        dialog.setVisible(true);

        ProductDTO product = dialog.getProduct();
        if (product == null) {
            return;
        }

        boolean success = productService.createProduct(adminUser, product);
        if (success) {
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this, "상품 등록 실패 (권한 또는 입력 확인)");
        }
    }

    private void handleUpdateProduct() {
        ProductDTO selected = getSelectedProduct();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "수정할 상품을 목록에서 선택하세요.");
            return;
        }

        ProductFormDialog dialog = new ProductFormDialog(this, "상품 수정", selected);
        dialog.setVisible(true);

        ProductDTO updated = dialog.getProduct();
        if (updated == null) {
            return;
        }

        boolean success = productService.updateProduct(adminUser, updated);
        if (success) {
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this, "상품 수정 실패 (권한 또는 입력 확인)");
        }
    }

    private void handleDeactivateProduct() {
        ProductDTO selected = getSelectedProduct();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "판매중지할 상품을 목록에서 선택하세요.");
            return;
        }

        if (!selected.isActive()) {
            JOptionPane.showMessageDialog(this, "이미 판매중지된 상품입니다.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "정말 판매중지할까요? (ID: " + selected.getProductId() + ")",
            "판매중지 확인",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success = productService.deactivateProduct(adminUser, selected.getProductId());
        if (success) {
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this, "상품 판매중지 실패 (권한 또는 존재 여부 확인)");
        }
    }

    private ProductDTO getSelectedProduct() {
        int viewRow = productTable.getSelectedRow();
        if (viewRow < 0) {
            return null;
        }

        int modelRow = productTable.convertRowIndexToModel(viewRow);
        int productId = parseIntValue(productTableModel.getValueAt(modelRow, 0));
        String productName = String.valueOf(productTableModel.getValueAt(modelRow, 1));
        int price = parseIntValue(productTableModel.getValueAt(modelRow, 2));
        int stock = parseIntValue(productTableModel.getValueAt(modelRow, 3));

        boolean active = !"판매중지".equals(
            String.valueOf(productTableModel.getValueAt(modelRow, 4))
        );

        return new ProductDTO(productId, productName, price, stock, active);
    }

    private int parseIntValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }

    private String statusForProduct(ProductDTO product) {
        if (!product.isActive()) {
            return "판매중지";
        }
        return product.getStock() == 0 ? "품절" : "판매중";
    }

    private boolean isAdmin(UserDTO user) {
        return user != null && "ADMIN".equals(user.getRole());
    }

    public static void main(String[] args) {
        UserDTO adminUser = new UserDTO();
        adminUser.setRole("ADMIN");
        SwingUtilities.invokeLater(() -> new AdminView(adminUser).setVisible(true));
    }

    private static class ProductFormDialog extends JDialog {
        private final Integer productId;
        private final JTextField nameField = new JTextField(14);
        private final JTextField priceField = new JTextField(10);
        private final JTextField stockField = new JTextField(10);
        private ProductDTO product;

        private ProductFormDialog(JFrame owner, String title, ProductDTO existing) {
            super(owner, title, true);
            this.productId = existing == null ? null : existing.getProductId();

            JPanel content = new JPanel(new BorderLayout(8, 8));
            content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            content.add(createFormPanel(), BorderLayout.CENTER);
            content.add(createButtonPanel(), BorderLayout.SOUTH);
            setContentPane(content);

            if (existing != null) {
                nameField.setText(existing.getProductName());
                priceField.setText(String.valueOf(existing.getPrice()));
                stockField.setText(String.valueOf(existing.getStock()));
            }

            pack();
            setLocationRelativeTo(owner);
        }

        private JPanel createFormPanel() {
            JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 6));

            formPanel.add(new JLabel("상품ID"));
            JLabel idLabel = new JLabel(productId == null ? "자동" : String.valueOf(productId));
            formPanel.add(idLabel);

            formPanel.add(new JLabel("상품명"));
            formPanel.add(nameField);

            formPanel.add(new JLabel("가격"));
            formPanel.add(priceField);

            formPanel.add(new JLabel("재고"));
            formPanel.add(stockField);

            return formPanel;
        }

        private JPanel createButtonPanel() {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));

            JButton saveButton = new JButton("저장");
            saveButton.addActionListener(e -> handleSave());
            buttonPanel.add(saveButton);

            JButton cancelButton = new JButton("취소");
            cancelButton.addActionListener(e -> dispose());
            buttonPanel.add(cancelButton);

            return buttonPanel;
        }

        private void handleSave() {
            String name = nameField.getText().trim();
            String priceInput = priceField.getText().trim();
            String stockInput = stockField.getText().trim();

            if (name.isEmpty() || priceInput.isEmpty() || stockInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "상품명/가격/재고를 입력하세요.");
                return;
            }

            try {
                int price = Integer.parseInt(priceInput);
                int stock = Integer.parseInt(stockInput);

                if (price < 0 || stock < 0) {
                    JOptionPane.showMessageDialog(this, "가격/재고는 0 이상이어야 합니다.");
                    return;
                }

                ProductDTO result = new ProductDTO();
                if (productId != null) {
                    result.setProductId(productId);
                }
                result.setProductName(name);
                result.setPrice(price);
                result.setStock(stock);
                product = result;
                dispose();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "가격/재고는 숫자로 입력하세요.");
            }
        }

        private ProductDTO getProduct() {
            return product;
        }
    }
}
