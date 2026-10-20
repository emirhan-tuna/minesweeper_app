package com.minesweeper_app.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

public class UserDao {
    private Connection c = DatabaseManager.getConnection();

    public static boolean register(String username, String password) {
        String sql = "INSERT INTO users (id, username, password_hash) VALUES (?, ?, ?)";

        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, username);
            ps.setString(3, BCrypt.hashpw(password, BCrypt.gensalt()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean login(String username, String password) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";

        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next() && BCrypt.checkpw(password, rs.getString(1));
        } catch (SQLException e) {
            return false;
        }
    }

    public static void recordWin(String username, String diff, int seconds) {

        String sql = """
                INSERT INTO user_times (id, user_id, difficulty, time_sec)
                SELECT ?, id, ?, ? FROM users WHERE username = ?""";

        try (Connection c = DatabaseManager.getConnection(); PreparedStatement timePs = c.prepareStatement(sql)) {
            timePs.setString(1, UUID.randomUUID().toString());
            timePs.setString(2, diff);
            timePs.setInt(3, seconds);
            timePs.setString(4, username);
            timePs.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Cant save result: " + e.getMessage());
        }
    }


    public static List<Integer> getTimes(String username, String diff) {
        String sql = """
                    SELECT t.time_sec
                    FROM user_times t JOIN users u ON u.id = t.user_id
                    WHERE u.username = ? AND t.difficulty = ?
                    ORDER BY t.time_sec
                     """;
        List<Integer> times = new ArrayList<>(); 
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, diff);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    times.add(rs.getInt("time_sec"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Cant load times: " + e.getMessage());
        }

        return times;
    }
}
