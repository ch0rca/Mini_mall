package com.mini_mall.view;

import com.mini_mall.dto.ProductDTO;
import com.mini_mall.service.ProductService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

public class ProductView extends JFrame {

    private final ProductService productService = new ProductService();
    private static final Font TITLE_FONT = new Font("Dialog", Font.BOLD, 20);
    private static final Font LABEL_FONT = new Font("Dialog", Font.PLAIN, 13);
    private static final Font VALUE_FONT = new Font("Dialog", Font.BOLD, 13);

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new Object[]{"ID", "이름", "가격", "재고", "상태"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable productTable = new JTable(tableModel);

    private final JTextField productIdField = new JTextField(10);
    private final JLabel idValueLabel = new JLabel("-");
    private final JLabel nameValueLabel = new JLabel("-");
    private final JLabel priceValueLabel = new JLabel("-");
    private final JLabel stockValueLabel = new JLabel("-");
    private final JLabel statusValueLabel = new JLabel("-");

    public ProductView() {
        setTitle("상품 조회");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createDetailPanel(), BorderLayout.SOUTH);

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() >= 0) {
                int row = productTable.getSelectedRow();
                int productId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                productIdField.setText(String.valueOf(productId));
                loadDetail(productId);
            }
        });

        loadProducts();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));

        JLabel titleLabel = new JLabel("상품 조회");
        titleLabel.setFont(TITLE_FONT);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel idLabel = new JLabel("상품ID");
        idLabel.setFont(LABEL_FONT);
        searchPanel.add(idLabel);
        searchPanel.add(productIdField);

        JButton detailButton = new JButton("상세 조회");
        detailButton.addActionListener(e -> handleDetailSearch());
        detailButton.setFocusable(false);
        searchPanel.add(detailButton);

        JButton refreshButton = new JButton("목록 새로고침");
        refreshButton.addActionListener(e -> loadProducts());
        refreshButton.setFocusable(false);
        searchPanel.add(refreshButton);

        topPanel.add(searchPanel, BorderLayout.SOUTH);

        return topPanel;
    }

    private JPanel createTablePanel() {
        productTable.setRowHeight(28);
        productTable.setFillsViewportHeight(true);
        productTable.setAutoCreateRowSorter(true);
        productTable.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        productTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        productTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column
            ) {
                Component component = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );
                String status = value == null ? "" : value.toString();
                if ("품절".equals(status)) {
                    component.setForeground(new Color(192, 57, 43));
                } else {
                    component.setForeground(new Color(39, 174, 96));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return component;
            }
        };
        productTable.getColumnModel().getColumn(4).setCellRenderer(statusRenderer);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createDetailPanel() {
        JPanel detailPanel = new JPanel(new GridLayout(5, 2, 8, 4));
        detailPanel.setBorder(BorderFactory.createTitledBorder("상품 상세"));

        JLabel idLabel = new JLabel("상품ID");
        idLabel.setFont(LABEL_FONT);
        detailPanel.add(idLabel);
        idValueLabel.setFont(VALUE_FONT);
        detailPanel.add(idValueLabel);

        JLabel nameLabel = new JLabel("상품명");
        nameLabel.setFont(LABEL_FONT);
        detailPanel.add(nameLabel);
        nameValueLabel.setFont(VALUE_FONT);
        detailPanel.add(nameValueLabel);

        JLabel priceLabel = new JLabel("가격");
        priceLabel.setFont(LABEL_FONT);
        detailPanel.add(priceLabel);
        priceValueLabel.setFont(VALUE_FONT);
        detailPanel.add(priceValueLabel);

        JLabel stockLabel = new JLabel("재고");
        stockLabel.setFont(LABEL_FONT);
        detailPanel.add(stockLabel);
        stockValueLabel.setFont(VALUE_FONT);
        detailPanel.add(stockValueLabel);

        JLabel statusLabel = new JLabel("상태");
        statusLabel.setFont(LABEL_FONT);
        detailPanel.add(statusLabel);
        statusValueLabel.setFont(VALUE_FONT);
        detailPanel.add(statusValueLabel);

        return detailPanel;
    }

    private void handleDetailSearch() {
        String input = productIdField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "상품ID를 입력하세요.");
            return;
        }

        try {
            int productId = Integer.parseInt(input);
            loadDetail(productId);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "상품ID는 숫자로 입력하세요.");
        }
    }

    private void loadProducts() {
        List<ProductDTO> products = productService.getProductList();
        tableModel.setRowCount(0);

        if (products == null) {
            JOptionPane.showMessageDialog(this, "상품 목록 조회 실패");
            clearDetail();
            return;
        }

        for (ProductDTO product : products) {
            tableModel.addRow(new Object[]{
                product.getProductId(),
                product.getProductName(),
                product.getPrice(),
                product.getStock(),
                statusForStock(product.getStock())
            });
        }
    }

    private void loadDetail(int productId) {
        ProductDTO detail = productService.getProductDetail(productId);
        if (detail == null) {
            JOptionPane.showMessageDialog(this, "해당 상품이 없습니다.");
            clearDetail();
            return;
        }

        idValueLabel.setText(String.valueOf(detail.getProductId()));
        nameValueLabel.setText(detail.getProductName());
        priceValueLabel.setText(String.valueOf(detail.getPrice()));
        stockValueLabel.setText(String.valueOf(detail.getStock()));
        statusValueLabel.setText(statusForStock(detail.getStock()));
    }

    private void clearDetail() {
        idValueLabel.setText("-");
        nameValueLabel.setText("-");
        priceValueLabel.setText("-");
        stockValueLabel.setText("-");
        statusValueLabel.setText("-");
    }

    private String statusForStock(int stock) {
        return stock == 0 ? "품절" : "판매중";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductView().setVisible(true));
    }
}
