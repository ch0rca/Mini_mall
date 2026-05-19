package com.mini_mall;

import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.UserService;

import javax.swing.*;
import java.awt.*;

public class Main {

    static UserService userService = new UserService();

    public static void main(String[] args) {
        showLoginFrame();
    }

    // ===================== 로그인 화면 =====================
    static void showLoginFrame() {
        JFrame frame = new JFrame("로그인");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        JLabel idLabel = new JLabel("아이디:");
        JTextField idField = new JTextField();

        JLabel pwLabel = new JLabel("비밀번호:");
        JPasswordField pwField = new JPasswordField();

        JButton loginBtn = new JButton("로그인");
        JButton registerBtn = new JButton("회원가입");

        panel.add(idLabel);    panel.add(idField);
        panel.add(pwLabel);    panel.add(pwField);
        panel.add(loginBtn);   panel.add(registerBtn);

        // 로그인 버튼
        loginBtn.addActionListener(e -> {
            String loginId  = idField.getText();
            String password = new String(pwField.getPassword());

            UserDTO user = userService.login(loginId, password);

            if (user != null) {
                JOptionPane.showMessageDialog(frame, user.getName() + "님 환영합니다!\n권한: " + user.getRole());
                frame.dispose();
                showMainFrame(user); // 메인 화면으로 이동
            } else {
                JOptionPane.showMessageDialog(frame, "아이디 또는 비밀번호가 틀렸습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 회원가입 버튼
        registerBtn.addActionListener(e -> {
            frame.dispose();
            showRegisterFrame(); // 회원가입 화면으로 이동
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    // ===================== 회원가입 화면 =====================
    static void showRegisterFrame() {
        JFrame frame = new JFrame("회원가입");
        frame.setSize(300, 220);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        JLabel idLabel = new JLabel("아이디:");
        JTextField idField = new JTextField();

        JLabel pwLabel = new JLabel("비밀번호:");
        JPasswordField pwField = new JPasswordField();

        JLabel nameLabel = new JLabel("이름:");
        JTextField nameField = new JTextField();

        JButton registerBtn = new JButton("가입하기");
        JButton backBtn = new JButton("뒤로가기");

        panel.add(idLabel);      panel.add(idField);
        panel.add(pwLabel);      panel.add(pwField);
        panel.add(nameLabel);    panel.add(nameField);
        panel.add(registerBtn);  panel.add(backBtn);

        // 가입하기 버튼
        registerBtn.addActionListener(e -> {
            UserDTO user = new UserDTO();
            user.setLoginId(idField.getText());
            user.setPassword(new String(pwField.getPassword()));
            user.setName(nameField.getText());

            boolean success = userService.register(user);

            if (success) {
                JOptionPane.showMessageDialog(frame, "회원가입 성공!");
                frame.dispose();
                showLoginFrame(); // 로그인 화면으로 이동
            } else {
                JOptionPane.showMessageDialog(frame, "이미 사용 중인 아이디입니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 뒤로가기 버튼
        backBtn.addActionListener(e -> {
            frame.dispose();
            showLoginFrame();
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    // ===================== 메인 화면 =====================
    static void showMainFrame(UserDTO user) {
        JFrame frame = new JFrame("미니몰 - " + user.getName());
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("환영합니다, " + user.getName() + "님! (권한: " + user.getRole() + ")", SwingConstants.CENTER);
        JButton logoutBtn = new JButton("로그아웃");

        // 로그아웃 버튼
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            showLoginFrame(); // 로그인 화면으로 이동
        });

        panel.add(welcomeLabel, BorderLayout.CENTER);
        panel.add(logoutBtn, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }
}