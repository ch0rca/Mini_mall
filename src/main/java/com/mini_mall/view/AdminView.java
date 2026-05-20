package com.mini_mall.view;

import com.mini_mall.dto.UserDTO;
import com.mini_mall.view.admin.OrderAdminPanel;
import com.mini_mall.view.admin.ProductAdminPanel;
import com.mini_mall.view.admin.UserAdminPanel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

public class AdminView extends JFrame {

    private final UserDTO adminUser;

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
        tabbedPane.addTab("상품 관리", new ProductAdminPanel(adminUser));
        tabbedPane.addTab("회원 목록", new UserAdminPanel(adminUser));
        tabbedPane.addTab("주문 조회", new OrderAdminPanel(adminUser));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 0, 12));

        JLabel titleLabel = new JLabel("관리자 화면");
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("로그아웃");
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        return headerPanel;
    }

    private boolean isAdmin(UserDTO user) {
        return user != null && "ADMIN".equals(user.getRole());
    }

}
