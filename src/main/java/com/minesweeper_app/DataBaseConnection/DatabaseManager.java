package com.minesweeper_app.DataBaseConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    public static Connection getConnection() {
        Properties prop = new Properties();

        try (FileInputStream in = new FileInputStream("database.properties")) {
            prop.load(in);
        } catch (IOException e) {
            System.out.println("File not found.");
            return null;
        }

        try {
            Connection con = DriverManager.getConnection(
                prop.getProperty("db.url"),
                prop.getProperty("db.user"),
                prop.getProperty("db.password")
            );

            return con;
        } catch (SQLException e) {
            System.out.println("Cant connect: " + e.getMessage());
        }

        return null;
    }
}