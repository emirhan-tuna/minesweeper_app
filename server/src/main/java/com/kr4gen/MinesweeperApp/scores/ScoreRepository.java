package com.kr4gen.MinesweeperApp.scores;

import com.kr4gen.MinesweeperApp.games.Difficulty;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScoreRepository {
    public record entry(String username, int timeSec) {}

    private final JdbcClient client;

    public ScoreRepository(JdbcClient client) {
        this.client = client;
    }

    public void saveTime(String userID, Difficulty diff, int timeSec) {
        client.sql("INSERT INTO user_times (user_id, difficulty, time_sec) VALUES (:user_id, :difficulty, :timeSec)")
                .param("user_id", userID)
                .param("difficulty", diff.name())
                .param("timeSec", timeSec)
                .update();
    }

    public List<Integer> findTimes(String userID, Difficulty diff) {
        return client.sql("""
                  SELECT time_sec FROM user_times
                  WHERE user_id = :user_id AND difficulty = :difficulty
                  ORDER BY time_sec""")
                .param("user_id", userID)
                .param("difficulty", diff.name())
                .query(Integer.class)
                .list();
    }

    public List<entry> getTopTimes(Difficulty diff, int limit) {
        return client.sql("""
                SELECT u.username, t.time_sec
                FROM user_times t JOIN users u ON u.id = t.user_id
                WHERE t.difficulty = :difficulty
                ORDER BY time_sec
                LIMIT :limit""")
                .param("difficulty", diff.name())
                .param("limit", limit)
                .query((rs, n) -> new entry(
                        rs.getString("username"),
                        rs.getInt("time_sec")
                ))
                .list();
    }
}
