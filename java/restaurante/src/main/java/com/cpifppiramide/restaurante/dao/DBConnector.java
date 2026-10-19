package com.cpifppiramide.restaurante.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static Connection connection;

    private DBConnector() {
    }

    public static Connection getInstance() {
        try {
            if (connection == null || connection.isClosed()) {
                String url = System.getenv().getOrDefault(
                        "DB_URL", "jdbc:mysql://localhost:3306/restaurante");
                String username = System.getenv().getOrDefault("DB_USERNAME", "root");
                String password = System.getenv().getOrDefault("DB_PASSWORD", "root");
                connection = DriverManager.getConnection(url, username, password);
            }
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo conectar con la base de datos", e);
        }
    }
}
