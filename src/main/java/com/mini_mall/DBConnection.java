package com.mini_mall;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	private static final String URL =
            "jdbc:mysql://localhost:3306/minidb" +
            "?serverTimezone=Asia/Seoul" +
            "&useSSL=false" +
            "&allowPublicKeyRetrieval=true" +
            "&useUnicode=true" +
            "&characterEncoding=UTF-8";
	
    private static final String USER = "ureca";
    private static final String PASSWORD = "ureca";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL 드라이버를 찾을 수 없습니다.", e);
        }
    }
}