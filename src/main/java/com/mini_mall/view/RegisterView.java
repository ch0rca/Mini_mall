package com.mini_mall.view;

import com.mini_mall.dto.UserDTO;
import com.mini_mall.service.UserService;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {

    private final UserService userService =
            new UserService();

    public RegisterView() {

        setTitle("회원가입");
        setSize(300, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        JTextField idField = new JTextField();
        JPasswordField pwField = new JPasswordField();
        JTextField nameField =new JTextField();
        JButton registerBtn = new JButton("가입하기");
        JButton backBtn = new JButton("뒤로가기");

        panel.add(new JLabel("아이디"));
        panel.add(idField);

        panel.add(new JLabel("비밀번호"));
        panel.add(pwField);

        panel.add(new JLabel("이름"));
        panel.add(nameField);

        panel.add(registerBtn);
        panel.add(backBtn);

        // 회원가입 버튼
        registerBtn.addActionListener(e -> {

            UserDTO user = new UserDTO();

            user.setLoginId(idField.getText());
            user.setPassword(new String(pwField.getPassword()));
            user.setName(nameField.getText());

            boolean result = userService.register(user);

            // 회원가입 성공
            if (result) {

                JOptionPane.showMessageDialog(
                        this,
                        "회원가입 성공!"
                );
                dispose();
                new LoginView().setVisible(true);
            }

            // 회원가입 실패
            else {

                JOptionPane.showMessageDialog(
                        this,
                        "이미 존재하는 아이디입니다.",
                        "회원가입 실패",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // 뒤로가기 버튼
        backBtn.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
        add(panel);
        setVisible(true);
    }
}