package com.mini_mall.view.admin;

import com.mini_mall.dto.ProductDTO;
import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.ProductService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.text.Collator;
import java.util.List;
import java.util.Locale;

public class ProductAdminPanel extends JPanel {

    private final ProductService productService = new ProductService();
    private final UserDTO adminUser;

    private final DefaultTableModel productTableModel = new DefaultTableModel(
        new Object[]{"ID", "이름", "가격", "재고", "상태"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                case 2:
                case 3:
                    return Integer.class;
                default:
                    return String.class;
            }
        }
    };
    private final JTable productTable = new JTable(productTableModel);

    public ProductAdminPanel(UserDTO adminUser) {
        this.adminUser = adminUser;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(createProductControlPanel(), BorderLayout.NORTH);
        add(createProductTablePanel(), BorderLayout.CENTER);

        loadProducts();
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
        productTable.getTableHeader().setReorderingAllowed(false);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(productTableModel);
        Collator collator = Collator.getInstance(Locale.KOREAN);
        sorter.setComparator(1, collator);
        sorter.setComparator(4, collator);
        productTable.setRowSorter(sorter);

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

    private void handleCreateProduct() {
        ProductFormDialog dialog = new ProductFormDialog(getOwnerFrame(), "상품 등록", null);
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

        ProductFormDialog dialog = new ProductFormDialog(getOwnerFrame(), "상품 수정", selected);
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

    private Frame getOwnerFrame() {
        return JOptionPane.getFrameForComponent(this);
    }
}
