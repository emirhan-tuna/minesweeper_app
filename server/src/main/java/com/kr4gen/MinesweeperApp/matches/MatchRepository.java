package com.kr4gen.MinesweeperApp.matches;

import com.kr4gen.MinesweeperApp.games.Difficulty;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MatchRepository {
    private final JdbcClient client;

    public MatchRepository(JdbcClient client) {
        this.client = client;
    }

    public void saveResult(String winnerID, String loserID, Difficulty diff) {
        client.sql("""
                        INSERT INTO match_stats(user_id, difficulty, wins, losses) VALUES (:winner, :difficulty, 1, 0),
                        (:loser, :difficulty, 0, 1) AS incoming
                        ON DUPLICATE KEY UPDATE 
                            wins = match_stats.wins + incoming.wins,
                            losses = match_stats.losses + incoming.losses""")
                .param("winner", winnerID)
                .param("loser", loserID)
                .param("difficulty", diff.name())
                .update();
    }

    public MatchDtos.Stats getResults(String userID, Difficulty diff) {
        return client.sql("""
                SELECT wins, losses FROM match_stats
                WHERE user_id =  :user_id AND difficulty = :diff""")
                .param("user_id", userID)
                .param("diff", diff.name())
                .query((rs, n) -> new MatchDtos.Stats(
                        rs.getInt("wins"),
                        rs.getInt("losses")
                ))
                .optional()
                .orElse(new MatchDtos.Stats(0, 0));
    }
}
