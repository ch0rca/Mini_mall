package com.mini_mall.view.admin;

import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.UserService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class UserAdminPanel extends JPanel {

    private final UserService userService = new UserService();
    private final UserDTO adminUser;

    private final DefaultTableModel userTableModel = new DefaultTableModel(
        new Object[]{"ID", "아이디", "이름", "권한"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable userTable = new JTable(userTableModel);

    public UserAdminPanel(UserDTO adminUser) {
        this.adminUser = adminUser;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

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

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        loadUsers();
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
}
