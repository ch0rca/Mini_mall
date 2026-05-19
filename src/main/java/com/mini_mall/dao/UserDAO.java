package com.mini_mall.dao;

import com.mini_mall.DBConnection;
import com.mini_mall.dto.UserDTO;

import java.sql.*;

public class UserDAO {

    // 회원가입
    public int insertUser(UserDTO user) {
        String sql = "INSERT INTO Users (login_id, password, name, role) VALUES (?, ?, ?, 'USER')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, user.getLoginId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 아이디 중복 체크
    public boolean isLoginIdDuplicate(String loginId) {
        String sql = "SELECT COUNT(*) FROM Users WHERE login_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, loginId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 로그인
    public UserDTO findByLoginIdAndPassword(String loginId, String password) {
        String sql = "SELECT * FROM Users WHERE login_id = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, loginId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new UserDTO(
                    rs.getInt("user_id"),
                    rs.getString("login_id"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("role")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}