package com.mini_mall;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final HikariDataSource dataSource;

    static {
        try {
            Properties props = new Properties();
            InputStream input = DBConnection.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");

            if (input == null) {
                throw new RuntimeException("db.properties 파일을 찾을 수 없습니다.");
            }

            props.load(input);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));
            
            // 고정 풀 사이즈 설정 (최적화 권장사항 반영)
            int poolSize = Integer.parseInt(props.getProperty("db.pool.max", "16"));
            config.setMaximumPoolSize(poolSize);
            config.setMinimumIdle(poolSize);

            dataSource = new HikariDataSource(config);

        } catch (Exception e) {
            throw new RuntimeException("DB Connection Pool 초기화 실패", e);
        }
    }

    // 이제 모든 DAO는 이 메서드를 통해 초고속으로 커넥션을 빌려갑니다.
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
