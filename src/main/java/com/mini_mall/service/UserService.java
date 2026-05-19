package com.mini_mall.service;

import com.mini_mall.dao.UserDAO;
import com.mini_mall.dto.UserDTO;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    // 회원가입
    public boolean register(UserDTO user) {
        if (userDAO.isLoginIdDuplicate(user.getLoginId())) {
            return false;
        }
        user.setRole("USER");
        return userDAO.insertUser(user) == 1;
    }

    // 로그인
    public UserDTO login(String loginId, String password) {
        return userDAO.findByLoginIdAndPassword(loginId, password);
    }
}