package com.mini_mall.view;

import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private UserService userService = new UserService();

    public LoginView() {

        setTitle("로그인");

        setSize(300, 200);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        // 패널
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        // 아이디
        JLabel idLabel = new JLabel("아이디");
        JTextField idField = new JTextField();

        // 비밀번호
        JLabel pwLabel = new JLabel("비밀번호");
        JPasswordField pwField = new JPasswordField();

        // 버튼
        JButton loginBtn = new JButton("로그인");
        JButton registerBtn = new JButton("회원가입");

        // 컴포넌트 추가
        panel.add(idLabel);
        panel.add(idField);

        panel.add(pwLabel);
        panel.add(pwField);

        panel.add(loginBtn);
        panel.add(registerBtn);

        // 로그인 버튼 이벤트
        loginBtn.addActionListener(e -> {

            String loginId = idField.getText();

            String password =
                    new String(pwField.getPassword());

            UserDTO user =
                    userService.login(loginId, password);

            // 로그인 성공
            if (user != null) {

                JOptionPane.showMessageDialog(
                        this,
                        user.getName() + "님 환영합니다!"
                );

                dispose();

                new MainView(user);

            }

            // 로그인 실패
            else {

                JOptionPane.showMessageDialog(
                        this,
                        "아이디 또는 비밀번호가 틀렸습니다.",
                        "로그인 실패",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // 회원가입 버튼 이벤트
        registerBtn.addActionListener(e -> {

            dispose();

            new RegisterView();
        });

        add(panel);

        setVisible(true);
    }
}