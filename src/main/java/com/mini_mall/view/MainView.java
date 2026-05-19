package com.mini_mall.view;

import com.mini_mall.dto.UserDTO;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    public MainView(UserDTO user) {

        setTitle("메인 화면");
        setSize(400, 300);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel(
                user.getName() + "님 환영합니다.",
                SwingConstants.CENTER
        );

        JButton logoutBtn = new JButton("로그아웃");

        // 로그아웃
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginView();
        });

        panel.add(welcomeLabel, BorderLayout.CENTER);
        panel.add(logoutBtn, BorderLayout.SOUTH);
        add(panel);
        setVisible(true);
    }
}