package com.kr4gen.MinesweeperApp.user;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final JdbcClient client;

    public UserRepository(JdbcClient client) {
        this.client = client;
    }

    public Optional<User> findByUsername(String username) {
        return client.sql("SELECT id, username, password_hash FROM users WHERE username = :username")
                .param("username", username)
                .query((rs, rowNum) -> new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password_hash")
                )).optional();
    }

    public User create(String username, String password) {
        String UUID = java.util.UUID.randomUUID().toString();

        client.sql("INSERT INTO users (id, username, password_hash) VALUES (:id, :username, :password_hash)")
            .param("id", UUID)
            .param("username", username)
            .param("password_hash", password)
            .update();

        return new User(UUID, username, password);
    }
}
