package com.mini_mall;

import com.mini_mall.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class DBTest {
    public static void main(String[] args) {
        System.out.println(">>> 연결 시도");

        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println(">>> DB 연결 성공");
                System.out.println(">>> 연결된 URL: " + conn.getMetaData().getURL());
            }
        } catch (SQLException e) {
            System.out.println(">>> DB 연결 실패");
            System.out.println(">>> 에러 원인: " + e.getMessage());
            e.printStackTrace();
        }
    }
}