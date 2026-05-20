package com.mini_mall;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {

        try {
            Properties props = new Properties();
            InputStream input =
                    DBConnection.class
                            .getClassLoader()
                            .getResourceAsStream(
                                    "db.properties"
                            );

            if(input == null) {
                throw new RuntimeException(
                        "db.properties 파일을 찾을 수 없습니다."
                );
            }

            props.load(input);

            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // DB 연결 메서드
    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}