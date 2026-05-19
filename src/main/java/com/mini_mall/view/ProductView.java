package com.mini_mall.view;

import com.mini_mall.dto.ProductDTO;
import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductView extends JFrame {

    private final ProductService productService =
            new ProductService();

    private final UserDTO loginUser;

    private static final Font TITLE_FONT =
            new Font("Dialog", Font.BOLD, 20);

    private static final Font LABEL_FONT =
            new Font("Dialog", Font.PLAIN, 13);

    private static final Font VALUE_FONT =
            new Font("Dialog", Font.BOLD, 13);

    private final DefaultTableModel tableModel =
            new DefaultTableModel(
                    new Object[]{
                            "ID",
                            "이름",
                            "가격",
                            "재고",
                            "상태"
                    }, 0
            ) {

                @Override
                public boolean isCellEditable(
                        int row,
                        int column
                ) {
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

    public ProductView(UserDTO user) {

        this.loginUser = user;
        setTitle("상품 목록");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));

        root.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        12,
                        12,
                        12
                )
        );

        setContentPane(root);
        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createDetailPanel(), BorderLayout.SOUTH);

        // 테이블 클릭 이벤트
        productTable.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting() && productTable.getSelectedRow() >= 0) {

                        int row = productTable.getSelectedRow();

                        int productId = Integer.parseInt(
                        		tableModel.getValueAt(
                                                row,
                                                0
                                        ).toString()
                                );

                        productIdField.setText( String.valueOf(productId));

                        loadDetail(productId);
                    }
                });

        loadProducts();
    }

    // 상단 패널
    private JPanel createTopPanel() {

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));

        JPanel titlePanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("상품 목록");

        titleLabel.setFont(TITLE_FONT);

        JLabel userLabel =
                new JLabel(
                        loginUser.getName()
                                + "님 환영합니다."
                );

        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(userLabel, BorderLayout.EAST);
        topPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                0
                        )
                );

        JLabel idLabel = new JLabel("상품ID");

        idLabel.setFont(LABEL_FONT);
        searchPanel.add(idLabel);
        searchPanel.add(productIdField);

        JButton detailButton = new JButton("상세 조회");

        detailButton.addActionListener(
        		e -> handleDetailSearch()
        );

        searchPanel.add(detailButton);
        JButton refreshButton = new JButton("목록 새로고침");

        refreshButton.addActionListener(
                e -> loadProducts()
        );

        searchPanel.add(refreshButton);

        JButton logoutButton = new JButton("로그아웃");

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        searchPanel.add(logoutButton);

        topPanel.add(searchPanel, BorderLayout.SOUTH);
        return topPanel;
    }

    // 상품 테이블
    private JPanel createTablePanel() {

        productTable.setRowHeight(28);
        productTable.setFillsViewportHeight(true);
        productTable.setAutoCreateRowSorter(true);
        productTable.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        productTable.getColumnModel()
                .getColumn(0)
                .setCellRenderer(centerRenderer);

        productTable.getColumnModel()
                .getColumn(2)
                .setCellRenderer(centerRenderer);

        productTable.getColumnModel()
                .getColumn(3)
                .setCellRenderer(centerRenderer);

        JPanel tablePanel = new JPanel(new BorderLayout());

        tablePanel.add(
                new JScrollPane(productTable),
                BorderLayout.CENTER
        );

        return tablePanel;
    }

    // 상세 정보 패널
    private JPanel createDetailPanel() {

        JPanel detailPanel = new JPanel(new GridLayout(5, 2, 8, 4));

        detailPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "상품 상세"
                )
        );

        detailPanel.add(new JLabel("상품ID"));
        detailPanel.add(idValueLabel);

        detailPanel.add(new JLabel("상품명"));
        detailPanel.add(nameValueLabel);

        detailPanel.add(new JLabel("가격"));
        detailPanel.add(priceValueLabel);

        detailPanel.add(new JLabel("재고"));
        detailPanel.add(stockValueLabel);

        detailPanel.add(new JLabel("상태"));
        detailPanel.add(statusValueLabel);

        return detailPanel;
    }

    // 상세 조회 버튼
    private void handleDetailSearch() {

        String input = productIdField.getText().trim();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "상품ID를 입력하세요."
            );
            return;
        }

        try {
            int productId = Integer.parseInt(input);
            loadDetail(productId);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "숫자로 입력하세요."
            );
        }
    }

    // 상품 목록 조회
    private void loadProducts() {

        List<ProductDTO> products = productService.getProductList();
        tableModel.setRowCount(0);

        if (products == null) {
        	JOptionPane.showMessageDialog(
                    this,
                    "상품 조회 실패"
            );

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

    // 상품 상세 조회
    private void loadDetail(int productId) {

        ProductDTO detail =
                productService.getProductDetail(
                        productId
                );

        if (detail == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "상품이 존재하지 않습니다."
            );
            clearDetail();
            return;
        }

        idValueLabel.setText(String.valueOf(detail.getProductId()));
        nameValueLabel.setText(detail.getProductName());
        priceValueLabel.setText(String.valueOf(detail.getPrice()));
        stockValueLabel.setText(String.valueOf(detail.getStock()));
        statusValueLabel.setText(statusForStock(detail.getStock()));
    }

    // 상세 정보 초기화
    private void clearDetail() {
        idValueLabel.setText("-");
        nameValueLabel.setText("-");
        priceValueLabel.setText("-");
        stockValueLabel.setText("-");
        statusValueLabel.setText("-");
    }

    // 재고 상태
    private String statusForStock(int stock) {

        return stock == 0
                ? "품절"
                : "판매중";
    }
}