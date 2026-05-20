package com.mini_mall.view.admin;

import com.mini_mall.dto.ProductDTO;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

public class ProductFormDialog extends JDialog {

    private final Integer productId;
    private final JTextField nameField = new JTextField(14);
    private final JTextField priceField = new JTextField(10);
    private final JTextField stockField = new JTextField(10);
    private ProductDTO product;

    public ProductFormDialog(Frame owner, String title, ProductDTO existing) {
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

    public ProductDTO getProduct() {
        return product;
    }

    private JPanel createFormPanel() {
        int rows = productId == null ? 3 : 4;
        JPanel formPanel = new JPanel(new GridLayout(rows, 2, 8, 6));

        if (productId != null) {
            formPanel.add(new JLabel("상품ID"));
            JLabel idLabel = new JLabel(String.valueOf(productId));
            formPanel.add(idLabel);
        }

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
}
